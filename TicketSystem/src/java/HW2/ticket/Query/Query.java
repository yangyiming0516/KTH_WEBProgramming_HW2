/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HW2.ticket.Query;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import static java.lang.Integer.parseInt;
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
@WebService(serviceName = "Query",portName = "10086",targetNamespace = "ticket.hw/Query")
@Stateless()
public class Query {
    
    static{
        File file=new File("Flights.xml");
        if (!file.exists())
        try{
            Element root = new Element("Flights");
            Document doc = new Document(root);
            
            XMLOutputter xmlOutput = new XMLOutputter();
            xmlOutput.setFormat(Format.getPrettyFormat());
            xmlOutput.output(doc, new FileOutputStream("Flights.xml"));
        }catch(IOException e){
        }
        
        file=new File("Records.xml");
        if (!file.exists())
        try{
            Element root = new Element("Records");
            Document doc = new Document(root);
            
            XMLOutputter xmlOutput = new XMLOutputter();
            xmlOutput.setFormat(Format.getPrettyFormat());
            xmlOutput.output(doc, new FileOutputStream("Records.xml"));
        }catch(IOException e){
        }
    }

    /**
     * Web 服务操作
     */
    @WebMethod(operationName = "getRoute")
    public String getRoute(@WebParam(name = "from") String from, @WebParam(name = "to") String to) throws JDOMException {
        String output = new String();
        //TODO write your implementation code here:
        try{
            File file=new File("Flights.xml");
            SAXBuilder saxBuilder = new SAXBuilder();
            Document document = saxBuilder.build(file);
            Element root = document.getRootElement();
            List<Element> flights = root.getChildren("Flight");
            for (int i=0;i<flights.size();i++)
                if (flights.get(i).getChild("From").getText().equals(from) && flights.get(i).getChild("To").getText().equals(to))
                {
                    output = output.concat("? ");
                    output = output.concat(flights.get(i).getChild("No").getText()+" ");
                    output = output.concat(flights.get(i).getChild("From").getText()+" ");
                    output = output.concat(flights.get(i).getChild("To").getText()+" ");
                    output = output.concat(flights.get(i).getChild("Departure").getText()+" ");
                    output = output.concat(flights.get(i).getChild("Landing").getText()+" ");
                    output = output.concat("? ");
                }
            for (int i=0;i<flights.size();i++)
                if (flights.get(i).getChild("From").getText().equals(from))
                    for (int j=0;j<flights.size();j++)
                        if (flights.get(j).getChild("From").getText().equals(flights.get(i).getChild("To").getText()) && flights.get(j).getChild("To").getText().equals(to))
                            if (flights.get(i).getChild("Landing").getText().compareTo(flights.get(j).getChild("Departure").getText())<0)
                            {
                                output = output.concat("! ");
                                output = output.concat("? ");
                                output = output.concat(flights.get(i).getChild("No").getText()+" ");
                                output = output.concat(flights.get(i).getChild("From").getText()+" ");
                                output = output.concat(flights.get(i).getChild("To").getText()+" ");
                                output = output.concat(flights.get(i).getChild("Departure").getText()+" ");
                                output = output.concat(flights.get(i).getChild("Landing").getText()+" ");
                                output = output.concat("? ");
                                output = output.concat("? ");
                                output = output.concat(flights.get(j).getChild("No").getText()+" ");
                                output = output.concat(flights.get(j).getChild("From").getText()+" ");
                                output = output.concat(flights.get(j).getChild("To").getText()+" ");
                                output = output.concat(flights.get(j).getChild("Departure").getText()+" ");
                                output = output.concat(flights.get(j).getChild("Landing").getText()+" ");
                                output = output.concat("? ");
                                output = output.concat("! ");
                            }
        }catch(IOException e){
         System.err.print(output);
        }
        return output;
    }

    /**
     * Web 服务操作
     */
    @WebMethod(operationName = "getData")
    public String getData(@WebParam(name = "number") int number, @WebParam(name = "first") String first, @WebParam(name = "second") String second, @WebParam(name = "date") String date) throws JDOMException {
        String output = new String("");
        double price=0;
        int available = 0;
        //TODO write your implementation code here:
        try{
            File file=new File("Records.xml");
            SAXBuilder saxBuilder = new SAXBuilder();
            Document document = saxBuilder.build(file);
            Element root = document.getRootElement();
            List<Element> records = root.getChildren("Record");
            
            for (int i=0;i<records.size();i++)
                if (records.get(i).getChild("No").getText().equals(first))
                {
                    price = Double.parseDouble(records.get(i).getChild("Price").getText());
                    available = Integer.parseInt(records.get(i).getChild("Remain-"+ date).getText());
                }
            if (number==2){
                for (int i=0;i<records.size();i++)
                    if (records.get(i).getChild("No").getText().equals(second))
                    {
                        price = price + Double.parseDouble(records.get(i).getChild("Price").getText());
                        if (available > Integer.parseInt(records.get(i).getChild("Remain-"+ date).getText()))
                        available = Integer.parseInt(records.get(i).getChild("Remain-"+ date).getText());
                    }
                
            }
            
        }catch(IOException e){
         System.err.print(output);
        }
        
        if (number > 1) price *= 0.75;
        output = "" + price + " "+available;
        
        return output;
    }
}
