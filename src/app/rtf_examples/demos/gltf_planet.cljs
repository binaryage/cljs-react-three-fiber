(ns rtf-examples.demos.gltf-planet
  (:require [cljs-bean.core :refer [bean]]
            [rtf-examples.lib.rtf :refer [use-frame use-three use-loader]]
            [rtf-examples.lib.ui :refer [<canvas> $ use-ref use-memo defnc]]
            [rtf-examples.lib.react :refer [<suspense>]]
            [rtf-examples.lib.helpers :refer [get-gltf-geometry
                                                            get-gltf-material
                                                            give-random-number
                                                            create-float32-array]]
            [rtf-examples.lib.misc :refer [gltf-loader
                                                         create-draco-loader
                                                         apply-draco-extension
                                                         extend-react-with-orbit-controls!
                                                         <orbit-controls>
                                                         update-controls
                                                         get-dom-element]]
            [rtf-examples.lib.three :refer [<group>
                                                          <object3d>
                                                          <points>
                                                          <mesh>
                                                          <mesh-standard-material>
                                                          <buffer-geometry>
                                                          <buffer-attribute>
                                                          <points-material>
                                                          <ambient-light>
                                                          <point-light>
                                                          <spot-light>
                                                          <fog>]]
            [applied-science.js-interop :as j]))

(extend-react-with-orbit-controls!)

; -- constants --------------------------------------------------------------------------------------------------------------

(def planet-url "/resources/gltf/planet.gltf")

; -- helpers ----------------------------------------------------------------------------------------------------------------

(defn get-random-star-position []
  (let [[r1 r2] (repeatedly give-random-number)]
    (* (+ 50 (* r1 1000)) (if (< r2 0.5) -1 1))))

; -- components -------------------------------------------------------------------------------------------------------------

(defnc <gltf-mesh> [props]
  (let [{:keys [gltf layer]} props
        mesh-props (dissoc props :gltf :layer)]
    ($ <mesh> {:& mesh-props}
      ($ <buffer-geometry> {:attach "geometry"
                            :&      (bean (get-gltf-geometry gltf layer))})
      ($ <mesh-standard-material> {:attach    "material"
                                   :roughness 1
                                   :&         (bean (get-gltf-material gltf layer))}))))

(defnc <planet> []
  (let [group-ref (use-ref nil)
        gltf (use-loader gltf-loader planet-url (partial apply-draco-extension "/draco-gltf/"))]
    ($ <group> {:ref group-ref}
      ($ <object3d> {:rotation #js [-1.5708 0 0]}
        ($ <object3d> {:position #js [-0.0033 0.0237 -6.3311]
                       :rotation #js [0.2380 -0.5454 0.5623]
                       :scale    #js [7 7 7]}
          ($ <gltf-mesh> {:gltf  gltf
                          :layer 5})
          ($ <gltf-mesh> {:gltf           gltf
                          :layer          6
                          :receive-shadow true
                          :cast-shadow    true}))))))

(defnc <stars> [props]
  (let [{:keys [star-count]
         :or   {star-count 5000}} props
        positions (use-memo [star-count]
                            (let [positions #js []]
                              (doseq [_i (range star-count)]
                                (j/push! positions (get-random-star-position))
                                (j/push! positions (get-random-star-position))
                                (j/push! positions (get-random-star-position)))
                              (create-float32-array positions)))]
    ($ <points>
      ($ <buffer-geometry> {:attach "geometry"}
        ($ <buffer-attribute> {:attach-object #js ["attributes" "position"]
                               :count         star-count
                               :array         positions
                               :item-size     3}))
      ($ <points-material> {:attach           "material"
                            :size             2
                            :size-attenuation true
                            :color            "white"
                            :transparent      true
                            :opacity          0.8
                            :fog              false}))))

(defnc <controls> [props]
  (let [{:keys [gl camera]} (use-three)
        controls-ref (use-ref nil)]
    (use-frame #(update-controls @controls-ref))
    ($ <orbit-controls> {:ref  controls-ref
                         :args #js [camera (get-dom-element gl)]
                         :&    props})))

(defnc <demo> []
  ($ <canvas> {:style      {:background "radial-gradient(at 50% 70%, #200f20 40%, #090b1f 80%, #050523 100%)"}
               :camera     #js {:position #js [0 0 15]}
               :shadow-map true}
    ($ <ambient-light> {:intensity 0.2})
    ($ <point-light> {:intensity 20
                      :position  #js [-10 -25 -10]
                      :color     "#200f20"})
    ($ <spot-light> {:cast-shadow            true
                     :intensity              4
                     :angle                  (/ Math/PI 8)
                     :position               #js [15 25 5]
                     :'shadow-mapSize-width  2048
                     :'shadow-mapSize-height 2048})
    ($ <fog> {:attach "fog" :args #js ["#090b1f" 0 25]})
    ($ <suspense> {:fallback "loading gltf-planet demo..."}
      ($ <planet>)
      ($ <stars>))
    ($ <controls> {:auto-rotate     true
                   :enable-pan      false
                   :enable-zoom     false
                   :enable-damping  true
                   :damping-factor  0.5
                   :rotate-speed    1
                   :max-polar-angle (/ Math/PI 2)
                   :min-polar-angle (/ Math/PI 2)})))
