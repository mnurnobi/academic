package mybooks.com.server;

import java.io.File;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import mybooks.com.shared.BookList;
import mybooks.com.shared.SharedUtility;

/*
 * Read books from xml file as BookList
 * 
 */
public class BookListReader {	
	
	/*
	 * BookList reader Function
	 */
	public static BookList getBookList()
	{
		BookList _bookList = null;
		JAXBContext _jaxbContext = null;
		
		//JAVA JAXB Context for BookList Class
		try {
			_jaxbContext = JAXBContext.newInstance(BookList.class);
		} catch (JAXBException e) {			
				e.printStackTrace();
		}

		Unmarshaller jaxbUnmarshaller = null;
		
		try {
			jaxbUnmarshaller = _jaxbContext.createUnmarshaller();
		} catch (JAXBException e) {					
				e.printStackTrace();
		}			     

		// Un-marshaling XML file
		try {					
			_bookList = (BookList) jaxbUnmarshaller.unmarshal( new File(Server.class.getProtectionDomain().getCodeSource().getLocation().getPath().toString()
					+ SharedUtility.BOOK_XML_PATH) );
						
			} catch (JAXBException e) {				
					e.printStackTrace();
		}			

		return _bookList;	
	}
}
