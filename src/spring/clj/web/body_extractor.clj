(ns spring.clj.web.body-extractor
  (:import
   (org.springframework.web.reactive.function.server ServerRequest)
   (org.springframework.web.reactive.function BodyExtractors)))

(defn body->parts [^ServerRequest request]
  (->> (BodyExtractors/toParts) (.body request)))

(defn body->data-buffers [^ServerRequest request]
  (->> (BodyExtractors/toDataBuffers) (.body request)))
