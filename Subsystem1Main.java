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

        String memberFileLocation = new String();
        String providerFileLocation = new String();
        memberFileLocation = "./directories/member_directory";
        providerFileLocation = "./directories/provider_directory";
        
        //Begin Prompts
        System.out.println("\n\nHello Provider,");
        System.out.println("-Please enter your 9 digit provider number-");
        System.out.println("ID Number: ");
        int providerID = getInt(inputScanner); //Gets input from User
        System.out.println("-Please enter your 6 digit service number-");
        int service = getInt(inputScanner); //Gets input from User

        //validates provider login
        boolean rc = obj.isValid(providerID, memberFileLocation);
        if(rc){
          System.out.println("Provider Number is VALID");
        }
        else{
          System.out.println("Provider Number is INVALID. Goodbye");
          return; 
        }
        

        //member validation
        System.out.println("-Please enter the 9 digit member number-");
        System.out.println("ID Number: ");
        int memberID = getInt(inputScanner); //Gets input from User

        //validates member login
        rc = obj.isValid(memberID, memberFileLocation);
        if(rc){
          System.out.println("Provider Number is VALID");
        }
        else{
          System.out.println("Provider Number is INVALID. Goodbye");
          return; 
        }

        rc = obj.isSuspended(memberID, memberFileLocation);
        if(rc){
          System.out.println("Member is Suspended. Please refer patient to billing services");
          return;
        }
        else{
          System.out.println("Member is NOT Suspended. Proceed with services.");
        }



        obj.writeReport(providerID, service, memberFileLocation);

        /*else {
            System.out.println("The Provider ID number you have entered is incorrect.");
            return;*/
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
