package mybooks.com.shared;

import java.io.Serializable;

/*
 * Entity Class to Hold Connection Info a Server
 */
@SuppressWarnings("serial")
public class ConnectionModel implements Serializable 
{
	//For serverid such as server1,server2
	private String serverId;
	//Object name associated with servers
	private String ServerObjName;
	//server host name
	private String serverHost;
	//server port
	private String serverPort;
	
	public ConnectionModel()
	{
		this.serverId="";
		this.ServerObjName="";
		this.serverHost="";
		this.serverPort="";
		
	}
	public String getServerId() {
		return serverId;
	}
	public void setServerId(String serverId) {
		this.serverId = serverId;
	}
	public String getServerObjName() {
		return ServerObjName;
	}
	public void setServerObjName(String serverObjName) {
		ServerObjName = serverObjName;
	}
	public String getServerHost() {
		return serverHost;
	}
	public void setServerHost(String serverHost) {
		this.serverHost = serverHost;
	}
	public String getServerPort() {
		return serverPort;
	}
	public void setServerPort(String serverPort) {
		this.serverPort = serverPort;
	}	
}
