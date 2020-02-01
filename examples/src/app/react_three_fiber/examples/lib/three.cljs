(ns react-three-fiber.examples.lib.three
  (:require-macros [react-three-fiber.examples.lib.three])
  (:require [three :refer [TextureLoader FontLoader LinearFilter WebGLRenderTarget Object3D Vector3
                           AnimationMixer CatmullRomCurve3 PCFSoftShadowMap
                           ShaderMaterial BackSide]]
            [helix.impl.props2]
            [applied-science.js-interop :as j]))

(def linear-filter LinearFilter)

(def texture-loader TextureLoader)

(defn create-texture-loader
  ([] (texture-loader.))
  ([manager] (texture-loader. manager)))

(def webgl-render-target-class WebGLRenderTarget)

(defn create-webgl-render-target
  ([width height] (webgl-render-target-class. width height))
  ([width height options] (webgl-render-target-class. width height options)))

(def object-3d-class Object3D)

(defn create-object-3d []
  (object-3d-class.))

(def font-loader FontLoader)

(defn create-font-loader
  ([] (font-loader.))
  ([manager] (font-loader. manager)))

(defn create-vector3
  ([] (Vector3.))
  ([x y z] (Vector3. x y z)))

(def animation-mixer-class AnimationMixer)

(defn create-animation-mixer
  ([] (animation-mixer-class.))
  ([root] (animation-mixer-class. root)))

(def catmull-rom-curve3-class CatmullRomCurve3)

(defn create-catmull-rom-curve3
  ([points] (catmull-rom-curve3-class. points)))

(defn deg-to-rad [n]
  (j/call-in three [.-Math .-degToRad] n))

(def pcf-soft-shadow-map PCFSoftShadowMap)

(def shader-material-class ShaderMaterial)

(def back-side BackSide)
