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
/*
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
*/


public class Subsystem1Main {

    public static void main(Scanner inputScanner) {
        MemberServices obj = new MemberServices();
        //int ProviderID;

        System.out.println("\n\nHello Provider,");

        int providerID;
        int serviceID;
        int memberID;
        boolean rc; //return code
        int response; //user responds 1 for yes, 2 for no
        String comments;
        String date;

        String memberFileLocation = new String();
        String providerFileLocation = new String();
        memberFileLocation = "./directories/member_directory";
        providerFileLocation = "./directories/provider_directory";

        //Provider Sign In
        System.out.println("\n****************************");
        System.out.println("**Welcome Provider**");
        System.out.println("****************************\n\n");

        System.out.println("**Please enter your 9 digit provider number\n");
        System.out.print("Provider Number: ");
        providerID = getInt(inputScanner); //Gets input from User

        //validates provider login
        rc = obj.isValid(providerID, providerFileLocation);
        if(rc){
            System.out.println("\n****************************");
            System.out.println("**Provider Number is VALID**");
            System.out.println("****************************\n");
        }
        else{
            System.out.println("\nError, Provider Number is INVALID. Goodbye\n");
            return;
        }

        do{
            //get updated date for report
            /*
            DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
            Date date = new Date();
            String currentDate = new String(dateFormat.format(date));
            */


            //member validation & Service Number
            System.out.println("**Please enter your 6 digit service number\n");
            System.out.print("Service Number: ");
            serviceID = getInt(inputScanner); //Gets input from User

            date = new String();
            System.out.println("**Please type in the date this service was provided (MM-DD-YYY)");
            System.out.print("Date (MM-DD-YYYY): ");
            date = getString(inputScanner);

            System.out.println("\n**Please enter the 9 digit member number\n");
            System.out.print("Member Number: ");
            memberID = getInt(inputScanner); //Gets input from User

            //validates member login
            rc = obj.isValid(memberID, memberFileLocation);
            if(rc){
                System.out.println("\n****************************");
                System.out.println("**Member Number is VALID**");
                System.out.println("****************************\n");
            }
            else{
                System.out.println("\nError, Member Number is INVALID. Goodbye\n");
                return;
            }

            //check if member is supended
            rc = obj.isSuspended(memberID, memberFileLocation);
            if(rc){
                System.out.println("\n**Member is Suspended. Please refer patient to billing services**\n");
                return;
            }
            else{
                System.out.println("\n**Member is NOT Suspended. Proceed with services.**\n");
            }

            //provider enters comments about visit with member
            System.out.println("\n**Would you like to write comments about this visit?\n1 - yes\n2 - no\n");
            response = getInt(inputScanner);
            if(response == 1){
                System.out.println("\nPlease type your comments: ");
                comments = new String();
                comments = getString(inputScanner);
            }
            else{
                System.out.println("\nYou answered no.\n");
                comments = null;
            }

            //Write new data to Provider Report
            //obj.writeReport(providerID, serviceID, memberFileLocation);

            System.out.println("\n**Would you like to add another visit?\n1 - yes\n2 - no\n");
            response = getInt(inputScanner);

            obj.buildObject(providerID, memberID, serviceID, date, comments);

        }while(response == 1);

    }

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
