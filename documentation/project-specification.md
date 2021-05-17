# Project Specification
_bachelor's in computer science_

In this project, I will design and implement a regular expression engine from scratch using 
Java. The program code and documentation in the project is written in english. 

## Data structures and algorithms

Project will be implemented using Thompson's algorithm to transform regular expression to nondeterministic finite automation, and using multiple-state simulation approach to do pattern matching. 
The algorithm has time consumption of O(mn), 
where m is length of regular expression and n is length of string to be matched. 
This algorithm was chosen due to been well known and documented, it is fast and reliable, 
with reasonable time consumption even with longer expressions or strings.

## Inputs and outputs

User interface takes two input, regex pattern and string to be matched, and gives output if 
matching was successful or not. 

## Sources

* [https://en.wikipedia.org/wiki/Regular_expression](https://en.wikipedia.org/wiki/Regular_expression)
* [https://en.wikipedia.org/wiki/Thompson%27s_construction](https://en.wikipedia.org/wiki/Thompson%27s_construction)
* [https://swtch.com/~rsc/regexp/regexp1.html](https://swtch.com/~rsc/regexp/regexp1.html)
