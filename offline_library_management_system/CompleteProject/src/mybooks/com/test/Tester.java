package mybooks.com.test;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import mybooks.com.shared.Connection;
import mybooks.com.shared.IBookSellerSub;
import mybooks.com.shared.PropertyUtil;
import mybooks.com.shared.ServerModel;
import mybooks.com.shared.SharedUtility;

public class Tester {
	
	public static void main(String args[]) 
	{
		  //Common Interface for Server and Client
	      //Function Test 
		  IBookSellerSub subBookSeller;   
	         
	      //Get the list of fronend servers
	      List<ServerModel> serverlist=new ArrayList<ServerModel>();
	      serverlist=PropertyUtil.getAllWorkerServerInfo();
	      
	      //Then Randomly Selected on of the fronend server
	      int randserverindex=SharedUtility.randInt(0, serverlist.size()-1);     
	      
	      
	      //Get Connection for the Server
	      subBookSeller=Connection.GetWorkerServerConnection(serverlist.get(randserverindex).getServerIp(),serverlist.get(randserverindex).getServiceServerObj());
	      
	      FunctionalTest functest=new FunctionalTest(subBookSeller);
	      
	      try {
			functest.ExecuteFunctionalTets();
		 } catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		 }
	     //End Functional Test
	     
	     //Test for ConcurrentOrder 
	     ConcurrencyTestInitiator.TestConcurrentOder();  
	     //End ConcurrentOrder Test
	    
	}	

}
