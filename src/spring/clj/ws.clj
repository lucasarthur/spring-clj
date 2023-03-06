(ns spring.clj.ws
  (:require
   [reactor-core.operations :as ops])
  (:import
   (org.springframework.web.reactive.socket WebSocketSession  WebSocketMessage WebSocketMessage$Type)
   (java.nio.charset StandardCharsets)))

(defn- string->bytes [s] (.getBytes s StandardCharsets/UTF_8))

(defn- websocket-session->map [^WebSocketSession session]
  (let [handshake-info (.getHandshakeInfo session)]
    {:id (.getId session)
     :factory (.bufferFactory session)
     :headers (.getHeaders handshake-info)
     :uri (.getUri handshake-info)
     :cookies (.getCookies handshake-info)
     :remote-address (.getRemoteAddress handshake-info)}))

(defn- wrap-message-handler ^WebSocketMessage [message-handler]
  #(let [data-buffer (.getPayload %)
         factory (.factory data-buffer)]
     (->> (.toString data-buffer StandardCharsets/UTF_8)
          (message-handler)
          (string->bytes)
          (.wrap factory)
          (WebSocketMessage. WebSocketMessage$Type/TEXT))))

(defn -ws-handler-wrapper [handler ^WebSocketSession session]
  (->> (.receive session)
       (ops/map (-> (websocket-session->map session)
                    (handler)
                    (wrap-message-handler)))))
