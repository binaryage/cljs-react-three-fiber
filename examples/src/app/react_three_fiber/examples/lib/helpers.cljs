(ns react-three-fiber.examples.lib.helpers
  (:require [cljs-bean.core :refer [bean]]
            [applied-science.js-interop :as j]))

(defn give-random-number []
  (Math/random))

(defn sin-pi [x]
  (Math/sin (* x Math/PI)))

(defn cos-pi [x]
  (Math/cos (* x Math/PI)))

(defn set-camera-layer! [camera layer]
  (j/call-in camera [.-layers .-set] layer))

(defn set-position!
  ([o v]
   (set-position! o (get v 0) (get v 1) (get v 2)))
  ([o x y z]
   (j/call-in o [.-position .-set] x y z)))

(defn set-rotation!
  ([o v]
   (set-rotation! o (get v 0) (get v 1) (get v 2)))
  ([o x y z]
   (j/call-in o [.-rotation .-set] x y z)))

(defn set-scale!
  ([o v]
   (set-scale! o (get v 0) (get v 1) (get v 2)))
  ([o x y z]
   (j/call-in o [.-scale .-set] x y z)))

(defn delta-rotation
  ([rotation dv]
   (delta-rotation rotation (get dv 0) (get dv 1) (get dv 2)))
  ([rotation dx dy dz]
   #js[(+ (j/get rotation 0) dx)
       (+ (j/get rotation 1) dy)
       (+ (j/get rotation 2) dz)]))

(defn transform-vec [v f]
  (f (aget v 0) (aget v 1) (aget v 2)))

(defn update-vec! [v f]
  (when-some [res (transform-vec v f)]
    (aset v 0 (aget res 0))
    (aset v 1 (aget res 1))
    (aset v 2 (aget res 2)))
  v)

(defn get-gltf-geometry [gltf & [index]]
  (j/get-in gltf [.-__$ (or index 0) .-geometry]))

(defn set-material! [model material]
  (j/assoc! model .-material material))

(defn get-texture [material]
  (.-texture material))

(defn set-min-filter! [texture filter]
  (j/assoc! texture .-minFilter filter))

(defn update-matrix! [model]
  (.updateMatrix model))

(defn set-matrix-at! [model index matrix]
  (.setMatrixAt model index matrix))

(defn get-elapsed-time [clock]
  (.getElapsedTime clock))

(defn get-matrix [model]
  (.-matrix model))

(defn set-instance-matrix-needs-update! [model val]
  (j/assoc-in! model [.-instanceMatrix .-needsUpdate] val))
