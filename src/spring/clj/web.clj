(ns spring.clj.web
  (:require
   [spring.clj.ws]
   [spring.clj.web.router-function :refer [all route]]
   [spring.clj.web.server.request :refer [request-map]]
   [spring.clj.web.server.response :refer [status content-type body-value]]
   [clojure.tools.logging :refer [info]]))

(defn -default-http-handler []
  (all
   (route :get "/echo" #(let [req-map (request-map %)]
                          (->> (status :ok)
                               (content-type :json)
                               (body-value req-map))))
   (route :get "/" (fn [_] (->> (status :not-implemented)
                                (content-type :json)
                                (body-value {:message "there is no handler installed"}))))))

(defn -default-ws-handler [session]
  (str "On thread: " (.getName (Thread/currentThread)) "\nEcho message: " session))

(def http-handler (atom nil))
(def ws-handler (atom nil))

(defn set-http-handler! [new-handler]
  (info "Assigning http handler to " new-handler)
  (swap! http-handler (constantly new-handler)))

(defn set-websocket-handler! [new-handler]
  (info "Assigning ws handler to " new-handler)
  (swap! ws-handler (constantly new-handler)))
