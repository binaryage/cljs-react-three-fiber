(ns react-three-fiber.examples.lib.react
  (:require [react :refer [Suspense]]
            [uix.hacks :refer [mark-as-native!]]))

; uix has a special way how to specify suspense via :#
; (def <:suspense> (mark-as-native! Suspense))
