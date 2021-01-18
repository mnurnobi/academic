package mybooks.com.shared;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;


/*
 * Class to load properties value from config file
 */
public class PropertyUtil {
	/*
	 * Loading property values
	 */
	public static String GetPropertyValue(String key)
	{
		Properties prop = new Properties();
    	InputStream input = null;
    	String propValue=null;
    	
    	try {
            		
    		File path=new File(PropertyUtil.class.getProtectionDomain().getCodeSource().getLocation().getPath().toString()
    				+ SharedUtility.PROP_ASSET_PATH);
    		input = new FileInputStream(path);
    		
    		//load a properties file from class path
    		if(null != input) {
	    		prop.load(input);
	    		propValue=prop.getProperty(key);
    		}
    	} catch (IOException ex) {
    		ex.printStackTrace();
        } finally{
        	if(input!=null){
        		try {
        			input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
        	}
        }

    	return propValue;			
	}
	
	/*
	 * Validate worker servers
	 */
	@SuppressWarnings("unused")
	public static boolean IsValidWorkerServer(String _serverName)
	{
		Properties prop = new Properties();
    	InputStream input = null;
    	String propValue=null;
    	String delims=",";
    	boolean ret=false;
    	
    	try {
            		
    		File path=new File(PropertyUtil.class.getProtectionDomain().getCodeSource().getLocation().getPath().toString()
    				+ SharedUtility.PROP_ASSET_PATH);
    		input = new FileInputStream(path);
    		
    		if(input==null){
    	            System.out.println("Sorry, unable to find property file" + path.toString());
    		    return false;
    		}

    		//loading property file from class path
    		prop.load(input);
 
            //get the property value and print it out
    		propValue=prop.getProperty("workerservers");
    		
    		//tokenize the server names
    		StringTokenizer st = new StringTokenizer(propValue, delims);
    		
    		//Check it matches with any of them in the list
    		while (st.hasMoreElements()) {
    			if(st.nextElement().equals(_serverName))
    			{
    				ret=true;
    				break;
    			}
    		}
    	} catch (IOException ex) {
    		ex.printStackTrace();
        } finally{
        	if(input!=null){
        		try {
        			input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
        	}
        }
    	
    	return ret;
	}
	
	/*
	 * Get the worker server IP /host name given server name such as server1, server2 etc
	 */
	@SuppressWarnings("unused")
	public static String getWorkerServerIP(String _serverName)
	{
		Properties prop = new Properties();
    	InputStream input = null;    	
    	String propname=null;
    	String propip=null;
    	try {
            		
    		File path=new File(PropertyUtil.class.getProtectionDomain().getCodeSource().getLocation().getPath().toString()
    				+ SharedUtility.PROP_ASSET_PATH);
    		input = new FileInputStream(path);
    		
    		if(input==null){
    	        System.out.println("Sorry, unable to find property file" + path.toString());
    		    return null;
    		}

    		//load a properties file from class path, inside static method
    		prop.load(input);
    		
    		//construct property format of ip/host
    		propname="worker_"+_serverName+"_ip";
 
            //get the property value
    		propip=prop.getProperty(propname);
    	} catch (IOException ex) {
    		ex.printStackTrace();
        } finally{
        	if(input!=null){
        		try {
        			input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
        	}
        }
    	
    	return propip;
	}
	
	/*
	 * find worker server's object name such as server1, server2 etc
	 */
	@SuppressWarnings("unused")
	public static String getWorkerServerTimeObj(String srvname)
	{
		Properties prop = new Properties();
    	InputStream input = null;   	
    	
    	String propname=null;
    	String propobj=null;
    	String timeobj=null;
    	
    	try {
            		
    		File path=new File(PropertyUtil.class.getProtectionDomain().getCodeSource().getLocation().getPath().toString()
    				+ SharedUtility.PROP_ASSET_PATH);
    		input = new FileInputStream(path);
    		
    		if(input==null){
    	            System.out.println("Sorry, unable to find property file" + path.toString());
    		    return null;
    		}

    		//load a properties file from class path, inside static method
    		prop.load(input);
    		
    		//get the obj name
    		propname="worker_"+srvname+"_obj";
 
            //get the property value and print it out
    		propobj=prop.getProperty(propname);
    		
    		//concat generic obj name with timeobj
    		timeobj=propobj+"timeobj";
    	       
 
    	} catch (IOException ex) {
    		ex.printStackTrace();
        } finally{
        	if(input!=null){
        		try {
        			input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
        	}
        }
    	
    	return timeobj;		
	}
	
	@SuppressWarnings("unused")
	public static String getWorkerServerServiceObj(String _serverName)
	{
		Properties prop = new Properties();
    	InputStream input = null;   	
    	
    	String propname=null;
    	String propobj=null;
    	String serviceobj=null;
    	
    	try {
            		
    		File path=new File(PropertyUtil.class.getProtectionDomain().getCodeSource().getLocation().getPath().toString()
    				+ SharedUtility.PROP_ASSET_PATH);
    		input = new FileInputStream(path);
    		
    		if(input==null){
    	            System.out.println("Sorry, unable to find property file" + path.toString());
    		    return null;
    		}

    		//load a properties file from class path, inside static method
    		prop.load(input);
    		
    		propname = "worker_"+_serverName+"_obj";
 
            //get the property value and print it out
    		propobj=prop.getProperty(propname);
    		
    		//concat generic obj name with serviceobj
    		serviceobj=propobj + "serviceobj";
    	       
 
    	} catch (IOException ex) {
    		ex.printStackTrace();
        } finally{
        	if(input!=null){
        		try {
        			input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
        	}
        }
    	
    	return serviceobj;
	}
	
	/*
	 * Get List of all worker servers
	 */
	@SuppressWarnings("unused")
	public static List<ServerModel> getAllWorkerServerInfo()
	{
		Properties prop = new Properties();
    	InputStream input = null;    	
    	String delims=",";
    	String srvnames=null;
    	String leadersrv=null;
    	List<ServerModel> serverlist=null;
    	
    	try {
            		
    		File path=new File(PropertyUtil.class.getProtectionDomain().getCodeSource().getLocation().getPath().toString()
    				+ SharedUtility.PROP_ASSET_PATH);
    		input = new FileInputStream(path);
    		
    		if(input==null){
    	            System.out.println("Sorry, unable to find property file" + input.toString());
    		    return null;
    		}

    		//load a properties file from class path, inside static method
    		prop.load(input);  		
    		
 
            //get the property value and print it out
    		srvnames=prop.getProperty("workerservers");    		                           
    		leadersrv=prop.getProperty("worker_main_server_ip");   		
    		serverlist=new ArrayList<ServerModel>();
    		
    		StringTokenizer st = new StringTokenizer(srvnames, delims);
    		
    		while (st.hasMoreElements()) 
    		{
    			//System.out.println(st.nextElement());
    			String servertxt=st.nextElement().toString();
    			
    			String ipprop="worker_"+servertxt+"_ip";
    			String objprop="worker_"+servertxt+"_obj";
    			    			 			
    			String ippropval=prop.getProperty(ipprop);
    			//String objpropval=prop.getProperty(objprop);
    			
    			ServerModel serverinfo=new ServerModel();
    			//add ip to serverinfo
    			serverinfo.setServerIp(ippropval);
    			//add server name to serverinfo
    			serverinfo.setServerName(servertxt);
    			//add timeobj name to serverinfo
    			serverinfo.setTimeServerObj(getWorkerServerTimeObj(servertxt));
    			//add serviceobj name to serverinfo
    			serverinfo.setServiceServerObj(getWorkerServerServiceObj(servertxt));    			
    			
    			//if it is a leader server make the property true.
    			if(ipprop.equals(leadersrv))
    				serverinfo.setServerType(true);
    			
    			serverlist.add(serverinfo);
    		}  		
    	} catch (IOException ex) {
    		ex.printStackTrace();
        } finally{
        	if(input!=null){
        		try {
        			input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
        	}
        }
    	
    	return serverlist;
	}
	
	/*
	 * Determine whether the server should have main server capability given serverid
	 */
	@SuppressWarnings("unused")
	public static boolean isMainServer(String strId)
	{
		Properties prop = new Properties();
    	InputStream input = null;    	
    	
    	String _workerMainServer=null;
    	boolean ret=false;
    	
    	try {
            		
    		File path=new File(PropertyUtil.class.getProtectionDomain().getCodeSource().getLocation().getPath().toString()
    				+ SharedUtility.PROP_ASSET_PATH);
    		input = new FileInputStream(path);
    		
    		if(input==null){
    	            System.out.println("Sorry, unable to find property file" + input.toString());
    		    return false;
    		}

    		//load a properties file from class path, inside static method
    		prop.load(input);  		
    		
 
            //get the property value and print it out    				                           
    		_workerMainServer=prop.getProperty("worker_main_server_ip");   
    		String serverip="worker_"+strId+"_ip";
    		
    		//yes this a leader server
    		if(_workerMainServer.equals(serverip))
    		{
    			//System.out.println(_workerMainServer + "+++++++MAIN SERVER++++++" + serverip);
    			ret=true;
    		}
    	} catch (IOException ex) {
    		ex.printStackTrace();
        } finally{
        	if(input!=null){
        		try {
        			input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
        	}
        }
    	
    	return ret;
	}
}
