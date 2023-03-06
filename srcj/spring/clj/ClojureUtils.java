package spring.clj;

import clojure.java.api.Clojure;
import clojure.lang.IFn;

import org.springframework.stereotype.Component;

@Component
public class ClojureUtils {
  private static final String PREFIX = "spring.clj.";
  public static final String WEB_NS = PREFIX.concat("web");
  public static final String WS_NS = PREFIX.concat("ws");
  public static final IFn REQUIRE = Clojure.var("clojure.core", "require");
  public static final IFn DEREF = Clojure.var("clojure.core", "deref");
}
