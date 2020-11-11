(ns rtf-examples.demos.physics
  (:require [cljs-bean.core :refer [bean ->js]]
            [rtf-examples.lib.ui :refer [$ defnc <canvas>]]
            [rtf-examples.lib.three :refer [<mesh>
                                            <color>
                                            <spot-light>
                                            <hemisphere-light>
                                            <plane-buffer-geometry>
                                            <box-buffer-geometry>
                                            <shadow-material>
                                            <mesh-lambert-material>]]
            [rtf-examples.lib.cannon :refer [<physics>
                                             use-plane
                                             use-box]]))

; -- constants --------------------------------------------------------------------------------------------------------------

(def half-pi (/ Math/PI 2))

; -- components -------------------------------------------------------------------------------------------------------------

(defnc <plane> [props]
  (let [plane-params (merge {:rotation #js [(- half-pi) 0 0]} props)
        mesh-ref (first (use-plane #(->js plane-params)))]
    ($ <mesh> {:ref            mesh-ref
               :receive-shadow true}
      ($ <plane-buffer-geometry> {:args #js [1009 1000]})
      ($ <shadow-material> {:color "#171717"}))))

(defnc <cube> [props]
  (let [box-params (merge {:mass     1
                           :position #js [0 5 0]
                           :rotation #js [0.4 0.2 0.5]}
                          props)
        mesh-ref (first (use-box #(->js box-params)))]
    ($ <mesh> {:receive-shadow true
               :cast-shadow    true
               :ref            mesh-ref}
      ($ <box-buffer-geometry>)
      ($ <mesh-lambert-material> {:color "hotpink"}))))

(defnc <demo> []
  ($ <canvas> {:shadow-map true
               :gl         #js {:alpha false}
               :camera     #js {:position #js [-1 2 5] :fov 50}}
    ($ <color> {:attach "background" :args #js ["lightblue"]})
    ($ <hemisphere-light> {:intensity 0.35})
    ($ <spot-light> {:position    #js [10 10 10]
                     :angle       0.3
                     :penumbra    1
                     :intensity   2
                     :cast-shadow true})
    ($ <physics>
      ($ <plane>)
      ($ <cube>)
      ($ <cube> {:position #js [0 10 -2]})
      ($ <cube> {:position #js [0 20 -2]}))))
