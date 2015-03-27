(ns csp-compiler.core
	(:use	[me.raynes.fs :only [file]]
			[csp-compiler.gen]
			[csp-compiler.mylog])
	(:require [clojure.java.io :as io]))
	; (:import [java.io File]))

(def ^{:dynamic true} *csp-ns-map* (atom {}))

(defn read-file [path option]
	(log "read-file:start#" path)
	(slurp (io/resource path) :encoding (:encoding option)))


(defn- parse
	"parse inputpath to generate namespace and outputPath"
	[path];File/separatorChar
	(let [fs "/" op (str "cspBuilt" fs path)]
		{:src path
		 :out-path (.replaceAll op ".\\w+$" ".clj")
		 :ns (.replaceAll (.replaceAll op ".\\w+$" "") (str fs) ".")}
		))

(defn invoke
	"call out (function) if no changes happen"
	[^String ns]
	(log "!!invoke:#####" ns)
	(binding [*ns* (-> ns symbol the-ns)]
		(log "!!invoke:" *ns* (eval 'out))
		((eval 'out))))

(defn if-recompile
	"whether recompile the csp or not"
	[csp-meta option*]
	(log "if-recompile:start#" csp-meta)
	(or (not csp-meta)
		(if-let [fs (:force-sync option*)]
			fs
			(< (:read-time csp-meta) (-> csp-meta :src io/resource file .lastModified)))))


(defn process
	[path option*]
	(log "process:start#" option*)
	(let [csp-meta (parse path) ns (:ns csp-meta)]
		(when (if-recompile (@*csp-ns-map* ns) option*)
				(write-file csp-meta (read-file path option*) option*)
				(swap! *csp-ns-map* assoc ns 
					(assoc csp-meta :read-time (System/currentTimeMillis))))
		(invoke ns)))
			

(defn csp
	"cps  call the clojure server page (also created or update if necessary)
	:option [:encoding UTF-8 :auto-sync true :force-sync false]"
	[path & op]
	(def option* (merge {:encoding "UTF-8"} (first op)))
	(process path option*))


