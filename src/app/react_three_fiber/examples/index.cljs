(ns react-three-fiber.examples.index
  (:require [shadow.loader :as shadow-loader]
            [react-three-fiber.examples.lib.ui :as ui :refer [defnc $]]
            [react-three-fiber.examples.lib.react-router-dom :refer [<router>]]
            [react-three-fiber.examples.lib.dom :refer [get-element-by-id
                                                        get-window-location-origin
                                                        get-window-location-pathname]]
            [react-three-fiber.examples.pages.intro :refer [<intro>]]
            [react-three-fiber.examples.lib.rtf :refer [use-frame <canvas>]]
            [react-three-fiber.examples.styles :refer [<global-styles>]]
            [react-three-fiber.examples.lib.helpers :refer [remove-trailing-slashes]]))

; -- app --------------------------------------------------------------------------------------------------------------------

(defnc <app> []
  ($ <router>
    ($ <global-styles>)
    ($ <intro>)))

; ---------------------------------------------------------------------------------------------------------------------------

(def root-el (get-element-by-id "root"))

(defn render! []
  (ui/render! ($ <app>) root-el))

(defn init-module-loader! []
  ; https://github.com/thheller/shadow-cljs/pull/646
  (let [origin (get-window-location-origin)
        pathname (get-window-location-pathname)
        prefix (remove-trailing-slashes (str origin pathname))]
    (shadow-loader/init prefix)))

(defn init! []
  (init-module-loader!)
  (render!))

(defn ^:dev/after-load reload! []
  (ui/unmount! root-el)
  (render!))
