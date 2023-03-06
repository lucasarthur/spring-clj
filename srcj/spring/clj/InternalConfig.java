package spring.clj;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static spring.clj.Utils.httpHandlerFn;
import static spring.clj.Utils.websocketHandlerFn;
import static java.util.Objects.requireNonNull;
import static java.util.Objects.nonNull;

@Configuration
@ConfigurationProperties(prefix = "clojure")
public class InternalConfig {
  private String rootPath = "/";
  private String wsPath = "/ws";
  private Boolean wsActive = false;
  private String initSymbol = "spring.core/main";

  @Autowired private ApplicationContext applicationContext;

  public String getRootPath() { return rootPath; }
  public String getWsPath() { return wsPath; }
  public Boolean getWsActive() { return wsActive; }
  public String getInitSymbol() { return initSymbol; }

  public void setRootPath(String _rootPath) { rootPath = _rootPath; }
  public void setWsPath(String _wsPath) { wsPath = _wsPath; }
  public void setWsActive(Boolean _wsActive) { wsActive = _wsActive; }
  public void setInitSymbol(String _initSymbol) { initSymbol = _initSymbol; }

  @Bean State state() {
    return new State(applicationContext, initSymbol);
  }

  @Bean
  @SuppressWarnings("unchecked")
  RouterFunction<ServerResponse> cljHttpHandler() {
    return (RouterFunction<ServerResponse>) httpHandlerFn.invoke();
  }

  @Bean
  @SuppressWarnings("unchecked")
  WebSocketHandler webSocketHandler() {
    return session -> session.send((Flux<WebSocketMessage>) websocketHandlerFn.invoke(session));
  }

  @Bean HandlerMapping handlerMapping(WebSocketHandler webSocketHandler) {
    Map<String, WebSocketHandler> map = new HashMap<>();
    if (wsActive) map.put(rootPath.concat(wsPath), webSocketHandler);
    return new SimpleUrlHandlerMapping(map, -1);
  }
}
