package mybooks.com.shared;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ITimeSyncOperator extends Remote 
{
	/*
	 * reading current time of the server
	 */
	long getCurrentTime() throws RemoteException;
	/*
	 * setting time offset
	 */
	void pushTimeOffset(long offset) throws RemoteException;
}
