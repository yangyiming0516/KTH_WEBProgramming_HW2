/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HW2.ticket.UserFromWSDL;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.List;
import javax.ejb.Stateless;
import javax.jws.WebService;
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
@WebService(serviceName = "User", portName = "13261", endpointInterface = "hw.ticket.user.User", targetNamespace = "ticket.hw/User", wsdlLocation = "WEB-INF/wsdl/UserFromWSDL/1.wsdl")
@Stateless
public class UserFromWSDL {
    
    static{
        File file=new File("Users.xml");
        if (!file.exists())
        try{
            Element root = new Element("Users");
            Document doc = new Document(root);
            
            XMLOutputter xmlOutput = new XMLOutputter();
            xmlOutput.setFormat(Format.getPrettyFormat());
            xmlOutput.output(doc, new FileOutputStream("Users.xml"));
        }catch(IOException e){
        }
    }

    public java.lang.String login(java.lang.String Username, java.lang.String Password) throws JDOMException {
        try{
            File file=new File("Users.xml");
            SAXBuilder saxBuilder = new SAXBuilder();
            Document document = saxBuilder.build(file);
            Element root = document.getRootElement();
            List<Element> users = root.getChildren("User");
            for (int i=0;i<users.size();i++)
                if (users.get(i).getChild("Username").getText().equals(Username) && users.get(i).getChild("Password").getText().equals(Password))
                {
                    String session = (new SecureRandom()).toString();
                    users.get(i).getChild("Session").setText(session);
                    XMLOutputter xmlOutput = new XMLOutputter();
                    xmlOutput.setFormat(Format.getPrettyFormat());
                    xmlOutput.output(document, new FileOutputStream("Users.xml"));
                    return session;
                }
        }catch(IOException e){
         e.printStackTrace();
        }	
        return "FALSE";
    }


    public java.lang.String register(java.lang.String Username, java.lang.String Password) throws JDOMException {
        try{
        File file=new File("Users.xml");
        SAXBuilder saxBuilder = new SAXBuilder();
        Document document = saxBuilder.build(file);
        
        Element root = document.getRootElement();
        List<Element> users = root.getChildren("User");
        for (int i=0;i<users.size();i++)
            if (users.get(i).getChildren("Username").get(0).getText().equals(Username)) return "FAIL";
        
        Element newUser = new Element("User");
        Element username = new Element("Username");
        username.setText(Username);
        Element password = new Element("Password");
        password.setText(Password);
        Element session = new Element("Session");
        session.setText("NULL");
        newUser.addContent(username);
        newUser.addContent(password);
        newUser.addContent(session);
        
        
        document.getRootElement().addContent(newUser);
        
        XMLOutputter xmlOutput = new XMLOutputter();
        xmlOutput.setFormat(Format.getPrettyFormat());
        xmlOutput.output(document, new FileOutputStream("Users.xml"));
        }catch(IOException e){
         e.printStackTrace();
        }		
        
        return "SUCCEED";
    }
    
}
