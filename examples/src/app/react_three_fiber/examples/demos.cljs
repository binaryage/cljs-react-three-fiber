(ns react-three-fiber.examples.demos
  (:require [react-three-fiber.examples.demos.box :as box]
            [react-three-fiber.examples.demos.refraction :as refraction]))

(defn <demo-placeholder> [name _component]
  [:div.demo-placeholder
   (str "demo placeholder for '" name "'")])

(def all-demos
  {"Box"        {:component box/<app>}
   "Refraction" {:desc      ""
                 :tags      []
                 :component refraction/app
                 :bright    false}
   "Demo1"      {:desc      ""
                 :tags      []
                 :component <demo-placeholder>
                 :bright    false}})
