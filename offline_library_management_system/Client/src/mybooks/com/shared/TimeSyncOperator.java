package mybooks.com.shared;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

@SuppressWarnings("serial")
public class TimeSyncOperator extends UnicastRemoteObject implements ITimeSyncOperator {
	
	private long offset;

	public TimeSyncOperator() throws RemoteException {
		super();
		offset=0;
	}

	@Override
	public long getCurrentTime() throws RemoteException
	{
		long time=System.nanoTime()+offset;
		
		return time;
	}

	@Override
	public void pushTimeOffset(long offset) throws RemoteException 
	{
		this.offset=offset;		
	}

}
