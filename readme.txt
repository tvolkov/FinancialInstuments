I tried to make this application concurrent: it reads the input file in a separate thread and puts the lines read into
the queue. On the other end of the queue there're several threads pulling the lines from it, parse 'em and make
necessary calculations.
The problem which was difficult for me to solve is how to arrange the database support, i.e. how to access the data,
since accessing db on each line is very time-consuming.
On the other hand, I tried to cache the db data using Guava Cache, but it's is not 100% correct. This cache updates
on timer, hence this approach can and will leave some db updates unattended.
I also investigated how the cache can be updated by db trigger, but this approach is tied to the exact database technology
(like h2, it has the Java API for triggers, unlike many other db's), so I decided to put it aside.
I believe there're several ways to solve this problem (like using JMS for instance), but I didn't want to make it very
sophisticated, so by default my app queries database each time (though I haven't delete the cacheable solution,
you can try it by using -DuseCachedMultiplierProvider=true)

usage examples:
- run app with specified input file:
    mvn clean integration-test -PcustomFile -DinputFile=<path to the input file>
- run app with specified input file and with multipliers cache:
    mvn clean integration-test -PcustomFile -DinputFile=<path to the input file> -DuseCachedMultiplierProvider=true