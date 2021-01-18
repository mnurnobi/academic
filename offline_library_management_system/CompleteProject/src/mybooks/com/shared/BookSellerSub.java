package mybooks.com.shared;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("serial")
public class BookSellerSub extends UnicastRemoteObject 
implements IBookSellerSub, Runnable{
	//No of Order Service Request Count
	private int orderServiceCount;
	
	//No of Lookup Service Request Count
	private int lookupServiceCount;
	
	//total time elapsed for search service
	private long searchServiceTime;
	
	//total time elapsed for order service
	private long orderServiceTime;
	
	//total time elapsed for lookup service
	private long lookupServiceTime;
	
	//BackEnd Server Reference
	private IBookSellerMain backBookSeller;
	
	//Object to hold books
	private BookList bookList;
	
	//Object to handle thread reader writer issue
	private ReadWriteLock lock;	
	
	//No of Search Service Request Count
	private int searchServiceCount;

	ConnectionModel connectionInfo;
	
	//To determine whether frontend server is registered to backend server
	private boolean isRegistered;
	
	//List to hold ResponseModel from back end server for Loopup service
	private List<ResponseModel> synchedLookupResponseList;
	
	//List to hold ResponseModel from back end server for order service
	private List<ResponseModel> synchedOrderResponseList;
	
	//Hold time object reference to get time during request
	private ITimeSyncOperator timeObject;
	
	//Theard to process respons
	private Thread threadObj;
	
	//For Serach service initialization
	private boolean searchSyncInit;
	
	public BookSellerSub(String serverid,ITimeSyncOperator timeobj) throws RemoteException 
	{
		super();		
		this.orderServiceCount=0;
		this.searchServiceTime=0;
		this.orderServiceTime=0;
		this.searchServiceCount=0;
		this.lookupServiceCount=0;
		this.lookupServiceTime=0;

		this.timeObject=timeobj;
		
		backBookSeller=Connection.GetMainServerConnection();
		lock=new ReadWriteLock();
		
		//BookList bookList=new BookList();
		connectionInfo=new ConnectionModel();
		connectionInfo.setServerId(serverid);
		connectionInfo.setServerHost(PropertyUtil.getWorkerServerIP(serverid));
		connectionInfo.setServerObjName(PropertyUtil.getWorkerServerServiceObj(serverid));
		connectionInfo.setServerPort("0");		
		
		this.isRegistered=false;
		this.searchSyncInit=false;
		
	    synchedLookupResponseList = Collections.synchronizedList(new LinkedList<ResponseModel>());
	    synchedOrderResponseList = Collections.synchronizedList(new LinkedList<ResponseModel>());
	    
	    threadObj=new Thread(this);		
	}
	
	/*
	 * Received the lookup request and forward the request to the main server
	 */
	@Override
	public synchronized BookModel Lookup(int itemnumber) throws RemoteException 
	{
		
		long startTime = System.nanoTime();	
		BookModel bk=null;
		if(isRegistered==false)
		{
			backBookSeller.registerWorkerServer(connectionInfo);
			isRegistered=true;
		}		
		
		ServerRequestModel request=new ServerRequestModel();
		//make request packet with request time 
		request.setFronServer(this);
		request.setItemNumber(itemnumber);
		request.setServerId(connectionInfo.getServerId());
		request.setRequestTime(timeObject.getCurrentTime());		
		
		//make request to back end server
		boolean ret;
		ret =backBookSeller.Lookup(request);	
		if(!ret) System.out.println("Request add failed !");
			
		//wait for a while to get the response from server
		synchronized (synchedLookupResponseList) 
		{
			while(synchedLookupResponseList.isEmpty())
			{			
				try {
					synchedLookupResponseList.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		//response removed as it is processed
		ResponseModel response = synchedLookupResponseList.remove(0);	
		
		lookupServiceCount++;
		
		bk=response.getResponseBook();
		
		long endTime = System.nanoTime();
		
		lookupServiceTime=lookupServiceTime+(endTime-startTime);
		
		return bk;
	}
	
	/*
	 * Receive the request for searching and forward it to the main server
	 */
	@Override
	public synchronized List<BookModel> Search(String topicName) throws RemoteException
	{
	   long startTime = System.nanoTime();		
	
	   List<BookModel> _booklist=new ArrayList<BookModel>();
	   
		//Register to main server
		if(this.isRegistered==false)
		{
			backBookSeller.registerWorkerServer(connectionInfo);
			isRegistered=true;				
		}
	  
		//for the first time get BookInformations from main server
		//And start thread for Book information to synch between main and sub server
	    if(this.searchSyncInit==false)
		{
			BookSyncWithServerForSearch();
			threadObj.start();
			this.searchSyncInit=true;
		}
		
		try {
			lock.lockReadOperation();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		try{
			//perform search operation
			searchServiceCount++;				
			_booklist=bookList.getBookByTopic(topicName);			  
		} 
		finally {
			lock.unlockReadOperation();		
		}		
			
		long endTime = System.nanoTime();
		searchServiceTime=searchServiceTime+(endTime-startTime);			

		return _booklist;			
	}
	
	/*
	 * Receive the request and forward it to the main server
	 */
	@Override
	public synchronized ResponseModel Order(int itemnumber) throws RemoteException {
		
		long startTime = System.nanoTime();
		ResponseModel response=new ResponseModel();
		if(isRegistered==false)
		{
			backBookSeller.registerWorkerServer(connectionInfo);
			isRegistered=true;
		}
		
		ServerRequestModel request=new ServerRequestModel();
		
		//make request packet with request time 
		request.setFronServer(this);
		request.setItemNumber(itemnumber);
		request.setServerId(connectionInfo.getServerId());	
		request.setRequestTime(timeObject.getCurrentTime());
		
		orderServiceCount++;
		
		//make request to main server
		boolean ret;
		ret =backBookSeller.Order(request);	
		if(!ret) System.out.println("Order Request add failed !");
				
		//wait for a while to get the response from server
		synchronized (synchedOrderResponseList) 
		{
			while(synchedOrderResponseList.isEmpty())
			{			
				try {
					synchedOrderResponseList.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}			
			}			
		}	
		
		response=synchedOrderResponseList.remove(0);
		
		long endTime = System.nanoTime();
		
		orderServiceTime=orderServiceTime+(endTime-startTime);
		
		return response;
	}

	/*
	 * receiving successful order request from client and forward to the main server
	 */	
	@Override
	public synchronized int ReportGoodOrders() throws RemoteException {
		// TODO Auto-generated method stub
		if(isRegistered==false)
		{
			backBookSeller.registerWorkerServer(connectionInfo);
			isRegistered=true;
		}
		
		int count=-1;
		//Ask backend to get ReportGoodOrders Query
		count=backBookSeller.reportSuccessfulOrders();
		
		return count;
	}

	/*
	 * 
	 * receiving unsuccessful order request from client and forward to the main server
	 */
	@Override
	public synchronized int ReportFailedOrders() throws RemoteException {
		
		if(isRegistered==false)
		{
			backBookSeller.registerWorkerServer(connectionInfo);
			isRegistered=true;
			
		}
		
		int count=-1;

		count=backBookSeller.reportUnsuccessfulOrders();
		
		return count;		
	}

	/*
	 * receiving overall service performance request from client and forward to the main server
	 */
	@Override
	public synchronized float ReportServicePerformance(String service) throws RemoteException {
		float avgServiceTime=-1;
		
		if(isRegistered==false)
		{
			backBookSeller.registerWorkerServer(connectionInfo);
			isRegistered=true;			
		}
		
		try {
			lock.lockReadOperation();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		try{
			//ask backend for the query
			avgServiceTime=backBookSeller.findServicePerformance(service);
		  
		} finally {
		  lock.unlockReadOperation();		
		}	
		
		return avgServiceTime;
	}
	
	/*
	 * Report Number of Request for different services
	 */
	@Override
	public int reportRequestNumber(String service) throws RemoteException {		
		int servicecount=-1;
		
		if(isRegistered==false)
		{
			backBookSeller.registerWorkerServer(connectionInfo);
			isRegistered=true;
		}
		
		try {
			lock.lockReadOperation();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		try{
			
			//Ask backend to get ReportRequestNumber Query
			servicecount=backBookSeller.findRequestCount(service);	
		  
		} finally {
		  lock.unlockReadOperation();		
		}			
		
		return servicecount;
	}
	
	/*
	 * receive the service request information
	 */	
	@Override
	public List<ServiceModel> GetServiceRequestInfo() throws RemoteException
	{
		
		List<ServiceModel> serviceinfolist=new ArrayList<ServiceModel>();
		
		try {
			lock.lockReadOperation();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		try
		{		
			ServiceModel serviceobj1=new ServiceModel();
			ServiceModel serviceobj2=new ServiceModel();
			ServiceModel serviceobj3=new ServiceModel();
		
			serviceobj1.setServiceName("Search");
			serviceobj1.setRequestCount(searchServiceCount);
			serviceobj1.setServiceTime(searchServiceTime);
		
			serviceobj2.setServiceName("LookUp");
			serviceobj2.setRequestCount(lookupServiceCount);
			serviceobj2.setServiceTime(lookupServiceTime);
		
			serviceobj3.setServiceName("Order");
			serviceobj3.setRequestCount(orderServiceCount);
			serviceobj3.setServiceTime(orderServiceTime);	
		
			serviceinfolist.add(serviceobj1);
			serviceinfolist.add(serviceobj2);
			serviceinfolist.add(serviceobj3);
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally {
			  lock.unlockReadOperation();		
		}	

		return serviceinfolist;
	}
	
	/*
	 * This function is called by separate thread to synchronise book 
	 * information periodically with main server
	 */	
	private void BookSyncWithServerForSearch() throws RemoteException
	{		
		ServerRequestModel request=new ServerRequestModel();
		
		request.setFronServer(this);
		request.setItemNumber(0);
		request.setServerId(connectionInfo.getServerId());	
		request.setRequestTime(timeObject.getCurrentTime());		
		
		try {
			lock.lockWriteOperation();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		try{
		
			bookList=backBookSeller.search(request);	  
		} 
		
		finally {
			try {
				lock.unlockWriteOperation();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}		
	}

	/*
	 * main server communicate to the worker server through this response update
	 */
	@Override
	public void UpdateResponse(ResponseModel response) throws RemoteException 
	{	
		if(response.getResponseServiceName().equals("Lookup"))
		{
			 //Got lookup service response
			 synchronized (synchedLookupResponseList)
			 {
				 //lookupResponseList.add(response);
				 synchedLookupResponseList.add(response);
				 synchedLookupResponseList.notifyAll();
				 
			 }
		}
		
		else if(response.getResponseServiceName().equals("Order"))
		{
			//Got lookup order response 
			synchronized (synchedOrderResponseList)
			 {
				 //lookupResponseList.add(response);
				 synchedOrderResponseList.add(response);
				 synchedOrderResponseList.notifyAll();
			 }
		}
	}
	
	/*
	 * sync Search Service BookModel info
	 */
	@Override
	public void run() 
	{
		boolean done = false;
		
		while (!done)
		{
			try {
				if(this.searchSyncInit==true)
				{
					BookSyncWithServerForSearch();
				}
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try 
			{
				//Wait for a while before making syncronization preodically
				Thread.sleep(10 *1000000);				
			} 
			catch (InterruptedException unexpected) 
			{
				System.out.println("error!");
				done = true;
			}	
		}
	}
}