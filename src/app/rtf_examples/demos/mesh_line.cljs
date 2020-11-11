(ns rtf-examples.demos.mesh-line
  (:require [rtf-examples.lib.ui :refer [$ defnc use-ref use-memo <canvas>]]
            [rtf-examples.lib.rtf :refer [use-three use-frame]]
            [rtf-examples.lib.helpers :refer [update-rotation!
                                              update-position!
                                              give-random-number
                                              get-client-x get-client-y
                                              get-inner-width get-inner-height
                                              get-points]]
            [rtf-examples.lib.three :refer [create-vector3
                                            vector3-add!
                                            vector3-clone
                                            camera-look-at!
                                            create-catmull-rom-curve3
                                            <mesh>]]
            [rtf-examples.lib.mesh-line :refer [<mesh-line> <mesh-line-material>]]
            [cljs-bean.core :refer [bean ->js]]
            [applied-science.js-interop :as j]))

; -- constants --------------------------------------------------------------------------------------------------------------

(def line-subdivisions 1000)
(def line-points-num 30)
(def line-colors ["#A2CCB6" "#FCEEB5" "#EE786E" "#e0feff" "lightpink" "lightblue"])

; -- updates ----------------------------------------------------------------------------------------------------------------

(defn update-mouse! [mouse-ref e]
  (let [x (- (get-client-x e) (/ (get-inner-width js/window) 2))
        y (- (get-client-y e) (/ (get-inner-height js/window) 2))]
    (reset! mouse-ref [x y])))

; -- components -------------------------------------------------------------------------------------------------------------

(defnc <fat-line> [props]
  (let [{:keys [curve width color speed]} props
        material-ref (use-ref nil)]
    (use-frame (fn []
                 (j/update-in! @material-ref [:uniforms :dashOffset :value] #(- % speed))))
    ($ <mesh>
      ($ <mesh-line> {:attach "geometry" :vertices curve})
      ($ <mesh-line-material> {:ref         material-ref
                               :transparent true
                               :depth-test  false
                               :line-width  width
                               :color       color
                               :dash-array  0.1
                               :dash-ratio  0.9}))))

(defnc <lines> [props]
  (let [{:keys [total colors]} props
        num-colors (count colors)
        lines-fn (fn []
                   (for [index (range total)]
                     (let [[r1 r2 r3 r4 r5 r6] (repeatedly give-random-number)
                           pos (create-vector3 (- 10 (* 20 r1))
                                               (- 10 (* 20 r2))
                                               (- 10 (* 20 r3)))
                           point-fn (fn []
                                      (let [[r1 r2 r3] (repeatedly give-random-number)
                                            displacement (create-vector3 (- 4 (* r1 8))
                                                                         (- 4 (* r2 8))
                                                                         (- 2 (* r3 4)))]
                                        (-> pos
                                            (vector3-add! displacement)
                                            (vector3-clone))))
                           points (map point-fn (range line-points-num))
                           curve (-> (->js points)
                                     (create-catmull-rom-curve3)
                                     (get-points line-subdivisions))]
                       {:key   index
                        :color (nth colors (Math/floor (* r4 num-colors)))
                        :width (Math/max 0.1 (* 0.65 r5))
                        :speed (Math/max 0.0001 (* 0.0005 r6))
                        :curve curve})))
        lines (use-memo [total colors] (lines-fn))]
    (for [line-props lines]
      ($ <fat-line> {:& line-props}))))

(defnc <rig> [props]
  (let [; FIXME:
        {:keys [mouseRef]} props
        mouse-ref mouseRef
        {:keys [camera]} (use-three)]
    (use-frame (fn []
                 (update-position! camera (fn [x y z]
                                            (let [[mx my] @mouse-ref
                                                  new-x (+ x (* 0.05 (- (/ mx 50) x)))
                                                  new-y (+ y (* 0.05 (- (/ my 50) y)))]
                                              [new-x new-y z])))
                 (camera-look-at! camera 0 0 0)))
    nil))

(defnc <demo> []
  (let [mouse-ref (use-ref [0 0])]
    ($ <canvas> {:style         {:background "#ffc9e7"}
                 :camera        #js {:fov 25 :position #js [0 0 10]}
                 :on-mouse-move (partial update-mouse! mouse-ref)}
      ($ <lines> {:total 200 :colors line-colors})
      ($ <rig> {:mouse-ref mouse-ref}))))
