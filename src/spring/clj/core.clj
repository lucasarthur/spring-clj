(ns spring.clj.core
  (:require
   [spring.clj.application-context :refer [beans]]
   [clojure.tools.logging :refer [info]])
  (:import (org.springframework.boot SpringApplication))
  (:gen-class
   :name ^{org.springframework.boot.autoconfigure.SpringBootApplication {}
           org.springframework.context.annotation.ComponentScan ["spring"]} spring.core.app))

(defn -main [& args]
  (SpringApplication/run (Class/forName "spring.core.app") (into-array String args)))

(defn main [_]
  (info "Initializing clojure application...")
  (info "Beans: " (beans)))
