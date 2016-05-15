# webcrawler

## Compilation: 
### As web service: 
mvn clean package
### As command line application:
mvn clean compile assembly:single

## Usage: 
### As web service: 
java -jar target/webcrawler-1.0-SNAPSHOT.jar 

####Browse tasks: 
GET http://localhost:8080/tasks/

####Browse unfinished tasks: 
GET http://localhost:8080/tasks/unfinished

####Browse specific tasks: 
GET http://localhost:8080/tasks/{id}

####Submit task: 
POST http://localhost:8080/tasks?baseUrl=\<URL\>&deep=\<DEEP\> 
#####example: 
POST http://localhost:8080/tasks?baseUrl=http://bash.org.pl/latest/&deep=1

## From command line:
java -jar target/webcrawler-1.0-command-line-SNAPSHOT.jar-jar-with-dependencies.jar \<URL\> \<DEEP\>
###example: 
java -jar target/webcrawler-1.0-command-line-SNAPSHOT.jar-jar-with-dependencies.jar http://bash.org.pl/latest/ 1
