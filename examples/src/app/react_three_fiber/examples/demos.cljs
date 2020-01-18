(ns react-three-fiber.examples.demos
  (:require [react-three-fiber.examples.demos.box :as box]
            [react-three-fiber.examples.demos.refraction :as refraction]))

(defn <demo-placeholder> [name _component]
  [:div.demo-placeholder
   (str "demo placeholder for '" name "'")])

(def all-demos
  ; note that order matters here, first demo is the default one
  ["box"
   {:component box/<app>}

   "refraction"
   {:desc      ""
    :tags      []
    :component refraction/<app>
    :bright    false}

   "demo1"
   {:desc      ""
    :tags      []
    :component <demo-placeholder>
    :bright    false}])
