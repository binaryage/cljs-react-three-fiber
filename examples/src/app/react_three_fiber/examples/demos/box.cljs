(ns react-three-fiber.examples.demos.box
  (:require [react-three-fiber.core :refer [<:canvas> use-three use-frame]]
            [uix.core.alpha :as uix]
            [applied-science.js-interop :as j]))

(defn rotate! [node]
  (j/update-in! node [.-rotation .-x] #(+ % 0.01))
  (j/update-in! node [.-rotation .-y] #(+ % 0.01)))

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
   [:# {:fallback nil}
    [<box>]]])
