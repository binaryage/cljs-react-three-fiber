(ns react-three-fiber.examples.lib.mesh-line
  (:require ["./../js/mesh-line.js" :as mesh-line]
            [react-three-fiber.core :refer [extend-react]]))

(extend-react mesh-line)

(def <mesh-line> "meshLine")
(def <mesh-line-material> "meshLineMaterial")
