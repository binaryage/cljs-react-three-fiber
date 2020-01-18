(ns react-three-fiber.core
  "A lightweight convenience wrapper for react-three-fiber library.

   https://github.com/react-spring/react-three-fiber"
  (:require [react-three-fiber :refer [Canvas useFrame useLoader useThree]]
            [cljs-bean.core :refer [bean]]
            [uix.hacks :refer [mark-as-native!]]))

(defn use-frame
  ([callback]
   (useFrame callback))
  ([callback priority]
   (useFrame callback priority)))

(def <:canvas> (mark-as-native! react-three-fiber/Canvas))

(defn use-loader
  ([loader-class url]
   (useLoader loader-class url))
  ([loader-class url extensions]
   (useLoader loader-class url extensions)))

(defn use-three []
  (bean (useThree)))
