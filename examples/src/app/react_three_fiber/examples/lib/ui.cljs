(ns react-three-fiber.examples.lib.ui
  (:require-macros [react-three-fiber.examples.lib.ui :refer [defc-native!]])
  (:require [helix.core]
            [helix.hooks]
            [helix.dom]
            [react-three-fiber :refer [Canvas Dom]]
            [react-dom :as rdom]
            [react :refer [useRef createContext useContext]]))

(defc-native! <canvas> Canvas)
(defc-native! <dom> Dom)

(defn render! [react-el dom-el]
  (rdom/render react-el dom-el))

(defn unmount! [dom-el]
  (rdom/unmountComponentAtNode dom-el))

(defn children-map
  ([children f]
   (.map react/Children children f))
  ([children f context]
   (.map react/Children children f context)))

(defn children-for-each
  ([children f]
   (.forEach react/Children children f))
  ([children f context]
   (.forEach react/Children children f context)))

(defn children-count [children]
  (.count react/Children children))

(defn children-to-array [children]
  (.toArray react/Children children))

(defn children-only [children]
  (.only react/Children children))

(def create-context createContext)
(def use-context useContext)

(def use-ref helix.hooks/use-ref)

; from uix
(deftype StateHook [value set-value]
  Object
  (equiv [this other]
    (-equiv this other))

  IHash
  (-hash [o] (goog/getUid o))

  IDeref
  (-deref [o]
    value)

  IReset
  (-reset! [o new-value]
    (set-value new-value))

  ISwap
  (-swap! [o f]
    (set-value f))
  (-swap! [o f a]
    (set-value #(f % a)))
  (-swap! [o f a b]
    (set-value #(f % a b)))
  (-swap! [o f a b xs]
    (set-value #(apply f % a b xs)))

  IPrintWithWriter
  (-pr-writer [o writer opts]
    (-write writer "#object [uix.hooks.alpha.StateHook ")
    (pr-writer {:val value} writer opts)
    (-write writer "]")))

(defn state [value]
  (let [[value set-value] (react/useState value)
        sh (react/useMemo #(StateHook. value set-value) #js [])]
    (react/useMemo (fn []
                     (set! (.-value sh) value)
                     (set! (.-set-value sh) set-value)
                     sh)
                   #js [value set-value])))

(def use-state state)
