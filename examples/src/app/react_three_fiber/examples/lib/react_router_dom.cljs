(ns react-three-fiber.examples.lib.react-router-dom
  (:require [react-router-dom :refer [HashRouter Link Route Redirect Switch useRouteMatch]]))

(def router HashRouter)

(defn use-route-match [path]
  (useRouteMatch path))

(def switch Switch)

(def route Route)

(def redirect Redirect)

(def link Link)
