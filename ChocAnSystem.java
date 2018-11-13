import java.util.Scanner;
import java.io.File;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 * ChocAnSystem is a wrapper that allows ChocAn to access the different systems
 * built by Group Five to verify that each subsystem is correct.
 * The below are good reference tutorials for json-simple:
 * https://www.mkyong.com/java/json-simple-example-read-and-write-json/
 * https://howtodoinjava.com/json/json-simple-read-write-json-examples/
 * 
 * @author  Group Five
 * @version 1.0
 * @since   2018-11-01 
 */
public class ChocAnSystem {
	public static void main(String[] args) {
		Scanner inputScanner = new Scanner(System.in);
		int subsystemToEnter;
		String memberFileLocation = "./directories/member_directory";
		String providerFileLocation = "./directories/provider_directory";

		System.out.println();
		System.out.println("Welcome to ChocAn's Data Processing System.");
		System.out.println("What subsystem would you like to use? (Please enter the arabic subsystem number)");
		System.out.println();
		System.out.println("\t0) Terminate this program");
		System.out.println("\t1) Providing and Billing a Member for a Service");
		System.out.println("\t2) Report Generator");
		System.out.println("\t3) Operator Mode");
		System.out.println();
		System.out.println("\t4) Other: Fill Member Directory with Data");
		System.out.println("\t5) Other: Fill Provider Directory with Data");

		while(true) {
			System.out.println();
			System.out.print("> ");
			subsystemToEnter = inputScanner.nextInt();

			switch(subsystemToEnter) {
				case 0:
					System.out.println("Goodbye");
					System.out.println();
					inputScanner.close();
					return;
				case 1:
					runSubsystem1(inputScanner);
					break;
				case 2:
					runSubsystem2(inputScanner);
					break;
				case 3:
					runSubsystem3(inputScanner);
					break;
				case 4:
					fillMemberDirectory(memberFileLocation);
					break;
				case 5:
					fillProviderDirectory(providerFileLocation);
					break;
				default:
					System.out.println("The input of [" + subsystemToEnter + "] is not valid; try again.");
					break;
			}
		}
	}

	private static void runSubsystem1(Scanner inputScanner) {
		System.out.println("Loading: Providing and Billing a Member for a Service");
		// TODO: put the caller code here
	}

	private static void runSubsystem2(Scanner inputScanner) {
		System.out.println("Loading: Report Generator");
		// TODO: put the caller code here
	}

	private static void runSubsystem3(Scanner inputScanner) {
		System.out.println("Loading: Operator Mode");
		// TODO: put the caller code here
	}

	private static void fillMemberDirectory(String memberFileLocation) {
		System.out.println("Loading: fill member directory");
		JSONObject member_directory = new JSONObject();

		member_directory.put("000000001", "Valid");
		member_directory.put("000000002", "Suspended");
		member_directory.put("000000004", "Suspended");
		member_directory.put("000000005", "Valid");
		member_directory.put("000000010", "Suspended");
		member_directory.put("000000015", "Suspended");
		member_directory.put("000000020", "Valid");
		member_directory.put("039390041", "Valid");

		// Note that this try/catch is the same as fillProviderDirectory()'s'
		try {
			System.out.println("Writing to member_directory at: " + memberFileLocation);
			File memberFile = new File(memberFileLocation);
			if (!memberFile.exists())
				memberFile.createNewFile();
			BufferedWriter memberFileWriter = new BufferedWriter(new FileWriter(memberFile));

			memberFileWriter.write(JSONValue.toJSONString(member_directory));
			memberFileWriter.close();
			System.out.println("Wrote the below to " + memberFileLocation + ":\n" + member_directory);

		} catch (IOException error) {
			error.printStackTrace();
		}
	}

	private static void fillProviderDirectory(String providerFileLocation) {
		System.out.println("Loading: fill provider directory");
		JSONObject provider_directory = new JSONObject();
		JSONObject providerNumber = new JSONObject();
		JSONObject serviceNumbers = new JSONObject();
		JSONObject serviceInfo1 = new JSONObject();
		JSONObject serviceInfo2 = new JSONObject();

		serviceInfo1.put("name", "Back Rub");
		serviceInfo1.put("fee", new Double(25.00));
		serviceNumbers.put("000000", serviceInfo1);

		serviceInfo2.put("name", "Therapy");
		serviceInfo2.put("fee", new Double(79.99));
		serviceNumbers.put("005020", serviceInfo2);

		providerNumber.put("name", "Kaiser");
		providerNumber.put("serviceNumbers", serviceNumbers);

		provider_directory.put("014358673", providerNumber);


		// Note that this try/catch is the same as fillMemberDirectory()'s'
		try {
			System.out.println("Writing to provider_directory at: " + providerFileLocation);
			File providerFile = new File(providerFileLocation);
			if (!providerFile.exists())
				providerFile.createNewFile();
			BufferedWriter providerFileWriter = new BufferedWriter(new FileWriter(providerFile));

			providerFileWriter.write(JSONValue.toJSONString(provider_directory));
			providerFileWriter.close();
			System.out.println("Wrote the below to " + providerFileLocation + ":\n" + provider_directory);

		} catch (IOException error) {
			error.printStackTrace();
		}
	}
}

