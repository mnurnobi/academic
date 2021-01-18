package mybooks.com.shared;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

/*
 * Entity class for server response
 */

@SuppressWarnings("serial")
public class ResponseModel implements Serializable
{
	//ResponseModel Failed/Passed
	private boolean responseStatus;
	//ResponseModel Msg
	private String responseMsg;
	//ResponseModel BookModel
	private BookModel responseBook;
	//ResponseModel Order Count
	private int responseOrderCount;
	//ResponseModel for responsetime
	private float responseTime;
	//ResponseModel for Service Name
	private String responseServiceName;

	public String getResponseServiceName() {
		return responseServiceName;
	}

	public void setResponseServiceName(String responseServiceName) {
		this.responseServiceName = responseServiceName;
	}

	private List<BookModel> responseListBook=new ArrayList<BookModel>();
	
	public ResponseModel() throws RemoteException
	{
		responseStatus=false;
		responseMsg="";
	}
	
	public boolean isResponseStatus() {
		return responseStatus;
	}

	public void setResponseStatus(boolean responseStatus) {
		this.responseStatus = responseStatus;
	}

	public String getResponseMsg() {
		return responseMsg;
	}

	public void setResponseMsg(String responseMsg) {
		this.responseMsg = responseMsg;
	}
	
	public BookModel getResponseBook() {
		return responseBook;
	}

	public void setResponseBook(BookModel responseBook) {
		this.responseBook = responseBook;
	}

	public List<BookModel> getResponseListBook() {
		return responseListBook;
	}

	public void setResponseListBook(List<BookModel> responseListBook) {
		this.responseListBook = responseListBook;
	}
	
	public int getResponseOrderCount() {
		return responseOrderCount;
	}

	public void setResponseOrderCount(int responseOrderCount) {
		this.responseOrderCount = responseOrderCount;
	}

	public float getResponseTime() {
		return responseTime;
	}

	public void setResponseTime(float responseTime) {
		this.responseTime = responseTime;
	}

}
