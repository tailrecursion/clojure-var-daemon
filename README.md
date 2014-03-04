# clojure-var-daemon

`clojure-var-daemon` is a thin Java layer between your Clojure program and [Apache Commons Daemon](http://commons.apache.org/proper/commons-daemon/) that saves you from necessarily AOT-ing Clojure code.

For details on Clojure daemons, check out the excellent [Clojure Cookbook: Daemonizing an Application](http://www.rkn.io/2014/02/06/clojure-cookbook-daemons/) blog post by [Ryan Neufeld](https://github.com/rkneufeld).

The approach was inspired by [Pedestal's ClojureVarServlet](https://github.com/pedestal/pedestal/blob/master/service/java/io/pedestal/servlet/ClojureVarServlet.java).

### Dependency

From Clojars:

    [tailrecursion/clojure-var-daemon "1.0.0"]

### Usage

First, create a namespace with `init`, `start`, `stop`, and `destroy` definitions:

```clojure
(ns my.daemon
  (:import (org.apache.commons.daemon
             DaemonContext             ;; Passed to init
             DaemonInitException)))    ;; Throw in init if you fail to initialize

(defn init [^DaemonContext ctx] ,,,)
(defn start [] ,,,)
(defn stop [] ,,,)
(defn destroy [] ,,,)
```

Then, create an uberjar of your project with `lein uberjar`.

Finally, run your program as a daemon with `jsvc`.  Specify the lifecycle namespace via `-Ddaemon.clojure.ns=my.daemon` and use `tailrecursion.ClojureVarDaemon` as the classfile argument:

```
jsvc                                                  \
  -java-home "$JAVA_HOME"                             \
  -cp target/my-project-0.1.0-SNAPSHOT-standalone.jar \
  -outfile "${PWD}/out.txt"                           \
  -pidfile "${PWD}/pid.txt"                           \
  -Ddaemon.clojure.ns=my.daemon                       \
  tailrecursion.ClojureVarDaemon
```

## License

    Copyright (c) Alan Dipert and Micha Niskin. All rights
    reserved. The use and distribution terms for this software are
    covered by the Eclipse Public License 1.0
    (http://opensource.org/licenses/eclipse-1.0.php) which can be
    found in the file epl-v10.html at the root of this
    distribution. By using this software in any fashion, you are
    agreeing to be bound by the terms of this license. You must not
    remove this notice, or any other, from this software.
