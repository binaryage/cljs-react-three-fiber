(ns rtf-examples.lib.dom)

(defn get-element-by-id [id]
  (js/document.getElementById id))

(defn get-window-location-origin []
  js/window.location.origin)

(defn get-window-location-pathname []
  js/window.location.pathname)
