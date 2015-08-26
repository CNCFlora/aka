(defproject aka "0.0.1"
  :description "Also Kown As database as a service"
  :url "http://github.com/CNCFlora/aka"
  :license {:name "MIT"}
  :main aka.web
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.immutant/web "2.0.2"]
                 [ring/ring-core "1.4.0"]
                 [ring/ring-devel "1.4.0"]
                 [compojure "1.4.0"]
                 [hiccup "1.0.5"]
                 [environ "1.0.0"]
                 [org.clojure/data.json "0.2.5"]
                 [org.clojure/java.jdbc "0.4.1"]
                 [org.xerial/sqlite-jdbc "3.8.11.1"]]
  :source-paths ["src"]
  :profiles {:uberjar {:aot :all}
             :dev  {:env {:database "./data/aka.db"}
                    :plugins [[com.jakemccrary/lein-test-refresh "0.9.0"]
                              [lein-environ "1.0.0"]]}})
