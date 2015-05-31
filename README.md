# cljdroid

This sample application is derived from a sample by Mark Godfrey, but also includes a some use of core async to solve a
problem described below for clojurejs. 

Main structure of code pinched from there too:

http://www.jayway.com/2014/09/16/comparing-core-async-and-rx-by-example/

# building

This can be built for development with:

```
lein droid doall
```

or with Skummet to produce a lean build with

```
lein with-profile lean droid doall
```

## License

Copyright © 2015 James Carnegie

Distributed under the Eclipse Public License, the same as Clojure.
