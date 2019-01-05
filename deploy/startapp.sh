mkdir -p log

java -Dspring.profiles.active=prod -jar rest-shorturl-0.0.1-SNAPSHOT.jar 2 >> log/rest-shorturl.err 1 >> log/rest-shorturl.stdout
