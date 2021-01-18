package mybooks.com.shared;

/*
 * Entity Class for Server Info
 */

public class ServerModel {

	//Server Name
	private String serverName;
	//Server Ip/Host
	private String serverIp;
	//Server Time Object
	private String timeServerObj;
	//server Service Object
	private String serviceServerObj;
	
	public String getTimeServerObj() {
		return timeServerObj;
	}

	public void setTimeServerObj(String timeServerObj) {
		this.timeServerObj = timeServerObj;
	}

	public String getServiceServerObj() {
		return serviceServerObj;
	}

	public void setServiceServerObj(String serviceServerObj) {
		this.serviceServerObj = serviceServerObj;
	}

	public boolean isLeaderServer() {
		return leaderServer;
	}

	public void setLeaderServer(boolean leaderServer) {
		this.leaderServer = leaderServer;
	}
	private boolean leaderServer;
	
	public ServerModel()
	{
		this.serverName=null;
		this.serverIp=null;
		this.leaderServer=false;
	}
	
	public String getServerName() {
		return serverName;
	}
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	public String getServerIp() {
		return serverIp;
	}
	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}
	public boolean getServerType() {
		return leaderServer;
	}
	public void setServerType(boolean serverType) {
		this.leaderServer = serverType;
	}	
	
}
