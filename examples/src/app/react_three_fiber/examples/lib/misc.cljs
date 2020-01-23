(ns react-three-fiber.examples.lib.misc
  (:require [cljs-bean.core :refer [bean ->js]]
            ["three/examples/jsm/loaders/GLTFLoader" :refer [GLTFLoader]]
            ["three/examples/jsm/loaders/SVGLoader" :refer [SVGLoader]]
            ["./../js/shaders/Backface" :default BackfaceMaterial]
            ["./../js/shaders/Refraction" :default RefractionMaterial]))

(def gltf-loader-class GLTFLoader)
(def svg-loader-class SVGLoader)

(def backface-material-class BackfaceMaterial)
(def refraction-material-class RefractionMaterial)

(defn create-backface-material []
  (backface-material-class.))

(defn create-refraction-material [options]
  (refraction-material-class. (->js options)))
