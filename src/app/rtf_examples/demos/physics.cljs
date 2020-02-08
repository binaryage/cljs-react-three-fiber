(ns rtf-examples.demos.physics
  (:require [cljs-bean.core :refer [bean ->js]]
            [rtf-examples.lib.ui :refer [$ defnc use-ref use-state use-effect <canvas>]]
            [rtf-examples.lib.rtf :refer [use-frame]]
            [rtf-examples.lib.interop :refer [doto!]]
            [rtf-examples.lib.react :refer [create-context use-context]]
            [rtf-examples.lib.three :refer [pcf-soft-shadow-map
                                            <mesh>
                                            <ambient-light>
                                            <spot-light>
                                            <plane-buffer-geometry>
                                            <mesh-phong-material>
                                            <mesh-standard-material>
                                            <box-buffer-geometry>]]
            [rtf-examples.lib.helpers :refer [set-position!
                                              add-shape!
                                              get-context-provider
                                              get-position
                                              get-quaternion
                                              set-position-copy!
                                              set-quaternion-copy!]]
            [rtf-examples.lib.cannon :refer [create-world
                                             create-naive-broadphase
                                             create-body
                                             create-plane
                                             create-box
                                             create-vec3
                                             step-world!
                                             add-body-to-world!
                                             remove-body-from-world!]]))

; -- constants --------------------------------------------------------------------------------------------------------------

(def plane-removal-delay 5000)
(def camera-config #js {:position #js [0 0 15]})

; -- context ----------------------------------------------------------------------------------------------------------------

(def context (create-context))

; -- helpers ----------------------------------------------------------------------------------------------------------------

(defn init-canvas! [props]
  (let [{:keys [gl]} (bean props)]
    (doto! gl
      (:shadow-map :-enabled true)
      (:shadow-map :-type pcf-soft-shadow-map))))

(defn setup-world! [world]
  (doto! world
    (:-broadphase (create-naive-broadphase))
    (:solver :-iterations 10)
    (:gravity :set 0 0 -25)))

(defn schedule-removing-upper-plane! [plane-state-atom]
  (js/setTimeout #(reset! plane-state-atom false) plane-removal-delay))

; -- hooks ------------------------------------------------------------------------------------------------------------------

; custom hook to maintain a world physics body
(defn use-cannon
  ([f props] (use-cannon f props :once))
  ([f props _deps]
   (let [mesh-ref (use-ref nil)
         world (use-context context)
         current-body (use-state #(create-body (->js props)))
         manage-body! (fn [body]
                        ; call user function so the user has a chance to setup the body
                        (if (some? f)
                          (f body))
                        ; add body to the world on mount
                        (add-body-to-world! world body)
                        ; remove body on unmount
                        #(remove-body-from-world! world body))
         update-mesh! (fn [mesh body]
                        (when (some? mesh)
                          (set-position-copy! mesh (get-position body))
                          (set-quaternion-copy! mesh (get-quaternion body))))]
     ; FIXME: allow passing deps
     (use-effect :once
                 (manage-body! @current-body))
     (use-frame #(update-mesh! @mesh-ref @current-body))
     mesh-ref)))

; -- update loop ------------------------------------------------------------------------------------------------------------

(defn simulate-world! [world]
  (step-world! world (/ 1 60)))

; -- components -------------------------------------------------------------------------------------------------------------

; this is a react context provider providing access to physics world simulation
(defnc <world-provider> [props]
  (let [{:keys [children]} props
        world (use-state #(create-world))
        provider (get-context-provider context)
        world-setup-fn #(do (setup-world! @world) js/undefined)]
    (use-effect [world]
                (world-setup-fn))
    (use-frame #(simulate-world! @world))
    ($ provider {:value @world}
      children)))

(defnc <plane> [props]
  (let [{:keys [position]} props
        mesh-update-fn (fn [body]
                         (add-shape! body (create-plane))
                         (set-position! body position))
        mesh-ref (use-cannon mesh-update-fn {:mass 0})]
    ($ <mesh> {:ref            mesh-ref
               :receive-shadow true}
      ($ <plane-buffer-geometry> {:attach "geometry"
                                  :args   #js [1000 1000]})
      ($ <mesh-phong-material> {:attach "material"
                                :color  "#272727"}))))

(defnc <box> [props]
  (let [{:keys [position]} props
        currently-hovered? (use-state false)
        mesh-update-fn (fn [body]
                         (add-shape! body (create-box (create-vec3 1 1 1)))
                         (set-position! body position))
        mesh-ref (use-cannon mesh-update-fn {:mass 100000})]
    ($ <mesh> {:ref             mesh-ref
               :cast-shadow     true
               :receive-shadow  true
               :on-pointer-over #(reset! currently-hovered? true)
               :on-pointer-out  #(reset! currently-hovered? false)}
      ($ <box-buffer-geometry> {:attach "geometry"
                                :args   #js [2, 2, 2]})
      ($ <mesh-standard-material> {:attach "material"
                                   :color  (if @currently-hovered? "lightpink" "white")}))))

(defnc <demo> []
  (let [currently-showing-upper-plane? (use-state true)]
    ; when React removes (unmounts) the upper plane after 5 sec, objects should drop...
    ; this may seem like magic, but as the plane unmounts it removes itself from cannon and that's that
    (use-effect :once (schedule-removing-upper-plane! currently-showing-upper-plane?))
    ($ <canvas> {:camera     camera-config
                 :on-created init-canvas!}
      ($ <ambient-light> {:intensity 0.25})                                                                                   ; TODO: (investigate) our ambient lights have double intensity for some reason
      ($ <spot-light> {:intensity   0.6
                       :position    #js [30 30 50]
                       :angle       0.2
                       :penumbra    1
                       :cast-shadow true})
      ($ <world-provider> {}
        ($ <plane> {:position #js [0 0 -10]})
        ($ <box> {:position #js [1 0 1]})
        ($ <box> {:position #js [2 1 5]})
        ($ <box> {:position #js [0 0 6]})
        ($ <box> {:position #js [-1 1 8]})
        ($ <box> {:position #js [-2 2 13]})
        ($ <box> {:position #js [2 -1 13]})
        (if @currently-showing-upper-plane?
          ($ <plane> {:position #js [0 0 0]}))
        (if-not @currently-showing-upper-plane?
          ($ <box> {:position #js [0.5 1.5 20]}))))))
