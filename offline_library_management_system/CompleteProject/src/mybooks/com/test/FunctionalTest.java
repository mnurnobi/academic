package mybooks.com.test;

import java.rmi.RemoteException;
import java.util.List;

import mybooks.com.shared.BookModel;
import mybooks.com.shared.IBookSellerSub;

public class FunctionalTest {

	private IBookSellerSub subBookSeller;      
	
	public FunctionalTest(IBookSellerSub addserverintf)
	{
		this.subBookSeller=addserverintf;
	}
	
	public void ExecuteFunctionalTets() throws RemoteException
	{
		TestStock();		
		TestOrder();		
		TestBookTopic();		
		TestLookup();		
		TestReportRequestNumber();		
		TestReportGoodOrders();		
		TestReportFailedOrders();		
		TestReportServicePerformance();
	}
	
	public void TestStock() throws RemoteException
	{
		
		//Test#1: Check whether after ordering book stockcount decreases by one
		int stockcount;
		
		BookModel book=subBookSeller.Lookup(1);
		
		stockcount=book.getStock();
		
		if(subBookSeller.Order(1).isResponseStatus())
			stockcount=stockcount-1;	
		
		book=subBookSeller.Lookup(1);
		
		if(stockcount==book.getStock())
			System.out.println("TestStock Test#1 Successful");
		else
			System.out.println("TestStock Test#1 Failed");
		//End Test#1:
		
		
		//Test#2: Check whether stockcount goes negative
		for(int i=0;i<205;i++)
			subBookSeller.Order(1);
		
		book=subBookSeller.Lookup(1);
		
		if(book.getStock()==0)
			System.out.println("TestStock Test#2 Successful");
		else
			System.out.println("TestStock Test#2 Failed");
		//End Test#2		
	}
	
	public void TestOrder() throws RemoteException
	{
		//Test#1: Check After successful order GoodOrder Count increases and Number of order also increases
		BookModel book1=subBookSeller.Lookup(2);
		int odercount=subBookSeller.ReportGoodOrders();		
		if(subBookSeller.Order(2).isResponseStatus())
		{
			BookModel book2=subBookSeller.Lookup(2);
			if((subBookSeller.ReportGoodOrders()==odercount+1) && (book1.getOrderCount()+1==book2.getOrderCount()))
			{
				System.out.println("TestOder Test#1 Successful");
			}
			else
			{
				System.out.println("TestOrder Test#1 Failed");
			}
		}
		//End Test#1
		
		//Test#2: Check if Order fails number of Failed Order Increases
		int badorder=subBookSeller.ReportFailedOrders();
		if(subBookSeller.Order(1).isResponseStatus()==false)
		{
			if(subBookSeller.ReportFailedOrders()==badorder+1)
			{
				System.out.println("TestOrder Test#2 Successful");
			}
			else
			{
				System.out.println("TestOrder Test#2 Failed");
			}
			
		}
		//End Test#2	
		
		//Test3: Oder with invalid book id
		 if(subBookSeller.Order(9999).isResponseStatus()==false)
			 System.out.println("TestOrder Test#3 Successful");
		 else
			 System.out.println("TestOrder Test#3 Failed");
		 //End Test3
		
	}
	
	public void TestBookTopic() throws RemoteException
	{
		
		//Test#1: Check Topic name is same if we get book topic with Search API and Lookup API
		boolean flag=false;		
		List<BookModel> booklist=subBookSeller.Search("distributed systems");
		
		int i=0;
		while(i<booklist.size())
		{
			BookModel book1=booklist.get(i);
			BookModel book2=subBookSeller.Lookup(book1.getItemNumber());
			
			if(book1.getBookTopic().equals(book2.getBookTopic()))
				flag=true;
			else
			{
				flag=false;
				break;
			}
			
			i++;
		}
		
		if(flag==true)
			System.out.println("TestBookTopic Test#1 Successful");
		else
			System.out.println("TestBookTopic Test#1 Failed");
		//End Test#1
		
		//Test2: Test Invalid Topic Name Search
		booklist=subBookSeller.Search("unknown topic");
		
		i=0;
		flag=true;
		while(i<booklist.size())
		{
			flag=false;			
		}
		
		if(flag==true)
			System.out.println("TestBookTopic Test#2 Successful");
		else
			System.out.println("TestBookTopic Test#2 Failed");
		
		//End Test2		
	}
	
	public void TestLookup() throws RemoteException
	{
		//Test#1: Test with invalid book id;
		BookModel book=subBookSeller.Lookup(-999);		
		if(book==null)
			System.out.println("TestLookup Test#1 Successful");
		else
			System.out.println("TestLookup Test#1 Failed");
		//End Test#1
		
		//Test#2: Test with valid book id
		book=subBookSeller.Lookup(1);		
		if(book.getItemNumber()==1)
			System.out.println("TestLookup Test#2 Successful");
		else
			System.out.println("TestLookup Test#2 Failed");		
	}	
	
	public void TestReportRequestNumber() throws RemoteException
	{
		//Test#1: Check ReportRequestNumber API with invalid service name
		int count=subBookSeller.reportRequestNumber("unknown service");		
		if(count==0)
			System.out.println("TestReportRequestNumber Test#1 Successful");
		else
			System.out.println("TestReportRequestNumber Test#1 Failed");	
		//End Test#1
		
		//Test#2: Check that for Service Request ReportRequestNumber service count increases
		count=subBookSeller.reportRequestNumber("search");
		List<BookModel> booklist=subBookSeller.Search("distributed systems");
		
		if((booklist.size()>0) && (count+1==subBookSeller.reportRequestNumber("search")))
			System.out.println("TestReportRequestNumber Test#2 Successful");
		else
			System.out.println("TestReportRequestNumber Test#2 Failed");	
		//End Test2				
	}
	
	public void TestReportGoodOrders() throws RemoteException
	{
		//Test1: Check if order placed succussefully then ReportGoodOrders increases by one
		int ordercount=subBookSeller.ReportGoodOrders();
		
		if(subBookSeller.Order(2).isResponseStatus())
		{
			if(ordercount+1==subBookSeller.ReportGoodOrders())
				System.out.println("TestReportGoodOrders Test#1 Successful");
			else
				System.out.println("TestReportGoodOrders Test#1 Failed");	
		}
		else
		{
			if(ordercount==subBookSeller.ReportGoodOrders())
				System.out.println("TestReportGoodOrders Test#1 Successful");
			else
				System.out.println("TestReportGoodOrders Test#1 Failed");					
		}
		//End Test#1		
	}
	
	
	public void TestReportFailedOrders() throws RemoteException
	{
	    //Test#1: Check for failed order ReportFailedOrders returns proper value;
		for(int i=0;i<100;i++)
			subBookSeller.Order(1);
		
		int ordercount=subBookSeller.ReportFailedOrders();
		
		subBookSeller.Order(1);
		
		if(ordercount+1==subBookSeller.ReportFailedOrders())
			System.out.println("TestReportFailedOrders Test#1 Successful");
		else
			System.out.println("TestReportFailedOrders Test#1 Failed");	
		//End Test#1		
	}
	
	public void TestReportServicePerformance() throws RemoteException
	{
		//Test#1: Check with invalid Service name
		float servicetime=subBookSeller.ReportServicePerformance("unknown service performance");
		
		if(servicetime==0)
			System.out.println("TestReportServicePerformance Test#1 Successful");
		else
			System.out.println("TestReportServicePerformance Test#1 Failed");
		//End Test#1
		
		//Test2: Check ReportServicePerformance API with proper service name
		servicetime=subBookSeller.ReportServicePerformance("search");	
				
		if(servicetime>0)
			System.out.println("TestReportServicePerformance Test#2 Successful");
		else
			System.out.println("TestReportServicePerformance Test#2 Failed");		
		//End Test#2		
	}
	
}
