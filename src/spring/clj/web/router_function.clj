(ns spring.clj.web.router-function
  (:refer-clojure :exclude [and or filter])
  (:require
   [spring.clj.web.request-predicate :refer [routing]]
   [spring.clj.web.handler-function :refer [->handler-function]]
   [spring.clj.web.filter-function :refer [->filter-function]])
  (:import
   (org.springframework.web.reactive.function.server RequestPredicate RouterFunction RouterFunctions)))

(defn and [^RouterFunction other ^RouterFunction f]
  (.and f other))

(defn or [^RouterFunction other ^RouterFunction f]
  (.or f other))

(defn filter [f ^RouterFunction router]
  (.filter router (->filter-function f)))

(defn route
  ([http-methods pattern handler] (route (routing http-methods pattern) handler))
  ([predicate handler] (RouterFunctions/route ^RequestPredicate predicate (->handler-function handler))))
