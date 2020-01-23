(ns react-three-fiber.examples.demos
  (:require [react-three-fiber.examples.lib.loader :refer [lazy-demo]]))

(def all-demos
  ; note that order matters here, first demo is the default one
  ["box" {:component (lazy-demo "box") :bright? true}
   "refraction" {:component (lazy-demo "refraction")}
   "font" {:component (lazy-demo "font")}
   "montage" {:component (lazy-demo "montage") :bright? true}
   "mesh-line" {:component (lazy-demo "mesh-line") :bright? true}
   "svg-loader" {:component (lazy-demo "svg-loader")}])
