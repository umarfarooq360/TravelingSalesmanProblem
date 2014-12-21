
# Travelling Salesman Problem (Genetic Algorithm) Java Program


####Overview
The program implements a genetic algorithm  that approximates (or at least finds potential solutions to) the Traveling Salesperson Problem (TSP). Recall that for an instance of the TSP problem consists of a set of cities. A circuit is a path that visits every city once and then returns to the city where the path starts. The TSP problem is to find the minimum length circuit. This problem is NP-hard. So we can’t expect to be able to handle even reasonable numbers of cities. An example of the problem with 25 cities, for example, has more than 1025 possible circuits. To give you a ballpark idea of the size of this number, if you had a computer that could check one billion circuits every second, it would take more than 491 million years to check all the circuits for this problem.

Despite this, however, running a genetic algorithm solution to TSP is feasible (though not at all guaranteed to give you the best solution, or even close, though they turn out to be pretty good). 

Author: Omar Farooq    
Version: 15 Apr 2014

####Compiling 
```
javac -cp src/*.java -d bin/ 
```
####Running
```
java -cp . bin/TravelingSalesmanGui 
```
 
####Configuration Parameters
- Num Generations: The number of generation the algorith should run for (e.g. 1000).

- Populations Size : The number of individuals (possible solutions) in each population (e.g. 10,000).

- Number of Cities : The number of cities. Can be clicked on the grid or randomly generated (e.g. 25).

- Crossover Probability : The probability that two individuals are crossed.
 (e.g. 0.5)
- Muatation Probability : The probability that an individual mutates (usually low e.g. 0.01).

- Percent Retained : The percentage of the best population retained for the next generation (e.g. 0.2). Allows us to not lose our best solutions.
