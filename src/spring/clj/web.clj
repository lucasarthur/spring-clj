(ns spring.clj.web
  (:require
   [spring.clj.web.router-function :as rf]
   [spring.clj.web.server.request :as req]
   [spring.clj.web.server.response :as res]
   [spring.clj.web.ws :as ws]
   [reactor-core.operations :as ops]
   [clojure.string :refer [upper-case]]
   [clojure.tools.logging :refer [info]]
   [spring.clj.util.sam :refer [->function]])
  (:import
   (org.springframework.web.reactive.socket WebSocketSession)
   (reactor.core.publisher Flux)))

(def http-handler (atom nil))
(def ws-handler (atom nil))

(defn set-http-handler! [new-handler]
  (swap! http-handler (constantly new-handler)))

(defn set-websocket-handler! [new-handler]
  (swap! ws-handler (constantly new-handler)))

(defn -default-http-handler []
  (rf/and
   (rf/route :get "/echo" #(let [req-map (req/request-map %)]
                             (info req-map)
                             (->> (res/status :ok)
                                  (res/content-type :json)
                                  (res/body-value req-map))))
   (rf/route :get "/" #(->> (res/status :not-implemented)
                            (res/content-type :json)
                            (res/body-value {:message "there is no handler installed"})))))

(defn -default-ws-handler [session]
  (info "new ws session: " session)
  (str "On thread: " (.getName (Thread/currentThread))
       "\nEcho message: " (upper-case session)))

(defn -handle-ws ^Flux [^WebSocketSession session]
  (->> (.receive session)
       (ops/map (-> (ws/websocket-session->map session)
                    (@ws-handler)
                    (ws/wrap-message-handler)
                    (->function)))))

(defonce _
  (do
    (info "Applying default handlers")
    (set-http-handler! -default-http-handler)
    (set-websocket-handler! -default-ws-handler)))
