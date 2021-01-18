package mybooks.com.shared;

import java.io.Serializable;
import java.rmi.RemoteException;

@SuppressWarnings({ "serial", "rawtypes" })
public class ServerRequestModel implements Comparable, Serializable
{
	//Worker Server that make the request to main server
	private IBookSellerSub workerServer;
	//request time 
	private long requestTime;
	//book item number
	private int itemNumber;
	//book topic name
	private String topicName;
	//desired service name
	private String serviceName;
	//server id from which request is made
	private String serverId;
	
	public String getServerId() {
		return serverId;
	}

	public void setServerId(String serverId) {
		this.serverId = serverId;
	}

	public ServerRequestModel() throws RemoteException
	{
		workerServer=null;
		requestTime=0;
	}
	
	public ServerRequestModel(IBookSellerSub frontserver,long requesttime) throws RemoteException
	{
		workerServer=frontserver;
		requestTime=requesttime;
	}
	
	public IBookSellerSub getFronServer() {
		return workerServer;
	}
	public void setFronServer(IBookSellerSub fronServer) {
		this.workerServer = fronServer;
	}
	public long getRequestTime() {
		return requestTime;
	}
	public void setRequestTime(long requestTime) {
		this.requestTime = requestTime;
	}
	
	public int getItemNumber() {
		return itemNumber;
	}

	public void setItemNumber(int itemNumber) {
		this.itemNumber = itemNumber;
	}

	public String getTopiName() {
		return topicName;
	}

	public void setTopiName(String topicName) {
		this.topicName = topicName;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	

	@Override
	public int compareTo(Object comparereq) {
		
		long comparereqtime=((ServerRequestModel)comparereq).getRequestTime();
	    /* For Ascending order*/
	    return (int)(this.getRequestTime()-comparereqtime);
		
	}

}
