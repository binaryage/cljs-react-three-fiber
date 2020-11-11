(ns rtf-examples.lib.mesh-line
  (:require [threejs-meshline :as meshline]
            [rtf-examples.lib.rtf :refer [extend-react]]))

(extend-react meshline)

(def <mesh-line> "meshLine")
(def <mesh-line-material> "meshLineMaterial")
