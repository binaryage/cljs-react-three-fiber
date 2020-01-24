(ns react-three-fiber.examples.lib.misc
  (:require [cljs-bean.core :refer [bean ->js]]
            [react-three-fiber.core :refer [extend-react]]
            ["three/examples/jsm/loaders/GLTFLoader" :refer [GLTFLoader]]
            ["three/examples/jsm/loaders/SVGLoader" :refer [SVGLoader]]
            ["three/examples/jsm/loaders/DRACOLoader" :refer [DRACOLoader]]
            ["three/examples/jsm/controls/OrbitControls" :refer [OrbitControls]]
            ["./../js/shaders/Backface" :default BackfaceMaterial]
            ["./../js/shaders/Refraction" :default RefractionMaterial]))

(def gltf-loader GLTFLoader)
(def svg-loader SVGLoader)
(def draco-loader DRACOLoader)

(defn create-draco-loader []
  (draco-loader.))

(def backface-material-class BackfaceMaterial)
(def refraction-material-class RefractionMaterial)

(defn create-backface-material []
  (backface-material-class.))

(defn create-refraction-material [options]
  (refraction-material-class. (->js options)))

(def orbit-controls OrbitControls)

(defn extend-react-with-orbit-controls! []
  (extend-react #js {:OrbitControls OrbitControls}))
