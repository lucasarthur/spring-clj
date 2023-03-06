(ns spring.clj.web.util.media-type
  (:require
   [spring.clj.web.util.utils :refer [keyword->str]]
   [clojure.string :refer [lower-case]])
  (:import (org.springframework.http MediaType)))

(def media-types
  {:all (MediaType/ALL_VALUE)
   :form-url-encoded (MediaType/APPLICATION_FORM_URLENCODED_VALUE)
   :json (MediaType/APPLICATION_JSON_VALUE)
   :ndjson (MediaType/APPLICATION_NDJSON_VALUE)
   :binary (MediaType/APPLICATION_OCTET_STREAM_VALUE)
   :pdf (MediaType/APPLICATION_PDF_VALUE)
   :xml (MediaType/APPLICATION_XML_VALUE)
   :gif (MediaType/IMAGE_GIF_VALUE)
   :jpeg (MediaType/IMAGE_JPEG_VALUE)
   :png (MediaType/IMAGE_PNG_VALUE)
   :form-data (MediaType/MULTIPART_FORM_DATA_VALUE)
   :sse (MediaType/TEXT_EVENT_STREAM_VALUE)
   :plain (MediaType/TEXT_PLAIN_VALUE)
   :html (MediaType/TEXT_HTML_VALUE)})

(defn keyword->media-type [k]
  ((-> k keyword->str lower-case keyword) media-types))

(defn parse [media-type-value]
  (MediaType/parseMediaType media-type-value))
