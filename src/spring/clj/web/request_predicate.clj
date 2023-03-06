(ns spring.clj.web.request-predicate
  (:refer-clojure :exclude [methods and or])
  (:require [spring.clj.web.util.utils :refer [keyword->http-method]])
  (:import (org.springframework.web.reactive.function.server RequestPredicate RequestPredicates)))

(defn and [^RequestPredicate other ^RequestPredicate p]
  (.and p other))

(defn or [^RequestPredicate other ^RequestPredicate p]
  (.or p other))

(def match-all (RequestPredicates/all))

(defn methods [http-methods]
  (->> http-methods
      keyword->http-method
      (if-not list? list)
      RequestPredicates/method))

(defn path [pattern]
  (RequestPredicates/path pattern))

(defn routing [http-methods pattern]
  (and (methods http-methods) (path pattern)))
