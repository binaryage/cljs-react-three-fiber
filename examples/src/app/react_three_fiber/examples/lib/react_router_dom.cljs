(ns react-three-fiber.examples.lib.react-router-dom
  (:require [react-router-dom :refer [HashRouter Link Route Redirect Switch useRouteMatch]]
            [react-three-fiber.examples.lib.ui :refer [defc-native!]]))

(defn use-route-match [path]
  (useRouteMatch path))

(defc-native! <router> HashRouter)

(defc-native! <switch> Switch)

(defc-native! <route> Route)

(defc-native! <redirect> Redirect)

(defc-native! <link> Link)
