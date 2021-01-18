package mybooks.com.shared;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/*
 * Interface of RMI Services for FronEnd Servers
 */
public interface IBookSellerSub extends Remote{
	List<BookModel> Search(String topikname) throws RemoteException;
	BookModel Lookup(int itemnumber) throws RemoteException;
	ResponseModel Order(int itemnumber) throws RemoteException;
	int reportRequestNumber(String service) throws RemoteException;
	int ReportGoodOrders() throws RemoteException;
	int ReportFailedOrders() throws RemoteException;
	float ReportServicePerformance(String service) throws RemoteException;
	void UpdateResponse(ResponseModel response) throws RemoteException;	
	List<ServiceModel> GetServiceRequestInfo() throws RemoteException;
}
