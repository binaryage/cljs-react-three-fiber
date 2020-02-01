(ns react-three-fiber.examples.lib.three)

(defmacro create-shader-material [opts]
  `(react-three-fiber.examples.lib.three/shader-material-class. (helix.impl.props2/translate-props ~opts)))
