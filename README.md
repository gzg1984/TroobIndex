# TroobIndex
## How to Move this project to anywhare
- basically you only need these files
```
pom.xml
src/*
```
- copy them ,then
```
mvn package -q
```

## How to Use the Jar file directyly
```
java -jar index-0.0.3-SNAPSHOT.jar -h
java -jar index-0.0.3-SNAPSHOT.jar -q Copyright

java -jar target/index-0.0.3-SNAPSHOT.jar -f /Users/gaozhigang/Downloads/index -q echo

java -jar target/index-0.0.3-SNAPSHOT.jar -f /Users/gaozhigang/Downloads/Index -q MessagingException 
```