(ns react-three-fiber.examples.lib.interop
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
  (for [form forms]
    (let [commands (take-while keyword? form)
          args (drop-while keyword? form)
          command-symbols (map command->symbol commands)]
      (if (is-a-call? (last commands))
        `(~'applied-science.js-interop/call-in ~gl-sym [~@command-symbols] ~@args)
        `(~'applied-science.js-interop/assoc-in! ~gl-sym [~@command-symbols] ~@args)))))

(defmacro doto! [& body]
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

  (macroexpand-1 '(doto!
                    (:setRenderTarget! 1)
                    (:-autoClear! false)))

  (macroexpand-1 '(doto! gl-symbol
                    (:setRenderTarget! 1)
                    (:-autoClear! false)))

  (macroexpand-1 '(doto! (some-code 1 2 3)
                    (:setRenderTarget! 1)
                    (:-autoClear! false)))

  (macroexpand-1 '(doto! (some-code 1 2 3)
                    (:sub-obj :setRenderTarget! 1)
                    (:sub-obj :-autoClear! false)))

  (kebab-to-camel-case "set-render-target")

  )
