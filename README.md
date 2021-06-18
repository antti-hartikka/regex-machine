# regex-machine

This project was part of course Data Structures Lab, start of summer 2021. 
During the six week period, I designed and implemented a regular expression engine.

## Documentation

[User manual](./documentation/manual.md)

[Project specification](./documentation/project-specification.md)

[Weekly reports](./documentation/weekly-reports)

[Implementation](./documentation/implementation.md)

[Testing](./documentation/testing.md)

## How to..

run jar file from release:

```
java -jar matcher.jar
```

run project with maven:
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
