(ns react-three-fiber.examples.demos.montage
  (:require [cljs-bean.core :refer [->js bean]]
            [react-three-fiber.examples.lib.ui :refer [defnc $ $$ use-effect <canvas>]]
            [react-three-fiber.core :refer [use-three use-frame]]
            [react-three-fiber.examples.lib.helpers :refer [update-rotation! give-random-number]]
            [react-three-fiber.examples.lib.react-spring-three :refer [use-springs animated]]
            [react-three-fiber.examples.lib.three :refer [deg-to-rad]]))

; -- constants --------------------------------------------------------------------------------------------------------------

(def number 35)
(def colors ["#A2CCB6" "#FCEEB5" "#EE786E" "#e0feff" "lightpink" "lightblue"])
(def shapes ["planeBufferGeometry" "planeBufferGeometry" "planeBufferGeometry"])

; -- helpers ----------------------------------------------------------------------------------------------------------------

(defn random [i]
  (let [[r1 r2 r3 r4 r5] (repeatedly give-random-number)]
    {:position #js [(- 100 (* 200 r1))
                    (- 100 (* 200 r2))
                    (* 1.5 i)]
     :color    (nth colors (Math/floor (* r3 (count colors))))
     :scale    #js [(+ 1 (* r4 14))
                    (+ 1 (* r4 14))
                    1]
     :rotation #js [0
                    0
                    (deg-to-rad (Math/round (* r5 45)))]}))

(def data (for [_i (range number)]
            (let [[r1 r2 r3 r4] (repeatedly give-random-number)
                  shape (nth shapes (Math/floor (* r1 (count shapes))))]
              {:shape shape
               :color (nth colors (Math/floor (* r2 (count colors))))
               :args  #js [(+ 0.1 (* r3 9))
                           (+ 0.1 (* r4 9))
                           10]})))

; -- components -------------------------------------------------------------------------------------------------------------

(defnc <content> []
  (let [spring-fn (fn [i]
                    (->js (merge (random i)
                                 {:from   (random i)
                                  :config {:mass     20
                                           :tension  150
                                           :friction 50}})))
        [springs set-springs!] (use-springs number spring-fn)
        update-fn #(set-springs! (fn [i]
                                   (->js (merge (random i)
                                                {:delay (* i 40)}))))]
    (use-effect :once
                (js/setInterval update-fn 3000))

    ($ :group
      (for [[index d] (map-indexed vector data)]
        (let [spring (bean (aget springs index))]
          ($$ (animated :mesh) (merge spring {:key           index
                                              :castShadow    true
                                              :receiveShadow true})
            ($ :boxBufferGeometry {:attach "geometry"
                                   :args   (:args d)})
            ($ (animated :meshStandardMaterial) {:attach    "material"
                                                 :color     (:color spring)
                                                 :roughness 0.75})))))))

(defnc <lights> []
  ($ :group
    ($ :pointLight {:intensity 0.3})
    ; TODO: it looks like our ambient light is applied twice
    ($ :ambientLight {:intensity 1})
    ($ :spotLight {:cast-shadow            true
                   :intensity              0.2
                   :angle                  (/ js/Math.PI 7)
                   :position               #js [150 150 250]
                   :penumbra               1
                   :'shadow-mapSize-width  2048
                   :'shadow-mapSize-height 2048})))

(defnc <desktop> []
  ($ :mesh {:receive-shadow true}
    ($ :planeBufferGeometry {:attach "geometry" :args #js [1000 1000]})
    ($ :meshStandardMaterial {:attach "material" :color "#A2ACB6" :roughness 1})))

(defnc <demo> []
  ($ <canvas> {:shadowMap true
               :style     {:background "#A2CCB6"}
               :camera    #js {:fov 100 :position #js [0 0 100]}}
    ($ <lights>)
    ($ <desktop>)
    ($ <content>)))
