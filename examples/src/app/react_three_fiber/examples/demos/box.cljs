(ns react-three-fiber.examples.demos.box
  (:require [react-three-fiber.core :refer [<:canvas> use-three use-frame]]
            [uix.core.alpha :as uix]
            [applied-science.js-interop :as j]
            [react-three-fiber.examples.lib.helpers :refer [update-rotation!]]))

(defn rotate! [node]
  (update-rotation! node (fn [x y z]
                           [(+ x 0.01) (+ y 0.01) z])))

(defn update! [mesh]
  (rotate! mesh))

(defn <box> []
  (let [mesh-ref (uix/ref)]
    (use-frame #(update! @mesh-ref))
    [:mesh {:ref             mesh-ref
            :on-click        #(js/console.log "click")
            :on-pointer-over #(js/console.log "hover")
            :on-pointer-out  #(js/console.log "unhover")}
     [:boxBufferGeometry {:attach "geometry" :args #js [1, 1, 1]}]
     [:meshNormalMaterial {:attach "material"}]]))

(defn <app> []
  [<:canvas> {:camera #js {:fov 75 :position #js [0 0 2]}}
   [:# {:fallback "loading box demo"}
    [<box>]]])
