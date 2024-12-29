# Homework 8

## Discussion 

For all three endpoint pairs, the algorithm was able to find the shortest path. 
I ran the driver for each pair of endpoints 5 times and each time, the driver would return the same number for total distance traversed.
Here I have listed the total distance for the shortest path between each pair of endpoints, in order of length. 
    7/11 -> Druid Lake: 5827.3652
    JHU -> Druid Lake: 8818.5187
    Inner Harbor -> JHU: 16570.4909

There were slight differences between each trial run for each pair of endpoints when running SystemRuntimeTest and MemoryMonitorTest.
For all three pairs, the time it took to load the network (around 75 ms), the memory required to create the StreetSearch object (around 13700 KB), 
and the memory required to load the network (21560 KB) was basically equal (minus tiny variations between each trial run). 
This makes sense as the same data set, "baltimore.streets.txt" is being analyzed and the same number of vertices and edges are being created
and stored from this data set. 

The difference in performance between each pair of endpoints is exemplified when examining the runtime and memory of the finding shortest path operation.
There seems to be a direct relationship between the length of the shortest path found, and the time it takes for the algorithm to run and the memory used. 
7/11 to Druid Lake, the shortest path of the three given pairs of endpoints, also had the shortest runtime for the findShortestPath Operation and the least 
amount of memory used for the operation (as measured by the difference between the memory used for loading the network and memory used to find the shortest path, labelled as delta below). 
Inner Harbor to JHU, the longest path of the three given pairs of endpoints, had the longest runtime for the findShortestPath Operation and used the most amount of memory during the operation. 
Intuitively, this makes sense as shorter paths will traverse (explore) fewer edges before reaching and exploring the endpoint vertex, at which point the while loop breaks as the shortest path has
been found. Longer paths will require more roads (edges) to be taken, and thus more vertices have to be explored before the shortest path is found.

Here is my profiling data for all three pairs of endpoints, shown for two out of the five trials that I ran for each:
                                                        Runtimes (in ms)                                                            Memory Usage (in KB)                         
                        total Distance          load Network        findShortestPath        baltimore.streets.txt       empty StreetSearcher        Loading Network         FindShortestPath     delta      setObjectsNull
JHU -> Druid Lake:      8818.5187                   77                  56                      12595.30                    13722.47                   21571.48                 21694.54          123.055       14660.63
                        8818.5187                   67                  49                      12588.40                    13717.95                   21557.34                 21691.22          133.875       14643.94

7/11 -> Druid Lake:     5827.3652                   76                  41                      12588.13                    13727.73                   21574.96                 21646.54         71.578         14641.35
                        5827.3652                   75                  43                      12576.85                    13709.54                   21548.66                 21629.49         80.828         14622.55

Inner Harbor -> JHU:    16570.4909                  69                  82                      12587.92                    13706.55                   21560.46                 21752.79         192.328        14669.18
                        16570.4909                  75                  84                      12565.91                    13697.63                   21620.51                 21808.77         188.258        14684.91