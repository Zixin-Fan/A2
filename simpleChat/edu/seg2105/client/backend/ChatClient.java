// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package edu.seg2105.client.backend;

import ocsf.client.*;

import java.io.*;

import edu.seg2105.client.common.*;

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 */
public class ChatClient extends AbstractClient
{
  //Instance variables **********************************************
  
  /**
   * The interface type variable.  It allows the implementation of 
   * the display method in the client.
   */
  ChatIF clientUI; 
  
  String loginID;

  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */
  
  public ChatClient( String host, int port, ChatIF clientUI) 
    throws IOException 
  {
    super(host, port); //Call the superclass constructor
    this.clientUI = clientUI;
    openConnection();
  }

  
  //Instance methods ************************************************
    
  public void setLoginID(String loginID) throws IOException{
	  this.loginID = loginID;
	  sendToServer("#login "+loginID);
  }
  
  public String getLoginID() {
	  return loginID;
  }
  
  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg) 
  {
    clientUI.display(msg.toString());
    
    
  }

  /**
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.    
   */
  public void handleMessageFromClientUI(String message)
  {
    try
    {
    	
    	if(message.startsWith("#")) {
    		handleCommand(message);
    	}
    	else{
    		sendToServer(message);
    	}
    }
    catch(IOException e)
    {
      clientUI.display
        ("Could not send message to server.  Terminating client.");
      quit();
    }
  }
  
  private void handleCommand(String command) throws IOException {
	  if(command.equals("#quit")) {
		  quit();
	  }
	  else if(command.equals("#logoff")) {
		  
		  if(isConnected()) {
			  closeConnection();
		  }
		  else{
			  clientUI.display("Error: You are already logged off");
		  }
	  }
	  else if(command.startsWith("#sethost")) {
		  if(!isConnected()) {
			  String host = command.substring("#sethost ".length());
			  setHost(host);
		  }
		  else {
			  clientUI.display("You should log off first");
		  }
	  }
	  else if(command.startsWith("#setport")) {
		  if(!isConnected()) {
			  String port = command.substring("#setport ".length());
			  setPort(Integer.parseInt(port));
		  }
		  else {
			  clientUI.display("You should log off first");
		  }
	  }
	  else if(command.equals("#login")) {
		  if(!isConnected()) {
			  openConnection();
			  sendToServer("#login "+loginID);
		  }
		  else{
			  clientUI.display("Error: You are already logged in");
		  }
		  
	  }
	  else if(command.equals("#gethost")) {
		  clientUI.display(getHost());
	  }
	  else if(command.equals("#getport")) {
		  clientUI.display(Integer.toString(getPort()));
	  }
	  else{
		  sendToServer(command);
	  }
  }
  
  /**
   * This method terminates the client.
   */
  public void quit()
  {
    try
    {
      closeConnection();
    }
    catch(IOException e) {}
    System.exit(0);
  }
  
  /**
	 * Hook method called each time an exception is thrown by the client's
	 * thread that is waiting for messages from the server. The method may be
	 * overridden by subclasses.
	 * 
	 * @param exception
	 *            the exception raised.
	 */
	protected void connectionException(Exception exception) {
		clientUI.display("The server is shut down");
		System.exit(0);
	}
	
	/**
	 * Hook method called after the connection has been closed. The default
	 * implementation does nothing. The method may be overriden by subclasses to
	 * perform special processing such as cleaning up and terminating, or
	 * attempting to reconnect.
	 */
	protected void connectionClosed() {
		clientUI.display("Connection closed");
	}
	
	
}
//End of ChatClient class
