(ns spring.clj.application-context
  (:require
   [clojure.walk :refer [keywordize-keys]]
   [clojure.tools.logging :refer [info]])
  (:import
   (org.springframework.core.env Environment)
   (java.util Map)
   (org.springframework.context ApplicationContext))
  (:gen-class
   :name ^{org.springframework.stereotype.Component ""} spring.clj.clojure_component
   :state _
   :constructors {[org.springframework.context.ApplicationContext] []}
   :init component-init))

(defonce state (atom {}))

(defn -component-init [^ApplicationContext ctx]
  (info "Initializing the clojure component")
  (swap! state assoc :ctx ctx)
  [[] {}])

(defn get-application-context ^ApplicationContext []
  (:ctx @state))

(defn id ^String []
  (.getId (get-application-context)))

(defn application-name ^String []
  (.getApplicationName (get-application-context)))

(defn parent ^ApplicationContext []
  (.getParent (get-application-context)))

(defn environment ^Environment []
  (.getEnvironment (get-application-context)))

(defn beans-of-type ^Map [^Class c]
  (.getBeansOfType (get-application-context) c))

(defn beans-with-annotation ^Map [^Class annotation]
  (.getBeansWithAnnotation (get-application-context) annotation))

(defn beans ^Map
  ([] (beans Object))
  ([^Class c] (->> c beans-of-type (into {}) keywordize-keys)))
