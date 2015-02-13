(ns csp-compiler.core
	(:use [me.raynes.fs :only [mkdirs parent file]]
				[csp-compiler.gen])
	(:require [clojure.java.io :as jio])
	(:import [java.io File]))

(def ^{:dynamic true} *csp-ns-map* (atom {}))

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
	"whether recompile the csp or not"
	[csp-meta option*]
	(if-let [fs (:force-sync option*)]
		fs
		(< (:read-time csp-meta) (-> csp-meta :src file .lastModified))))


(defn process
	[path option*]
	(println (str option*))
	(let [csp-meta (parse path) ns (:ns csp-meta)]
		(if (if-recompile (@*csp-ns-map* ns) option*)
			(let [content (read-file path option*)]
				(write-file csp-meta content option*)
				(swap! *csp-ns-map* assoc ns 
					(assoc csp-meta :read-time (System/currentTimeMillis))))
			(invoke ns))))
			

(defn csp
	"cps  call the clojure server page (also created or update if necessary)
	:option [:encoding UTF-8 :auto-sync true :force-sync false]"
	[path & op]
	(def option* (merge {:encoding "UTF-8"} (first op)))
	(process path option*))


