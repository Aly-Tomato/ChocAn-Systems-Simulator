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
        System.out.println("-Please enter your 9 digit provider number-");
        System.out.println("ID Number: ");
        int ProviderID = getInt(inputScanner); //Gets input from User

        String memberFileLocation = new String();
        memberFileLocation = "./directories/member_directory";

        if(obj.isValid(ProviderID, memberFileLocation)) {
            System.out.println("TRUE");
            return;
        }

        else {
            System.out.println("The Provider ID number you have entered is incorrect.");
            return;
        }
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
        return return_this;
    }
    }
