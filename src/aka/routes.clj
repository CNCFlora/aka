(ns aka.routes
  (:require [ring.util.response :as response])
  (:require [compojure.core :as r :refer [GET POST PUT DELETE defroutes context]]
            [compojure.route :as route])
  (:require [clojure.data.json :refer [write-str read-str]])
  (:require [aka.core :as core])
  (:require [aka.views :as views]))

(defmulti render
  (fn [req view & data]
    (keyword (second (.split (first (.split (or (get-in req [:headers "accept"]) "application/json") ",")) "/")))))

(defmethod render :json [_ view & data] (write-str (first data)))
(defmethod render :default [_ view & data] (apply view data))

(defroutes main
  (GET "/" req (render req views/index (core/get-all)))

  (GET "/nouns" req (render req views/index (core/get-all))) 
  (GET "/search" req (render req views/index (core/search (get-in req [:params :query])))) 

  (POST "/nouns" {{n1 :noun1 n2 :noun2} :params}
   (core/insert n1 n2)
   (response/redirect (views/to "/")))

  (POST "/noun/:n1"
    {{n1 :n1 n2 :noun2} :params}
     (core/insert n1 n2)
     (response/redirect (views/to "/")))

  (POST "/noun/:n1/:n2"
    {{n1 :n1 n2 :n2} :params}
     (core/insert n1 n2)
     (response/redirect (views/to "/")))

  (GET "/noun/:n1" req
    (render req views/index 
    (let [n1 (get-in req [:params :n1])]
      (map
        (fn [n2] {:noun1 n1 :noun2 n2})
        (core/get-noun n1)))))

  (GET "/noun/:n1/:n2/delete"
     {{n1 :n1 n2 :n2} :params} 
     (core/delete n1 n2)
     (response/redirect (views/to "/")))
  (DELETE "/noun/:n1/:n2" {{n1 :n1 n2 :n2} :params} 
     {{n1 :n1 n2 :n2} :params} 
     (core/delete n1 n2)
     (response/redirect (views/to "/")))

  (route/not-found (views/not-found)))

