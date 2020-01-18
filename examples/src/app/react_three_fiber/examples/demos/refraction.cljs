(ns react-three-fiber.examples.demos.refraction
  (:require [react-three-fiber.core :refer [<:canvas> use-three use-frame use-loader]]
            [react-three-fiber.examples.lib.react :refer [<:suspense>]]
            [react-three-fiber.examples.lib.three :refer [create-texture-loader
                                                          texture-loader-class
                                                          linear-filter
                                                          create-webgl-render-target
                                                          create-object-3d]]
            [react-three-fiber.examples.lib.helpers :refer [sin-pi cos-pi
                                                            give-random-number
                                                            set-camera-layer!]]
            [uix.core.alpha :as uix]
            [cljs-bean.core :refer [bean]]
            [applied-science.js-interop :as j]
            ["three/examples/jsm/loaders/GLTFLoader" :refer [GLTFLoader]]
            ["./../js/shaders/Backface" :default BackfaceMaterial]
            ["./../js/shaders/Refraction" :default RefractionMaterial]))

(def texture-url "/resources/images/backdrop.jpg")
(def diamond-url "/resources/gltf/diamond.glb")

(def aspect-h 3800)
(def aspect-w 5000)

(defn <background> []
  (let [{:keys [viewport aspect]} (use-three)
        texture (use-loader texture-loader-class texture-url)
        aspect-ratio (/ aspect-w aspect-h)
        vw (/ (.-width viewport) aspect-w)
        vh (/ (.-height viewport) aspect-h)
        base (if (> aspect aspect-ratio) vw vh)
        adapted-height (* aspect-h base)
        adapted-width (* aspect-w base)]
    (uix/memo (fn []
                (j/assoc! texture .-minFilter linear-filter))
              [(.-minFilter texture)])

    [:mesh {:layers 1
            :scale  #js [adapted-width adapted-height 1]}
     [:planeBufferGeometry {:attach "geometry"}]
     [:meshBasicMaterial {:attach    "material"
                          :map       texture
                          :depthTest false}]]))

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

(defn update-diamonds [gl viewport camera scene clock dummy model-ref env-fbo backface-fbo backface-material refraction-material diamonds]
  (let [model @model-ref]
    (doseq [[i diamond] (map-indexed vector diamonds)]
      (let [t (.getElapsedTime clock)
            position (j/get diamond .-position)
            rotation (j/get diamond .-rotation)
            direction (j/get diamond .-direction)
            factor (j/get diamond .-factor)
            rot-delta (* factor t)
            s (+ 1 factor)]
        (j/update! position 1 (fn [old-y] (- old-y (* (/ factor 5) direction))))

        (when (if (= direction 1) (< (j/get position 1) -50) (> (j/get position 1) 50))
          (let [w (.-width viewport)
                [r1] (repeatedly give-random-number)]
            (j/assoc! diamond .-position #js[(if (< i 5) 0 (- (/ w 2) (* r1 w)))
                                             (* 50 direction)
                                             (j/get position 2)])))

        (j/call-in dummy [.-position .-set] (j/get position 0) (j/get position 1) (j/get position 2))
        (j/call-in dummy [.-rotation .-set]
                   (+ (j/get rotation 0) rot-delta)
                   (+ (j/get rotation 1) rot-delta)
                   (+ (j/get rotation 2) rot-delta))
        (j/call-in dummy [.-scale .-set] s s s)

        (.updateMatrix dummy)
        (.setMatrixAt model i (.-matrix dummy))))

    (j/assoc-in! model [.-instanceMatrix .-needsUpdate] true)

    ; render env to fbo
    (j/assoc! gl .-autoClear false)
    (set-camera-layer! camera 1)
    (j/call gl .-setRenderTarget env-fbo)
    (j/call gl .-render scene camera)

    ; render cube backfaces to fbo
    (set-camera-layer! camera 0)
    (j/assoc! model .-material backface-material)
    (j/call gl .-setRenderTarget backface-fbo)
    (j/call gl .-clearDepth)
    (j/call gl .-render scene camera)

    ; render env to screen
    (set-camera-layer! camera 1)
    (j/call gl .-setRenderTarget nil)
    (j/call gl .-render scene camera)
    (j/call gl .-clearDepth)

    ; render cube with refraction material to screen
    (set-camera-layer! camera 0)
    (j/assoc! model .-material refraction-material)
    (j/call gl .-render scene camera)))

(defn <diamonds> []
  (let [{:keys [size viewport gl scene camera clock]} (use-three)
        model (uix/ref)
        gltf (use-loader GLTFLoader diamond-url)
        gltf-geometry (j/get-in gltf [.-__$ 1 .-geometry])

        [env-fbo backface-fbo backface-material refraction-material]
        (uix/memo (fn []
                    (let [w (.-width size)
                          h (.-height size)
                          env-fbo (create-webgl-render-target w h)
                          backface-fbo (create-webgl-render-target w h)
                          backface-material (BackfaceMaterial.)
                          refraction-material (RefractionMaterial. #js {:envMap      (.-texture env-fbo)
                                                                        :backfaceMap (.-texture backface-fbo)
                                                                        :resolution  #js [w h]})]
                      #js [env-fbo backface-fbo backface-material refraction-material]))
                  [size])

        dummy (uix/memo #(create-object-3d))

        diamonds
        (uix/memo (fn []
                    (into-array
                      (->> (range 80)
                           (map-indexed (partial gen-random-diamond viewport)))))
                  [viewport])

        ]

    (use-frame (partial update-diamonds gl viewport camera scene clock dummy model env-fbo backface-fbo backface-material refraction-material diamonds) 1)

    [:instancedMesh {:ref  model
                     :args #js [nil nil (count diamonds)]}
     [:bufferGeometry (merge {:dispose false
                              :attach  "geometry"}
                             (bean gltf-geometry))]
     [:meshBasicMaterial {:attach "material"}]]))

(defn app []
  [:> <:canvas> {:camera #js {:fov 50 :position #js [0 0 30]}}
   [:> <:suspense> {:fallback nil}
    [<background>]
    [<diamonds>]]])
