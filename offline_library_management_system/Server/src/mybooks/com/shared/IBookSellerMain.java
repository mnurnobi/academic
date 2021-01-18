package mybooks.com.shared;

import java.rmi.Remote;
import java.rmi.RemoteException;

/*
 * Interface of RMI Services for BackEndServer
 */
public interface IBookSellerMain extends Remote{
	//Interface to Search
	BookList search(ServerRequestModel request) throws RemoteException;
	//Interface for Lookup
	boolean Lookup(ServerRequestModel request) throws RemoteException;
	//Interface for Order
	boolean Order(ServerRequestModel request) throws RemoteException;
	//Interface to get RequestNumber according to service parameter
	int findRequestCount(String service) throws RemoteException;
	//Interface 
	int reportSuccessfulOrders() throws RemoteException;
	int reportUnsuccessfulOrders() throws RemoteException;
	float findServicePerformance(String service) throws RemoteException;
	//Interface to register backend server
	void registerWorkerServer(ConnectionModel info) throws RemoteException;
}
