(ns spring.clj.web.util.headers
  (:require [spring.clj.web.util.utils :refer [keyword->header]])
  (:import
   (org.springframework.http HttpHeaders)
   (org.springframework.util MultiValueMapAdapter)
   (java.util HashMap)))

(defn- ->multi-value-map [headers-map]
  (->> headers-map (HashMap.) (MultiValueMapAdapter.)))

(defn get-header [name ^HttpHeaders headers]
  (->> name keyword->header (.getFirst headers)))

(defn add-headers [header-map ^HttpHeaders headers]
  (->> header-map
       (map #(vector (-> % first keyword->header) (-> % second vector flatten)))
       (into {})
       (->multi-value-map)
       (.addAll headers)) headers)

(defn add-header [name value ^HttpHeaders headers]
  (add-headers #{[name value]} headers) headers)

(defn remove-header [name ^HttpHeaders headers]
  (->> name keyword->header (.remove headers)) headers)

(defn contains-header? [name ^HttpHeaders headers]
  (->> name keyword->header (.containsKey headers)))

(defn bearer-auth [token ^HttpHeaders headers]
  (.setBearerAuth headers token) headers)
