/**
 * Created by Victor and Alyssa on 11/15/2018.
 */
import java.util.Scanner;
import java.io.File;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Subsystem1Main {

    public static void main(Scanner inputScanner) {
        //data members
        memberServices obj = new memberServices();
        int providerID;
        int serviceID;
        boolean rc; //return code
        int memberID;
        String comments;
        DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
        Date date = new Date();
        

        String memberFileLocation = new String();
        String providerFileLocation = new String();
        memberFileLocation = "./directories/member_directory";
        providerFileLocation = "./directories/provider_directory";
        
        //Begin Prompts
        System.out.println("\n\nHello Provider,");
        System.out.println("-Please enter your 9 digit provider number-");
        System.out.println("ID Number: ");
        providerID = getInt(inputScanner); //Gets input from User
        System.out.println("-Please enter your 6 digit service number-");
        serviceID = getInt(inputScanner); //Gets input from User

        //validates provider login
        rc = obj.isValid(providerID, memberFileLocation);
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
        memberID = getInt(inputScanner); //Gets input from User

        //validates member login
        rc = obj.isValid(memberID, memberFileLocation);
        if(rc){
          System.out.println("Provider Number is VALID");
        }
        else{
          System.out.println("Provider Number is INVALID. Goodbye");
          return; 
        }

        //check if member is supended
        rc = obj.isSuspended(memberID, memberFileLocation);
        if(rc){
          System.out.println("Member is Suspended. Please refer patient to billing services");
          return;
        }
        else{
          System.out.println("Member is NOT Suspended. Proceed with services.");
        }

        //provider enters comments about visit with member
        System.out.println("\n\nWould you like to write comments about this visit?\n1 - yes\n2 - no\n");
        int response = getInt(inputScanner);
        if(response == 1){
          System.out.println("\nPlease type your comments: \n");
          comments = new String();
        }
        else{
          System.out.println("\nYou answered no.\n");
        }
        

        obj.writeReport(providerID, serviceID, memberFileLocation);

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
        inputScanner.nextLine();
        return return_this;
    }
    }
