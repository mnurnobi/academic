package mybooks.com.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/*
 * book object
 */
@XmlRootElement(name = "bookshelf")
public class BookList implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@XmlElement(name = "book")
	private List<BookModel> bookList=null;
	
	/*
	 * Constructor
	 */
	public BookList()
	{
		bookList=new ArrayList<BookModel>();
	}
	
	/*
	 * Get the List of Books
	 */
	public List<BookModel> getBooks() {
        return bookList;
    }
	
	/*
	 * Add BookModel to the list
	 */
	public void addBookToList(BookModel _book)
	{
		bookList.add(_book);		
	}
	
	/*
	 * Remove book from the list
	 */
	public boolean removeBookFromList(String bookname)
	{
		boolean _ret=false;
		
		Iterator<BookModel> _iterator = bookList.iterator();
		
		while (_iterator.hasNext()) {
		    if (_iterator.next().getBookName().equalsIgnoreCase(bookname))
		    {
		        _iterator.remove();
		        _ret=true;
		    }
		}	
		
		return _ret;
		
	}
	
	/*
	 * Get Book By different Topic
	 */
	public List<BookModel> getBookByTopic(String topic)
	{
		List<BookModel> _bookList=new ArrayList<BookModel>();
		Iterator<BookModel> _iterator = bookList.iterator();
		
		while (_iterator.hasNext())
		{
			BookModel book=_iterator.next();
			
		    if (book.getBookTopic().equalsIgnoreCase(topic))
		    {	      
		    	_bookList.add(book);		        
		    }
		}	
		
		return _bookList;		
	}
	
	/*
	 * Get Book information by item id
	 */	
	public BookModel getBookDetail(int _itemId)
	{
		
		boolean _flag=false;
		BookModel _book=null;
		
		Iterator<BookModel> _iterator = bookList.iterator();
		
		while (_iterator.hasNext())
		{
			_book=_iterator.next();
			
		    if (_book.getItemNumber()==_itemId)
		    {	
		    	_flag=true;
		    	break;
		    }
		}	
		
		if(_flag==false)
			_book=null;
		
		return _book;
	}
	
	
	/*
	 * Order books until books are available
	 */
	public boolean orderBook(int _itemNumber)
	{
		boolean _flag=false;
		boolean _ret=false;
		BookModel _book=null;
		System.out.println("book list order book");
		
		Iterator<BookModel> _iterator = bookList.iterator();
		
		while (_iterator.hasNext())
		{
			_book=_iterator.next();
			
		    if (_book.getItemNumber()==_itemNumber)
		    {	
		    	//book found
		    	_flag=true;
		    	break;
		    }
		}
		
		if(_flag==false)
			_book=null;
		
		//check availability of the stock
		else
		{
			int _stock=_book.getStock();		
			
			if(_stock>0)
			{
				//Stock count decreases by one
				_book.setStock(_stock-1);
				//Order count increases by one
				_book.setOrderCount(_book.getOrderCount()+1);
				_ret=true;
			}
		}		
		
		return _ret;		
	}
}
