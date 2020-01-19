(ns react-three-fiber.core
  "A lightweight convenience wrapper for react-three-fiber library.

   https://github.com/react-spring/react-three-fiber"
  (:require [react-three-fiber :refer [Canvas Dom useFrame useLoader useThree useUpdate extend]]
            [cljs-bean.core :refer [bean ->js]]
            [uix.hacks :refer [mark-as-native!]]))

(def <:canvas> (mark-as-native! react-three-fiber/Canvas))

(def <:dom> (mark-as-native! react-three-fiber/Dom))

(defn use-frame
  ([callback]
   (useFrame callback))
  ([callback priority]
   (useFrame callback priority)))

(defn use-loader
  ([loader-class url]
   (useLoader loader-class url))
  ([loader-class url extensions]
   (useLoader loader-class url extensions)))

(defn use-three []
  (bean (useThree)))

(defn use-update
  ([f] (useUpdate f))
  ([f deps] (useUpdate f (->js deps))))

(def extend-react extend)
