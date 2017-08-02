# JPMJava

Very Simple java Exercise

## Running the project
 
The project was created in eclipse using m2e (Maven to Eclipse) plugin. To build the project Eclipse is not needed, it's
enough to run `mvn clean package` to generate the jar or `mvn clean test` to execute test cases. 

Maven will download the dependencies to the local maven repo, so nothing more is needed.

## Code description

The main code implementation in at `ReportGenerator.java`. 
Tests can be found at  `ReportGeneratorTest.java` and they use a mock 
implementation for the `Dataset` interface, `MemoryDataset`.
Every test compares the generated report with sample reports on `src/test/resources`.

## General Comments

### Objective of the exercise

I was not really strict with OO on this project, as it was a really simple project
and it didn't seem, from requirements, that the objective was to test OO ability
in deep. Probably in a real case, with more detailed requirements, I would have
worried more about classes responsibilities, reusability, etc. 

However, the main objective seemed to be checking java 8 knowledge. That's why
I implemented everything I could using streams, new date types, new try syntax, etc.
BUT I have passed the last years using python and C++ and I am a little rusty in
Java, I have used java 8 just in 1 project lately when I was able to learn some 
of its new features, so please forgive me if something is still too "old style". 

### About the tests

One big decision I made on the design was how to write the tests.
In the requirements, it was stated: 
"Output format to be plain text, printed out to the console."

If it was a real case, I would have thought about several layers of tests, 
considering the Fowler's test "piramid", with unit tests, testing just classes, 
then integration tests, testing component executions, and then behavior tests, 
testing the whole user workflow, when possible. 

I am not sure, therefore, in a real scenario I would be testing the output of the
console against files, describing the output (you can see them at `src/test/resources`).
Maybe the best approach would be having unit tests to test logic of each class, 
then having some integration tests executing the whole program and capturing the full 
report if needed. 

However, for this exercise, I thought it would be safer to have tests against what
was printed in the output as I thought this could be what was being expected. I decided
to test the full output to be sure I was sticking to the requirements of writing
things to the console.
Also, the first test, which sends data to the console, I would probably not use 
in a real case, but I wanted to make clear that results are written to the console 
if desired that way, although the code is generic for other types of stream.

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


### UTF-8

I saved and commited every file and resource file in UTF-8. Things are expected 
to be in UTF-8 for tests to work. 







