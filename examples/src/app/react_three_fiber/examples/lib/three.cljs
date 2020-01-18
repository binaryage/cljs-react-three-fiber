(ns react-three-fiber.examples.lib.three
  (:require [three :refer [TextureLoader LinearFilter WebGLRenderTarget Object3D]]))

(def linear-filter LinearFilter)

(def texture-loader-class TextureLoader)

(defn create-texture-loader
  ([] (texture-loader-class.))
  ([manager] (texture-loader-class. manager)))

(def webgl-render-target-class WebGLRenderTarget)

(defn create-webgl-render-target
  ([width height] (webgl-render-target-class. width height))
  ([width height options] (webgl-render-target-class. width height options)))

(def object-3d-class Object3D)

(defn create-object-3d []
  (object-3d-class.))
