package FBS;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import FBS.FlightDetails;
public class BookingSystem {
	static Scanner input = new Scanner(System.in);
        static List<FlightDetails> l = new ArrayList<FlightDetails>();
        static void addflight() {
        	FlightDetails f =new FlightDetails();
        	l.add(f);
        }
        static FlightDetails flightNo(int flightNo) {
        	FlightDetails f;
        
	 		try {
	 			f=l.get(flightNo-101);
	 		}
	 		catch(Exception e) {
	 			return null;
	 		}
	 		
	 		return f;
        	
        }
        static FlightDetails flightNo() {
        	FlightDetails f;
        
        	int flightNo;
        	System.out.println("Enter the flight no");
	 		flightNo=input.nextInt();
	 		try {
	 			f=l.get(flightNo-101);
	 		}
	 		catch(Exception e) {
	 			return null;
	 		}
	 		
	 		return f;
        	
        }
        static List<Integer> availableFlight(){
        	List<Integer> availflight = new ArrayList<Integer>();
        	FlightDetails f;
        	for(int i=0;i<l.size();i++) {
        		f=l.get(i);
        		availflight.add(f.getFlightNo());
        		
        	}
        	return availflight;
            
        } 
        static void bookingDetail(int bId) {
        	boolean flag;
        	for(FlightDetails f: l) {
        		flag=f.displayBookingSummary(bId);
        		if(flag)
        			return;
        	}
        	System.out.println("invalid booking id");
        	
        	
        }
        
        public static Connection connectToDb() {
    		try {

    			Class.forName("oracle.jdbc.driver.OracleDriver");

    		} catch (ClassNotFoundException e) {
    			e.printStackTrace();
    		}

    		Connection connection = null;

    		try {

    			connection = DriverManager.getConnection(
    					"jdbc:oracle:thin:@localhost:1521:" + "XE","SYSTEM",
    					"rec123");
    			return connection;

    		} catch (SQLException e) {

    			e.printStackTrace();
    			return null;
    		}
    	}
	public static void updateOld(Connection con) {
		Statement stmt = null;
		FlightDetails f;
		int tempId;
		
	    String query ="SELECT * from booking";
	 
	    try {
	    	stmt = con.createStatement();
	        ResultSet rs = stmt.executeQuery(query);
	        while (rs.next()) {
	      
	        	tempId = rs.getInt("fNo")-101;
	        	try{f=l.get(tempId);}
    			catch(Exception e) {
    				addflight();
    				f=l.get(tempId);
    			}
    				
	        	String s1= rs.getString("seats");
  			  s1 = s1.replace("[","");
  			 s1 = s1.replace("]","");
  			 s1 = s1.replaceAll(" ","");
  			 List<String> myList = new ArrayList<String>(Arrays.asList(s1.split(",")));
  			List<Integer> seats = new ArrayList<Integer>(Arrays.asList());
  			for(String s : myList) {
  				seats.add(Integer.parseInt(s));
  			}
  			//System.out.println(seats+" "+rs.getDouble("cost")+" "+(rs.getInt("meal")==1 ? true:false)+" "+(seats.get(0)<6)+" "+(rs.getInt("status")==1 ? true:false));
  			f.makeBooking(seats,rs.getDouble("cost"),(rs.getInt("meal")==1 ? true:false),(seats.get(0)<6),(rs.getInt("status")==1 ? true:false));
	    	
	    }}
	    catch(Exception e) {
	    	e.printStackTrace();
	    	
	    	System.out.println(e.getMessage());
	    }
	    
		
	}
	public static void main(String[] args) throws SQLException {
		// TODO Auto-generated method stub
		int bookingid,ch=1;
		int noOfSeats;
		int Pref;
		boolean mealPref,classPref;
		FlightDetails f;
		Connection con =connectToDb();
		
		if (con != null) {
 			System.out.println("nSuccessfullly connected to Oracle DB");
 		
 		} else {
 			System.out.println("nFailed to connect to Oracle DB");
 			
 		}
		updateOld(con);
		
			
	while(ch!=0) {
		System.out.print("1.addFilght 2.checkAvailablity \n"
				+ "3.BookTicket 4.cancelTicket 5.mealPref\n 6.BookingSummary"
				+ " 7.Avialble FlightNo 8.Booking Details or enter 0 to end");
		 ch = input.nextInt();
		 
		 switch(ch) {
		 	case 0:
			    con.close();
		 		break;
		 	case 1:
		 		addflight();
		 		break;
		 	case 2:
		 		f=flightNo();
		 		if(f==null) {
		 			System.out.println("invalid flight no");break;
		 		}
		 		System.out.println(f.availability());
		 		break;
		 		
		 	case 3:
		 		f=flightNo();
		 		if(f==null) {
		 			System.out.println("invalid flight no");break;
		 		}
		 		System.out.println("Enter no of seats");
		 		noOfSeats = input.nextInt();
		 		System.out.println("Enter 1 for Business 0 for Economy");
		 		System.out.println("seat no 0-5 Business class 6-14 EconomyClass");
		 		Pref = input.nextInt();
		 		classPref = (Pref==1)? true:false;
		 		System.out.println("Enter 1 for meal 0 for not");
		 		Pref = input.nextInt();
		 		mealPref = (Pref==1)? true:false;
		 		//List<Integer>seats;
		 		f.bookTicket(noOfSeats, classPref, mealPref);
		 		//writeToDb(con,seats);
		 		break;
		 	case 4:

		 		f=flightNo();
		 		if(f==null) {
		 			System.out.println("invalid flight no");break;
		 		}
		 		System.out.println("Enter the booking id");
		 		bookingid = input.nextInt();
		 		f.cancelBooking(bookingid);
		 		break;
		 	case 5:
		 		f=flightNo();
		 		if(f==null) {
		 			System.out.println("invalid flight no");break;
		 		}
		 		System.out.println("Meal Prefernce"+f.mealPref());
		 		break;
		 	case 6:
		 		f= flightNo();
		 		if(f==null) {
		 			System.out.println("invalid flight no");break;
		 		}
		 		f.displayBookingSummary();
		 		break;
		 	case 7:
		 		System.out.println(availableFlight());
		 		break;
		 	case 8:
		 		 System.out.println("Enter the booking ID");
		 		 bookingid = input.nextInt();
		 		 bookingDetail(bookingid);
		 		 
		 		 break;
		 	default:
		 		System.out.println("invalid choice");
		 		
		 }
		 
	}
	input.close();
	}

}
