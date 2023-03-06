(ns spring.clj.web.util.media-type
  (:require
   [spring.clj.web.util.utils :refer [keyword->str]]
   [clojure.string :refer [lower-case]])
  (:import (org.springframework.http MediaType)))

(def all (MediaType/ALL_VALUE))
(def form-url-encoded (MediaType/APPLICATION_FORM_URLENCODED_VALUE))
(def json (MediaType/APPLICATION_JSON_VALUE))
(def ndjson (MediaType/APPLICATION_NDJSON_VALUE))
(def binary (MediaType/APPLICATION_OCTET_STREAM_VALUE))
(def pdf (MediaType/APPLICATION_PDF_VALUE))
(def xml (MediaType/APPLICATION_XML_VALUE))
(def gif (MediaType/IMAGE_GIF_VALUE))
(def jpeg (MediaType/IMAGE_JPEG_VALUE))
(def png (MediaType/IMAGE_PNG_VALUE))
(def form-data (MediaType/MULTIPART_FORM_DATA_VALUE))
(def sse (MediaType/TEXT_EVENT_STREAM_VALUE))
(def plain (MediaType/TEXT_PLAIN_VALUE))
(def html (MediaType/TEXT_HTML_VALUE))

(defn keyword->media-type [k]
  (-> k str keyword->str lower-case symbol resolve deref))

(defn parse [media-type-value]
  (MediaType/parseMediaType media-type-value))
