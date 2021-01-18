package mybooks.com.shared;

import java.util.HashMap;
import java.util.Map;

/*
 * Implementation of classic reader-writer problem to maintain synchronisation
 */
public class ReadWriteLock{

	   private Map<Thread, Integer> readerThreadMap = new HashMap<Thread, Integer>();

	   private int wrtAccesses    = 0;
	   private int wrtRequests    = 0;
	   private Thread wrtThread = null;


	  //Function for Read Lock
	  public synchronized void lockReadOperation() throws InterruptedException
	  {
		//Get the Current or callong thread
	    Thread callThread = Thread.currentThread();
	    
	    //Checking if current thread can get the access
	    while(! checkReadAccess(callThread))
	    {
	      wait();
	    }

	    //Increase thread access count by one
	    readerThreadMap.put(callThread, (getReadAccessCount(callThread) + 1));
	  }

	  private boolean checkReadAccess(Thread callingThread)
	  {
	   
		boolean ret=true;
	    
		if( wrtThread == callingThread )
			ret=true;
		
		else if(wrtThread != null)
	    	ret=false;
	    
		else if( readerThreadMap.get(callingThread) != null)
	    	ret=true;
	    
		else if( this.wrtRequests>0) 
	    	ret=false;
	    
	    return ret;
	  }


	  public synchronized void unlockReadOperation()
	  {
	    Thread callThread = Thread.currentThread();
	    
	    //Not Read Thread
	    if(readerThreadMap.get(callThread) == null)
	    {
	      throw new IllegalMonitorStateException("Not Holding Read Lock");
	    }
	    
	    //Get the Read Access Count
	    int accessCount = getReadAccessCount(callThread);
	    
	    if(accessCount == 1)
	    {
	    	readerThreadMap.remove(callThread);
	    }
	    else
	    {
	    	readerThreadMap.put(callThread, (accessCount -1));
	    }
	    
	    //Notifu other threads to wake up
	    notifyAll();
	  }

	  
	  
	  //function to lock write operation
	  public synchronized void lockWriteOperation() throws InterruptedException
	  {
		 Thread callThread = Thread.currentThread();
		 wrtRequests++;   
	    
	    while(! canGrantWriteAccess(callThread))
	    {
	      wait();
	    }
	    
	    wrtThread = callThread;	   
	    wrtAccesses++;
	    wrtRequests--;
	   
	  }

	  //function to unlock write lock
	  public synchronized void unlockWriteOperation() throws InterruptedException
	  {
	    if(wrtThread != Thread.currentThread())
	    {
	      throw new IllegalMonitorStateException("Not Holding Write Lock");
	    }
	    
	    wrtAccesses--;
	    
	    if(wrtAccesses == 0)
	    {
	      wrtThread = null;
	    }
	    
	    notifyAll();
	  }

	  //Returns ReadAccessCount  in the map
	  private int getReadAccessCount(Thread callThread)
	  {
	    Integer accessCount = readerThreadMap.get(callThread);
	    
	    if(accessCount == null) 
	    	return 0;
	    
	    return accessCount.intValue();
	  }	

	  
	  //Determines WriteAccess status
	  private boolean canGrantWriteAccess(Thread callThread)
	  {
	    boolean ret=true;
	    
		if(readerThreadMap.size() == 1 && readerThreadMap.get(callThread) != null)   
			ret=true;
		
		else if(readerThreadMap.size() > 0)     
	    	ret=false;
		
		else if(wrtThread == null) 
	    	ret=true;
		
		else if(wrtThread != callThread) 
	    	ret=false;
	    
	    return ret;
	  }	
	}