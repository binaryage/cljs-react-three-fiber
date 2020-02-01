(ns react-three-fiber.examples.lib.ui
  (:require [helix.core]
            [clojure.pprint :refer [pprint]]
            [helix.impl.props :as impl.props]))

(defmacro defc-native! [name c]
  `(def ~name ~c))

(defmacro defnc [& args]
  `(helix.core/defnc ~@args))

(defn gen-create-element [type & args]
  `^js/React.Element (helix.core/create-element ~type ~@args))

; TODO: wait for https://github.com/Lokeh/helix/issues/9
(defmacro $ [type & args]
  (let [type (if (keyword? type)
               (name type)
               type)
        rest-args (rest args)
        props (first args)]
    (cond
      (empty? args) (gen-create-element type)
      (map? props) (apply gen-create-element type `(impl.props/native-props ~props) rest-args)
      :else (apply gen-create-element type nil args))))

(defmacro <> [& args]
  `(helix.core/<> ~@args))

(defmacro use-memo [& args]
  `(helix.hooks/use-memo ~@args))

(defmacro use-effect [& args]
  `(helix.hooks/use-effect ~@args))

(comment


  (defmethod cljs.analyzer/error-message :ui-custom-warning
    [_type info]
    (:msg info))

  (defn print-compile-time-warning! [env msg]
    (binding [cljs.analyzer/*cljs-warnings* (assoc cljs.analyzer/*cljs-warnings* :ui-custom-warning true)]
      (cljs.analyzer/warning :ui-custom-warning env {:msg msg})))


  (pprint (macroexpand '($ (animated :mesh) (merge spring {:key           index
                                                           :castShadow    true
                                                           :receiveShadow true}))))

  (pprint (macroexpand '($ :meshBasicMaterial {:attach "material"})))
  (pprint (macroexpand '($ <c> ($ <child> {:prop 1}))))
  (pprint (macroexpand '($ :instancedMesh {:ref model-ref :args [nil nil (count diamonds)]}
                          ($$ :bufferGeometry geometry-props)
                          ($ :meshBasicMaterial {:attach "material"})))))

