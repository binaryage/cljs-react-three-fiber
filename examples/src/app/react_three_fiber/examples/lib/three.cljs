(ns react-three-fiber.examples.lib.three
  (:require [three :refer [TextureLoader FontLoader LinearFilter WebGLRenderTarget Object3D Vector3
                           AnimationMixer]]))

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

(def font-loader-class FontLoader)

(defn create-font-loader
  ([] (font-loader-class.))
  ([manager] (font-loader-class. manager)))

(defn create-vector3 []
  (Vector3.))

(def animation-mixer-class AnimationMixer)

(defn create-animation-mixer
  ([] (animation-mixer-class.))
  ([root] (animation-mixer-class. root)))
