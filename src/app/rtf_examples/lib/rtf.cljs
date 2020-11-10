(ns rtf-examples.lib.rtf
  "A lightweight convenience wrapper for react-three-fiber library.

   https://github.com/react-spring/react-three-fiber"
  (:require [react-three-fiber :refer [Canvas Dom useFrame useLoader useThree useUpdate extend]]
            [cljs-bean.core :refer [bean ->js]]))

(def <canvas> react-three-fiber/Canvas)

(def <dom> react-three-fiber/Dom)

(defn use-frame
  ([callback]
   (useFrame callback))
  ([callback priority]
   (useFrame callback priority)))

(defn use-loader
  ([loader url]
   (useLoader loader url))
  ([loader url extensions]
   (useLoader loader url extensions)))

(defn use-three []
  (bean (useThree)))

(defn use-update
  ([f] (useUpdate f))
  ([f deps] (useUpdate f (->js deps))))

(def extend-react extend)

(defn preload-use-loader
  ([loader url]
   (.preload useLoader loader url))
  ([loader url extensions]
   (.preload useLoader loader url extensions)))
