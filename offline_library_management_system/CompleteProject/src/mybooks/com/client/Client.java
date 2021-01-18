package mybooks.com.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import mybooks.com.shared.Connection;
import mybooks.com.shared.IBookSellerSub;
import mybooks.com.shared.PropertyUtil;
import mybooks.com.shared.ServerModel;
import mybooks.com.shared.SharedUtility;

public class Client {
  @SuppressWarnings("resource")
public static void main(String args[]) {
    try {
   
      Scanner _input;
      //Common Interface for Server and Client
      IBookSellerSub _iBookSellerSub;   
      //Object to handle MenuSelection.
      ActionRouter _actionRouter=null;      
      
      //Get the list of worker servers
      List<ServerModel> _serverlist=new ArrayList<ServerModel>();
      _serverlist=PropertyUtil.getAllWorkerServerInfo();
      
      //Then Randomly Selected on of the fronend server
      int _randomIndex=SharedUtility.randInt(0, _serverlist.size()-1);
      
      //System.out.println("ServerIndex"+_randomIndex);
      
      //Get Connection for the Server
      _iBookSellerSub=Connection.GetWorkerServerConnection(_serverlist.get(_randomIndex).getServerIp(),_serverlist.get(_randomIndex).getServiceServerObj());

      //Class the handle menu execution according to the item selected
      if(_iBookSellerSub!=null)
    	  _actionRouter=new ActionRouter(_iBookSellerSub);
      
      while(true)
      {
    	   String _choice=null;
    	  int _actionId=-1;
    	  System.out.println("\n+-------------------------------------------------+\n");
    	  System.out.println("Select option for desired actions:"
    	  		+ "\n1.Search(Topic Name)"
    	  		+ "\n2.LookUp(Item Number)"
    	  		+ "\n3.Order(Item Number)"
    	  		+ "\n4.Report Servicewise Requests Count(Search/Lookup/Order)"
    	  		+ "\n5.Report Successful Orders"
    	  		+ "\n6.Report Unsuccessful Orders"
    	  		+ "\n7.Report Servicewise Performance (Search/Lookup/Order)"
    	  		+ "\n0.Exit");
    	  System.out.println("\n+-------------------------------------------------+\n");
    	  System.out.print("Enter Your Choice:");
    	  
    	  _input = new Scanner(System.in);    	 
    	  _choice=_input.nextLine();
    	  
    	  try {
    		  _actionId=Integer.parseInt(_choice);
    		} catch (NumberFormatException e) {
    		    System.out.println("Wrong option, sorry!");
    		    _actionId = -1;
    		}
    	  
    	  if(_actionId<0 || _actionId>7)
    		  System.out.println("Please Select Valid Option From List");
    	  
    	  else if(_actionId==0)
    		  break; 

    	  else
    		  _actionRouter.chooseRequiredAction(_actionId);
      }  
    }
    catch(Exception e) {
      System.out.println("Exception: " + e);
    }
  }
}