(ns react-three-fiber.examples.pages.intro
  (:require [uix.core.alpha :refer [as-react as-element]]
            [applied-science.js-interop :as j]
            [react-three-fiber.examples.demos :as demos]
            [react-three-fiber.examples.styles :refer [<:page-styles>]]
            [react-three-fiber.examples.lib.styled-components :refer [<:styled-div> simple-css]]
            [react-three-fiber.examples.lib.react-router-dom :refer [use-route-match <:switch> <:route> <:redirect> <:link>]]
            [cljs-bean.core :refer [bean ->js]]
            [clojure.string :as string]))

(def github-home-url "https://github.com/binaryage/cljs-react-three-fiber")

; -- data -------------------------------------------------------------------------------------------------------------------

(def all-demos (apply hash-map demos/all-demos))
(def all-demo-names (take-nth 2 demos/all-demos))
(def default-demo-name (first demos/all-demos))

; -- helpers ----------------------------------------------------------------------------------------------------------------

(defn get-match-param [match & [index]]
  (j/get-in match [.-params (or index 0)]))

(defn lookup-component [name & [default]]
  (get-in all-demos [name :component] default))

(defn prepare-allowed-paths [names]
  (map #(str "/demo/(" % ")") names))

; -- style components -------------------------------------------------------------------------------------------------------

(def <:page>
  (<:page-styles>
    (simple-css
      {:padding "20px"
       "& > h1" {:position "absolute"
                 :top      "70px"
                 :left     "60px"}
       "& > a"  {:position  "absolute"
                 :bottom    "60px"
                 :right     "60px"
                 :font-size "1.2em"}})))

(def <:demos-selection-panel>
  (<:styled-div>
    (simple-css
      {:position  "absolute"
       :bottom    "50px"
       :left      "50px"
       :max-width "260px"})))

(def <:demo-selection-spot>
  (<:styled-div>
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

(defn <demo-canvas> [name <demo>]
  [:div#demo-canvas {:className (str "demo-" (string/lower-case name))}
   [<demo> name]])

(defn <demos-selection> []
  (let [match (use-route-match "/demo/(.*)")
        selected-name (or (get-match-param match) default-demo-name)
        bright? (get-in all-demos [selected-name :bright?])]
    [<:demos-selection-panel>
     (for [name all-demo-names]
       (let [selected? (= name selected-name)
             background-color (cond
                                selected? "salmon"
                                bright? "#2c2d31"
                                :default "white")]
         [<:link> {:key name
                   :to  (str "/demo/" name)}
          [<:demo-selection-spot> {:style {:background-color background-color}}]]))]))

(defn <intro> []
  (let [match (use-route-match "/demo/(.*)")
        selected-name (or (get-match-param match) default-demo-name)
        bright? (get-in all-demos [selected-name :bright?])]
    [<:page>
     [:# {:fallback nil}
      [<:switch>
       [<:route> {:exact    true
                  :path     (->js (prepare-allowed-paths all-demo-names))
                  :children (fn [route-props]
                              (let [{:keys [match]} (bean route-props)
                                    selected-name (get-match-param match)
                                    _ (assert selected-name)
                                    component (lookup-component selected-name default-demo-name)
                                    _ (assert component)]
                                (as-element (<demo-canvas> selected-name component))))}]
       [<:redirect> {:to (str "/demo/" default-demo-name)}]]]
     [<demos-selection>]
     [:a {:href  github-home-url
          :style {:color (if bright? "#2c2d31" "white")}}
      "Github"]]))
