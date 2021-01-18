package mybooks.com.server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import mybooks.com.shared.BookSellerMain;
import mybooks.com.shared.BookList;
import mybooks.com.shared.IBookSellerMain;
import mybooks.com.shared.PropertyUtil;

/*
 * RMI Server Class
 */
public class Server{

	 public static void main(String args[]) throws RemoteException 
	 {
	
		 try {
			 
			 	System.out.println("Starting Main Server...");
			 	
			 	String strIp=PropertyUtil.GetPropertyValue("serverhost");
			 	String strPort=PropertyUtil.GetPropertyValue("rmi_port");
			 	int port = Integer.parseInt(strPort);
			 	
			 	//This property set is required linux as in linux hostname can be mapped to
			 	//127.0.1.1 ip in /etc/hosts file. So connection from other pc fails
			 	System.setProperty("java.rmi.server.hostname", strIp);
	        	
	        	BookList _bookList=new BookList();		  
	   		  	_bookList=BookListReader.getBookList();	   		  	
	   		  	
	   		  	IBookSellerMain _addMainServer = new BookSellerMain(_bookList);	            
	   		  
	   		    UnicastRemoteObject.unexportObject(_addMainServer, true);   
	   		   
	   		    
	   		    IBookSellerMain _mainStub =
		                (IBookSellerMain) UnicastRemoteObject.exportObject(_addMainServer, 0);
	   		    
	             Registry _registry = LocateRegistry.getRegistry(port);	             
	           
	             //Bind Main Server
	             _registry.rebind("BookSellerMain", _mainStub);
	             
	             System.out.println("Server Started Successfully!!!");  
	        } catch (Exception e) {
	            System.err.println("Server exception:");
	            e.printStackTrace();
	        } 	
		
	  }	

}
