(ns rtf-examples.index
  (:require [shadow.loader :as shadow-loader]
            [rtf-examples.lib.ui :as ui :refer [defnc $]]
            [rtf-examples.lib.react-router-dom :refer [<router>]]
            [rtf-examples.lib.dom :refer [get-element-by-id
                                                        get-window-location-origin
                                                        get-window-location-pathname]]
            [rtf-examples.pages.intro :refer [<intro>]]
            [rtf-examples.lib.rtf :refer [use-frame <canvas>]]
            [rtf-examples.styles :refer [<global-styles>]]
            [rtf-examples.lib.helpers :refer [remove-trailing-slashes]]))

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
