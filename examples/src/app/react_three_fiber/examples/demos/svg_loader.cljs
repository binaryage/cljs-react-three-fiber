(ns react-three-fiber.examples.demos.svg-loader
  (:require [uix.core.alpha :as uix]
            [cljs-bean.core :refer [bean ->js]]
            [react-three-fiber.core :refer [<:canvas> use-loader]]
            [react-three-fiber.examples.lib.misc :refer [svg-loader-class]]
            [react-three-fiber.examples.lib.react-spring-three :refer [animated use-spring use-transition]]
            [react-three-fiber.examples.lib.helpers :refer [camera-look-at!
                                                            interpolate
                                                            get-shape-uuid
                                                            get-path-fill-opacity
                                                            get-path-color
                                                            path->raw-shapes
                                                            get-spring-color]]))

; -- constants --------------------------------------------------------------------------------------------------------------

(def colors ["#21242d" "#ea5158" "#0d4663" "#ffbcb7" "#2d4a3e" "#8bd8d2"])
(def svg-names ["night" "city" "morning" "tubes" "woods" "beach"])
(def all-urls (map #(str "resources/images/svg/" % ".svg") svg-names))

; -- helpers ----------------------------------------------------------------------------------------------------------------

(defn init-canvas! [props]
  (let [{:keys [camera]} (bean props)]
    (camera-look-at! camera 0 0 0)))

(defn path->shapes [index path]
  (let [raw-shapes (path->raw-shapes path true)
        raw-shape->shape (fn raw-shape->shape [shape]
                           #js {:shape       shape
                                :color       (get-path-color path)
                                :fillOpacity (get-path-fill-opacity path)
                                :index       index})]
    (map raw-shape->shape raw-shapes)))

(defn prepare-page-shapes-map [svgs]
  (for [svg svgs]
    (let [{:keys [paths]} (bean svg)]
      (flatten (map-indexed path->shapes paths)))))

; -- components -------------------------------------------------------------------------------------------------------------

(defn <scene> [props]
  (let [{:keys [urls]} props
        current-page (uix/state 0)
        flip-page! #(swap! current-page (fn [i] (js-mod (inc i) (count urls))))
        svgs (use-loader svg-loader-class (->js urls))
        page-shapes-map (uix/memo #(->js (prepare-page-shapes-map svgs)) [svgs])
        spring (use-spring #js {:from   #js {:color (first colors)}
                                :color  (nth colors @current-page)
                                :delay  500
                                :config #js {:mass     5
                                             :tension  800
                                             :friction 400}})
        transitions (use-transition (nth page-shapes-map @current-page)
                                    get-shape-uuid
                                    #js {:from   #js {:rotation #js [0 0.4 0] :position #js [-500 0 0] :opacity 0}
                                         :enter  #js {:rotation #js [0 0 0] :position #js [0 0 0] :opacity 1}
                                         :leave  #js {:rotation #js [0 -0.4 0] :position #js [500 0 0] :opacity 0}
                                         :order  #js ["leave" "enter" "update"]
                                         :config #js {:mass 4 :tension 500 :friction 100}
                                         :trail  5
                                         :lazy   true
                                         :unique true
                                         :reset  true})]
    (uix/effect! #(js/setInterval flip-page! 3000), [])
    [:<>
     [:ambientLight {:intensity 0.5}]
     [:spotLight {:intensity 0.5
                  :position  #js [300 300 4000]}]
     [:mesh {:scale    #js [10000 10000 1]
             :rotation #js [0 -0.2 0]}
      [:planeBufferGeometry {:attach "geometry"
                             :args   #js [1 1]}]
      [:> (animated :meshPhongMaterial) {:attach    "material"
                                         :color     (get-spring-color spring)
                                         :depthTest false}]]
     [:> (animated :group) {:position #js [1220 700 @current-page]
                            :rotation #js [0 0 Math/PI]}
      (for [transition transitions]
        (let [{:keys [item key props]} (bean transition)
              {:keys [shape color fillOpacity index]} (bean item)
              {:keys [opacity position rotation]} (bean props)
              position (interpolate position (fn [x y z] #js [x y (+ z index)]))
              opacity (interpolate opacity (fn [o] (* o fillOpacity)))]
          [:> (animated :mesh) {:key      key
                                :rotation rotation
                                :position position}
           [:> (animated :meshPhongMaterial) {:attach      "material"
                                              :color       color
                                              :opacity     opacity
                                              :depthWrite  false
                                              :transparent true}]
           [:shapeBufferGeometry {:attach "geometry"
                                  :args   #js [shape]}]]))]]))

(defn <demo> []
  [<:canvas> {:invalidate-frameloop true
              :on-created           init-canvas!
              :camera               #js {:fov      90
                                         :position #js [0 0 550]
                                         :near     0.1
                                         :far      20000}}
   [:# {:fallback "svg-loader demo is loading"}
    [<scene> {:urls all-urls}]]])
