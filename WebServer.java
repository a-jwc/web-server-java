import java.io.*;
import java.net.*;
import java.time.*;
import java.util.*;

import server.*;

public class WebServer {
  public WebServer() {

  }

  public static void main(String[] args) {
    Server server = new Server();
    try {
      server.start();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}