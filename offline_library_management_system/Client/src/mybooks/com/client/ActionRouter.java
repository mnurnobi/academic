package mybooks.com.client;

import java.rmi.RemoteException;
import java.util.List;

import mybooks.com.shared.BookModel;
import mybooks.com.shared.IBookSellerSub;
import mybooks.com.shared.ResponseModel;
import mybooks.com.shared.SharedUtility;

public class ActionRouter {
	private  IBookSellerSub iBookSellerSub;
	
	public ActionRouter(IBookSellerSub _ibooksellerSub)
	{
		this.iBookSellerSub=_ibooksellerSub;		
	}
	
	
	/*
	 * select option to trigger further.
	 */
	public void chooseRequiredAction(int _menuId) throws RemoteException
	{
		if(_menuId==1)
			searchMenu();
		
		else if(_menuId==2)
			lookupMenu();
		
		else if(_menuId==3)
			orderMenu();	
		
		else if(_menuId==4)
			getServiceReport();
		
		else if(_menuId==5)
			getGoodOrderReport();
		
		else if(_menuId==6)
			getUnsuccessfulOrderReport();
		
		else if(_menuId==7)
			getServicePerformanceReport();
	}
	
	/*
	 * to search a specific service
	 */	
	public void searchMenu() throws RemoteException
	{
		String _topicName=null;

		System.out.println("Enter Topic Name:");
		_topicName=System.console().readLine();
		
		List<BookModel> booklist=iBookSellerSub.Search(_topicName);	
		
		if(booklist==null || booklist.size()==0)
		{
			System.out.println("No book found for this topic");
		}
		
		else
		{
			System.out.println("\n\n");
		
			for(int i=0;i<booklist.size();i++)
			{
				BookModel book=booklist.get(i);			
				System.out.println("Name:"+book.getBookName()+" Item Number:"+book.getItemNumber());
				System.out.println("");
			}
		}
	}
	
	/*
	 * To order a book
	 */	
	public void orderMenu() throws RemoteException
	{		
		String _bookId=null;
		ResponseModel _response=new ResponseModel();
		int _id=0;

		System.out.println("Enter Item Number of the book for order:");
		
		_bookId=System.console().readLine();
		
		_id=SharedUtility.StringtoInt(_bookId);		

		if(_id >= 0)
		{		
			_response=iBookSellerSub.Order(_id);
			if(_response.isResponseStatus())
				System.out.println(_response.getResponseMsg());
			else
				System.out.println(_response.getResponseMsg());		
		}
	}
	
	/*
	 * Method to lookup the book
	 */	
	public void lookupMenu() throws RemoteException
	{
		String bookid=null;
		int id=0;
		
		System.out.println("Enter Item Number of the Book for Lookup:");
		
		bookid=System.console().readLine();
		
		id=SharedUtility.StringtoInt(bookid);	

		if(id >= 0)
		{
			BookModel book=iBookSellerSub.Lookup(id);
			if(book==null)
			{
				System.out.println("No book found for this id!");
			}
			else
			{
				System.out.println("\n\n");
				System.out.println("Book Name:"+book.getBookName());
				System.out.println("Topic:"+book.getBookTopic());
				System.out.println("Item Number:"+book.getItemNumber());
				System.out.println("Cost:"+book.getCost());
				System.out.println("Available in Stock:"+book.getStock());
				System.out.println("");
			}
		}
	}
	
	/*
	 * to report request count for a specific service
	 */	
	public void getServiceReport() throws RemoteException
	{
		String servicename=null;
		
		System.out.println("Enter Service Name:");
		servicename=System.console().readLine();
		
		int number=iBookSellerSub.reportRequestNumber(servicename);
		
		System.out.println("Number of Request for Service:"+servicename+" Is:"+number);
		
	}
	
	/*
	 * Print count of the successful orders
	 */	
	public void getGoodOrderReport() throws RemoteException
	{
		int number=iBookSellerSub.ReportGoodOrders();
		
		System.out.println("Number of Good Orders:"+number);
		
	}
	
	/*
	 * Print count of unsuccessful orders
	 */	
	public void getUnsuccessfulOrderReport() throws RemoteException
	{
		int _number=iBookSellerSub.ReportFailedOrders();
		
		System.out.println("Number of Bad Orders:"+_number);
	}
	
	/*
	 * to show average execution time in the form of performance
	 */	
	public void getServicePerformanceReport() throws RemoteException
	{
		String _serviceName=null;
		
		System.out.println("Enter Service Name:");
		_serviceName=System.console().readLine();
		
		float _averagetime=iBookSellerSub.ReportServicePerformance(_serviceName);
		
		System.out.println("Average Time for "+_serviceName+" Service: "+_averagetime+" nanosecond");
	}
}
