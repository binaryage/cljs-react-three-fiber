(ns react-three-fiber.examples.demos
  (:require [react-three-fiber.examples.lib.loader :refer [lazy-demo]]))

(def all-demos
  ; note that order matters here, first demo is the default one
  [

   "box"
   {:component (lazy-demo "box")
    :bright?   true}

   "refraction"
   {:desc      ""
    :tags      []
    :component (lazy-demo "refraction")}

   "font"
   {:desc      ""
    :tags      []
    :component (lazy-demo "font")}

   "montage"
   {:desc      ""
    :tags      []
    :component (lazy-demo "montage")
    :bright?   true}

   "mesh-line"
   {:desc      ""
    :tags      []
    :component (lazy-demo "mesh-line")
    :bright?   true}

   "svg-loader"
   {:desc      ""
    :tags      []
    :component (lazy-demo "svg-loader")}])
