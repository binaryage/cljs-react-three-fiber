(ns react-three-fiber.examples.lib.styled-components
  (:require-macros [react-three-fiber.examples.lib.styled-components])
  (:require [styled-components :default styled :refer [createGlobalStyle]]
            [uix.hacks :refer [mark-as-native!]]))

(defn <:global-style> [css-text]
  (mark-as-native! (createGlobalStyle #js [css-text])))

(defn <:styled-div> [css-text]
  (mark-as-native! ((.-div styled) #js [css-text])))

(defn <:inherit-styles> [component css-text]
  (mark-as-native! ((styled component) #js [css-text])))
