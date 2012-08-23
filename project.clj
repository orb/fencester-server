(defproject fencester-server "0.1.0-SNAPSHOT"
  :description "Fencester Server"
  :url "http://example.com/FIXME"
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [cheshire "4.0.1"]
                 [hiccup "1.0.1"]
                 [clj-http "0.5.3"]
                 [compojure "1.1.1"]]
  :plugins [[lein-ring "0.7.1"]
            [lein-swank "1.4.2"]]
  :ring {:handler fencester-server.handler/app}
  :dev-dependencies [[ring-mock "0.1.2"]])
