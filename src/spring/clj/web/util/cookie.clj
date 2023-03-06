(ns spring.clj.web.util.cookie
  (:require [spring.clj.web.util.utils :refer [keyword->str]])
  (:import
   (org.springframework.http HttpCookie)
   (clojure.lang MapEntry)))

(defn ->cookie
  ([seq] (->cookie (first seq) (second seq)))
  ([key value] (-> key keyword->str (HttpCookie. value))))

(defn cookie->entry [^HttpCookie cookie]
  (MapEntry. (-> (.getName cookie) keyword) (.getValue cookie)))
