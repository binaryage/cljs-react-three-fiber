(ns react-three-fiber.examples.demos.refraction
  (:require [react-three-fiber.core :refer [<:canvas> use-three use-frame use-loader]]
            [react-three-fiber.examples.lib.three :refer [create-texture-loader
                                                          texture-loader-class
                                                          linear-filter
                                                          create-webgl-render-target
                                                          create-object-3d]]
            [react-three-fiber.examples.lib.helpers :refer [sin-pi cos-pi
                                                            give-random-number
                                                            set-camera-layer!
                                                            set-position!
                                                            set-rotation!
                                                            set-scale!
                                                            delta-rotation
                                                            update-vec!
                                                            get-gltf-geometry
                                                            set-material!]]
            [react-three-fiber.examples.lib.misc :refer [gltf-loader-class
                                                         create-backface-material
                                                         create-refraction-material]]
            [react-three-fiber.examples.lib.gl :refer [with-gl!]]
            [uix.core.alpha :as uix]
            [cljs-bean.core :refer [bean]]
            [applied-science.js-interop :as j]))

; -- constants --------------------------------------------------------------------------------------------------------------

(def texture-url "/resources/images/backdrop.jpg")
(def diamond-url "/resources/gltf/diamond.glb")

(def aspect-height 3800)
(def aspect-width 5000)

; -- helpers ----------------------------------------------------------------------------------------------------------------

(defn gen-random-diamond [viewport i]
  (let [w (.-width viewport)
        [r1 r2 r3 r4 r5 r6 r7 r8] (repeatedly give-random-number)]
    #js {:position  #js [(if (< i 5) 0 (- (/ w 2) (* r1 w)))
                         (- 40 (* r2 40))
                         (if (< i 5) 26 (- 10 (* r3 20)))]
         :factor    (+ 0.1 r4)
         :direction (if (< r5 0.5) -1 1)
         :rotation  #js [(sin-pi r6)
                         (sin-pi r7)
                         (cos-pi r8)]}))

; -- update loop ------------------------------------------------------------------------------------------------------------

(defn update-diamonds [gl viewport clock camera scene dummy model-ref resources diamonds]
  (let [model @model-ref
        {:keys [env-fbo backface-fbo backface-material refraction-material]} resources]
    (doseq [[i diamond] (map-indexed vector diamonds)]
      (let [t (.getElapsedTime clock)
            {:keys [position rotation direction factor]} (bean diamond)
            rot-delta (* factor t)
            scale (+ 1 factor)]
        (update-vec! position (fn [x y z] #js [x (- y (* (/ factor 5) direction)) z]))

        (when (if (= direction 1) (< (aget position 1) -50) (> (aget position 1) 50))
          (let [w (.-width viewport)
                [r1] (repeatedly give-random-number)]
            (update-vec! position (constantly #js[(if (< i 5) 0 (- (/ w 2) (* r1 w)))
                                                  (* 50 direction)
                                                  (aget position 2)]))))

        (set-position! dummy position)
        (set-rotation! dummy (delta-rotation rotation rot-delta rot-delta rot-delta))
        (set-scale! dummy scale scale scale)

        (.updateMatrix dummy)
        (.setMatrixAt model i (.-matrix dummy))))

    (j/assoc-in! model [.-instanceMatrix .-needsUpdate] true)

    ; render env to fbo
    (set-camera-layer! camera 1)
    (with-gl! gl
      (:-auto-clear false)
      (:set-render-target env-fbo)
      (:render scene camera))

    ; render cube backfaces to fbo
    (set-camera-layer! camera 0)
    (set-material! model backface-material)
    (with-gl! gl
      (:set-render-target backface-fbo)
      (:clear-depth)
      (:render scene camera))

    ; render env to screen
    (set-camera-layer! camera 1)
    (with-gl! gl
      (:set-render-target nil)
      (:render scene camera)
      (:clear-depth))

    ; render cube with refraction material to screen
    (set-camera-layer! camera 0)
    (set-material! model refraction-material)
    (with-gl! gl (:render scene camera))))

; -- components -------------------------------------------------------------------------------------------------------------

(defn <diamonds> []
  (let [{:keys [size viewport gl scene camera clock]} (use-three)
        model-ref (uix/ref)
        gltf (use-loader gltf-loader-class diamond-url)
        gltf-geometry (get-gltf-geometry gltf 1)
        resources-fn (fn []
                       (let [width (.-width size)
                             height (.-height size)
                             env-fbo (create-webgl-render-target width height)
                             backface-fbo (create-webgl-render-target width height)
                             refraction-material-opts {:envMap      (.-texture env-fbo)
                                                       :backfaceMap (.-texture backface-fbo)
                                                       :resolution  #js [width height]}]
                         {:env-fbo             env-fbo
                          :backface-fbo        backface-fbo
                          :backface-material   (create-backface-material)
                          :refraction-material (create-refraction-material refraction-material-opts)}))
        resources (uix/memo resources-fn [size])
        diamonds-fn (fn []
                      (into-array
                        (->> (range 80)
                             (map-indexed (partial gen-random-diamond viewport)))))
        diamonds (uix/memo diamonds-fn [viewport])
        dummy (uix/memo #(create-object-3d))
        update-fn (partial update-diamonds
                           gl viewport clock
                           camera scene
                           dummy model-ref
                           resources
                           diamonds)]
    (use-frame update-fn 1)
    [:instancedMesh {:ref model-ref :args #js [nil nil (count diamonds)]}
     [:bufferGeometry (merge {:dispose false
                              :attach  "geometry"}
                             (bean gltf-geometry))]
     [:meshBasicMaterial {:attach "material"}]]))

(defn <background> []
  (let [{:keys [viewport aspect]} (use-three)
        texture (use-loader texture-loader-class texture-url)
        aspect-ratio (/ aspect-width aspect-height)
        viewport-width (/ (.-width viewport) aspect-width)
        viewport-height (/ (.-height viewport) aspect-height)
        base (if (> aspect aspect-ratio) viewport-width viewport-height)
        adapted-height (* aspect-height base)
        adapted-width (* aspect-width base)]
    (uix/memo (fn []
                (j/assoc! texture .-minFilter linear-filter))
              [(.-minFilter texture)])

    [:mesh {:layers 1
            :scale  #js [adapted-width adapted-height 1]}
     [:planeBufferGeometry {:attach "geometry"}]
     [:meshBasicMaterial {:attach    "material"
                          :map       texture
                          :depthTest false}]]))

(defn <app> []
  [<:canvas> {:camera #js {:fov 50 :position #js [0 0 30]}}
   [:# {:fallback "loading refraction demo"}
    [<background>]
    [<diamonds>]]])
