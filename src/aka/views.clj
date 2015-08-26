(ns aka.views
  (:use hiccup.core
        hiccup.page)
  (:require [environ.core :refer [env]])
  (:require [clojure.data.json :as json]))

(defn to
  [to] (str (or (env :proxy) "") to))

(defn get-lang
  [] 
  (apply str (first (partition 2 (or (env :lang) "en")))))

(def strings 
  (merge
    (json/read-str (slurp (clojure.java.io/resource (str "locales/en.json"))) :key-fn keyword)
    (if-let [lang-file (clojure.java.io/resource (str "locales/" (get-lang) ".json"))]
      (json/read-str (slurp lang-file) :key-fn keyword))))

(defn localize
  [info]
  (get strings info))

(def t localize)

(defn icon
  [iname] [:span.fa {:class (str "fa-" (name iname))}])

(defn page
  [title & content] 
   (html5
     [:head
      [:meta {:charset "UTF-8"}]
      [:meta {:name "viewport" :content "width=device-width, initial-scale=1"}]
      [:title (str "A.K.A Database " title)]
      (include-css "http://yui.yahooapis.com/pure/0.6.0/pure-min.css")
      (include-css "https://maxcdn.bootstrapcdn.com/font-awesome/4.4.0/css/font-awesome.min.css")
      (include-css "/css/aka.css")]
     [:body
      [:div.layout.pure-g
       [:header 
        [:h1.pure-u-1 (t :title)]
        [:p.pure-u-1 (icon :info) (t :names-are-bi)]]
       [:div.pure-u-1.content content]
      ]]))

(defn search-form
  [] 
  [:form.pure-form.pure-form-stacked.pure-u-1 {:method "GET" :action (to "/search")}
   [:field-set
    [:legend (t :search)]
    [:input.pure-input-1 {:type "text" :placeholder "Term or noun" :name "query"} ]
    [:button.pure-button (icon :search)]
    ]])

(defn add-form
  []
  [:form.pure-form.pure-form-stacked.pure-u-1 {:method "POST" :action (to "/nouns" )}
   [:field-set
    [:legend (t :add)]
    [:label {:for "noun1"} (t :noun)]
    [:input.pure-input-1 {:type "text" :placeholder "Term or noun" :name "noun1"}]
    [:label {:for "noun2"} (t :aka)]
    [:input.pure-input-1 {:type "text" :placeholder "Term or noun" :name "noun2"}]
    [:button.pure-button (icon :plus)]
    ]])

(defn listing
  [data] 
   [:ul 
    (for [item data]
      [:li 
       (:noun1 item)
       (icon :arrows-h)
       (:noun2 item)
       [:a {:href (to (str  "/noun/" (:noun1 item) "/" (:noun2 item) "/delete"))}
        (icon :trash)]])])

(defn index
  [data]
    (page "Index"
      [:div.main.pure-u-4-5 
       [:h2 (t :known-aka) ":"]
       (listing data)]
      [:div.sidebar.pure-u-1-5
        (search-form)
        (add-form)]
          ))

(defn not-found
  [] (page "Not Found"
     [:h2 "Page not found"]
     [:p "Sorry, we can't find the page you were looking for."]))

