(ns react-three-fiber.examples.index
  (:require [uix.dom.alpha :as uix.dom]
            [react-three-fiber.examples.lib.react-router-dom :refer [router]]
            [react-three-fiber.examples.lib.dom :refer [get-element-by-id]]
            [react-three-fiber.examples.pages.intro :refer [<intro>]]
            [react-three-fiber.core :refer [use-frame canvas]]
            [react-three-fiber.examples.styles :refer [global-styles]]))

; -- app --------------------------------------------------------------------------------------------------------------------

(defn <app> []
  [:> router
   [:> global-styles]
   [<intro>]])

; ---------------------------------------------------------------------------------------------------------------------------

(def root-el (get-element-by-id "root"))

(defn render! []
  (uix.dom/render [<app>] root-el))

(defn main! []
  (render!))

(defn ^:dev/after-load reload! []
  (uix.dom/unmount-at-node root-el)
  (render!))
