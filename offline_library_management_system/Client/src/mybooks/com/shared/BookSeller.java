package mybooks.com.shared;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

@SuppressWarnings("serial")
public class BookSeller extends UnicastRemoteObject 
implements IBookSeller {
	/**
	 * 
	 */
	//private static final long serialVersionUID = 1L;

	//Object to hold books
	private BookList bookShelf;
	
	//Object to handle thread reader writer issue
	private ReadWriteLock lock;	
	
	//No of Search Service Request Count
	private int searchServiceCount;
	
	//No of Order Service Request Count
	private int orderServiceCount;
	
	//No of Lookup Service Request Count
	private int lookupServiceCount;
	
	//No of successful order request count
	private int successfulOderCount;
	
	//No of failed order request count
	private int failedOrderCount;
	
	//total time elapsed for search service
	private long searchServiceTime;
	
	//total time elapsed for order service
	private long orderServiceTime;
	
	//total time elapsed for lookup service
	private long lookupServiceTime;
	
	
	
	public BookSeller(BookList bookshelf) throws RemoteException 
	{
		super();
		this.bookShelf=bookshelf;
		
		this.searchServiceCount=0;
		this.lookupServiceCount=0;
		this.orderServiceCount=0;
		
		this.searchServiceTime=0;
		this.orderServiceTime=0;
		this.lookupServiceTime=0;	
		
		lock=new ReadWriteLock();
	}

	/*
	 * find the books by topic name
	 */
	@Override
	public synchronized List<BookModel> searchBook(String _topicName) throws RemoteException {
		
		long _startTime = System.nanoTime();		
		List<BookModel> _booklist=null;
		
		try {
			lock.lockReadOperation();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		try{
			
			searchServiceCount++;			
			_booklist=bookShelf.getBookByTopic(_topicName);			
		  
		} finally {
		  lock.unlockReadOperation();		
		}		
				
		long endTime = System.nanoTime();
		searchServiceTime=searchServiceTime+(endTime-_startTime);
		
		return _booklist;			
	}
	
	/*
	 * lookup book by item number
	 */
	@Override
	public synchronized BookModel Lookup(int _itemNumber) throws RemoteException {
		long startTime = System.nanoTime();	
		BookModel _book=null;
		
		try {
			lock.lockReadOperation();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		try{
			//Critical code here
			lookupServiceCount++;
			_book= bookShelf.getBookDetail(_itemNumber);		  
		} finally {
		  lock.unlockReadOperation();		
		}		
		
		long endTime = System.nanoTime();
		
		lookupServiceTime=lookupServiceTime+(endTime-startTime);
		
		return _book;
	}
	
	/*
	 * Order book by item number
	 */
	@Override
	public synchronized boolean Order(int _item) throws RemoteException {
		long startTime = System.nanoTime();	
		boolean ret=false;
		
		try {
			lock.lockWriteOperation();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		try{
			orderServiceCount++;
			ret=bookShelf.orderBook(_item);
			
			//order is placed successfully if return value is true
			if(ret==true)
				successfulOderCount++;
			//other wise order place failed
			else
				failedOrderCount++;
		  
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
		
		return ret;
	}
	
	/*
	 * find request count by service name
	 */
	@Override
	public int findRequestCountByServiceName(String service) throws RemoteException {		
		int _service = -1;
		
		try {
			lock.lockReadOperation();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		try{
			if(service.equalsIgnoreCase("Search"))
				_service =searchServiceCount;
			else if(service.equalsIgnoreCase("Lookup"))
				_service =lookupServiceCount;				
			else if(service.equalsIgnoreCase("Order"))
				_service =orderServiceCount;
				
		  
		} finally {
		  lock.unlockReadOperation();		
		}			
		
		return _service ;
	}

	/*
	 * find all successful orders so far
	 */	
	@Override
	public int ReportSuccessfulOrders() throws RemoteException {
		// TODO Auto-generated method stub
		int count=-1;
		try {
			lock.lockReadOperation();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		try{
			count=successfulOderCount;
		  
		} finally {
		  lock.unlockReadOperation();		
		}		
		return count;
	}

	
	/*
	 * find all the unsuccessful orders
	 */
	@Override
	public int findUnsuccessfulOrders() throws RemoteException {
		int count=-1;
		
		try {
			lock.lockReadOperation();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		try{
			count = failedOrderCount;
		  
		} finally {
		  lock.unlockReadOperation();		
		}
		
		return count;		
	}

	/*
	 * find overall service performance by service name
	 */
	@Override
	public float findServicePerformance(String _serviceName) throws RemoteException {
		float avgServiceTime=-1;
		
		try {
			lock.lockReadOperation();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		try{
			if(_serviceName.equalsIgnoreCase("Search"))
			{
				avgServiceTime=(searchServiceTime/searchServiceCount);				
			}
			else if(_serviceName.equalsIgnoreCase("Lookup"))
			{
				avgServiceTime=(lookupServiceTime/lookupServiceCount);
			}
			else if(_serviceName.equalsIgnoreCase("Order"))
			{
				avgServiceTime=(orderServiceTime/orderServiceCount);
			}
		  
		} finally {
		  lock.unlockReadOperation();		
		}	
		
		return avgServiceTime;
	}

}
