import java.net.*;
import java.io.*;

public class WebServer {
    public static void main(String[] args) {

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
    public void getRequest(string URI) {

    }   

    public void checkRequest(String request) {
        string http_method = request.split(0, 1);
        string uri = request.split(1, 2);
        switch(http_method) {
            case "GET":
                getRequest(uri);
                break;
        }
    }
}
