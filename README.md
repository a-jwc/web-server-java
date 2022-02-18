# Web Server Application

## Description
This is a simple Apache-like multithreaded web server written in Java. It handles a subset of HTTP. 

## Milestones / Specifications

- [ ] 1. Read, and store, standard configuration files for use in responding to client requests
- [ ] 2. Parse HTTP Requests
- [ ] 3. Generate and send HTTP Responses (this involves many possible code paths, and is probably the most significant implementation step)
- [ ] 4. Respond to multiple simultaneous requests through the use of threads
- [ ] 5. Execute server side processes to handle server side scripts
- [ ] 6. Support simple authentication
- [ ] 7. Support simple caching
- [ ] 8. Logging

## Server Workflow

![diagram](https://user-images.githubusercontent.com/68071075/154633945-727b12f4-2cb5-4ebb-a118-bf1b024a9c90.png)

## Run

```
// TODO: Simplify to javac WebServer.java;java WebServer
javac server/config/MimeTypes.java; javac server/config/HttpdConfig.java;javac server/config/Htpassword.java; javac public_html/RunScript.java;javac server/config/Configuration.java;javac server/config/HtAccess.java;javac server/Server.java;javac server/Worker.java;javac WebServer.java;java WebServer
```
