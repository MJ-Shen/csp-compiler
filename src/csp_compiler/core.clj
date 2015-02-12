(ns csp-compiler.core
	(:use [me.raynes.fs :only [mkdirs parent file]]
				[csp-compiler.gen])
	(:require [clojure.java.io :as jio])
	(:import [java.io File]))

(def *csp-ns-map* (ref {}))

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

(defn invoke
	"call out (function) if no changes happen"
	[^String ns]
	(binding [*ns* (the-ns ns)]
		(println *ns* (eval 'out))
		((eval 'out))))

(defn if-recompile
	[csp-meta]
	(cond
		(:force-sync option*) true
		(:read-time csp-meta)))

(defn process
	[path option*]
	(println (str option*))
	(let [csp-meta (parse path)]
		if())
	(let [content (read-file path option*) ]
		(write-file csp-meta content option*)))

(defn csp
	"cps  call the clojure server page (also created or update if necessary)
	:option [:encoding UTF-8 :auto-sync true :force-sync false]"
	[path & op]
	(def option* (merge {:encoding "UTF-8"} (first op)))
	(process path option*))


