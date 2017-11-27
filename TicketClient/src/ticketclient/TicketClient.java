/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ticketclient;


import hw.ticket.query.JDOMException_Exception;
import java.util.Scanner;
import java.util.HashMap;

/**
 *
 * @author YY_More
 */
public class TicketClient {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws JDOMException_Exception {
        // TODO code application logic here
        String username,password,session;
        boolean success = false;
        Scanner sc=new Scanner(System.in);
        System.out.println("Welcome to ticket booking system");
        do{
        success = false;
        System.out.println("Please sign in first to use our service");
        System.out.println("Are your a new user?(YES/NO)");
        String tmp = sc.nextLine();
        if (tmp.equals("YES")){
            System.out.println("Please enter your Username:");
            username=sc.nextLine();
            System.out.println("Please enter your Password:");
            password=sc.nextLine();
            tmp = register(username,password);
            if (tmp.equals("FAIL")){
                System.out.println("Sorry the Username is occupied, Please try again");
            }else{
                System.out.println("Register Succeed!");
            }
        }
        else{
            System.out.println("Please enter your Username:");
            username=sc.nextLine();
            System.out.println("Please enter your Password:");
            password=sc.nextLine();
            session = login(username,password);
            if (session.equals("FALSE")){
                System.out.println("Input Username/Password not valid, please check!");
            }else{
                success=true;
            }
        }
        }while (!success);
        while (true){
        System.out.println("Choose: search for flights (1)  or  check for booking (2)  or  exit(3)");
        String choose = sc.nextLine();
        if (choose.equals("3")) break;
        String From = null;
        String To = null;
        if (choose.equals("1")){
            System.out.println("From:");
            From = sc.nextLine();
            System.out.println("To:");
            To = sc.nextLine();
            String query = getRoute(From,To);
            int cnt=0;
            Scanner reader=new Scanner(query);
            HashMap<Integer,String> map = new HashMap<>();
            while (reader.hasNext()){
                cnt++;
                System.out.println("\nRoute "+ cnt+":");
                String flag = reader.next();
                if (flag.equals("?")){
                    String tmp1 = reader.next();
                    map.put(cnt,"Single " +tmp1);
                    System.out.println("Flight:   "+tmp1);
                    System.out.println("From:   "+reader.next());
                    System.out.println("To:   "+reader.next());
                    System.out.println("Departure at:   "+reader.next());
                    System.out.println("Land at:   "+reader.next());
                    reader.next();
                }else 
                if (flag.equals("!")){
                    reader.next();
                    String tmp1 = reader.next();
                    System.out.println("Flight:   "+tmp1);
                    System.out.println("From:   "+reader.next());
                    System.out.println("To:   "+reader.next());
                    System.out.println("Departure at:   "+reader.next());
                    System.out.println("Land at:   "+reader.next());
                    reader.next();
                    reader.next();
                    String tmp2 = reader.next();
                    System.out.println("Flight:   "+tmp2);
                    System.out.println("From:   "+reader.next());
                    System.out.println("To:   "+reader.next());
                    System.out.println("Departure at:   "+reader.next());
                    System.out.println("Land at:   "+reader.next());
                    reader.next();
                    map.put(cnt,"double "+tmp1+" "+tmp2);
                    reader.next();
                }
            }
            System.out.println(" ");
            System.out.println("Which one do you prefer? input NO to leave");
            String flag = sc.nextLine();
            if (flag.equals("NO")) continue;
            int choice = Integer.parseInt(flag);
            System.out.println("What date do you want?");
            String date = sc.nextLine();
            reader=new Scanner(map.get(choice));
            String FirstFlight=null;
            String SecondFlight=null;
            int trips=1;
            if (reader.next().equals("Single")){
                FirstFlight = reader.next();
            }
            else{
                FirstFlight = reader.next();
                SecondFlight = reader.next();
                trips=2;
            }
            
            String data= getData(trips,FirstFlight,SecondFlight,date);    
            reader=new Scanner(data);
            System.out.println("Your trip costs "+reader.next()+ " in total.");
            System.out.println(reader.next()+ " seats is still available.");
            System.out.println("Please input your credit card number for booking");
            String cardNumber = sc.nextLine();
            String ref = booking(trips,FirstFlight,SecondFlight,date,cardNumber);
            while (true){
                System.out.println("Thanks for your booking! Your booking reference is "+ref);
                System.out.println("PLEASE REMEMBER YOUR REFERENCE!");
                System.out.println("Input YES to leave");
                flag = sc.nextLine();
                if (flag.equals("YES")) break;
            }
            
            
        }
        if (choose.equals("2")){
            System.out.println("PLEASE input your BOOKING REFERENCE:");
            String ref=sc.nextLine();
            String data = issue(ref);
            Scanner reader = new Scanner(data);
            if (reader.hasNext()){
                System.out.println("Your first flight is "+reader.next());
                System.out.println("You're now checked in. The ticket number is: "+reader.next());
            }
            if (reader.hasNext()){
                System.out.println("Your second flight is "+reader.next());
                System.out.println("You're now checked in. The ticket number is: "+reader.next());
            }
            System.out.println("Thank you, have a nice trip!");
        }
        }
    }

    private static String getRoute(java.lang.String from, java.lang.String to) throws hw.ticket.query.JDOMException_Exception {
        hw.ticket.query.Query_Service service = new hw.ticket.query.Query_Service();
        hw.ticket.query.Query port = service.get10086();
        return port.getRoute(from, to);
    }

    private static String getData(int number, java.lang.String first, java.lang.String second, java.lang.String date) throws hw.ticket.query.JDOMException_Exception {
        hw.ticket.query.Query_Service service = new hw.ticket.query.Query_Service();
        hw.ticket.query.Query port = service.get10086();
        return port.getData(number, first, second, date);
    }



    private static String login(java.lang.String username, java.lang.String password) {
        hw.ticket.user.User_Service service = new hw.ticket.user.User_Service();
        hw.ticket.user.User port = service.get13261();
        return port.login(username, password);
    }

    private static String register(java.lang.String username, java.lang.String password) {
        hw.ticket.user.User_Service service = new hw.ticket.user.User_Service();
        hw.ticket.user.User port = service.get13261();
        return port.register(username, password);
    }

    private static String booking(int number, java.lang.String first, java.lang.String second, java.lang.String date, java.lang.String card) {
        hw.ticket.booking.Booking_Service service = new hw.ticket.booking.Booking_Service();
        hw.ticket.booking.Booking port = service.get10010();
        return port.booking(number, first, second, date, card);
    }

    private static String issue(java.lang.String bookingnumber) {
        hw.ticket.booking.Booking_Service service = new hw.ticket.booking.Booking_Service();
        hw.ticket.booking.Booking port = service.get10010();
        return port.issue(bookingnumber);
    }
    
}
