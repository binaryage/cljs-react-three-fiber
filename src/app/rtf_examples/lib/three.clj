(ns rtf-examples.lib.three)

(defmacro create-shader-material [opts]
  `(rtf-examples.lib.three/shader-material-class. (helix.impl.props2/translate-props ~opts)))
