package mybooks.com.test;

import java.rmi.RemoteException;

import mybooks.com.shared.Connection;
import mybooks.com.shared.IBookSellerSub;

public class ConcurrencyTest extends Thread {
	
	private int testCaseID;
	private int numberOfThread;	
	private int successoder;
	private int failedorder;
	
	
	private static int succefulorderCount;
	private static int failedOrderCount;
	
	public int getNumberOfThread() {
		return numberOfThread;
	}

	public void setNumberOfThread(int numberOfThread) {
		System.out.println(numberOfThread);
		this.numberOfThread = numberOfThread;
		
	}

	public ConcurrencyTest(int testcaseid)
	{
		super();	
		successoder=0;
		failedorder=0;
	}	
	
	public int getSuccessoder() {
		return successoder;
	}

	public void setSuccessoder(int successoder) {
		this.successoder = successoder;
	}

	public int getFailedorder() {
		return failedorder;
	}

	public void setFailedorder(int failedorder) {
		this.failedorder = failedorder;
	}

	public static int getSuccefulorderCount() {
		return succefulorderCount;
	}

	public static void setSuccefulorderCount(int succefulorderCount) {
		ConcurrencyTest.succefulorderCount = succefulorderCount;
	}

	public static int getFailedOrderCount() {
		return failedOrderCount;
	}

	public static void setFailedOrderCount(int failedOrderCount) {
		ConcurrencyTest.failedOrderCount = failedOrderCount;
	}

	public int getTestCaseID() {
		return testCaseID;
	}

	public void setTestCaseID(int testCaseID) {
		this.testCaseID = testCaseID;
	}

	public void run()
	{
		TestConcurrentOder();
	}
	
	public void PreConcurrencyOrderTest() throws RemoteException
	{
		IBookSellerSub addServerIntf;   
	      
	    addServerIntf=Connection.GetRandomFrontEndServer();
	    
	    successoder=addServerIntf.ReportGoodOrders();
	    failedorder=addServerIntf.ReportFailedOrders();
		
	}
	
	public void PostConcurrencyOrderTest() throws RemoteException
	{
		IBookSellerSub addServerIntf;   
	      
	    addServerIntf=Connection.GetRandomFrontEndServer();
	     
	    if((addServerIntf.ReportGoodOrders()+addServerIntf.ReportFailedOrders())==(successoder+failedorder+numberOfThread))
	    {
	    	System.out.println("TestConcurrentOder Successful");
	    }
	    else
	    {
	    	System.out.println("TestConcurrentOder Failed");
	    	
	    	System.out.println(successoder+failedorder+numberOfThread);
	    }
	    		
	}
	
	public void TestConcurrentOder()
	{
		 IBookSellerSub addServerIntf;   
	      
	     addServerIntf=Connection.GetRandomFrontEndServer();
	     
	     try {
			if(addServerIntf.Order(3).isResponseStatus())
				succefulorderCount++;
			else
				failedOrderCount++;
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	     
	     
	}
	
	public void PreConcurrencyOrderSearchTest() throws RemoteException
	{
		IBookSellerSub addServerIntf;   
	      
	    addServerIntf=Connection.GetRandomFrontEndServer();
	    
	    successoder=addServerIntf.ReportGoodOrders();
	    failedorder=addServerIntf.ReportFailedOrders();
		
	}
	
	public void PostConcurrencyOrderSearch() throws RemoteException
	{
		IBookSellerSub addServerIntf;   
	      
	    addServerIntf=Connection.GetRandomFrontEndServer();
	     
	    if((addServerIntf.ReportGoodOrders()+addServerIntf.ReportFailedOrders())==(successoder+failedorder+numberOfThread))
	    {
	    	System.out.println("TestConcurrentOder Successful");
	    }
	    else
	    {
	    	System.out.println("TestConcurrentOder Failed");
	    	
	    	System.out.println(successoder+failedorder+numberOfThread);
	    	///System.out.println(failedorder+failedOrderCount);
	    }
	    		
	}
	
	public void TestConcurrentOrderSearch()
	{
		 IBookSellerSub addServerIntf;   
	      
	     addServerIntf=Connection.GetRandomFrontEndServer();
	     
	     try {
			if(addServerIntf.Order(3).isResponseStatus())
				addServerIntf.Search(addServerIntf.Lookup(4).getBookTopic());
			else
				failedOrderCount++;
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	     
	     
	}

}
