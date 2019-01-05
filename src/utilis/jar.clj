(ns utilis.jar)

(defmacro version
  "Return the version during the compilation phase.
  Only works in leiningen"
  [jar-name]
  `~(System/getProperty (str jar-name ".version")))
