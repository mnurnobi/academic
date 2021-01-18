package mybooks.com.test;

import java.rmi.RemoteException;

public class ConcurrencyTestInitiator {
	
	public static void TestConcurrentOder()
	{
		ConcurrencyTest initiator=new ConcurrencyTest(1);
	    initiator.setNumberOfThread(20);
	     
	    //Set the Preconditions for Concurrency Test
	      try {
			initiator.PreConcurrencyOrderTest();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	      
	     
	      //Start different Threads to submit order
	      ConcurrencyTest[] threadobj=new ConcurrencyTest[20];
	      
	      for(int i=0;i<20;i++)
	      {
	    	  threadobj[i]=new ConcurrencyTest(1);
	      }
	      
	      for(int i=0;i<20;i++)
	      {
	    	  threadobj[i].start();
	      }
	      
	      for(int i=0;i<20;i++)
	      {
	    	  try {
				threadobj[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	      }
		 //End of Concurrent Order
	      
	      //Post condition of concurrent test
	      try {
				initiator.PostConcurrencyOrderTest();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

}
