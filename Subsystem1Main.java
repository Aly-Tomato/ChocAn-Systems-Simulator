/**
 * Created by Victor on 11/15/2018.
 */
import java.util.Scanner;
import java.io.File;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.FileReader;
import java.io.FileNotFoundException;

public class Subsystem1Main {

    public static void main(Scanner inputScanner) {
        memberServices obj = new memberServices();
        //int ProviderID;

        System.out.println("\n\nHello Provider,");

        System.out.println("-Please enter your 9 digit PROVIDER number-");
        System.out.println("ID Number: ");
        int providerID = 0000000000;//getInt(inputScanner); //Gets input from User

        System.out.println("-Please enter your 9 digit MEMBER number-");
        System.out.println("ID Number: ");
        int memberID = 111111111;//getInt(inputScanner); //Gets input from User

        System.out.println("-Please enter your 6 digit service number-");
        int serviceID = 666666;//getInt(inputScanner); //Gets input from User

        System.out.println("-Please enter the date of service-");
        String date = "11-01-2018";//getString(inputScanner); //Gets input from User

        System.out.println("-Comment-");
        String comment  = "Here is my comment";//getString(inputScanner); //Gets input from User

        String memberFileLocation = new String();
        String providerFileLocation = new String();

        memberFileLocation = "./directories/member_directory";
        providerFileLocation = "./directories/provider_directory";

        JSONObject key = new JSONObject(); //maybe remove
        key = obj.buildObject(providerID, memberID, serviceID, date, comment);

        //JSONObject data; //creates new JSONObject
        //data = obj.buildProviderFile(providerID, memberID, serviceID, date); //Creates JSONObject and stores into data
        //obj.createFile(providerID, memberID, serviceID, providerFileLocation, data);
        //System.out.println(data);

        //obj.read(providerID, "./reports/"+date);

        /*JSONObject key = new JSONObject(); //The key JSONObject is created
        JSONObject var = obj.returnSub(providerID, "./reports/"+date); //Get the inner part of the JSON file
        key.put(providerID,var); //Add in the stuff you want to add (still has to be another function)
        obj.writeToFile("./reports/"+date, key); //Write it to the file*/

        }
        //}

/////////////////////////////////////////////////////////////////////////////////////////////////////////

    private static String getString(Scanner inputScanner) {
        String returnThis = new String();
        returnThis = inputScanner.nextLine();
        return returnThis;
    }

    private static int getInt(Scanner inputScanner) {
        int return_this;
        return_this = inputScanner.nextInt();
        return return_this;
    }
    }
