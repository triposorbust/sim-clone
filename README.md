# Sim-Clone: Simulating Clonal Expansion
## Start the day right with simulations!

A Clojure application for simulating clonal expansion in cellular/molecular repertoire. Intended for use with some metrics.


### Quickstart

Package can be run with leiningen:

```
% lein run -- --help
```

Or compiled to a standalone application:

```
% lein compile && lein uberjar
...
% java -jar targets/sim-clone-standalone.jar --help
```

The default settings should be sensible. Tweaks may be made to the simulation parameters depending on needs.


### Parameters

The following parameters can be set:

 - `x-dim`: Cardinality of the x-dimension.
 - `y-dim`: Cardinality of the y-dimension.
 - `space`: Number of tuples to be sampled out of the Cartesian product.
 - `max-fraction`: When the expanded clones occupy this fraction, *exeunt*!
 - `generations`: Set an upper limit on the number of simulation generations.
 - `number`: Number of clones to expand. i.e. One (1) is monoclonal.


### Tests

Package provides unit tests for each source module.

```
% lein test
Ran 14 tests containing 29 assertions.
0 failures, 0 errors.
```


### Developers

 - [Andy Chiang][ahc] (Columbia University, Biomedical Informatics)
 - [Boris Grinshpun][bg] (Columbia University, Systems Biology)


### License

Copyright © 2013 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.

[ahc]: http://www.andy-chiang.com
[bg]: http://www.columbia.edu/~bg2178/