(ns rtf-examples.demos.font
  (:require [rtf-examples.lib.rtf :refer [use-frame use-loader use-update]]
            [rtf-examples.lib.ui :refer [<canvas> use-ref use-memo $ defnc use-state use-effect]]
            [rtf-examples.lib.react :refer [<suspense>]]
            [rtf-examples.lib.three :refer [font-loader
                                            create-animation-mixer
                                            create-vector3
                                            update-mixer
                                            play-animation-action!
                                            get-clip-animation-action
                                            compute-bounding-box!
                                            export-bounding-box-size!
                                            get-geometry
                                            get-x get-y get-z
                                            <group>
                                            <mesh>
                                            <text-geometry>
                                            <mesh-normal-material>
                                            <scene>
                                            <buffer-geometry>
                                            <box-buffer-geometry>
                                            <mesh-standard-material>
                                            <ambient-light>
                                            <point-light>]]
            [rtf-examples.lib.helpers :refer [give-random-number
                                              set-position!
                                              get-elapsed-time
                                              sin
                                              cos
                                              get-position
                                              update-rotation!
                                              get-gltf-named-geometry
                                              get-gltf-animation
                                              get-gltf-named-material
                                              get-gltf-named-morph-target-dictionary
                                              get-gltf-named-morph-target-influences]]
            [rtf-examples.lib.misc :refer [gltf-loader]]
            [cljs-bean.core :refer [bean]]))

; -- constants --------------------------------------------------------------------------------------------------------------

(def flamingo-url "/resources/gltf/flamingo.glb")
(def parrot-url "/resources/gltf/parrot.glb")
(def stork-url "/resources/gltf/stork.glb")

(def font-url "/resources/fonts/bold.blob")

; -- data -------------------------------------------------------------------------------------------------------------------

(def birds [flamingo-url parrot-url stork-url])

; -- components -------------------------------------------------------------------------------------------------------------

(defnc <text> [props]
  (let [{:keys [children v-align h-align size color]
         :or   {v-align "center"
                h-align "center"
                size    1
                color   "#000000"}} props
        font (use-loader font-loader font-url)
        font-config (use-memo :auto-deps
                              #js {:font           font
                                   :size           40
                                   :height         30
                                   :curveSegments  32
                                   :bevelEnabled   true
                                   :bevelThickness 6
                                   :bevelSize      2.5
                                   :bevelOffset    0
                                   :bevelSegments  8})
        update-fn (fn [self]
                    (let [geom (get-geometry self)
                          size (create-vector3)]
                      (compute-bounding-box! geom)
                      (export-bounding-box-size! geom size)
                      (let [px (case h-align
                                 "center" (- (/ (get-x size) 2))
                                 "right" 0
                                 "left" (- (get-x size)))
                            py (case v-align
                                 "center" (- (/ (get-y size) 2))
                                 "top" 0
                                 "bottom" (- (get-y size)))
                            pz (get-z (get-position self))]
                        (set-position! self px py pz))))
        mesh (use-update update-fn [children])]
    ($ <group> {:scale #js [(* 0.1 size) (* 0.1 size) 0.1]
                :&     props}
      ($ <mesh> {:ref mesh}
        ($ <text-geometry> {:args #js [children font-config]})
        ($ <mesh-normal-material>)))))

(defnc <jumbo> []
  (let [text-ref (use-ref)]
    (use-frame (fn [props]
                 (let [text @text-ref
                       {:keys [clock]} (bean props)
                       t (get-elapsed-time clock)
                       rot (* 0.3 (sin t))]
                   (update-rotation! text (fn [_x _y _z] [rot rot rot])))))
    ($ <group> {:ref text-ref}
      ($ <text> {:h-align "left" :position #js [0 4.2 0] :children "REACT"})
      ($ <text> {:h-align "left" :position #js [0 0 0] :children "THREE"})
      ($ <text> {:h-align "left" :position #js [0 -4.2 0] :children "FIBER"})
      ($ <text> {:h-align "left" :position #js [12 0 0] :children "3" :size 3})
      ($ <text> {:h-align "left" :position #js [16.5 -4.2 0] :children "X"}))))

(defnc <bird> [props]
  (let [{:keys [speed factor url position rotation]} props
        gltf (use-loader gltf-loader url)
        group-ref (use-ref)
        mixer (use-state #(create-animation-mixer))]
    (use-effect :auto-deps
                (let [first-animation (get-gltf-animation gltf)
                      action (get-clip-animation-action @mixer first-animation @group-ref)]
                  (play-animation-action! action)))
    (use-frame (fn [_state delta]
                 (let [group @group-ref]
                   (update-rotation! group (fn [x y z]
                                             (let [rs (sin (/ (* delta factor) 2))
                                                   rc (cos (/ (* delta factor) 2))
                                                   new-y (+ y (* rs rc 1.5))]
                                               [x new-y z])))
                   (update-mixer @mixer (* delta speed)))))
    ($ <group> {:ref group-ref}
      ($ <scene> {:name     "Scene"
                  :position position
                  :rotation rotation}
        ($ <mesh> {:name                    "Object_0"
                   :material                (get-gltf-named-material gltf :Material_0_COLOR_0)
                   :geometry                (get-gltf-named-geometry gltf :Object_0)
                   :morph-target-dictionary (get-gltf-named-morph-target-dictionary gltf :Object_0)
                   :morph-target-influences (get-gltf-named-morph-target-influences gltf :Object_0)
                   :rotation                #js [1.5707964611537577, 0, 0]})))))

(defnc <birds> []
  ($ <group>
    (for [i (range 100)]
      (let [[r1 r2 r3 r4 r5 r6] (repeatedly give-random-number)
            x (* (+ 15 (* 30 r1)) (if (> r2 0.5) -1 1))
            y (+ -10 (* 20 r3))
            z (+ -5 (* 10 r4))
            index (Math/round (* r5 (dec (count birds))))
            speed (case index
                    0 2
                    1 5
                    2 0.5)
            factor (case index
                     2 (+ 0.5 r6)
                     0 (+ 0.25 r6)
                     1 (+ 0.5 r6))]
        ($ <bird> {:key      i
                   :url      (nth birds index)
                   :position #js [x y z]
                   :rotation #js [0 (if (> x 0) Math/PI 0) 0]
                   :speed    speed
                   :factor   factor})))))

(defnc <demo> []
  ($ <canvas> {:camera #js {:position #js [0 0 35]}
               :style  {:background "radial-gradient(at 50% 60%, #873740 0%, #272730 40%, #171720 80%, #070710 100%)"}}
    ($ <ambient-light> {:intensity 2})
    ($ <point-light> {:position #js [40 40 40]})
    ($ <suspense> {:fallback "loading font demo"}
      ($ <jumbo>)
      ($ <birds>))))
