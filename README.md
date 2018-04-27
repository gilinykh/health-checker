# Health checker app

Total payload is available on url ```/api/endpoints/health```

## Build

Clone repository:

```git clone <url>```

Change directory to cloned project and build application by running command:

```mvn package```

Copy built app  ```.\target\health-checker-0.0.1-SNAPSHOT.jar``` to destination and launch it:

```java -jar health-checker-0.0.1-SNAPSHOT.jar```

## Configure endpoints

Place file called ```application.yml``` in the same directory as jar file.
This file accepts configuration of endpoints to be checked in format:

```
app.endpoints: http://service1-health-url,
               http://service2-health-url
```
