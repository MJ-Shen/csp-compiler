(ns csp-compiler.gen
	(:use	[me.raynes.fs :only [mkdirs parent file]]
			[clojure.string :only [triml]]
			[csp-compiler.mylog])
	(:require [clojure.java.io :as jio]))

(defn- create-dir
	[^String fp]
	(println fp)
	(-> fp file parent mkdirs))

(def CR (System/getProperty "line.separator"))

(defn init-ns
	[^String ns ^String content ^java.io.Writer w ]
	(log "init-ns:start#" ns)
	(let [header (triml content)]
		(if (= "<`@" (.substring header 0 3))
			(let [offset (.indexOf header "`>")]
				(.write w 
					(apply str "(ns " ns CR (.substring header 3 offset) ")" CR "(defn out [] (apply str "))
				offset)
			(do 
				(.write w 
					(apply str "(ns " ns ")" CR "(defn out [] (apply str "))
				0))))

(defn filter! [content]
	(.replaceAll content "\"" "\\\\\""))

;(load-file "cspBuilt/csp/aa.clj")
(defn write-file [csp-meta ^String content option*]
	(log "write-file:start#" csp-meta)
	(let [ fp (:out-path csp-meta)]
		(create-dir fp)
		(with-open [^java.io.Writer w (jio/writer fp :encoding (:encoding option*))]
	    	(loop [offset (init-ns (:ns csp-meta) content w)]
	    		(let [nexl (.indexOf content "<`" offset)]
	  				(if (= -1 nexl)
							(.write w (str \" (filter! (.substring content (+ offset 2) )) \"))
	  					(let [nexr (.indexOf content "`>" nexl)]
	    					(.write w 
	    						(str \" (filter! (.substring content (+ offset 2) nexl)) "\" " (.substring content (+ nexl 2) nexr ) " "))
	    					(recur nexr)))))
	    	(.write w "))")
	    	(load-file fp))
    	(log "write-file:end-to-load#" fp (type fp))
    	(try
    		(load-string (slurp fp))
    		(catch Exception e (log e)))
		; (load-file fp)
		(log "write-file:load-end#")))

