(ns aka.core
  (:require [environ.core :refer (env)])
  (:require [clojure.java.io :refer (file)])
  (:require [clojure.java.jdbc :refer :all]))

(declare conn)

(defn create-db
  [db] 
   (db-do-commands db
    (create-table-ddl 
      :aka
      [:noun1 :text]
      [:noun2 :text])))

(defn connect
  [] 
  (let [db-path   (or (env :database) "/var/data/aka/aka.db")
        db-file   (java.io.File. db-path)
        db-folder (java.io.File. (.getParent db-file))
        create    (not (.exists db-file))]
      (if (not (.exists db-folder)) (.mkdir db-folder))
      (def conn {:classname "org.sqlite.JDBC" :subprotocol "sqlite" :subname (.getAbsolutePath db-file)})
      (if create (create-db conn))))


(defn insert
  [n1 n2] 
   (insert! conn :aka {:noun1 (.trim n1) :noun2 (.trim n2)}))

(defn get-all
  [] (query conn "SELECT * FROM aka;"))

(defn delete
  [n1 n2] 
   (delete! conn :aka ["noun1 = ? AND noun2 = ?" n1 n2]))

(defn get-noun 
  [n] 
  (sort
    (distinct
      (query conn [ "SELECT * FROM aka WHERE noun1 = ? OR noun2 = ?" n n]
       :row-fn (fn [row] (if (= (:noun1 row) n) (:noun2 row) (:noun1 row)))))))

(defn search
  [q] 
  (query conn ["SELECT * FROM aka where noun1 like ? or noun2 like ?" (str "%" q "%") (str "%" q "%")]))

