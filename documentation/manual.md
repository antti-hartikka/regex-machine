# User manual

This project is written with Java 15. 
You can run this program from the source with `mvn compile exec:java -Dexec.mainClass=matcherapp.Main`, or from jar with `java -jar matcher.jar`. 

The command line interface asks you first, if you would like to open testing interface. 
If this is the case, simply answer `y`. All other answers opens the matcher app and its graphical user interface. 

## Matcher app

The use of this app is rather simple. There are two text fields: one for text input, and other for regex pattern. 
Matching text input against regex pattern is done by either pressing the match button, or pressing enter key at either one of the fields. 
You can also tick the box labelled as Automatch, and while this box is ticked, the matching is done every time a key is pressed and released at the text fields. 

Text underneath tells you if the match was found or not with time matching took in milliseconds. 
It also tells you if there is a syntax error with the regex pattern.

## Testing side

If you answer `y` to the `test?` question, the cli asks you a buch of other questions for the performance test.

`max n` is the largest amount of input strings are repeated in these tests.

`step size` defines the first `n`, and next tests `n` is calculated with `n += step`.

`text to be matched (repeated n times):` is pretty self descriptive. Ie `a` repeated five times is `aaaaa`

Next three inputs are regex patterns, part one, two and three. You can leave those parts empty you don't need. 
First and second parts are repeated, third is not. Regex string is formed this way `regex = pattern1(repeated n times) + pattern2(repeated n times) + pattern3(repeated n times)`.

The final input is `max time for one match in milliseconds:`. The performance test loop breaks if there is a matching, that has taken more time than this value.

The tests do not give any progress indications what so ever, but after the tests the line chart is opened into a new window. 

## Available grammar

* `(abc)` for grouping.
* `[afr-z]` for sets, this would be "character a, f, or anything from r to z".
* `{a,b}` defining that previous pattern is repeated a to b times. Works also as `{a,}` (at least a times), `{,b}` (max b times) or `{a}` (exactly a times).
* `?`, `+` and `*` for zero or one time, one or more times and zero or more times.
* `|` for "or" syntax: `a|b` is a or b.
* `\` as escape character if there special characters are needed as themselves.
