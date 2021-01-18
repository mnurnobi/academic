package mybooks.com.shared;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

public class TimeSyncDaemon extends TimerTask 
{
	private List<ServerModel> serverList=new ArrayList<ServerModel>();
	private List<ITimeSyncOperator> serverTimeObjList=new ArrayList<ITimeSyncOperator>();
	private int leaderServerIndex;
	
	public TimeSyncDaemon( List<ServerModel> serverlist)
	{
		this.serverList=serverlist;
		
		this.leaderServerIndex=-1;
		int index=0;
		
		for(int i=0;i<serverList.size();i++)
		{
			ITimeSyncOperator obj=Connection.GetTimeSyncServers(serverList.get(i).getServerIp(),serverList.get(i).getTimeServerObj());
			
			if(obj!=null)
			{
				serverTimeObjList.add(obj);	
				
				if(serverList.get(i).getServerType()==true)
				{
					//find the leader server index
					leaderServerIndex=index;
				}
				
				index++;
			}
		}
		
		if(leaderServerIndex<0)
			System.out.println("Please define leader server in config file");
		
		
	}
	
	 @Override
	 public void run()
	 {   
	     long[] servertime=new long[serverTimeObjList.size()];
	     
	     long sumtimediff=0;
	     long avgOffsetVal=0;
	     long newoffset=0;
	     
	     //Get all the server time
	     for(int i=0;i<serverTimeObjList.size();i++)
	     {
	    	 try {
				servertime[i]=serverTimeObjList.get(i).getCurrentTime();
				
				if(i==leaderServerIndex) {
				}			
				
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	 
	     }
	     
	     //now find the difference with leader server and make average of that as offset
	     for(int i=0;i<serverTimeObjList.size();i++)
	     {
	    	 sumtimediff=sumtimediff+(servertime[i]-leaderServerIndex);	    	 
	     }
	     
	     //Got the average of diff offset in between servers;
	     avgOffsetVal=(long)(sumtimediff/serverTimeObjList.size());
	     
	     
	     //push the time offset to all the front end servers
	     for(int i=0;i<serverTimeObjList.size();i++)
	     {
	    	  try {	    		 
	    		if(avgOffsetVal>200)
	    		{
	    			newoffset=(servertime[leaderServerIndex]+avgOffsetVal)-servertime[i];
	    			serverTimeObjList.get(i).pushTimeOffset(newoffset);		    				
	    		}
				
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	 
	     }  
	     
	 }
}
