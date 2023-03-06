package spring;

import clojure.java.api.Clojure;
import clojure.lang.Symbol;
import clojure.lang.Var;
import org.springframework.context.ApplicationContext;

import static spring.Utils.requireFn;
import static java.util.Objects.nonNull;

public class State {
  private final ApplicationContext applicationContext;

  private void initialize(String initSymbol) {
    Var var = (Var) Clojure.var(initSymbol);
    Symbol symbol = var.toSymbol();
    String ns = symbol.getNamespace();
    requireFn.invoke(Clojure.read(ns));
    var.invoke(applicationContext);
  }

  public State(ApplicationContext _applicationContext, String appInitSymbol) {
    applicationContext = _applicationContext;
    if (nonNull(appInitSymbol)) initialize(appInitSymbol);
  }
}
