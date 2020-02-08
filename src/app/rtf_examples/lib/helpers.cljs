(ns rtf-examples.lib.helpers
  (:require [cljs-bean.core :refer [bean]]
            [applied-science.js-interop :as j]
            [clojure.string :as string]))

(defn sin [x]
  (Math/sin x))

(defn cos [x]
  (Math/cos x))

(defn give-random-number []
  (Math/random))

(defn sin-pi [x]
  (Math/sin (* x Math/PI)))

(defn cos-pi [x]
  (Math/cos (* x Math/PI)))

(defn set-camera-layer! [camera layer]
  (j/call-in camera [.-layers .-set] layer))

(defn camera-look-at! [camera x y z]
  (j/call camera .-lookAt x y z))

(defn set-position!
  ([o v]
   (set-position! o (get v 0) (get v 1) (get v 2)))
  ([o x y z]
   (j/call-in o [.-position .-set] x y z)))

(defn get-position [o]
  (j/get o .-position))

(defn update-position! [o f]
  (let [r (get-position o)
        [x y z] (f (j/get r .-x) (j/get r .-y) (j/get r .-z))]
    (set-position! o x y z)))

(defn set-rotation!
  ([o v]
   (set-rotation! o (get v 0) (get v 1) (get v 2)))
  ([o x y z]
   (j/call-in o [.-rotation .-set] x y z)
   o))

(defn get-rotation [o]
  (j/get o .-rotation))

(defn update-rotation! [o f]
  (let [r (get-rotation o)
        [x y z] (f (j/get r .-x) (j/get r .-y) (j/get r .-z))]
    (set-rotation! o x y z)))

(defn set-scale!
  ([o v]
   (set-scale! o (get v 0) (get v 1) (get v 2)))
  ([o x y z]
   (j/call-in o [.-scale .-set] x y z)))

(defn get-scale [o]
  (j/get o .-scale))

(defn update-scale! [o f]
  (let [r (get-scale o)
        [x y z] (f (j/get r .-x) (j/get r .-y) (j/get r .-z))]
    (set-scale! o x y z)))

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

(defn get-match-param [match & [index]]
  (j/get-in match [.-params (or index 0)]))


(defn get-gltf-geometry [gltf & [index]]
  (j/get-in gltf [.-__$ (or index 0) .-geometry]))

(defn get-gltf-material [gltf & [index]]
  (j/get-in gltf [.-__$ (or index 0) .-material]))

(defn get-gltf-morph-target-dictionary [gltf & [index]]
  (j/get-in gltf [.-__$ (or index 0) .-morphTargetDictionary]))

(defn get-gltf-morph-target-influences [gltf & [index]]
  (j/get-in gltf [.-__$ (or index 0) .-morphTargetInfluences]))

(defn get-gltf-animation [gltf & [index]]
  (j/get-in gltf [.-animations (or index 0)]))

(defn interpolate [o f]
  (j/call o .-interpolate f))

(defn get-shape-uuid [o]
  (j/get-in o [.-shape .-uuid]))

(defn get-path-fill-opacity [path]
  (j/get-in path [.-userData .-style .-fillOpacity]))

(defn get-path-color [path]
  (j/get path .-color))

(defn path->raw-shapes
  ([path]
   (j/call path .-toShapes))
  ([path is-ccw?]
   (j/call path .-toShapes is-ccw?))
  ([path is-ccw? no-holes?]
   (j/call path .-toShapes is-ccw? no-holes?)))

(defn get-spring-color [spring]
  (j/get spring .-color))

(defn add-shape! [body shape]
  (.addShape body shape))

(defn get-context-provider [context]
  (.-Provider context))

(defn get-quaternion [o]
  (.-quaternion o))

(defn set-position-copy! [o source]
  (j/call-in o [.-position .-copy] source))

(defn set-quaternion-copy! [o source]
  (j/call-in o [.-quaternion .-copy] source))

(defn create-float32-array [a]
  (js/Float32Array. a))

(defn get-client-x [e]
  (.-clientX e))

(defn get-client-y [e]
  (.-clientY e))

(defn get-inner-width [w]
  (.-innerWidth w))

(defn get-inner-height [w]
  (.-innerHeight w))

(defn get-points [o subs]
  (.getPoints o subs))

(defn remove-trailing-slashes [s]
  (string/replace s #"/+$" ""))
