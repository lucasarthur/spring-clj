(ns spring.clj.web.server.response
  (:require
   [spring.clj.web.body-inserter :as inserter]
   [spring.clj.web.util.status-code :refer [http-status->code code->http-status keyword->http-status]]
   [spring.clj.web.util.media-type :as mime :refer [keyword->media-type]]
   [spring.clj.web.util.headers :as h]
   [reactor-core.util.sam :refer [->consumer]]
   [clojure.string :refer [lower-case join]]
   [cheshire.core :refer [generate-string]])
  (:import
   (org.springframework.web.reactive.function.server ServerResponse ServerResponse$BodyBuilder)
   (org.reactivestreams Publisher)
   (java.util Map)))

(defn status [s]
  (cond
    (keyword? s) (-> s keyword->http-status ServerResponse/status)
    (int? s) (-> s code->http-status ServerResponse/status)
    :else (ServerResponse/status 200)))

(defn content-length [length ^ServerResponse$BodyBuilder b]
  (.contentLength b length))

(defn content-type [media-type ^ServerResponse$BodyBuilder b]
  (->> media-type keyword->media-type mime/parse (.contentType b)))

(defn headers [headers ^ServerResponse$BodyBuilder b]
  (->> (->consumer #((-> headers (h/add-headers %)))) (.headers b)))

(defn body-value [data ^ServerResponse$BodyBuilder b]
  (->> data generate-string (.bodyValue b)))

(defn body
  ([^Publisher p ^ServerResponse$BodyBuilder b] (body Map p b))
  ([type ^Publisher p ^ServerResponse$BodyBuilder b] (.body b p type)))
  
(defn empty-body [^ServerResponse$BodyBuilder b]
  (->> (inserter/empty-body) (.body b)))

(defn sse-response [^Publisher event-publisher ^ServerResponse$BodyBuilder b]
  (->> event-publisher inserter/sse-body (.body b)))

(defn get-status [^ServerResponse response]
  (-> response .statusCode http-status->code))

(defn get-headers [^ServerResponse response]
  (persistent! (reduce
                (fn [acc [k v]] (assoc! acc (lower-case k) (join "," v)))
                (transient {})
                (.headers response))))

;; TODO - cookies
(defn get-cookies [^ServerResponse response]
  (.cookies response))
