(ns react-three-fiber.examples.preload
  (:require [clojure.string :as string]))

;(defn strip-common-ns-prefixes [s]
;  (string/replace s #"^react-three-fiber.examples." "~"))
;
;(defonce orig-format-display-name uix-compiler/*format-display-name*)
;
;(defn my-format-display-name [s]
;  (let [name (orig-format-display-name s)]
;    (if-some [m (re-matches #"^(.*)/<(.*)>" name)]
;      (str "<" (nth m 2) "> " (strip-common-ns-prefixes (nth m 1)))
;      (strip-common-ns-prefixes name))))
;
;(set! uix-compiler/*format-display-name* my-format-display-name)
;
(js/console.info "react-three-fiber.examples.preload activated")
