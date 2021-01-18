package mybooks.com.shared;

import java.io.Serializable;

/*
 * Entity class to hold service statistics related information
 */

@SuppressWarnings("serial")
public class ServiceModel implements Serializable {
	//Service Name
	private String serviceName;
	//Request count
	private long requestCount;
	//Processing time
	private long serviceTime;
	
	public ServiceModel()
	{
		this.serviceName="";
		this.requestCount=0;
		this.serviceTime=0;
	}
	
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public long getRequestCount() {
		return requestCount;
	}
	public void setRequestCount(long requestCount) {
		this.requestCount = requestCount;
	}
	public long getServiceTime() {
		return serviceTime;
	}
	public void setServiceTime(long serviceTime) {
		this.serviceTime = serviceTime;
	}

}
