(ns react-three-fiber.examples.lib.three
  (:require-macros [react-three-fiber.examples.lib.three])
  (:require [three :refer [TextureLoader FontLoader LinearFilter WebGLRenderTarget Object3D Vector3
                           AnimationMixer CatmullRomCurve3 PCFSoftShadowMap
                           ShaderMaterial BackSide]]
            [helix.impl.props2]
            [applied-science.js-interop :as j]))

; -- components -------------------------------------------------------------------------------------------------------------

(def <group> "group")
(def <scene> "scene")

(def <mesh> "mesh")
(def <instanced-mesh> "instancedMesh")

(def <object3d> "object3D")
(def <points> "points")
(def <fog> "fog")

(def <box-buffer-geometry> "boxBufferGeometry")
(def <buffer-geometry> "bufferGeometry")
(def <text-geometry> "textGeometry")
(def <plane-buffer-geometry> "planeBufferGeometry")
(def <shape-buffer-geometry> "shapeBufferGeometry")

(def <buffer-attribute> "bufferAttribute")

(def <mesh-basic-material> "meshBasicMaterial")
(def <mesh-standard-material> "meshStandardMaterial")
(def <mesh-normal-material> "meshNormalMaterial")
(def <mesh-phong-material> "meshPhongMaterial")
(def <points-material> "pointsMaterial")

(def <ambient-light> "ambientLight")
(def <point-light> "pointLight")
(def <spot-light> "spotLight")

; -- wrappers ---------------------------------------------------------------------------------------------------------------

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

(defn update-mixer [mixer delta-time]
  (.update mixer delta-time))

(def catmull-rom-curve3-class CatmullRomCurve3)

(defn create-catmull-rom-curve3
  ([points] (catmull-rom-curve3-class. points)))

(defn deg-to-rad [n]
  (j/call-in three [.-Math .-degToRad] n))

(def pcf-soft-shadow-map PCFSoftShadowMap)

(def shader-material-class ShaderMaterial)

(def back-side BackSide)

(defn play-animation-action! [action]
  (.play action))

(defn get-clip-animation-action
  ([mixer clip] (.clipAction mixer clip))
  ([mixer clip root] (.clipAction mixer clip root)))

(defn compute-bounding-box! [geom]
  (.computeBoundingBox geom))

(defn get-geometry [o]
  (.-geometry o))

(defn export-bounding-box-size! [geom out-size]
  (j/call-in geom [.-boundingBox .-getSize] out-size))

(defn get-x [o]
  (.-x o))

(defn get-y [o]
  (.-y o))

(defn get-z [o]
  (.-z o))

(defn vector3-add! [v d]
  (.add v d))

(defn vector3-clone [v]
  (.clone v))

(defn camera-look-at! [camera x y z]
  (.lookAt camera x y z))
