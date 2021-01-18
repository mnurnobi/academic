package mybooks.com.shared;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

public class TimeSynchronizer
{	
	private Timer broadTask;	
	
	public TimeSynchronizer()
	{
		super();
		//Create a timer 
		broadTask=new Timer();		
	}	
	
	public void startSynchronizing()
	{
		List<ServerModel> serverlist=new ArrayList<ServerModel>();
		serverlist = PropertyUtil.getAllWorkerServerInfo();
		
		//Schedule time sync after each 5000 in seperate thread
		this.broadTask.schedule(new TimeSyncDaemon(serverlist), 0, 5000);		
	}
	
	public void stopSynchronizing()
	{
		this.broadTask.cancel();
	}
}
