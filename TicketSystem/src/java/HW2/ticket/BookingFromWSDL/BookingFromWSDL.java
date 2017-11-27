/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HW2.ticket.BookingFromWSDL;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.List;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

/**
 *
 * @author YY_More
 */
@WebService(serviceName = "Booking", portName = "10010", endpointInterface = "hw.ticket.booking.Booking", targetNamespace = "ticket.hw/Booking", wsdlLocation = "WEB-INF/wsdl/BookingFromWSDL/2.wsdl")
@Stateless
public class BookingFromWSDL {
    int cnt=13579;
     int cnt2 = 900661288;
     static{
        File file=new File("Booking.xml");
        if (!file.exists())
        try{
            Element root = new Element("Bookings");
            Document doc = new Document(root);
            
            XMLOutputter xmlOutput = new XMLOutputter();
            xmlOutput.setFormat(Format.getPrettyFormat());
            xmlOutput.output(doc, new FileOutputStream("Bookings.xml"));
        }catch(IOException e){
        }
    }

    public java.lang.String issue(java.lang.String bookingnumber) throws JDOMException {
        //TODO implement this method
         String output = new String("");
        try{
            File file=new File("Bookings.xml");
            SAXBuilder saxBuilder = new SAXBuilder();
            Document document = saxBuilder.build(file);
            Element root = document.getRootElement();
            List<Element> bookings = root.getChildren("Booking");
            for (int i=0;i<bookings.size();i++){
                if (bookings.get(i).getChild("BookingNo").getText().equals(bookingnumber)){
                    int trips = Integer.parseInt(bookings.get(i).getChild("trips").getText());
                    output = output.concat(bookings.get(i).getChild("FirstNo").getText());
                    output = output.concat(" "+bookings.get(i).getChild("FirstTk").getText());
                    
                    if (trips>1){
                        output = output.concat(" "+bookings.get(i).getChild("SecondNo").getText());
                        output = output.concat(" "+bookings.get(i).getChild("SecondTk").getText());
                    }
                }
            }
            }catch(IOException e){
         System.err.print("error");
        }
        return output;
    }

    public java.lang.String booking(int number, java.lang.String first, java.lang.String second, java.lang.String date, java.lang.String card) throws JDOMException {
        String Rd = (new SecureRandom()).toString();
        cnt++;
        try{
            File file=new File("Bookings.xml");
            SAXBuilder saxBuilder = new SAXBuilder();
            Document document = saxBuilder.build(file);
            Element root = document.getRootElement();
            Element newbooking = new Element("Booking");
            Element newNo = new Element("BookingNo");
            newNo.setText(String.valueOf(cnt));
            newbooking.addContent(newNo);
            Element trip = new Element("trips");
            trip.setText(String.valueOf(number));
            newbooking.addContent(trip);
            Element Card = new Element("Card");
            Card.setText(card);
            newbooking.addContent(Card);
            
            Element newFirstNo = new Element("FirstNo");
            newFirstNo.setText(first);
            newbooking.addContent(newFirstNo);
            cnt2++;
            Element newFirstTk = new Element ("FirstTk");
            newFirstTk.setText(String.valueOf(cnt2));
            newbooking.addContent(newFirstTk);
            
            if (number==2){
                Element newSecondNo = new Element("SecondNo");
                newSecondNo.setText(second);
                newbooking.addContent(newSecondNo);
                cnt2++;
                Element newSecondTk = new Element ("SecondTk");
                newSecondTk.setText(String.valueOf(cnt2));
                newbooking.addContent(newSecondTk);
            }
                   
            root.addContent(newbooking);
            XMLOutputter xmlOutput = new XMLOutputter();
            xmlOutput.setFormat(Format.getPrettyFormat());
            xmlOutput.output(document, new FileOutputStream("Bookings.xml"));
            
            file=new File("Records.xml");
            document = saxBuilder.build(file);
            root = document.getRootElement();
            List<Element> records = root.getChildren("Record");
            
            for (int i=0;i<records.size();i++)
                if (records.get(i).getChild("No").getText().equals(first))
                {
                    records.get(i).getChild("Remain-"+ date).setText(String.valueOf(Integer.parseInt(records.get(i).getChild("Remain-"+ date).getText())-1));
                }
            if (number==2){
                for (int i=0;i<records.size();i++)
                    if (records.get(i).getChild("No").getText().equals(second))
                    {
                        records.get(i).getChild("Remain-"+ date).setText(String.valueOf(Integer.parseInt(records.get(i).getChild("Remain-"+ date).getText())-1));
                    }
                
            }
            xmlOutput.output(document, new FileOutputStream("Records.xml"));
            
        }catch(IOException e){
         System.err.print("error");
        }
        
        return String.valueOf(cnt);
    }
    
}
