(ns react-three-fiber.examples.demos
  (:require [react-three-fiber.examples.demos.box :as box]
            [react-three-fiber.examples.demos.refraction :as refraction]
            [react-three-fiber.examples.demos.font :as font]
            [react-three-fiber.examples.demos.montage :as montage]
            [react-three-fiber.examples.demos.mesh-line :as mesh-line]))

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

   "montage"
   {:desc      ""
    :tags      []
    :component montage/<demo>
    :bright?   true}

   "mesh-line"
   {:desc      ""
    :tags      []
    :component mesh-line/<demo>
    :bright?   true}

   "demo1"
   {:desc      ""
    :tags      []
    :component <demo-placeholder>
    :bright?   true}])
