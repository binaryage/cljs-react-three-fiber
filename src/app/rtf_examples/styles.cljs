(ns rtf-examples.styles
  (:require [clojure.string :as string]
            [rtf-examples.lib.styled-components :refer [inline-css
                                                                      <styled-div>
                                                                      <global-style>
                                                                      <inherit-styles>]]))

(def <shared-page-styles>
  (-> (inline-css "./styles/page.scss")
      (<styled-div>)))

(def <global-styles>
  (-> (inline-css "./styles/global.scss")
      (<global-style>)))

(defn <page-styles> [& css]
  (->> (string/join "\n" css)
       (<inherit-styles> <shared-page-styles>)))
