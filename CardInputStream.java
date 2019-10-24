package assignment5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;

public class CardInputStream extends InputStream{
	BufferedReader reader;
	
	// sets a new bufferedReader
	public CardInputStream(InputStream input) {
		this.reader = new BufferedReader(new InputStreamReader(input));
	}
	
	// it closes the reader (bufferedReader)
	public void close() {
		try {
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 *this method uses the bufferedReader to load Cards from the server
	 * the cards always come in the following format ("CARD",newline,ID,newline,NAME,newline,RANK,newline,PRICE)
	 * the method uses that information to create a Card using the Card constructor and eventually returns it
	 */
    public Card readCard(){
        String str;
        Long id=null;
        String name=null;
        Rank rank=null;
        Long price = null;
        try {
			while ((str=reader.readLine()) != null){ //we start reading every line sent by the server
			    if (str.equals("CARD")){  //if the server sends the word "CARD" the next 4 lines will contain the information to create a new card
			        if((str=reader.readLine())!=null){ //the first line after "CARD" is the card's ID
			        	id = Long.parseLong(str);
			        }
			        if((str=reader.readLine())!=null){ //the second line is the card's name
			            name = str;
			        }
			        if((str=reader.readLine())!=null){ //the third line is the card's Rank
			            rank = Rank.valueOf(str.toUpperCase());
			        }
			        if((str=reader.readLine())!=null){ //the last line is the card's price
			        	price = Long.parseLong(str);
			        }
			        Card card = new Card(id,name,rank); //creates a new card with the information acquired 
			        card.price = price; //sets the price as we dont have a constructor that takes also price as argument
			        return card;
			    }else if (str.equals("OK")) // "OK" is used by the server to specify when a job has been done,thus in case we get "OK" the method returns null
			        return null;
			}
		} catch (NumberFormatException | IOException e) {
			System.out.println("an error inside CARDREADER() happened");
			return null;
		}
		return null;
	}
	
    /*
     * it reads the entire information held in the bufferedReader, stores it in a String variable and ultimately return the String
     */
	public String readResponse() throws IOException {
		String fullMessage = "";
		String receivedMessage;
		while ((receivedMessage = reader.readLine()) != null) {       
            fullMessage += receivedMessage;
		}
		return fullMessage;
	}
	
	@Override
	public int read() throws IOException {
		return 0;
	}

}
