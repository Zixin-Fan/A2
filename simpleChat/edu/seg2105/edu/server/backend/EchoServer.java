package edu.seg2105.edu.server.backend;
// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 


import java.io.IOException;

import ocsf.server.*;

/**
 * This class overrides some of the methods in the abstract 
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 */
public class EchoServer extends AbstractServer 
{
  //Class variables *************************************************
  
  /**
   * The default port to listen on.
   */
  final public static int DEFAULT_PORT = 5555;
  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public EchoServer(int port) 
  {
    super(port);
  }

  
  //Instance methods ************************************************
  
  /**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   */
  public void handleMessageFromClient
    (Object msg, ConnectionToClient client)
  {
	  
	  if(((String) msg).startsWith("#login")) {
	    	if(client.getInfo("loginID") == null) {
	    		String loginID = ((String) msg).substring("#login ".length());
	  		  //System.out.println(loginID);
	  		  client.setInfo("loginID",loginID);
	  		  //System.out.println("loginID: " + client.getInfo("loginID"));
	  	    System.out.println("Message received: " + msg + " from null.");
	  	    System.out.println(loginID+ " has logged on.");
	  	    this.sendToAllClients(client.getInfo("loginID")+" has logged on.");
	    	}
	    	else {
	    		try {
					client.sendToClient("The #login command should only be allowed as the first command received after a client connects.");
					client.close();
					return ;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    	}
		  }
	    else {
	    	System.out.println("Message received: " + msg + " from " + client.getInfo("loginID"));
	        this.sendToAllClients(client.getInfo("loginID")+": "+(String)msg);
	    }
  }
    
  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  protected void serverStarted()
  {
    System.out.println
      ("Server listening for connections on port " + getPort());
  }
  
  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  protected void serverStopped()
  {
    System.out.println
      ("Server has stopped listening for connections.");
  }
  
  /**
   * Implements the hook method called each time a new client connection is
   * accepted. The default implementation does nothing.
   * @param client the connection connected to the client.
   */
  protected void clientConnected(ConnectionToClient client) {
	  System.out.println("A new client has connected to the server.");
  }

  /**
   * Implements the hook method called each time a client disconnects.
   * The default implementation does nothing. The method
   * may be overridden by subclasses but should remains synchronized.
   *
   * @param client the connection with the client.
   */
  synchronized protected void clientDisconnected(
    ConnectionToClient client) {
	  System.out.println(client.getInfo("loginID")+" has disconnected");
  }
  
  
  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of 
   * the server instance (there is no UI in this phase).
   *
   * @param args[0] The port number to listen on.  Defaults to 5555 
   *          if no argument is entered.
   */
  
}
//End of EchoServer class
