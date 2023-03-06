(ns spring.clj.web.body-inserter
  (:import
   (org.springframework.web.reactive.function BodyInserters)
   (org.reactivestreams Publisher)))

(defn empty-body []
  (BodyInserters/empty))

(defn sse-body [^Publisher publisher]
  (BodyInserters/fromServerSentEvents ^Publisher publisher))
