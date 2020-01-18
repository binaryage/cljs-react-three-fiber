(ns react-three-fiber.examples.lib.react
  (:require [react :refer [Suspense]]
            [uix.hacks :refer [mark-as-native!]]))

(def <:suspense> (mark-as-native! Suspense))
