# JPMJava

Very Simple java Exercise


## Running the project
 
The project was created in eclipse using m2e (Maven to Eclipse) plugin. To build the project Eclipse is not needed, it's
enough to run `mvn clean package` to generate the jar or `mvn clean test` to execute test cases. 

Maven will download the dependencies to the local maven repo, so nothing more is needed.

## General Comments



## Assumptions

### BigDecimal

I used `Double` to store values. I took as an assumption that this would be enough
for any inputs I might receive. However, in a real scenario, it could be the case 
of using BigDecimal. As this was not stated in the requirements, I used the simpler 
approach.

### Fit in memory

It was stated in the requirements: "All data to be in memory"
I implemented a dataset interface returning an `Iterable` anyway, instead of a 
`List`, for instance, but I mapped all values from the dataset to memory.

From requirements, that was the simplest approach. With more information, the approach
could have been different. For instance, maybe the whole data would not fit in memory, 
but data for a single settlement date would. I used the simpler approach. 

### What I understood by ranking

The requirements just stated:
"Ranking of entities based on incoming and outgoing amount. Eg: If entity foo instructs the highest
amount for a buy instruction, then foo is rank 1 for outgoing"

I assumed the rank 1 meant the first one in the results. The text could mean many different
things, like "rank just the k-top", "calculate rank value for each entity and print it",
"order by rank and print on the order", among other things. 

Also, I assumed there was a rank for incoming and other different one for outgoing. 
Maybe it was meant to be rank of `incoming - outgoing`, but that's what I understood
from the text.

The approach I used was to order the entities by ranked value and print them in this order, 
for each date. However, whatever could be the requirement, it should be easy to modify 
the code created to fulfil it. 










