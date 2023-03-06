(ns spring.clj.web.filter-function
  (:require [reactor-core.util.sam :refer [->function]])
  (:import (org.springframework.web.reactive.function.server HandlerFilterFunction)))

(defn ->filter-function ^HandlerFilterFunction [f]
  (reify HandlerFilterFunction (filter [_ request next] (f request next))))

(defn before [f]
  (HandlerFilterFunction/ofRequestProcessor (->function f)))

(defn after [f]
  (HandlerFilterFunction/ofResponseProcessor (->function f)))
