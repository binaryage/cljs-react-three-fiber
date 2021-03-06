(ns rtf-examples.lib.loader
  (:refer-clojure :exclude [resolve])
  (:require [clojure.pprint :refer [pprint]]))

; see https://shadow-cljs.github.io/docs/UsersGuide.html#_loading_code_dynamically
(defmacro promise-lazy-export [module-name component]
  `(fn []
     ; see https://reactjs.org/docs/code-splitting.html#reactlazy
     (js/Promise. (fn [resolve#]
                    (let [module-name# ~module-name]
                      (.then (shadow.loader/load module-name#)
                             (fn []
                               (let [component# (deref (~'cljs.core/resolve ~component))]
                                 (resolve# (~'js-obj "default" component#))))))))))

(defmacro lazy-demo [name]
  (let [module-name (str "demo-" name)
        component-symbol (symbol (str "rtf-examples.demos." name "/<demo>"))]
    `(react-lazy (promise-lazy-export ~module-name (quote ~component-symbol)))))

; ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

(comment
  (pprint (macroexpand-1 '(promise-lazy-export "my-module" 'my-namespace/my-symbol)))
  (pprint (macroexpand-1 '(promise-lazy-export "my-module" (symbol (str "my-namespace" "-part" "/my-symbol")))))

  (pprint (macroexpand-1 '(lazy-demo "box")))
  )
