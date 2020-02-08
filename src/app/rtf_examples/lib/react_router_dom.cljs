(ns rtf-examples.lib.react-router-dom
  (:require [react-router-dom :refer [HashRouter Link Route Redirect Switch useRouteMatch]]))

(defn use-route-match [path]
  (useRouteMatch path))

(def <router> HashRouter)
(def <switch> Switch)
(def <route> Route)
(def <redirect> Redirect)
(def <link> Link)
