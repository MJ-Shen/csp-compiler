(defproject csp-compiler "0.1.1"
  :description "Compiler for clojure server page"
  :url "https://github.com/MJ-Shen/csp-compiler"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :deploy-repositories [["releases" {:url "https://oss.sonatype.org/service/local/staging/deploy/maven2/"
                                   :creds :gpg}
                       "snapshots" {:url "https://oss.sonatype.org/content/repositories/snapshots/"
                                    :creds :gpg}]]
  :dependencies [[org.clojure/clojure "1.6.0"]
  							 [me.raynes/fs "1.4.6"]])
