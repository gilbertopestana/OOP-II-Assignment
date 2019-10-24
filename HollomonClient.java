package assignment5;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.net.ssl.SSLSocketFactory;

public class HollomonClient {
	private String server;
	private int port;
	Socket socket;
	BufferedWriter writer;
	BufferedReader reader;
	CardInputStream cardcreator;

	/*
	 *Create a new Hollomon Client Object it sets a server's name and its port 
	 */
	public HollomonClient(String server, int port) {
		this.server = server;
		this.port = port;
	}
	
	/*
	 * Needed to perform any further interaction with the server. it takes a username and a password as arguments
	 */
	public List<Card> login(String username, String password) {
		try {
			socket = SSLSocketFactory.getDefault().createSocket(this.server, this.port);
			writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			Card card = null;
			List<Card> list = new ArrayList<Card>();
			// write log in details in the bufferedwriter
			writer.write(username);
			writer.newLine();     
			writer.write(password);   
			writer.newLine();
			writer.flush(); // send log in details to the server
			String recievedMessage;   
			
			if ((recievedMessage = reader.readLine()) != null) { //keeps reading lines until there is nothing else
                System.out.println(recievedMessage);
                if (!recievedMessage.equals("User "+username+" logged in successfully.")) { //if the first message received from the server is not the successful message will return null
                    return null;
                }
            }
            cardcreator = new CardInputStream(socket.getInputStream());
            while ((card = cardcreator.readCard()) !=null) { //while we still get cards from Card.InputStream.readCard() the while loop will keep running
                if (card != null) {
                    list.add(card); //add the card to they list (which is going to be returned by the log in method)
                }
            }
            list.sort(null); //sorts the list of cards using the override compareTo method inside Card.java
            return list;
		} catch (UnknownHostException e) {
			e.printStackTrace();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null; //if any error happens while trying to log in we will return null

	}
	
	/*
	 * ask to the server and prints out the credits left
	 * server replies in the following way (CREDITS,newLine,"OK",newLine)
	 */
	public long getCredits() {
        String str; 
        Long credits = null;
        try {
        	// writes the command to get your credits
			writer.write("CREDITS");
	        writer.newLine();
	        writer.flush(); //send the command to the server
			while ((str = reader.readLine()) != null) { //while there are lines to read it will keep reading lines
			    if (str.equals("OK")){ //if the line is equal to "OK" it breaks
			        break;
			    }else
			    credits = Long.parseLong(str); //convert the message (credits) from the server and converts it into long
			}
		} catch (NumberFormatException | IOException e) { //if it fails reading or converting the string to a long number, it will print and error message
			System.out.println("the connection was lost");
		}
        return credits;
	}
	/*
	 * ask to the server and return the cards that the logged user has
	 * server replies sending ("CARD",newline,ID,newline,NAME,newline,RANK,newline,PRICE) for each card and ("OK",newline) once finished
	 */
	public List<Card> getCards() {
        List<Card> list = new ArrayList<Card>();
        Card card = null;
        try {
        	// prepare the commands to be send to the server
			writer.write("CARDS");
	        writer.newLine();
	        writer.flush(); //sends the commands to the server
			while ((card = cardcreator.readCard()) !=null) { //while we still get cards from Card.InputStream.readCard() the while loop will keep running
                if (card != null) {
                    list.add(card); //add the card to they list (which is going to be returned by the log in method)
                }
            }
		} catch (NumberFormatException | IOException e) { //if it fails reading or converting the string to a long number, it will print and error message
			System.out.println("the connection was lost");
		}
		list.sort(null); //sorts the list of cards using the override compareTo method inside Card.java
        return list;
	}
	
	/*
	 * ask to the server and return the offered cards in the market
	 * server replies sending ("CARD",newline,ID,newline,NAME,newline,RANK,newline,PRICE) for each card and ("OK",newline) once there are no more cards left
	 */
	public List<Card> getOffers() {
        List<Card> list = new ArrayList<Card>();
        Card card = null;
        try {
        	// prepare the commands to be send to the server
			writer.write("OFFERS");
	        writer.newLine();
	        writer.flush(); //sends the commands to the server
			while ((card = cardcreator.readCard()) !=null) { //while we still get cards from Card.InputStream.readCard() the while loop will keep running
                if (card != null) {
                    list.add(card); //add the card to they list (which is going to be returned by the log in method)
                }
            }
		} catch (IOException e) { //if it fails reading the string, it will print and error message
			System.out.println("the connection was lost");
		}
		list.sort(null); //sorts the list of cards using the override compareTo method inside Card.java
        return list;
	}
	
	/*
	 * tells the server to put one card on sell (it will appear in the market)
	 * server replies sending: "OK" if everything went just fine or "ERROR" in case an error happened
	 */
	public boolean sellCard(Card card,long price) {
        String str; 
        try {
        	// prepare the commands to be send to the server
			writer.write("SELL "+card.id+" "+price);
	        writer.newLine();
	        writer.flush(); //sends the commands to the server
			while ((str = reader.readLine()) != null) { //while we still get information from the BufferedReader the while loop will keep running
			    if (str.equals("OK")){
			        return true;
			    }else if (str.equals("ERROR")){
                    return false;
                }
			}
		} catch (IOException e) { //if it fails reading the string, it will print and error message
			System.out.println("the connection was lost");
		}
        return false;
	}
	
	/*
	 * tells the server to buy one of the cards in the market
	 * server replies sending: "OK" if everything went just fine or "ERROR" in case an error happened
	 */
	public boolean buyCard(Card card) {
        String str; 
        try {
            if (!(getCredits()>=card.price)){ //checks if we have enough credits to buy the selected card
                return false;
            }
            // prepare the commands to be send to the server
			writer.write("BUY "+card.id);
	        writer.newLine();
	        writer.flush(); //sends the commands to the server
			while ((str = reader.readLine()) != null) { //while we still get information from the BufferedReader the while loop will keep running
			    if (str.equals("OK")){
			        return true;
			    }else if (str.equals("ERROR")){
                    return false;
                }
			}
		} catch (IOException e) { //if it fails reading, it will print and error message
			System.out.println("the connection was lost");
		}
        return false;
	}
	 
	/*
	 * it closes the reader, writer and socket
	 */
	 public void close() {
		 try {
			socket.close();
			writer.close();
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	 }
}