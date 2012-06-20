This is the home of the **omath** project.

We're writing a computer algebra system called **omath** which should be able to run many simple Mathematica programs.

==Getting started==

Checkout a copy of the git repository, then run

    ./fix-sbt
    ./sbt "omath-ui/run-main org.omath.ui.repl.omath"

to get a text-mode interface.

==Running omath==
* There is a very primitive javascript client connected to an omath kernel running on Heroku available at <a href="http://pure-sky-1860.herokuapp.com/omath.html">http://pure-sky-1860.herokuapp.com/omath.html</a>.
* You can use the same javascript client with a local instance of the kernel, launched via Java Web Start from <a href="http://omath.org/webstart/omath.jnlp">http://omath.org/webstart/omath.jnlp</a>.
* As above `./sbt "omath-ui/run-main org.omath.ui.repl.omath"` gives a Read-Eval-Print loop.

Perhaps one day you'll be able to:
* The javascript client may be able to talk directly to a kernel running as an applet within the browser.
* The mathematica front-end can use an omath kernel.
* Compile the entire omath project to javascript and run in the browser.

==Project organization==

                            api
                             |
       +---------------+-----+--------+------------+
       |               |              |            |
    tungsten        patterns        parser      bootstrap
       |               |              \____________/
       |               |                    |
       |               |                   core
       |               |                    | 
       +---------------+-----+--------------+
                             |
                             ui
