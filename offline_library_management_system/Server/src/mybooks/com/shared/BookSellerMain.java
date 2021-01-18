package mybooks.com.shared;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@SuppressWarnings("serial")
public class BookSellerMain extends UnicastRemoteObject
implements IBookSellerMain, Runnable {
	//Object to hold books
	private BookList bookList;
	
	//Object to handle thread reader writer issue
	private ReadWriteLock lock;	
	
	//No of successful order request count
	private int successfulOrderCount;
	
	//No of failed order request count
	private int unsuccessfulOrderCount;
	
	//total time elapsed for order service
	private long orderServiceTime;
	
	//total time elapsed for lookup service
	private long lookupServiceTime;
	
	//List to hold incoming request for Lookup Service	
	private List<ServerRequestModel> lookupqueue;
	
	//Lookup Processing queue to process requests
	private List<ServerRequestModel> lookupprocessingqueue;
	
	//List to hold incoming request for order Service
	private List<ServerRequestModel> orderqueue;
	
	//Oder Processing queue to process requests
	private List<ServerRequestModel> orderprocessingqueue;
	
	//Thread to process incoming requests
	private Thread requestHandlerThread; 	
	
	//Map to hold front end server objects with server names.
	private HashMap<String, IBookSellerSub> objectMap = new HashMap<String,IBookSellerSub>();	
	
	
	public BookSellerMain(BookList bookList) throws RemoteException 
	{
		super();
		
		this.bookList = bookList;		
		this.orderServiceTime=0;
		this.lookupServiceTime=0;	
		
		lookupqueue=Collections.synchronizedList(new LinkedList<ServerRequestModel>());
		orderqueue=Collections.synchronizedList(new LinkedList<ServerRequestModel>());	
		
		lookupprocessingqueue=Collections.synchronizedList(new LinkedList<ServerRequestModel>());
		orderprocessingqueue=Collections.synchronizedList(new LinkedList<ServerRequestModel>());
		
		lock=new ReadWriteLock();
		requestHandlerThread = new Thread(this);	
		
		//Start thread to process requests periodically
		requestHandlerThread.start();		
		
	}

	/*
	 * search book list
	 */
	@Override
	public synchronized BookList search(ServerRequestModel request) throws RemoteException 
	{			
		return this.bookList;	
	}
	
	
	/*
	 * lookUp Book by book number
	 */
	@Override
	public synchronized boolean Lookup(ServerRequestModel request) throws RemoteException
	{
				
		boolean ret=false;
		
		//For Service just pushed the reuest to queque. This queue will processed later according to request time
		synchronized(lookupqueue)
		{
			lookupqueue.add(request);
			ret=true;
		}			
		
		return ret;
	}
	
	
	/*
	 * to look up server by item number
	 */
	private synchronized BookModel serverLookup(int _itemNumber) throws RemoteException {
		
		long startTime = System.nanoTime();	
		BookModel bk=null;
		
		try {
			lock.lockReadOperation();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		try{
			bk= bookList.getBookDetail(_itemNumber);		  
		} finally {
		  lock.unlockReadOperation();		
		}		
		
		long endTime = System.nanoTime();
		
		lookupServiceTime=lookupServiceTime+(endTime-startTime);
		
		return bk;
	}
	
	
	/*
	 * order of a book
	 */		
	@Override
	public synchronized boolean Order(ServerRequestModel request) throws RemoteException
	{
		boolean ret=false;
		//Oder added to the queue
		synchronized(orderqueue)
		{
			orderqueue.add(request);
			ret=true;
		}		
		
		return ret;			
	}
	
	/*
	 * to place order in the server side
	 */
	private synchronized ResponseModel serverOrder(int itemnumber) throws RemoteException {
		long startTime = System.nanoTime();	
		boolean ret=false;
		ResponseModel _response=new ResponseModel();
		
		_response.setResponseServiceName("Order");
		
		try {
			lock.lockWriteOperation();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		try{
			ret=bookList.orderBook(itemnumber);

			if(ret==true)
			{
				successfulOrderCount++;
				_response.setResponseStatus(true);				
				
				String strcount=PropertyUtil.GetPropertyValue("discount_count");
								
				int count=Integer.parseInt(strcount);
				
				int mod=(successfulOrderCount % count);				
				
				//Make the functionality of discount on 100th order request
				if ((mod==0) && (successfulOrderCount>0))
				{
					_response.setResponseMsg("Congratulation!! As "+strcount+"th Order Requester you are given 10% discount");
				}
				else
				{
					_response.setResponseMsg("Order Placed Successfully");
				}
			}
			//other wise order place failed
			else
			{
				unsuccessfulOrderCount++;
				_response.setResponseStatus(false);
				_response.setResponseMsg("Order Failed");
			}		
		  
		} finally {
			  
			try {
				lock.unlockWriteOperation();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
		}		
		
		long endTime = System.nanoTime();
		
		orderServiceTime=orderServiceTime+(endTime-startTime);
		
		return _response;
	}

	/*
	 * find total successful order count
	 */	
	@Override
	public int reportSuccessfulOrders() throws RemoteException {
		// TODO Auto-generated method stub
		int count=-1;
		try {
			lock.lockReadOperation();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		try{
			count=successfulOrderCount;
		  
		} finally {
		  lock.unlockReadOperation();		
		}		
		return count;
	}
	
	/*
	 * 
	 * find unsuccessful orders
	 */
	@Override
	public int reportUnsuccessfulOrders() throws RemoteException {
		int count=-1;
		
		try {
			lock.lockReadOperation();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		try{
			//Critical code here
			count=unsuccessfulOrderCount;
		  
		} finally {
		  lock.unlockReadOperation();		
		}
		
		return count;		
	}
	
	/*
	 * to find service wise request count
	 */
	@Override
	public int findRequestCount(String service) throws RemoteException {		
		
		int servicecount=0;	
		
		try {
			lock.lockWriteOperation();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		//As requests are finally served by the front end servers, they can give the exact number of request
		//for a service. Even Search does not heat back-end server. So, this block of code connects to all front end server
		//and gets their report request number.		
		Iterator<Map.Entry<String, IBookSellerSub>> _iterator = objectMap.entrySet().iterator();	
		
		try
		{	
			while (_iterator.hasNext())
			{
				Map.Entry<String, IBookSellerSub> _entry = _iterator.next();
	        
				List<ServiceModel> serviceinfo=new ArrayList<ServiceModel>();
	        
	              
				serviceinfo=objectMap.get(_entry.getKey()).GetServiceRequestInfo();  
	        
				for(int i=0;i<serviceinfo.size();i++)
				{
					if(serviceinfo.get(i).getServiceName().equalsIgnoreCase(service))
					{
						//Summing of service request count for the desired service
						servicecount=servicecount+(int)serviceinfo.get(i).getRequestCount();
					}
				}
			}
		}
		finally {
		  try {
				lock.unlockWriteOperation();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}
		
		return servicecount;
	}

	/*
	 * to find service wise performance
	 */
	@Override
	public float findServicePerformance(String service) throws RemoteException 
	{
		float avgServiceTime=-1;
		long servicecount=0;
		long servicetime=0;
		
		try {
			lock.lockWriteOperation();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		Iterator<Map.Entry<String, IBookSellerSub>> iterator = objectMap.entrySet().iterator();	
		
		try
		{
			while (iterator.hasNext())
			{
				Map.Entry<String, IBookSellerSub> entry = iterator.next();
	        
				List<ServiceModel> serviceinfo=new ArrayList<ServiceModel>();      
	        
				serviceinfo=objectMap.get(entry.getKey()).GetServiceRequestInfo(); 	 
				
				for(int i=0;i<serviceinfo.size();i++)
				{
					if(serviceinfo.get(i).getServiceName().equalsIgnoreCase(service))
					{
						servicecount=servicecount+serviceinfo.get(i).getRequestCount();
						servicetime=servicetime+serviceinfo.get(i).getServiceTime();
					}
				}
			}
		}
		finally {
			  try {
					lock.unlockWriteOperation();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
		}
		
		avgServiceTime = servicecount <= 0 ? 0 : (servicetime/servicecount);
		
		return avgServiceTime;
	}

	/*
	 * to include/ register worker servers
	 */
	@Override
	public synchronized  void registerWorkerServer(ConnectionModel connectioninfo) throws RemoteException 
	{
		
		IBookSellerSub _obj=Connection.GetWorkerServerConnection(connectioninfo.getServerHost(),connectioninfo.getServerObjName());
		objectMap.put(connectioninfo.getServerId(), _obj);
		
		System.out.println(connectioninfo.getServerId()+": Server Registered");
	}

	@Override
	public void run() 
	{
		boolean done = false;
	
		while (!done)
		{
			try 
			{
				Thread.sleep(10 *300);				
			} 
			catch (InterruptedException unexpected) 
			{
				System.out.println("error !");
				done = true;
			}
			
			this.LookUpWorkHandler();
			this.OrderWorkHandler();
		}
	}
	
	@SuppressWarnings("unchecked")
	private synchronized void LookUpWorkHandler()
	{
		synchronized(lookupqueue)
		{
			 Iterator<ServerRequestModel> iterator = lookupqueue.iterator();
			 
			 while (iterator.hasNext())
				 lookupprocessingqueue.add((ServerRequestModel)(iterator.next()));
			 lookupqueue.clear();
		}

		synchronized(lookupprocessingqueue)
		{
			
			Collections.sort(lookupprocessingqueue);
			ServerRequestModel request;
		
			Iterator<ServerRequestModel> requestiterator = lookupprocessingqueue.iterator();

			try 
			{
				while (requestiterator.hasNext())
				{
					request=requestiterator.next();				
					ResponseModel response=new ResponseModel();
					BookModel bk=new BookModel();
				
					try {
						bk=this.serverLookup(request.getItemNumber());					
					
					} catch (RemoteException e) {
						e.printStackTrace();
					}				
				
					//Prepare ResponseModel packet
					response.setResponseBook(bk);
					response.setResponseStatus(true);
					response.setResponseServiceName("Lookup");
					
					try {
						if(objectMap.get(request.getServerId())!=null)
						{
							objectMap.get(request.getServerId()).UpdateResponse(response);		
							
						}
					} catch (RemoteException e) {
						objectMap.remove(request.getServerId());
						e.printStackTrace();
					}
				}
				lookupprocessingqueue.clear();
			}  catch (RemoteException e1) {			
				e1.printStackTrace();
			}		
		}
 }

	@SuppressWarnings("unchecked")
	private synchronized void OrderWorkHandler()
	{
		synchronized(orderqueue)
		{
			 Iterator<ServerRequestModel> iterator = orderqueue.iterator();
			 
			 while (iterator.hasNext())
				 orderprocessingqueue.add((ServerRequestModel)(iterator.next()));
			 
			 orderqueue.clear();
		}
		
		synchronized(orderprocessingqueue)
		{
			Collections.sort(orderprocessingqueue);
			ServerRequestModel request;
		
			Iterator<ServerRequestModel> requestiterator = orderprocessingqueue.iterator();
		
			while (requestiterator.hasNext())
			{
				request=requestiterator.next();				
				ResponseModel response=null;
				try {
					response = new ResponseModel();
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}		
			
				try {
					response=this.serverOrder(request.getItemNumber());					
				
				} catch (RemoteException e) {
				e.printStackTrace();
				}
								
				try {
					//Send the response to the desired front end server
					if(objectMap.get(request.getServerId())!=null)
						objectMap.get(request.getServerId()).UpdateResponse(response);						
				} catch (RemoteException e) {
				// TODO Auto-generated catch block
					objectMap.remove(request.getServerId());
					e.printStackTrace();
				}
			}
			orderprocessingqueue.clear();		
		}		
	}
}


