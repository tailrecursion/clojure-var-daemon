package tailrecursion;

import clojure.lang.IFn;
import clojure.lang.Var;
import clojure.lang.RT;
import org.apache.commons.daemon.Daemon;
import org.apache.commons.daemon.DaemonContext;

public class ClojureVarDaemon implements Daemon {
  private static Var REQUIRE = RT.var("clojure.core", "require");
  private static Var SYMBOL = RT.var("clojure.core", "symbol");

  @Override
  public void init(DaemonContext daemonContext) {
    getVar("init").invoke(daemonContext);
  }

  @Override
  public void start() {
    (new Thread(getVar("start"))).start();
  }

  @Override
  public void stop() {
    getVar("stop").invoke();
  }

  @Override
  public void destroy() {
    getVar("destroy").invoke();
  }

  private static Var getVar(String name) {
    String namespace = System.getProperties().getProperty("daemon.clojure.ns");

    if (namespace == null) {
      throw new RuntimeException("daemon.clojure.ns system property not set");
    }

    try {
      REQUIRE.invoke(SYMBOL.invoke(namespace));
    } catch(Throwable t) {
      throw new RuntimeException("Failed to load namespace '" + namespace + "'", t);
    }

    Var var = RT.var(namespace, name);
    if (var == null) {
      throw new RuntimeException("Var '" + name + "' not found");
    }

    return var;
  }
}
