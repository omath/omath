This is the home of the **omath** project.

We're writing a computer algebra system called **omath** which should be able to run many simple Mathematica programs.

==Getting started==

Checkout a copy of the git repository, then run

    sbt "run-main org.omath.ui.omath"

to get a text-mode interface.

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
