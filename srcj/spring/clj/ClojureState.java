package spring.clj;

import clojure.java.api.Clojure;
import clojure.lang.Var;
import org.springframework.context.ApplicationContext;

import static java.util.Objects.nonNull;
import static spring.clj.ClojureUtils.REQUIRE;

public class ClojureState {
  private final ApplicationContext applicationContext;

  private void initialize(String initSymbol) {
    Var var = (Var) Clojure.var(initSymbol);
    REQUIRE.invoke(Clojure.read(var.toSymbol().getNamespace()));
    var.invoke(applicationContext);
  }

  public ClojureState(ApplicationContext _applicationContext, String appInitSymbol) {
    applicationContext = _applicationContext;
    if (nonNull(appInitSymbol)) initialize(appInitSymbol);
  }
}
