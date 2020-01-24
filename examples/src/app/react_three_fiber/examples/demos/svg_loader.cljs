(ns react-three-fiber.examples.demos.svg-loader
  (:require [uix.core.alpha :as uix]
            [cljs-bean.core :refer [bean ->js]]
            [react-three-fiber.core :refer [<:canvas> use-loader]]
            [react-three-fiber.examples.lib.misc :refer [svg-loader]]
            [react-three-fiber.examples.lib.react-spring-three :refer [animated use-spring use-transition]]
            [react-three-fiber.examples.lib.helpers :refer [camera-look-at!
                                                            interpolate
                                                            get-shape-uuid
                                                            get-path-fill-opacity
                                                            get-path-color
                                                            path->raw-shapes
                                                            get-spring-color]]))

; -- constants --------------------------------------------------------------------------------------------------------------

(def page-flip-delay 3000)
(def background-color-spring-delay 500)

(def svg-colors ["#21242d" "#ea5158" "#0d4663" "#ffbcb7" "#2d4a3e" "#8bd8d2"])
(def svg-names ["night" "city" "morning" "tubes" "woods" "beach"])
(def svg-urls (map #(str "resources/images/svg/" % ".svg") svg-names))

(def camera-config
  #js {:fov      90
       :position #js [0 0 550]
       :near     0.1
       :far      20000})

(def transition-spring-config
  #js {:mass     4
       :tension  500
       :friction 100})

(def background-color-spring-config
  #js {:mass     5
       :tension  800
       :friction 400})

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

(defn prepare-page-shapes [svgs]
  (for [svg svgs]
    (let [{:keys [paths]} (bean svg)]
      (flatten (map-indexed path->shapes paths)))))

(defn make-transition-spec []
  #js {:from   #js {:rotation #js [0 0.4 0]
                    :position #js [-500 0 0]
                    :opacity  0}
       :enter  #js {:rotation #js [0 0 0]
                    :position #js [0 0 0]
                    :opacity  1}
       :leave  #js {:rotation #js [0 -0.4 0]
                    :position #js [500 0 0]
                    :opacity  0}
       :order  #js ["leave" "enter" "update"]
       :config transition-spring-config
       :trail  5
       :lazy   true
       :unique true
       :reset  true})

(defn make-background-color-spring-config [page]
  #js {:from   #js {:color (first svg-colors)}
       :color  (nth svg-colors page)
       :delay  background-color-spring-delay
       :config background-color-spring-config})

; -- components -------------------------------------------------------------------------------------------------------------

(defn <scene> [props]
  (let [{:keys [urls]} props
        current-page (uix/state 0)
        flip-page! #(swap! current-page (fn [i] (js-mod (inc i) (count urls))))
        svgs (use-loader svg-loader (->js urls))
        pages (uix/memo #(->js (prepare-page-shapes svgs)) [svgs])
        background-color-spring (use-spring (make-background-color-spring-config @current-page))
        transitions (use-transition (nth pages @current-page) get-shape-uuid (make-transition-spec))]                         ; transition spec will be mutated
    (uix/effect! #(js/setInterval flip-page! page-flip-delay), [])
    [:<>
     ; lights
     [:ambientLight {:intensity 0.5}]
     [:spotLight {:intensity 0.5
                  :position  #js [300 300 4000]}]
     ; background plane
     [:mesh {:scale    #js [10000 10000 1]
             :rotation #js [0 -0.2 0]}
      [:planeBufferGeometry {:attach "geometry"
                             :args   #js [1 1]}]
      [:> (animated :meshPhongMaterial) {:attach     "material"
                                         :color      (get-spring-color background-color-spring)
                                         :depth-test false}]]
     ; page shapes
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
                                              :depth-write false
                                              :transparent true}]
           [:shapeBufferGeometry {:attach "geometry"
                                  :args   #js [shape]}]]))]]))

(defn <demo> []
  [<:canvas> {:invalidate-frameloop true
              :on-created           init-canvas!
              :camera               camera-config}
   [:# {:fallback "svg-loader demo is loading"}
    [<scene> {:urls svg-urls}]]])
