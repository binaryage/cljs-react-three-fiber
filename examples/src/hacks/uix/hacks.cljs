(ns uix.hacks
  (:require [uix.compiler.alpha :as uix-compiler]))

(defonce orig-component-element uix-compiler/component-element)

(defn is-js-symbol? [o]
  (= (goog/typeOf o) "symbol"))

; some react elements can be used symbols (e.g. Suspense)
; we don't touch them and treat them as native
(defn mark-as-native! [o]
  (if-not (is-js-symbol? o)
    (set! (.-$$uixnative o) true))
  o)

(defn is-native? [o]
  (boolean (or (is-js-symbol? o) (.-$$uixnative o))))

(defn hacked-component-element [tag v]
  (if (is-native? tag)
    (uix-compiler/interop-element (vec (concat [:>] v)))
    (orig-component-element tag v)))

(set! uix-compiler/component-element hacked-component-element)
