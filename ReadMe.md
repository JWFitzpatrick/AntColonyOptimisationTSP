## How to compile

To run the application you will need to first compile the java files by running the following command in the terminal:

```
javac *.java
```

When running the command, make sure you are in the same directory as the java files which is the src folder.

## How to run the applicaiton

To run the application you will need to run the following command in the terminal:

```
java Main <inputFileDirectory>
```

When running the command, make sure you are in the same directory as the compiled java files.
The above command will run the application with the optimal parameters found for the brazil58.xml file.

## How to run with custom parameters

To run the application with custom parameters you will need to run one of the following command in the terminal:

```
java Main <filePath> <numberOfAnts> <evaporationRate> <Q> <heuristicValue> <mode>
java Main <filePath> <numberOfAnts> <evaporationRate> <Q> <heuristicValue> <mode> <searchMode>
java Main <filePath> <numberOfAnts> <evaporationRate> <Q> <heuristicValue> <mode> <searchMode> <tabuSize> <tabuIterations>
```

The above commands will allow you to alter the parameters of the application. The parameters are as follows:
filePath - The path to the xml file you wish to run the application on.
numberOfAnts - The number of ants to be used in the application.
evaporationRate - The evaporation rate to be used in the application.
Q - The amount of pheromone to be deposited for each ant.
heuristicValue - The value to be used for the heuristic function.
mode:
0 = Standard ACO,
1 = MMAS,
2 = Rank-based Ant System,
3 = Elitist Ant System
searchMode:
0 = No Search,
1 = Hill Climbing,
2 = Tabu Search
tabuSize - The size of the tabu list to be used in the tabu search.
tabuIterations - The number of iterations to be used in the tabu search.

When running with the searchmode parameter, if you wish to run tabu search you must also provide the tabuSize and tabuIterations parameters.

## How to run the application to see different results from parameter changes

To run the application to see different results from parameter changes you will need to run one of the following command in the terminal:

```
java Main calculateBestNumberOfAnts
java Main calculateBestEvaportationRate
java Main calculateBestHeuristicValue
java Main calculateBestPheromoneValue
java Main calculateBestACOApproach
java Main calculateBestSearchMode
java Main findBestParamaters
```

This will use the file brazil58.xml in the data folder which needs to be in the same directory as the src folder.
The above commands will show you the effect or changing an individual parameter on the results of the application.
If you wish to run these commands on a different file you will need to change the file path in the Main.java file or
change the file name to brazil58.xml. The findBestParamaters command will run a grid search to find the best parameters
for the application to use. This will take a while to run. This command is due to different combinations of parameters
potentially being more optimal than what is found by changing a single parameter.
