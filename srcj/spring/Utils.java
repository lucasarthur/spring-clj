package spring;

import clojure.java.api.Clojure;
import clojure.lang.IFn;

import java.util.List;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Utils {
  private static final String PREFIX = "spring.clj.";
  public static final IFn requireFn = Clojure.var("clojure.core", "require");
  public static final IFn derefFn = Clojure.var("clojure.core", "deref");
  public static final IFn httpHandlerFn;
  public static final IFn websocketHandlerFn;

  static {
    requires().stream().map(PREFIX::concat).map(Clojure::read).forEach(requireFn::invoke);
    httpHandlerFn = (IFn) derefFn.invoke(Clojure.var(PREFIX.concat("web")), "http-handler");
    websocketHandlerFn = Clojure.var(PREFIX.concat("web"), "-handle-ws");
  }

  private static List<String> requires() {
    return List.of("web");
  }
}
