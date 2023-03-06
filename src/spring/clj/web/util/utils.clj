(ns spring.clj.web.util.utils
  (:refer-clojure :exclude [replace])
  (:require [clojure.string :refer [replace upper-case split capitalize join]])
  (:import (org.springframework.http HttpMethod)))

(defn keyword->str [k]
  (-> k str (replace ":" "")))

(defn keyword->const-field-str [k]
  (-> k keyword->str (replace "-" "_") upper-case))

(defn keyword->enum [type k]
  (->> k keyword->const-field-str (Enum/valueOf type)))

(defn keyword->http-method [k]
  (-> k keyword->str upper-case HttpMethod/valueOf))

(defn keyword->header [k]
  (-> k keyword->str (split #"-")
      (->> (map capitalize) (join "-"))))
