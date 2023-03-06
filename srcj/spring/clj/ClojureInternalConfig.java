package spring.clj;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import reactor.core.publisher.Flux;

import java.util.HashMap;
import java.util.Map;

import clojure.java.api.Clojure;
import clojure.lang.IFn;
import clojure.lang.Atom;

import static spring.clj.ClojureUtils.REQUIRE;
import static spring.clj.ClojureUtils.DEREF;
import static spring.clj.ClojureUtils.WEB_NS;
import static spring.clj.ClojureUtils.WS_NS;
import static java.util.Objects.nonNull;

@Configuration
@ConfigurationProperties(prefix = "clojure")
public class ClojureInternalConfig {
  private String rootPath = "/";
  private String wsPath = null;
  private String initSymbol = "spring.clj.core/main";

  @Autowired private ApplicationContext applicationContext;

  static {
    REQUIRE.invoke(Clojure.read(WEB_NS));
    REQUIRE.invoke(Clojure.read(WS_NS));
  }

  @Bean
  ClojureState clojureState() {
    return new ClojureState(applicationContext, initSymbol);
  }

  @Bean
  @DependsOn({"clojureState"})
  @SuppressWarnings("unchecked")
  RouterFunction<ServerResponse> cljHttpHandler() {
    return (RouterFunction<ServerResponse>) ((IFn) ((Atom) DEREF.invoke(Clojure.var(WEB_NS, "http-handler"))).deref()).invoke();
  }

  @Bean
  @DependsOn({"clojureState"})
  @SuppressWarnings("unchecked")
  WebSocketHandler webSocketHandler() {
    return session -> session.send((Flux<WebSocketMessage>) Clojure.var(WS_NS, "-ws-handler-wrapper")
      .invoke(((Atom) DEREF.invoke(Clojure.var(WEB_NS, "ws-handler"))).deref(), session));
  }

  @Bean
  @DependsOn({"webSocketHandler"})
  HandlerMapping handlerMapping(WebSocketHandler webSocketHandler) {
    Map<String, WebSocketHandler> map = new HashMap<>();
    if (nonNull(wsPath)) map.put(rootPath.concat(wsPath), webSocketHandler);
    return new SimpleUrlHandlerMapping(map, -1);
  }

  public String getRootPath() { return rootPath; }
  public String getWsPath() { return wsPath; }
  public String getInitSymbol() { return initSymbol; }

  public void setRootPath(String _rootPath) { rootPath = _rootPath; }
  public void setWsPath(String _wsPath) { wsPath = _wsPath; }
  public void setInitSymbol(String _initSymbol) { initSymbol = _initSymbol; }
}
