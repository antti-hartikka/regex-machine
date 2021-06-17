# Implementation document

## Structure

Javadoc with automatically generated, interactive UML diagrams: TODO: link

The Matcher app is divided into three packages:
* domain
* ui
* utils

The domain package contains main logical structure of the regex engine and it matches strings to regex patterns.
The ui package contains the graphical user interface and performance tester. 
Both are using domain package to match inputs.
The utils package contains self implemented classes of hash set for characters, stack for fragments, and dynamic list for states.

Inside of the main package matcherapp is also Main class. This class asks user if matcher app or performance testing is invoked.


## Space and time analysis
Matching can be divided into two sections: first the NFA is compiled from the regex pattern, and after this the input string is matched in the NFA simulator.

Analyzing the compiler is a bit tricky, but if we have regex `(abc...){n,}`, the compiler compiles the `abc...` pattern `n` times.
If the parenthesis holds `m` characters, the compiler needs `nm` time to compile the NFA. 
If we have a much simpler regex, consisting only individual characters without grammatical characters, the compiler only adds them to the stack at constant time. 

NFASimulator analysis is a bit easier. Every character in the text matched against the regex pattern follows same procedure: 
```
for character in input string
    for state in all current states we are at
        match character to state and if match is found, add proceeding states to list for next character in input string
```
Because the states in the NFA can lead to practically any other unique state in the NFA, and in the worst case scenario the NFA is in all states in a step, the time complexity is `O(nm)`, where n is length of the input string and m is number of states. 
How many states can there be? In the "pathological" case of $a?^na^n$, there are n states in the state list at every given moment making time complexity to $O(n^2)$, witch is something of a sensible worst case scenario.

## Deficiensies and improvements

The grammar of this engine is quite constricted, but still versatile. 
The set syntax ([]) could use not-grammar, and dot (.) could be used as a any character.
Compiler is a little mess and could benefit a lot from refactoring. 
