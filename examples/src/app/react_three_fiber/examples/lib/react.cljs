(ns react-three-fiber.examples.lib.react
  (:require [react :refer [Suspense createContext useContext]]
            [uix.hacks :refer [mark-as-native!]]))

; uix has a special way how to specify suspense via :#
; (def <:suspense> (mark-as-native! Suspense))

(def use-context useContext)
(def create-context createContext)
