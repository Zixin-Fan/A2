package edu.seg2105.edu.server.backend;

import java.io.IOException;
import java.util.Scanner;

public class ServerConsole {
	
	final public static int DEFAULT_PORT = 5555;
	
	EchoServer server;
	
	Scanner fromConsole; 
	
	ServerConsole(EchoServer server){
		this.server = server;
		this.fromConsole = new Scanner(System.in);
	}
	
	public void accept() 
	  {
	    try
	    {

	      String message;

	      while (true) 
	      {
	        message = fromConsole.nextLine();
	        if(message.startsWith("#")) {
	        	handleCommand(message);
	        }
	        else if(message!=null) {
	        	display(message);
	        	server.sendToAllClients("server"+message);
	        }
	      }
	    } 
	    catch (Exception ex) 
	    {
	      System.out.println
	        ("Unexpected error while reading from console!!!");
	    }
	  }
	
	public void handleCommand(String command){
		if(command.equals("#quit")) {
			
			  try{
				  
			      server.close();
			      
			  }catch(IOException e) {}
			  
			  System.exit(0);
			    
		  }
		  else if(command.equals("#stop")) {
			  
			  server.stopListening();
			  
		  }
		  else if(command.startsWith("#close")) {
			  
			  try{
				  
			      server.close();
			      
			  }catch(IOException e) {}
			  
			  
		  }
		  else if(command.startsWith("#setport")) {
			  if(!server.isListening()) {
				  String port = command.substring("#setport ".length());
				  server.setPort(Integer.parseInt(port));
			  }
			  else {
				  display("Only allowed if the server is closed");
			  }
		  }
		  else if(command.equals("#start")){
			  if(!server.isListening()) {
				  try {
					  
					  server.listen();
					  
				  }catch(IOException e) {
					  
					  System.out.println("ERROR - Could not listen for clients!");
					  
				  }
				  
			  }
			  else {
				  display("Only valid if the server is stopped");
			  }
			  
		  }
		  else if(command.equals("#getport")) {
			  display(Integer.toString(server.getPort()));
		  }
		  else {
			  display(command);
	          server.sendToAllClients("server"+command);
		  }
	}
	
	public void display(String message) 
	  {
	    System.out.println("SERVER MESSAGE> " + message);
	  }

	
	public static void main(String[] args) 
	  {
		  
	    int port = 0; //Port to listen on

	    try
	    {
	      port = Integer.parseInt(args[0]); //Get port from command line
	    }
	    catch(Throwable t)
	    {
	      port = DEFAULT_PORT; //Set port to 5555
	    }
		
	    EchoServer sv = new EchoServer(port);
	    
	    ServerConsole serverConsole = new ServerConsole(sv);
	    
	    try 
	    {
	      sv.listen(); //Start listening for connections
	      
	      serverConsole.accept();
	      
	    } 
	    catch (Exception ex) 
	    {
	      System.out.println("ERROR - Could not listen for clients!");
	    }
	  }
}
