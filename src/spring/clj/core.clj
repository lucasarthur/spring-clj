(ns spring.clj.core
  (:require
   [spring.clj.web :refer [set-http-handler! set-websocket-handler! -default-http-handler -default-ws-handler]]
   [clojure.tools.logging :refer [info]])
  (:import (org.springframework.boot SpringApplication))
  (:gen-class
   :name ^{org.springframework.boot.autoconfigure.SpringBootApplication {}
           org.springframework.context.annotation.ComponentScan ["spring.clj"]} spring.clj.app))

(defn -main [& args]
  (SpringApplication/run (Class/forName "spring.clj.app") (into-array String args)))

(defn main [_]
  (info "Initializing clojure application")
  (info "Assigning default handlers")
  (set-http-handler! -default-http-handler)
  (set-websocket-handler! -default-ws-handler)
  (info "Done! Clojure application initialized"))
