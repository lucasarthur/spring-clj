package spring.clj;

import clojure.java.api.Clojure;
import clojure.lang.IFn;
import clojure.lang.Atom;

import org.springframework.stereotype.Component;

@Component
public class Utils {
  private static final String PREFIX = "spring.clj.";
  private static final String WEB_NS = PREFIX.concat("web");
  public static final IFn requireFn = Clojure.var("clojure.core", "require");
  public static final IFn derefFn = Clojure.var("clojure.core", "deref");
  public static final IFn httpHandlerFn;
  public static final IFn websocketHandlerFn;

  static {
    requireFn.invoke(Clojure.read(WEB_NS));
    httpHandlerFn = (IFn) ((Atom) derefFn.invoke(Clojure.var(WEB_NS, "http-handler"))).deref();
    websocketHandlerFn = Clojure.var(WEB_NS, "-handle-ws");
  }
}
