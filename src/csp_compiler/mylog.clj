(ns csp-compiler.mylog
	(:use [me.raynes.fs :only [mkdirs parent file]])
	(:require [clojure.java.io :as jio])
	(:import [java.io File]))

(defn log
 [& form]
 (println form))


