(ns rtf-examples.lib.mesh-line
  (:require ["./../js/mesh-line.js" :as mesh-line]
            [rtf-examples.lib.rtf :refer [extend-react]]))

(extend-react mesh-line)

(def <mesh-line> "meshLine")
(def <mesh-line-material> "meshLineMaterial")
