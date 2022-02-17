# csc667-web-server

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

![image](https://user-images.githubusercontent.com/68071075/134900964-552f296d-bdfb-4d1e-98c3-c1afae770a6f.png)

## Run

```
// TODO: Simplify to javac WebServer.java;java WebServer
javac server/config/MimeTypes.java; javac server/config/HttpdConfig.java;javac server/config/Htpassword.java; javac public_html/RunScript.java;javac server/config/Configuration.java;javac server/config/HtAccess.java;javac server/Server.java;javac server/Worker.java;javac WebServer.java;java WebServer
```
