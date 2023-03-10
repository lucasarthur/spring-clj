(ns spring.clj.web.server.request
  (:require
   [clojure.string :refer [lower-case join]]
   [spring.clj.web.util.utils :refer [keyword->str]])
  (:import
   (org.springframework.web.reactive.function.server ServerRequest)
   (org.springframework.web.server ServerWebExchange)
   (org.springframework.http.server.reactive ServerHttpRequest)
   (java.util Map)))

(defn request->server-web-exchange ^ServerWebExchange [^ServerRequest request]
  (.exchange request))

(defn exchange->http-request ^ServerHttpRequest [^ServerWebExchange exchange]
  (.getRequest exchange))

(defn request->http-request ^ServerHttpRequest [^ServerRequest request]
  (-> request request->server-web-exchange exchange->http-request))

(defn request-method [^ServerHttpRequest request]
  (-> request .getMethod .name lower-case keyword))

(defn uri [^ServerHttpRequest request]
  (-> request .getURI str))

(defn path [^ServerHttpRequest request]
  (-> request .getPath .pathWithinApplication .value))

(defn server-port [^ServerHttpRequest request]
  (-> request .getLocalAddress .getPort))

(defn server-name [^ServerHttpRequest request]
  (-> request .getLocalAddress .getHostName))

(defn remote-address [^ServerHttpRequest request]
  (-> request .getRemoteAddress .getHostName))

(defn scheme [^ServerHttpRequest request]
  (-> request .getURI .getScheme keyword))

(defn path-variable [name ^ServerRequest request]
  (->> name keyword->str (.pathVariable request)))

(defn path-variables [^ServerRequest request]
  (persistent!
   (reduce
    (fn [acc [k v]] (assoc! acc (-> k lower-case keyword) (join "," v)))
    (transient {})
    (.pathVariables request))))

(defn query-string [^ServerHttpRequest request]
  (when-let [query-params (.getQueryParams request)]
    (->> query-params
         (map (fn [[k vs]] (map #(str k "=" %) vs)))
         (flatten)
         (join "&"))))

(defn query-string-map [^ServerHttpRequest request]
  (when-let [query-params (.getQueryParams request)]
    (persistent!
     (reduce
      (fn [acc [k v]] (assoc! acc (-> k lower-case keyword) (join "," v)))
      (transient {})
      query-params))))

(defn headers [^ServerHttpRequest request]
  (persistent!
   (reduce
    (fn [acc [k v]] (assoc! acc (-> k lower-case keyword) (join "," v)))
    (transient {})
    (.getHeaders request))))

(defn cookies [^ServerHttpRequest request]
  (persistent!
   (reduce
    (fn [acc [k v]] (assoc! acc (-> k lower-case keyword) (join "," (str v))))
    (transient {})
    (.getCookies request))))

(defn body->mono
  ([^ServerRequest request] (body->mono Map request))
  ([type ^ServerRequest request] (-> request (.bodyToMono type))))

(defn body->flux
  ([^ServerRequest request] (body->flux Map request))
  ([type ^ServerRequest request] (-> request (.bodyToFlux type))))

(defn body->form-data [^ServerRequest request]
  (.formData request))

(defn body->multipart-data [^ServerRequest request]
  (.multipartData request))

(defn request-map [^ServerRequest request]
  (let [req (-> request request->server-web-exchange exchange->http-request)]
    (merge {:server-port (server-port req)
            :server-name (server-name req)
            :remote-addr (remote-address req)
            :uri (uri req)
            :path (path req)
            :cookies (cookies req)
            :scheme (scheme req)
            :request-method (request-method req)
            :headers (headers req)}
           (when-let [cookies (cookies req)]
             {:cookies cookies})
           (when-let [ssl-info (.getSslInfo req)]
             {:ssl-client-cert (-> ssl-info .getPeerCertificates (aget 0))})
           (when-let [query-string (query-string req)]
             {:query-string query-string}))))
