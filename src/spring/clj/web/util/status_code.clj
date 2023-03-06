(ns spring.clj.web.util.status-code
  (:require [spring.clj.web.util.utils :refer [keyword->enum]])
  (:import (org.springframework.http HttpStatus)))

(defn keyword->http-status [k]
  (keyword->enum HttpStatus k))

(defn code->http-status [^Integer code]
  (HttpStatus/valueOf ^Integer code))

(defn http-status->code [^HttpStatus status]
  (.value status))

(defn error? [^HttpStatus status]
  (.isError status))

(defn is-1xx? [^HttpStatus status]
  (.is1xxInformational status))

(defn is-2xx? [^HttpStatus status]
  (.is2xxSuccessful status))

(defn is-3xx? [^HttpStatus status]
  (.is3xxRedirection status))

(defn is-4xx? [^HttpStatus status]
  (.is4xxClientError status))

(defn is-5xx? [^HttpStatus status]
  (.is5xxServerError status))
