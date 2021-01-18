package mybooks.com.shared;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/*
 * Interface of RMI Services
 */
public interface IBookSeller extends Remote{
	List<BookModel> searchBook(String topikname) throws RemoteException;
	BookModel Lookup(int itemnumber) throws RemoteException;
	boolean Order(int itemnumber) throws RemoteException;
	int findRequestCountByServiceName(String service) throws RemoteException;
	int ReportSuccessfulOrders() throws RemoteException;
	int findUnsuccessfulOrders() throws RemoteException;
	float findServicePerformance(String service) throws RemoteException;
}
