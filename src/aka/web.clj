(ns aka.web
  (:use ring.middleware.params)
  (:use ring.middleware.keyword-params)
  (:use ring.middleware.resource)
  (:use ring.middleware.content-type)
  (:use ring.middleware.not-modified)
  (:use ring.middleware.reload)
  (:use hiccup.middleware)
  (:require [environ.core :refer (env)])
  (:require [immutant.web :as web]
            [compojure.route :as route])
  (:require [aka.routes :as routes]
            [aka.core   :as core])
  (:gen-class))

(def app
  (-> #'routes/main
    (wrap-resource "public")
    (wrap-content-type)
    (wrap-not-modified)
    (wrap-keyword-params)
    (wrap-params)
    (wrap-base-url (env :proxy))
    (wrap-reload)))

(defn -main
  [ & args] 
  (let
    [host (System/getProperty "HOST" "0.0.0.0")
     port (System/getProperty "PORT" "8080")]
    (println "Listening on" (str host ":" port))
    (core/connect)
    (web/run #'app {:port (Integer/valueOf port) :host host})))


