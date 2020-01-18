(ns react-three-fiber.examples.pages.intro
  (:require [uix.core.alpha :refer [as-react as-element]]
            [applied-science.js-interop :as j]
            [react-three-fiber.examples.demos :refer [all-demos]]
            [react-three-fiber.examples.lib.react :refer [suspense]]
            [react-three-fiber.examples.styles :refer [page-styles]]
            [react-three-fiber.examples.lib.styled-components :refer [styled-div simple-css]]
            [react-three-fiber.examples.lib.react-router-dom :refer [use-route-match switch route redirect link]]
            [clojure.string :as string]))

; -- data -------------------------------------------------------------------------------------------------------------------

(def default-component-name "Box")
(def visible-components all-demos)

; -- helpers ----------------------------------------------------------------------------------------------------------------

(defn get-match-name [match & [default]]
  (j/get-in match [.-params .-name] default))

(defn lookup-component [name]
  (get-in visible-components [name :component]))

(defn demo-canvas [name demo]
  [:div#demo-canvas {:className (str "demo-" (string/lower-case name))}
   [demo name]])

; -- styles -----------------------------------------------------------------------------------------------------------------

(def page
  (page-styles
    (simple-css
      {:padding "20px"
       "& > h1" {:position "absolute"
                 :top      "70px"
                 :left     "60px"}
       "& > a"  {:position  "absolute"
                 :bottom    "60px"
                 :right     "60px"
                 :font-size "1.2em"}})))

(def demo-panel
  (styled-div
    (simple-css
      {:position  "absolute"
       :bottom    "50px"
       :left      "50px"
       :max-width "260px"})))

(def spot
  (styled-div
    (simple-css
      {:display       "inline-block"
       :width         "20px"
       :height        "20px"
       :border-radius "50%"
       :margin        "8px"

       :border-color  "lightgray"
       :border-width  "2px"
       :border-style  "solid"})))

; -- components -------------------------------------------------------------------------------------------------------------

(defn <demos> []
  (let [match (use-route-match "/demo/:name")
        selected-component-name (get-match-name match default-component-name)
        bright? (get-in visible-components [selected-component-name :bright])]
    [:> demo-panel
     (for [[name] visible-components]
       (let [selected? (= name selected-component-name)]
         [:> link {:key name
                   :to  (str "/demo/" name)}
          [:> spot {:style {:background (cond
                                          selected? "salmon"
                                          bright? "#2c2d31"
                                          :default "white")}}]]))]))

(defn <intro> []
  (let [match (use-route-match "/demo/:name")
        selected-component-name (get-match-name match default-component-name)
        bright? (get-in visible-components [selected-component-name :bright])]
    [:> page
     [:> suspense {:fallback nil}
      [:> switch
       [:> route {:exact    true
                  :path     "/demo/:name"
                  :children (fn [route-props]
                              (let [match (.-match route-props)
                                    selected-component-name (get-match-name match default-component-name)
                                    component (lookup-component selected-component-name)]
                                (assert component)
                                (as-element (demo-canvas selected-component-name component))))}]
       [:> redirect {:to (str "/demo/" default-component-name)}]]]
     [<demos>]
     [:a {:href  "https://github.com/drcmda/react-three-fiber"
          :style {:color (if bright? "#2c2d31" "white")}}
      "Github"]]))
