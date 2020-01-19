(ns react-three-fiber.examples.demos
  (:require [react-three-fiber.examples.demos.box :as box]
            [react-three-fiber.examples.demos.refraction :as refraction]
            [react-three-fiber.examples.demos.font :as font]))

(defn <demo-placeholder> [props _component]
  [:div.demo-placeholder
   (str "demo placeholder for '" (:name props) "'")])

(def all-demos
  ; note that order matters here, first demo is the default one
  ["box"
   {:component box/<demo>
    :bright?   true}

   "refraction"
   {:desc      ""
    :tags      []
    :component refraction/<demo>}

   "font"
   {:desc      ""
    :tags      []
    :component font/<demo>}

   "demo1"
   {:desc      ""
    :tags      []
    :component <demo-placeholder>
    :bright?   true}])
