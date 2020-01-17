(ns react-three-fiber.examples.lib.styled-components
  (:require-macros [react-three-fiber.examples.lib.styled-components])
  (:require [styled-components :default styled :refer [createGlobalStyle]]))

(defn create-global-style [css-text]
  (createGlobalStyle #js [css-text]))

(defn styled-div [css-text]
  ((.-div styled) #js [css-text]))

(defn inherit-styles [component css-text]
  ((styled component) #js [css-text]))
