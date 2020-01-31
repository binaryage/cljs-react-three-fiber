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
  (let [type (if (keyword? type)
               (name type)
               type)
        maybe-props (first args)]
    (if (map? maybe-props)
      `^js/React.Element (helix.core/create-element
                           ~type
                           (impl.props/native-props ~maybe-props)
                           ~@(rest args))
      ; TODO: move this into a helper fn
      `(let [m# ~maybe-props]
         (if (map? m#)
           ^js/React.Element (helix.core/create-element
                               ~type
                               (impl.props/native-props m#)
                               ~@(rest args))
           ^js/React.Element (helix.core/create-element
                               ~type
                              nil
                               m#
                               ~@(rest args)))))))

(defmacro <> [& args]
  `(helix.core/<> ~@args))

(defmacro use-memo [& args]
  `(helix.hooks/use-memo ~@args))

(defmacro use-effect [& args]
  `(helix.hooks/use-effect ~@args))

(comment
  (pprint (macroexpand '($$ (animated :mesh) (merge spring {:key           index
                                                            :castShadow    true
                                                            :receiveShadow true}))))

  (pprint (macroexpand '($ :meshBasicMaterial {:attach "material"})))
  (pprint (macroexpand '($ :instancedMesh {:ref model-ref :args [nil nil (count diamonds)]}
                          ($$ :bufferGeometry geometry-props)
                          ($ :meshBasicMaterial {:attach "material"})))))
