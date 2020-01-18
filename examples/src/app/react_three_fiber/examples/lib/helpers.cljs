(ns react-three-fiber.examples.lib.helpers
  (:require [cljs-bean.core :refer [bean]]
            [applied-science.js-interop :as j]))

(defn give-random-number []
  (Math/random))

(defn sin-pi [x]
  (Math/sin (* x Math/PI)))

(defn cos-pi [x]
  (Math/cos (* x Math/PI)))

(defn set-camera-layer! [camera layer]
  (j/call-in camera [.-layers .-set] layer))
