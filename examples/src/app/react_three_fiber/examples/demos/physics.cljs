(ns react-three-fiber.examples.demos.physics
  (:require [uix.core.alpha :as uix]
            [cljs-bean.core :refer [bean ->js]]
            [react-three-fiber.core :refer [<:canvas> use-frame]]
            [react-three-fiber.examples.lib.react :refer [create-context use-context]]
            [react-three-fiber.examples.lib.three :refer [pcf-soft-shadow-map]]
            [react-three-fiber.examples.lib.helpers :refer [set-position!
                                                            add-shape!
                                                            get-context-provider
                                                            get-position
                                                            get-quaternion
                                                            set-position-copy!
                                                            set-quaternion-copy!]]
            [react-three-fiber.examples.lib.cannon :refer [create-world
                                                           create-naive-broadphase
                                                           create-body
                                                           create-plane
                                                           create-box
                                                           create-vec3
                                                           add-body-to-world!
                                                           remove-body-from-world!]]
            [react-three-fiber.examples.lib.gl :refer [with-gl!]]
            [applied-science.js-interop :as j]))

; -- constants --------------------------------------------------------------------------------------------------------------

(def plane-removal-delay 5000)
(def camera-config #js {:position #js [0 0 15]})

; -- context ----------------------------------------------------------------------------------------------------------------

(def context (create-context))

; -- helpers ----------------------------------------------------------------------------------------------------------------

(defn assign-keys [components]
  (let [assign-key (fn [index [c props]]
                     (if (some? c)
                       [c (assoc props :key index)]))]
    (map-indexed assign-key components)))

(defn init-canvas! [props]
  (let [{:keys [gl]} (bean props)]
    (with-gl! gl
      (:shadow-map :-enabled true)
      (:shadow-map :-type pcf-soft-shadow-map))))

(defn setup-world! [world]
  (-> world
      (j/assoc! .-broadphase (create-naive-broadphase))
      (j/assoc-in! [.-solver .-iterations] 10)
      (j/call-in [.-gravity .-set] 0 0 -25)))

(defn schedule-removing-upper-plane! [plane-state-atom]
  (js/setTimeout #(reset! plane-state-atom false) plane-removal-delay))

; -- hooks ------------------------------------------------------------------------------------------------------------------

; custom hook to maintain a world physics body
(defn use-cannon
  ([f props] (use-cannon f props []))
  ([f props deps]
   (let [ref (uix/ref)
         world (use-context context)
         current-body (uix/state #(create-body (->js props)))
         manage-body! (fn [body]
                        ; call user function so the user can setup the body
                        (if (some? f)
                          (f body))
                        ; add body to the world on mount
                        (add-body-to-world! world body)
                        ; remove body on unmount
                        #(remove-body-from-world! world body))]
     (uix/effect! #(manage-body! @current-body) deps)
     (use-frame (fn []
                  (when-some [r @ref]
                    (let [body @current-body]
                      (set-position-copy! r (get-position body))
                      (set-quaternion-copy! r (get-quaternion body))))))
     ref)))


; -- update loop ------------------------------------------------------------------------------------------------------------

(defn simulate-world! [world]
  (.step world (/ 1 60)))

; -- components -------------------------------------------------------------------------------------------------------------

; this is a react context provider providing access to physics world simulation
(defn <world-provider> [& children]
  (let [world (uix/state #(create-world))
        provider (get-context-provider context)
        setup-fn #(do (setup-world! @world) js/undefined)]
    (uix/effect! setup-fn [world])
    (use-frame #(simulate-world! @world))
    [:> provider {:value @world}
     (assign-keys children)]))

(defn <plane> [props]
  (let [{:keys [position]} props
        mesh-update-fn (fn [body]
                         (add-shape! body (create-plane))
                         (set-position! body position))
        mesh-ref (use-cannon mesh-update-fn {:mass 0})]
    [:mesh {:ref            mesh-ref
            :receive-shadow true}
     [:planeBufferGeometry {:attach "geometry"
                            :args   #js [1000 1000]}]
     [:meshPhongMaterial {:attach "material"
                          :color  "#272727"}]]))

(defn <box> [props]
  (let [{:keys [position]} props
        currently-hovered? (uix/state false)
        mesh-update-fn (fn [body]
                         (add-shape! body (create-box (create-vec3 1 1 1)))
                         (set-position! body position))
        mesh-ref (use-cannon mesh-update-fn {:mass 100000})]
    [:mesh {:ref             mesh-ref
            :cast-shadow     true
            :receive-shadow  true
            :on-pointer-over #(reset! currently-hovered? true)
            :on-pointer-out  #(reset! currently-hovered? false)}
     [:boxBufferGeometry {:attach "geometry"
                          :args   #js [2, 2, 2]}]
     [:meshStandardMaterial {:attach "material"
                             :color  (if @currently-hovered? "lightpink" "white")}]]))

(defn <demo> []
  (let [currently-showing-upper-plane? (uix/state true)]
    ; when React removes (unmounts) the upper plane after 5 sec, objects should drop...
    ; this may seem like magic, but as the plane unmounts it removes itself from cannon and that's that
    (uix/effect! #(schedule-removing-upper-plane! currently-showing-upper-plane?) [])
    [<:canvas> {:camera     camera-config
                :on-created init-canvas!}
     [:ambientLight {:intensity 0.25}]                                                                                        ; TODO: (investigate) our ambient lights have double intensity for some reason
     [:spotLight {:intensity   0.6
                  :position    #js [30 30 50]
                  :angle       0.2
                  :penumbra    1
                  :cast-shadow true}]
     [<world-provider>
      [<plane> {:position #js [0 0 -10]}]
      (if @currently-showing-upper-plane?
        [<plane> {:position #js [0 0 0]}])
      [<box> {:position #js [1 0 1]}]
      [<box> {:position #js [2 1 5]}]
      [<box> {:position #js [0 0 6]}]
      [<box> {:position #js [-1 1 8]}]
      [<box> {:position #js [-2 2 13]}]
      [<box> {:position #js [2 -1 13]}]
      (if-not @currently-showing-upper-plane?
        [<box> {:position #js [0.5 1.5 20]}])]]))
