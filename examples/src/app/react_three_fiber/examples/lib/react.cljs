(ns react-three-fiber.examples.lib.react
  (:require [react :refer [Suspense createContext useContext]]
            [react-three-fiber.examples.lib.ui :refer [defc-native!]]))

; uix has a special way how to specify suspense via :#
; (def <:suspense> (mark-as-native! Suspense))

(def use-context useContext)
(def create-context createContext)

(defc-native! <suspense> Suspense)
