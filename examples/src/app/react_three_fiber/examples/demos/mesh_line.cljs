(ns react-three-fiber.examples.demos.mesh-line
  (:require [uix.core.alpha :as uix]
            [react-three-fiber.core :refer [<:canvas> use-three use-frame]]
            [react-three-fiber.examples.lib.helpers :refer [update-rotation!
                                                            update-position!
                                                            give-random-number]]
            [react-three-fiber.examples.lib.three :refer [create-vector3
                                                          create-catmull-rom-curve3]]
            [react-three-fiber.examples.lib.mesh-line]
            [cljs-bean.core :refer [bean ->js]]
            [applied-science.js-interop :as j]))

; -- constants --------------------------------------------------------------------------------------------------------------

(def line-subdivisions 1000)
(def line-points-num 30)
(def line-colors ["#A2CCB6" "#FCEEB5" "#EE786E" "#e0feff" "lightpink" "lightblue"])

; -- updates ----------------------------------------------------------------------------------------------------------------

(defn update-mouse! [mouse-ref e]
  (let [x (- (.-clientX e) (/ (.-innerWidth js/window) 2))
        y (- (.-clientY e) (/ (.-innerHeight js/window) 2))]
    (reset! mouse-ref [x y])))

; -- components -------------------------------------------------------------------------------------------------------------

(defn <fat-line> [props]
  (let [{:keys [curve width color speed]} props
        material-ref (uix/ref)]
    (use-frame (fn []
                 (j/update-in! @material-ref [.-uniforms .-dashOffset .-value] #(- % speed))))
    [:mesh
     [:meshLine {:attach "geometry" :vertices curve}]
     [:meshLineMaterial {:attach      "material"
                         :ref         material-ref
                         :transparent true
                         :depth-test  false
                         :line-width  width
                         :color       color
                         :dash-array  0.1
                         :dash-ratio  0.9}]]))

(defn <lines> [props]
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
                                            (.add displacement)
                                            (.clone))))
                           points (map point-fn (range line-points-num))
                           curve (-> (->js points)
                                     (create-catmull-rom-curve3)
                                     (.getPoints line-subdivisions))]
                       {:key   index
                        :color (nth colors (Math/floor (* r4 num-colors)))
                        :width (Math/max 0.1 (* 0.65 r5))
                        :speed (Math/max 0.0001 (* 0.0005 r6))
                        :curve curve})))
        lines (uix/memo lines-fn [total colors])]
    (for [line-props lines]
      [<fat-line> line-props])))

(defn <rig> [props]
  (let [{:keys [mouse-ref]} props
        {:keys [camera]} (use-three)]
    (use-frame (fn []
                 (update-position! camera (fn [x y z]
                                            (let [[mx my] @mouse-ref
                                                  new-x (+ x (* 0.05 (- (/ mx 50) x)))
                                                  new-y (+ y (* 0.05 (- (/ my 50) y)))]
                                              [new-x new-y z])))
                 (.lookAt camera 0 0 0)))
    nil))

(defn <demo> []
  (let [mouse-ref (uix/ref [0 0])]
    [<:canvas> {:style         {:background "#ffc9e7"}
                :camera        #js {:fov 25 :position #js [0 0 10]}
                :on-mouse-move (partial update-mouse! mouse-ref)}
     [<lines> {:total 200 :colors line-colors}]
     [<rig> {:mouse-ref mouse-ref}]]))
