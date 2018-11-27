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

public class Subsystem1Main {

    public static void main(Scanner inputScanner) {
        memberServices obj = new memberServices();
        //int ProviderID;


        System.out.println("\n\nHello Provider,");

        System.out.println("-Please enter your 9 digit PROVIDER number-");
        System.out.println("ID Number: ");
        int providerID = 999999111;//getInt(inputScanner); //Gets input from User

        System.out.println("-Please enter your 9 digit MEMBER number-");
        System.out.println("ID Number: ");
        int memberID = 111111111;//getInt(inputScanner); //Gets input from User

        System.out.println("-Please enter your 6 digit service number-");
        int service = 666666;//getInt(inputScanner); //Gets input from User

        System.out.println("-Please enter the date of service-");
        String date = "11-26-2018";//getString(inputScanner); //Gets input from User

        String memberFileLocation = new String();
        String providerFileLocation = new String();
        memberFileLocation = "./directories/member_directory";
        providerFileLocation = "./directories/provider_directory";

        /*JSONObject data;
        data = obj.buildProviderFile(providerID, memberID, service, date);
        obj.createFile(providerID, service, providerFileLocation, data);*/

        //obj.read(providerID, "./reports/"+date);
        JSONObject key = new JSONObject();
        JSONObject var = obj.returnSub(providerID, "./reports/"+date);
        key.put(providerID,var);

        obj.writeToFile("./reports/"+date, key);

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
