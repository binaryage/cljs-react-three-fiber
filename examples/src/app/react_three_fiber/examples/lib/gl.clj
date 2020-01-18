(ns react-three-fiber.examples.lib.gl
  (:require [clojure.string :as string]))

(defn is-a-call? [command]
  (not= (first (name command)) \-))

(defn kebab-to-camel-case [^String s]
  (clojure.string/replace s #"-(\w)" #(clojure.string/upper-case (second %1))))

(defn remove-exclamation [s]
  (if (string/ends-with? s "!")
    (subs s 0 (- (count s) 1))
    s))

(defn remove-dash [s]
  (if (string/starts-with? s "-")
    (subs s 1)
    s))

(defn command->symbol [command]
  (symbol (str ".-" (-> (name command)
                        (remove-dash)
                        (remove-exclamation)
                        (kebab-to-camel-case)))))


(defn prepare-gl-commands [gl-sym forms]
  (for [[command & args] forms]
    (let [command-sym (command->symbol command)]
      (if (is-a-call? command)
        `(~'applied-science.js-interop/call ~gl-sym ~command-sym ~@args)
        `(~'applied-science.js-interop/assoc! ~gl-sym ~command-sym ~@args)))))

(defmacro with-gl! [& body]
  (let [first-item (first body)
        implicit-gl? (and (list? first-item) (keyword? (first first-item)))
        plain-gl? (symbol? first-item)
        gl-sym (cond
                 implicit-gl? 'gl
                 plain-gl? first-item
                 :else (gensym "gl_"))
        effective-body (if implicit-gl? body (rest body))
        commands (prepare-gl-commands gl-sym effective-body)]
    (if (or implicit-gl? plain-gl?)
      `(do ~@commands)
      `(let [~gl-sym ~first-item]
         ~@commands))))

; ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

(comment

  (macroexpand-1 '(with-gl!
                    (:setRenderTarget! 1)
                    (:-autoClear! false)))

  (macroexpand-1 '(with-gl! glsymbol
                    (:setRenderTarget! 1)
                    (:-autoClear! false)))

  (macroexpand-1 '(with-gl! (some-code 1 2 3)
                    (:setRenderTarget! 1)
                    (:-autoClear! false)))

  (kebab-to-camel-case "set-render-target")

  )
