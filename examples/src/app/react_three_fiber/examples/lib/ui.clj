(ns react-three-fiber.examples.lib.ui
  (:require [helix.core]
            [clojure.pprint :refer [pprint]]
            [helix.impl.props :as impl.props]))

(defmacro defc-native! [name c]
  `(def ~name ~c))

(defmacro defnc [& args]
  `(helix.core/defnc ~@args))

; TODO: wait for https://github.com/Lokeh/helix/issues/9
(defmacro $ [type & args]
  (let [native? true
        type (if (keyword? type)
               (name type)
               type)]
    (cond
      (map? (first args)) `^js/React.Element (helix.core/create-element
                                               ~type
                                               ~(if native?
                                                  `(impl.props/native-props ~(first args))
                                                  `(impl.props/props ~(first args)))
                                               ~@(rest args))

      :else `^js/React.Element (helix.core/create-element ~type nil ~@args))))

(defmacro $$ [& args]
  `(helix.core/$$ ^:native ~@args))

(defmacro <> [& args]
  `(helix.core/<> ~@args))

(defmacro use-memo [& args]
  `(helix.hooks/use-memo ~@args))

(defmacro use-effect [& args]
  `(helix.hooks/use-effect ~@args))

(comment
  (pprint (macroexpand '($ :meshBasicMaterial {:attach "material"})))
  (pprint (macroexpand '($ :instancedMesh {:ref model-ref :args [nil nil (count diamonds)]}
                          ($$ :bufferGeometry geometry-props)
                          ($ :meshBasicMaterial {:attach "material"})))))
