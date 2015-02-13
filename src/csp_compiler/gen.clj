(ns csp-compiler.gen
	(:use [me.raynes.fs :only [mkdirs parent file]]
				[clojure.string :only [triml]])
	(:require [clojure.java.io :as jio]))

(defn- create-dir
	[^String fp]
	(println fp)
	(-> fp file parent mkdirs))

(def CR (System/getProperty "line.separator"))

(defn init-ns
	[^String ns ^String content ^java.io.Writer w ]
	(let [header (triml content)]
		(if (= "<`@" (.substring header 0 3))
			(let [offset (.indexOf header "`>")]
				(.write w 
					(str "(ns " ns CR (.substring header 3 offset) ")" CR "(defn out [] (str "))
				offset)
			(do 
				(.write w 
					(str "(ns " ns ")" CR "(defn out [] (str "))
				0))))

(defn filter! [content]
	(.replaceAll content "\"" "FFFFF\\\\\""))

;(load-file "cspBuilt/csp/aa.clj")
(defn write-file [csp-meta ^String content option]
	(let [ fp (:out-path csp-meta)]
		(create-dir fp)
		(with-open [^java.io.Writer w (jio/writer fp :encoding (:encoding option))]
    	(loop [offset (init-ns (:ns csp-meta) content w)]
    		(let [nexl (.indexOf content "<`" offset)]
  				(if (= -1 nexl)
						(.write w (str \" (filter! (.substring content (+ offset 2) )) \"))
  					(let [nexr (.indexOf content "`>" nexl)]
    					(.write w 
    						(str \" (filter! (.substring content (+ offset 2) nexl)) "\" " (.substring content (+ nexl 2) nexr ) " "))
    					(recur nexr)))))
    	(.write w "))"))
		(load-file fp)))

