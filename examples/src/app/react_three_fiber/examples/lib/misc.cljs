(ns react-three-fiber.examples.lib.misc
  (:require [cljs-bean.core :refer [bean ->js]]
            [react-three-fiber.core :refer [extend-react]]
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

(defn extend-react-with-orbit-controls! []
  (extend-react #js {:OrbitControls OrbitControls}))
