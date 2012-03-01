(defproject exceptional-clj "1.0.0-SNAPSHOT"
  :description "FIXME: write description"

  :disable-implicit-clean true

  :dev-dependencies [[s3-wagon-private "1.0.0"]]

  :dependencies [[org.clojure/clojure "1.3.0"]
                 [clj-http "0.2.7"]
                 [org.clojure/data.json "0.1.1"]
                 [gears "1.0.0-SNAPSHOT"]]

  :repositories {"nfr-releases" "s3p://newfound-mvn-repo/releases/"
                 "nfr-snapshots" "s3p://newfound-mvn-repo/snapshots/"})
