import java.io.*;
import java.net.*;

public class test {

  static String get_req =
    "GET /index.html HTTP/1.1
User-Agent: Mozilla/4.0 (compatible; MSIE5.01; Windows NT)
Host: www.tutorialspoint.com
Accept-Language: en-us
Accept-Encoding: gzip, deflate
Connection: Keep-Alive";

  public static void main(String[] args) {
    checkRequest(get_req);
  }

  /*
        GET /hello.htm HTTP/1.1
        User-Agent: Mozilla/4.0 (compatible; MSIE5.01; Windows NT)
        Host: www.tutorialspoint.com
        Accept-Language: en-us
        Accept-Encoding: gzip, deflate
        Connection: Keep-Alive
    */

  /* • HTTP_METHOD IDENTIFIER HTTP_VERSION

        HTTP_HEADERS

        BODY
        
        • HTTP_METHOD specifies an HTTP method (verb): GET, HEAD, 
        POST, PUT, DELETE, TRACE, OPTIONS, CONNECT, PATCH
        • IDENTIFIER is the URI of the resource or the body
        • HTTP_VERSION is the HTTP version being used by the client
        • BODY is an optional payload (the Content-Length header must be present if BODY is present)
    */

  public static void getRequest(String URI) {}
  
  public static void checkRequest(String get_req) {
    // Split by new lines
    String reqArr[] = get_req.split("\\r?\\n");

    // Split first line by whitespace
    String headerArr[] = reqArr[0].split(" ");

    for (int i = 0; i < reqArr.length; i++) {
      System.out.println(reqArr[i] + "\n");
    }

    // HTTP method string
    String http_method = headerArr[0];
    // identifier string
    String uri = headerArr[1];
    // HTTP version string
    String http_version = headerArr[2];

    switch (http_method) {
      case "GET":
        System.out.println(
          "HTTP_Method: " +
          http_method +
          "\nURI: " +
          uri +
          "\nHTTP_Version: " +
          http_version +
          "\n"
        );
        getRequest(uri);
        break;
      case "POST":
        break;
      case "HEAD":
        break;
      case "PUT":
        break;
      case "DELETE":
        break;
    }
  }
}
