(ns react-three-fiber.examples.pages.intro
  (:require [cljs-bean.core :refer [bean ->js]]
            [clojure.string :as string]
            [react-three-fiber.examples.lib.ui :refer [defnc $ $$ children-only]]
            [react-three-fiber.examples.lib.react :refer [<suspense>]]
            [react-three-fiber.examples.demos :as demos]
            [react-three-fiber.examples.styles :refer [<page-styles>]]
            [react-three-fiber.examples.lib.styled-components :refer [<styled-div> simple-css]]
            [react-three-fiber.examples.lib.react-router-dom :refer [use-route-match <switch> <route> <redirect> <link>]]
            [react-three-fiber.examples.lib.helpers :refer [get-match-param]]))

; -- constants --------------------------------------------------------------------------------------------------------------

(def home-url "https://github.com/binaryage/cljs-react-three-fiber")
(def home-label "Github")

; -- data -------------------------------------------------------------------------------------------------------------------

(def all-demos (apply hash-map demos/all-demos))
(def all-demo-names (take-nth 2 demos/all-demos))
(def default-demo-name (first demos/all-demos))

; -- helpers ----------------------------------------------------------------------------------------------------------------

(defn lookup-component [name & [default]]
  (get-in all-demos [name :component] default))

(defn all-allowed-paths [names]
  (map #(str "/demo/(" % ")") names))

; -- base components with styles --------------------------------------------------------------------------------------------

(def <page>
  (<page-styles>
    (simple-css
      {:padding "20px"
       "& > h1" {:position "absolute"
                 :top      "70px"
                 :left     "60px"}
       "& > a"  {:position  "absolute"
                 :bottom    "60px"
                 :right     "60px"
                 :font-size "1.2em"}})))

(def <demo-selection-panel-base>
  (<styled-div>
    (simple-css
      {:position  "absolute"
       :bottom    "50px"
       :left      "50px"
       :max-width "260px"})))

(def <demo-selection-button>
  (<styled-div>
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

(defnc <demo-canvas> [props]

  (let [{:keys [name children]} props
        ; TODO: this should work
        ; <demo> (children-only children)
        <demo> children]
    (assert <demo>)
    ($ :div {:id        "demo-canvas"
             :className (str "demo-" (string/lower-case name))}
      ($ <demo> {:name name}))))

(defnc <demo-selection-panel> []
  (let [match (use-route-match "/demo/(.*)")
        selected-demo-name (or (get-match-param match) default-demo-name)
        selected-demo (get all-demos selected-demo-name)]
    ($ <demo-selection-panel-base>
      (for [demo-name all-demo-names]
        (let [selected? (= demo-name selected-demo-name)
              bg-color (cond
                         selected? "salmon"
                         (:bright? selected-demo) "#2c2d31"
                         :default "white")]
          ($ <link> {:key demo-name :to (str "/demo/" demo-name)}
            ($ <demo-selection-button> {:style {:background-color bg-color}})))))))

(defnc <intro> []
  (let [match (use-route-match "/demo/(.*)")
        selected-demo-name (or (get-match-param match) default-demo-name)
        selected-demo (get all-demos selected-demo-name)]
    ($ <page>
      ($ <suspense> {:fallback "loading..."}
        ($ <switch>
          ($ <route> {:exact    true
                      :path     (->js (all-allowed-paths all-demo-names))
                      :children (fn [route-props]
                                  (let [{:keys [match]} (bean route-props)
                                        selected-name (get-match-param match)
                                        component (lookup-component selected-name default-demo-name)]
                                    ($ <demo-canvas> {:name selected-name} component)))})
          ($ <redirect> {:to (str "/demo/" default-demo-name)})))
      ($ <demo-selection-panel>)
      ($ :a {:href home-url :style {:color (if (:bright? selected-demo) "#2c2d31" "white")}} home-label))))
