(ns react-three-fiber.examples.dev.preload
  (:require [helix.core]
            [clojure.string :as string]))

(defn helix-display-name [name]
  (let [[ns name] (string/split name #"/")
        abbrev-ns (string/replace ns "react-three-fiber.examples." "~")]
    (str name " " abbrev-ns)))

(set! helix.core/*display-name-hook* helix-display-name)
