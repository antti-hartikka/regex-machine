# regex-machine

This project is for course Data Structures Lab, start of summer 2021. 
In the following weeks, I will design and implement a regular expression engine.

## Documentation

[Project specification](https://github.com/antti-hartikka/regex-machine/blob/main/documentation/project-specification.md)

[Weekly reports](https://github.com/antti-hartikka/regex-machine/tree/main/documentation/weekly-reports)

## How to..

run matcher app:
```
mvn compile exec:java -Dexec.mainClass=matcherapp.Main
```

run tests:
```
mvn test
```

run jacoco for test coverage report (can be found from target/site/jacoco):
```
mvn test jacoco:report
```

run checkstyle (report can be found from target/site):
```
mvn checkstyle:checkstyle jxr:jxr
```
