# Testing document

## Unit testing

There is two testing classes for unit testing. 
CompilerTest is checking that different syntaxes will produce correct states. 
MatcherTest is testing matching with different syntaxes.

## Manual testing

I have done manual testing while implementing new features, and I have made sure different thing are working. 
I did test with one million character string with simple [a-z]+ pattern, and matching took 1,5 seconds. 
Another test with similar pattern, but testing string was almost seven million characters, took 10 seconds. 
It seems that I have implemented engine with O(n) time complexity at least with simple inputs. 
