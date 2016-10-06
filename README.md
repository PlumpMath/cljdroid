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

To run inside a docker container
--------------------------------

You can now build, package and run this microservice using Docker.

Now you can build your docker image by entering from a terminal where you have access to Docker, execute the following command:

```shell
> ./mvnw clean package docker:build
```

Even push it to a repository of your choice:

```shell
> ./mvnw clean package docker:build -DpushImage
```
