(ns spring.clj.web.body-extractor
  (:import
   (org.springframework.web.reactive.function.server ServerRequest)
   (org.springframework.web.reactive.function BodyExtractors)
   (java.util Map)))

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

(defn body->parts [^ServerRequest request]
  (->> (BodyExtractors/toParts) (.body request)))

(defn body->data-buffers [^ServerRequest request]
  (->> (BodyExtractors/toDataBuffers) (.body request)))
