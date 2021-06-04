# Implementation document

## Structure

The Matcher app is divided into three packages:
* domain
* ui
* utils

The domain package contains main logical structure of the regex engine and it matches strings to regex patterns.
The ui package contains only the graphical user interface, and uses domain package to match inputs.
The utils package contains self implemented classes of hash set for characters, stack for fragments, and dynamic list for states.

Outside of these packages is Main.java. This class only invokes the ui.


## Space and time analysis



## Deficiensies and improvements

The grammar of this engine is quite constricted, but still versatile. 
The set syntax ([]) could use not-grammar, and dot (.) could be used as a any character.
Compiler is a little mess and could benefit a lot from refactoring. 
