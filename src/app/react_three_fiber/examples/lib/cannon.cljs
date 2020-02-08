(ns react-three-fiber.examples.lib.cannon
  (:require [cannon :refer [World NaiveBroadphase Body Plane Box Vec3]]))

(defn create-world []
  (World.))

(defn create-naive-broadphase []
  (NaiveBroadphase.))

(defn create-body [props]
  (Body. props))

(defn create-plane []
  (Plane.))

(defn create-box [v]
  (Box. v))

(defn create-vec3 [x y z]
  (Vec3. x y z))

(defn add-body-to-world! [world body]
  (.addBody world body))

(defn remove-body-from-world! [world body]
  (.removeBody world body))

(defn step-world! [world delta-time]
  (.step world delta-time))
