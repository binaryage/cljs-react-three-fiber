(ns react-three-fiber.core
  "A lightweight convenience wrapper for react-three-fiber library.

   https://github.com/react-spring/react-three-fiber"
  (:require [react-three-fiber :refer [Canvas useFrame useLoader useThree]]
            [cljs-bean.core :refer [bean]]))

(defn use-frame
  ([callback]
   (useFrame callback))
  ([callback priority]
   (useFrame callback priority)))

(def <:canvas> react-three-fiber/Canvas)

(defn use-loader
  ([proto url]
   (useLoader proto url))
  ([proto url extensions]
   (useLoader proto url extensions)))

(defn use-three []
  (bean (useThree)))
