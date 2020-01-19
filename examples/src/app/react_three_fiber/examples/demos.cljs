(ns react-three-fiber.examples.demos
  (:require [react-three-fiber.examples.demos.box :as box]
            [react-three-fiber.examples.demos.refraction :as refraction]
            [react-three-fiber.examples.demos.font :as font]))

(defn <demo-placeholder> [name _component]
  [:div.demo-placeholder
   (str "demo placeholder for '" name "'")])

(def all-demos
  ; note that order matters here, first demo is the default one
  ["box"
   {:component box/<app>
    :bright?   true}

   "refraction"
   {:desc      ""
    :tags      []
    :component refraction/<app>}

   "font"
   {:desc      ""
    :tags      []
    :component font/<app>}

   "demo1"
   {:desc      ""
    :tags      []
    :component <demo-placeholder>
    :bright?   true}])
