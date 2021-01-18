package mybooks.com.shared;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/*
 * Entity Class to hold book properties
 * 
 */
@SuppressWarnings("serial")
@XmlRootElement(name = "book")
public class BookModel implements Serializable{
	private String bookName;
	private String bookTopic;
	private int cost;
	private int stock;
	private int itemNumber;
	private int orderCount;

	public BookModel()
	{
	}
	
	/*
	 * Constructor for BookModel Entity	 
	 */
	public BookModel(String bookname, String booktopic, int itemnumber, int cost, int stock)
	{
		this.bookName=bookname;
		this.bookTopic=booktopic;
		this.cost=cost;
		this.stock=stock;	
		this.itemNumber=itemnumber;
		this.orderCount=0;
		
	}

	/*
	 * Getter Method for BookModel Name
	 */	
	public String getBookName() {
		return bookName;
	}

	/*
	 * Setter Method for BookModel Name
	 */
	@XmlElement
	public void setBookName(String bookName) {
		this.bookName = bookName;
	}

	/*
	 * Getter Method for Topic Name
	 */
	public String getBookTopic() {
		return bookTopic;
	}

	/*
	 * Setter Method for Topic Name
	 */
	@XmlElement
	public void setBookTopic(String bookTopic) {
		this.bookTopic = bookTopic;
	}

	/*
	 * Getter Method for Cost
	 */
	public int getCost() {
		return cost;
	}

	/*
	 * setter Method for Cost
	 */
	@XmlElement
	public void setCost(int cost) {
		this.cost = cost;
	}

	/*
	 * Getter Method for Stock
	 */
	public int getStock() {
		return stock;
	}

	/*
	 * Setter Method for Stock
	 */
	@XmlElement
	public void setStock(int stock) {
		this.stock = stock;
	}
	
	public int getItemNumber() {
		return itemNumber;
	}

	@XmlElement
	public void setItemNumber(int itemNumber) {
		this.itemNumber = itemNumber;
	}
	
	public int getOrderCount() {
		return orderCount;
	}

	@XmlElement
	public void setOrderCount(int orderCount) {
		this.orderCount = orderCount;
	}
}
