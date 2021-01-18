package mybooks.com.shared;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class Connection {
		
	//Get Worker Server Connection by host and object Name
	public static IBookSellerSub GetWorkerServerConnection(String host,String objectName)
	{
		IBookSellerSub iBookSellerSub=null;	
		
	 	String strPort=PropertyUtil.GetPropertyValue("rmi_port");
		String addServerURL = "rmi://" + host +":"+strPort+"/"+objectName;
		//System.out.println("=====++++++=====" + addServerURL);
		try {
			iBookSellerSub=(IBookSellerSub)Naming.lookup(addServerURL);			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
		return iBookSellerSub;
	}
	
	//Given connection of main Server
	public static IBookSellerMain GetMainServerConnection()
	{
		IBookSellerMain iBookSellerMain=null;
		String host=null;	
		
		//Load host name from config.property file.
		host=PropertyUtil.GetPropertyValue("serverhost");
		String strPort=PropertyUtil.GetPropertyValue("rmi_port");	 	
		
		String addServerURL = "rmi://" + host+":"+strPort+ "/BookSellerMain";
		//System.out.println("=====++++++=====" + addServerURL);
		try {
			iBookSellerMain=(IBookSellerMain)Naming.lookup(addServerURL);			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
		return iBookSellerMain;
	}
	
	public static IBookSellerSub GetRandomFrontEndServer()
	{
		  IBookSellerSub iBookSellerSub;   
	      	      
	      //Get the list of worker servers
	      List<ServerModel> serverlist=new ArrayList<ServerModel>();
	      serverlist=PropertyUtil.getAllWorkerServerInfo();
	      
	      //Then Randomly Selected on of the worker server
	      int randserverindex=SharedUtility.randInt(0, serverlist.size()-1);
	      
	          
	      //Get Connection for the Server
	      iBookSellerSub=Connection.GetWorkerServerConnection(serverlist.get(randserverindex).getServerIp(),serverlist.get(randserverindex).getServiceServerObj());
	      
	      return iBookSellerSub;
	}
	
	public static ITimeSyncOperator GetTimeSyncServers(String serverIp, String serverObj)
	{
		 ITimeSyncOperator iTime=null;
		 String strPort=PropertyUtil.GetPropertyValue("rmi_port");
		 
		 String addServerURL = "rmi://" + serverIp +":"+strPort+ "/"+serverObj;
		 
		 try {
			 iTime=(ITimeSyncOperator)Naming.lookup(addServerURL);			
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NotBoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			 
		 
		 return iTime;
	}
}
