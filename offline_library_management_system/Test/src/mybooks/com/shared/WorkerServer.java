package mybooks.com.shared;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class WorkerServer 
{
	 public static void main(String args[]) throws IOException, InterruptedException 
	 {
		 //For Server ID such as Server1, Server2
		 String strId=null;
		 //To hold Server IP
		 String strIp=null;
		 //To Hold Front End Server time object
		 String strtimeobj=null;
		 //To Hold Front End Server service object
		 String strserviceobj=null;
		 
		 String strPort=PropertyUtil.GetPropertyValue("rmi_port");
		 int port = Integer.parseInt(strPort);
			 
		 BufferedReader br= new BufferedReader(new InputStreamReader(System.in));		
			 	
		while(true)
		{
			 if(args.length==0)
			 {
				 System.out.println("Enter Server ID(e.g server1, server2 or server3) To Start the Server:");
			 
				 //read the serverid and it should be consistent with config file
				 strId=br.readLine();	
			 }
			 else
			 {
				 strId=args[0];
			 }
			 
			 //Check the consistency with config file
			 boolean ret= PropertyUtil.IsValidWorkerServer(strId);
			 		
			 if(ret==true)
			 {
				//using server id get the server ip
			 	strIp=PropertyUtil.getWorkerServerIP(strId);
			 	//using server id get the server time object name
			 	strtimeobj=PropertyUtil.getWorkerServerTimeObj(strId);
			 	//using server id get the server service object name
			 	strserviceobj=PropertyUtil.getWorkerServerServiceObj(strId);
			 	
			 	//if ip is null the it should be defined in the config file.
			 	if(strIp==null)
			 	{
			 		System.out.println("Server IP not configured in config file. Please set proper ip address");
					System.exit(0);			 				
			 	}
			 	else
			 	{			 			
			 		//This property set is required linux as in linux hostname can be mapped to
			 		//127.0.1.1 ip in /etc/hosts file. So connection from other pc fails
			 		System.setProperty("java.rmi.server.hostname",strIp);
			 				break;
			 	}
			 }
			 else
			 {
			 	System.out.println("Entered Wrong Server Id. Id should be as server1, server2 or server3");
			 }
		}
		
		ITimeSyncOperator timeSync = new TimeSyncOperator();
		IBookSellerSub  fronServer=new BookSellerSub(strId, timeSync);
		
		UnicastRemoteObject.unexportObject(timeSync,true); 
		UnicastRemoteObject.unexportObject(fronServer,true); 

		ITimeSyncOperator stub =
            (ITimeSyncOperator) UnicastRemoteObject.exportObject(timeSync, 0);

		IBookSellerSub stubFron =
	            (IBookSellerSub) UnicastRemoteObject.exportObject(fronServer, 0);

		Registry registry = LocateRegistry.getRegistry(port);	 
         
        registry.rebind(strtimeobj, stub); 
        registry.rebind(strserviceobj, stubFron); 
        
        
        //As per configuration, if it is a leader front-end server it should manage time 
        // Synchronisation using daemon
        if(PropertyUtil.isMainServer(strId))       
        {        
        	Thread.sleep(100);  
        	TimeSynchronizer timesyncmgr=new TimeSynchronizer();
        	timesyncmgr.startSynchronizing();
        }
        
        System.out.println("Server With Id:"+ strId+" Started Successfully!!!");
	 }  	
}	


