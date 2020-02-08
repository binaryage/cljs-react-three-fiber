(ns react-three-fiber.examples.lib.misc
  (:require [react-three-fiber.examples.lib.rtf :refer [extend-react]]
            [applied-science.js-interop :as j]
            ["three/examples/jsm/loaders/GLTFLoader" :refer [GLTFLoader]]
            ["three/examples/jsm/loaders/SVGLoader" :refer [SVGLoader]]
            ["three/examples/jsm/loaders/DRACOLoader" :refer [DRACOLoader]]
            ["three/examples/jsm/controls/OrbitControls" :refer [OrbitControls]]))

(def gltf-loader GLTFLoader)
(def svg-loader SVGLoader)
(def draco-loader DRACOLoader)

(defn create-draco-loader []
  (draco-loader.))

(def orbit-controls OrbitControls)

(def <orbit-controls> "orbitControls")

(defn extend-react-with-orbit-controls! []
  (extend-react #js {:OrbitControls OrbitControls}))

(defn apply-draco-extension [path loader]
  (let [draco-loader (create-draco-loader)]
    (j/assoc! draco-loader .-decoderPath path)
    (.setDRACOLoader loader draco-loader)))

(defn get-dom-element [gl]
  (.-domElement gl))

(defn update-controls [controls]
  (.update controls))
