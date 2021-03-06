(ns rtf-examples.demos.refraction
  (:require [shadow.resource :refer [inline]]
            [rtf-examples.lib.ui :refer [use-ref use-memo $ defnc <canvas>]]
            [rtf-examples.lib.rtf :refer [use-three use-frame use-loader]]
            [rtf-examples.lib.react :refer [<suspense>]]
            [rtf-examples.lib.three :refer [create-texture-loader
                                            create-shader-material
                                            texture-loader
                                            linear-filter
                                            create-webgl-render-target
                                            create-object-3d
                                            back-side
                                            create-vector3
                                            <mesh>
                                            <instanced-mesh>
                                            <buffer-geometry>
                                            <mesh-basic-material>
                                            <plane-buffer-geometry>]]
            [rtf-examples.lib.helpers :refer [sin-pi cos-pi
                                              give-random-number
                                              set-camera-layer!
                                              set-position!
                                              set-rotation!
                                              set-scale!
                                              delta-rotation
                                              update-vec!
                                              get-gltf-named-geometry
                                              set-material!
                                              get-texture
                                              set-min-filter!
                                              update-matrix!
                                              set-matrix-at!
                                              get-elapsed-time
                                              get-matrix
                                              set-instance-matrix-needs-update!]]
            [rtf-examples.lib.misc :refer [gltf-loader]]
            [rtf-examples.lib.interop :refer [doto!]]
            [cljs-bean.core :refer [bean]]))

; -- constants --------------------------------------------------------------------------------------------------------------

(def texture-url "/resources/images/backdrop.jpg")
(def diamond-url "/resources/gltf/diamond.glb")

(def refraction-vertex-shader (inline "./../shaders/refraction.vert"))
(def refraction-fragment-shader (inline "./../shaders/refraction.frag"))

(def backface-vertex-shader (inline "./../shaders/backface.vert"))
(def backface-fragment-shader (inline "./../shaders/backface.frag"))

(def aspect-height 3800)
(def aspect-width 5000)

; -- helpers ----------------------------------------------------------------------------------------------------------------

(defn gen-random-diamond [viewport i]
  (let [w (.-width (viewport))
        [r1 r2 r3 r4 r5 r6 r7 r8] (repeatedly give-random-number)]
    #js {:position  #js [(if (< i 5) 0 (- (/ w 2) (* r1 w)))
                         (- 40 (* r2 40))
                         (if (< i 5) 26 (- 10 (* r3 20)))]
         :factor    (+ 0.1 r4)
         :direction (if (< r5 0.5) -1 1)
         :rotation  #js [(sin-pi r6)
                         (sin-pi r7)
                         (cos-pi r8)]}))

(defn create-refraction-material [opts]
  (let [{:keys [env-map backface-map resolution]} opts]
    (create-shader-material {:uniforms        #js {:envMap      #js {:value env-map}
                                                   :backfaceMap #js {:value backface-map}
                                                   :resolution  #js {:value resolution}}
                             :vertex-shader   refraction-vertex-shader
                             :fragment-shader refraction-fragment-shader})))

(defn create-backface-material []
  (create-shader-material {:vertex-shader   backface-vertex-shader
                           :fragment-shader backface-fragment-shader
                           :side            back-side}))

; -- update loop ------------------------------------------------------------------------------------------------------------

(defn update-diamonds [gl viewport clock camera scene dummy model-ref resources diamonds]
  (let [model @model-ref
        {:keys [env-fbo backface-fbo backface-material refraction-material]} resources]
    (doseq [[i diamond] (map-indexed vector diamonds)]
      (let [t (get-elapsed-time clock)
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

        (update-matrix! dummy)
        (set-matrix-at! model i (get-matrix dummy))))

    (set-instance-matrix-needs-update! model true)

    ; render env to fbo
    (set-camera-layer! camera 1)
    (doto! gl
      (:-auto-clear false)
      (:set-render-target env-fbo)
      (:render scene camera))

    ; render cube backfaces to fbo
    (set-camera-layer! camera 0)
    (set-material! model backface-material)
    (doto! gl
      (:set-render-target backface-fbo)
      (:clear-depth)
      (:render scene camera))

    ; render env to screen
    (set-camera-layer! camera 1)
    (doto! gl
      (:set-render-target nil)
      (:render scene camera)
      (:clear-depth))

    ; render cube with refraction material to screen
    (set-camera-layer! camera 0)
    (set-material! model refraction-material)
    (doto! gl (:render scene camera))))

; -- components -------------------------------------------------------------------------------------------------------------

(defnc <diamonds> []
  (let [{:keys [size viewport gl scene camera clock]} (use-three)
        model-ref (use-ref)
        gltf (use-loader gltf-loader diamond-url)
        gltf-geometry (get-gltf-named-geometry gltf :Cylinder)
        resources (use-memo :auto-deps
                            (let [width (.-width size)
                                  height (.-height size)
                                  env-fbo (create-webgl-render-target width height)
                                  backface-fbo (create-webgl-render-target width height)
                                  refraction-material-opts {:env-map      (get-texture env-fbo)
                                                            :backface-map (get-texture backface-fbo)
                                                            :resolution   #js [width height]}]
                              {:env-fbo             env-fbo
                               :backface-fbo        backface-fbo
                               :backface-material   (create-backface-material)
                               :refraction-material (create-refraction-material refraction-material-opts)}))
        diamonds (use-memo :auto-deps
                           (into-array
                             (->> (range 80)
                                  (map-indexed (partial gen-random-diamond viewport)))))
        dummy (use-memo :once
                        (create-object-3d))
        update-fn (partial update-diamonds
                           gl viewport clock
                           camera scene
                           dummy model-ref
                           resources
                           diamonds)]
    (use-frame update-fn 1)
    ($ <instanced-mesh> {:ref model-ref :args #js [gltf-geometry nil (count diamonds)] :dispose false}
      ($ <mesh-basic-material>))))

(defnc <background> []
  (let [{:keys [viewport aspect]} (use-three)
        {:keys [width height]} (bean (viewport))
        texture (use-loader texture-loader texture-url)
        aspect-ratio (/ aspect-width aspect-height)
        viewport-width (/ width aspect-width)
        viewport-height (/ height aspect-height)
        base (if (> aspect aspect-ratio) viewport-width viewport-height)
        adapted-height (* aspect-height base)
        adapted-width (* aspect-width base)]
    (use-memo :auto-deps
              (set-min-filter! texture linear-filter))

    ($ <mesh> {:layers 1
               :scale  #js [adapted-width adapted-height 1]}
      ($ <plane-buffer-geometry>)
      ($ <mesh-basic-material> {:map       texture
                                :depthTest false}))))

(defnc <demo> []
  ($ <canvas> {:color-management false :camera #js {:fov 50 :position #js [0 0 30]}}
    ($ <suspense> {:fallback "loading refraction demo"}
      ($ <background>)
      ($ <diamonds>))))
