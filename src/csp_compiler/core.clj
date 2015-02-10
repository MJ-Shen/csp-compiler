(ns csp-compiler.core
	(:use [me.raynes.fs :only [mkdirs parent file]]
				[csp-compiler.gen])
	(:require [clojure.java.io :as jio])
	(:import [java.io File]))


(defn read-file [path option]
	(let [content (slurp path :encoding (:encoding option))]
		(str content "####" option)))


(defn- parse
	"parse inputpath to generate namespace and outputPath"
	[path]
	(let [fs File/separatorChar op (str "cspBuilt" fs path)]
		{:src path
		 :out-path (.replaceAll op ".\\w+$" ".clj")
		 :ns (.replaceAll (.replaceAll op ".\\w+$" "") (str fs) ".")}
		))

(defn process
	[path option*]
	(println (str option*))
	(let [content (read-file path option*) csp-meta (parse path)]
		(write-file csp-meta content option*)))

(defn csp
	"cps  call the clojure server page (also created or update if necessary)
	:option [:encoding :auto-sync]"
	[path & op]
	(def option* (merge {:encoding "UTF-8"} (first op)))
	(process path option*))
