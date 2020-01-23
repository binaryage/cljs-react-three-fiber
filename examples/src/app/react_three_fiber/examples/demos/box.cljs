(ns react-three-fiber.examples.demos.box
  (:require [uix.core.alpha :as uix]
            [react-three-fiber.core :refer [<:canvas> use-frame]]
            [react-three-fiber.examples.lib.helpers :refer [update-rotation!]]))

; -- constants --------------------------------------------------------------------------------------------------------------

(def camera-config #js {:fov      75
                        :position #js [0 0 2]})

; -- helpers ----------------------------------------------------------------------------------------------------------------

(defn rotate! [node dx dy dz]
  (update-rotation! node (fn [x y z]
                           [(+ x dx) (+ y dy) (+ z dz)])))

; -- update loop ------------------------------------------------------------------------------------------------------------

(defn update! [mesh]
  (rotate! mesh 0.01 0.01 0))

; -- components -------------------------------------------------------------------------------------------------------------

(defn <box> []
  (let [mesh-ref (uix/ref)]
    (use-frame #(update! @mesh-ref))
    [:mesh {:ref             mesh-ref
            :on-click        #(js/console.log "click")
            :on-pointer-over #(js/console.log "hover")
            :on-pointer-out  #(js/console.log "unhover")}
     [:boxBufferGeometry {:attach "geometry"
                          :args   #js [1, 1, 1]}]
     [:meshNormalMaterial {:attach "material"}]]))

(defn <demo> []
  [<:canvas> {:camera camera-config}
   [<box>]])
