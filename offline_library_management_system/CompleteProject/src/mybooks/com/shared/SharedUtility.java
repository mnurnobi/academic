package mybooks.com.shared;

import java.util.Random;

/*
 * Class to convert differnt types such as String to Int
 */
public class SharedUtility {
	//path to access configuration
	public static String PROP_ASSET_PATH = "//mybooks//com//asset//config.properties";
	
	//path to access book xml (database)
	public static String BOOK_XML_PATH = "//mybooks//com//asset//Books.xml";
	
	//String to Int Conversion utility func
	public static int StringtoInt(String stringvalue)
	{
		int value=0;
		
		try {
			value=Integer.parseInt(stringvalue);
  		} catch (NumberFormatException e) {
  		    System.out.println("Wrong number");
  		    value = -9999;
  		}
		
		return value;
	}

    // generate random number
	public static int randInt(int min, int max) {
	    Random rand=new Random();

	    int randomNum = rand.nextInt((max - min) + 1) + min;

	    return randomNum;
	}
}
