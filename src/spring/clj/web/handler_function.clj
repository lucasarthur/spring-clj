(ns spring.clj.web.handler-function
  (:import (org.springframework.web.reactive.function.server HandlerFunction)))

(defn ->handler-function ^HandlerFunction [f]
  (reify HandlerFunction (handle [_ request] (f request))))
