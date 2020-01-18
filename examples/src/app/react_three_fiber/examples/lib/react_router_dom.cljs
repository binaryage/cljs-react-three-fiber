(ns react-three-fiber.examples.lib.react-router-dom
  (:require [react-router-dom :refer [HashRouter Link Route Redirect Switch useRouteMatch]]
            [uix.hacks :refer [mark-as-native!]]))

(defn use-route-match [path]
  (useRouteMatch path))

(def <:router> (mark-as-native! HashRouter))

(def <:switch> (mark-as-native! Switch))

(def <:route> (mark-as-native! Route))

(def <:redirect> (mark-as-native! Redirect))

(def <:link> (mark-as-native! Link))
