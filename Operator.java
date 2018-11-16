import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.InputMismatchException;
import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileNotFoundException;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Operator will allow editing of member_directory and provider_directory.
 *
 * @author  Medina Lamkin and Sawyer Watts
 * @version 1.0
 * @since   2018-11-13 
 */
public class Operator {
	protected String memberFileLocation;
	protected String providerFileLocation;

	/**
	 * Default Constructor
	 * Sets the file locations to ./directories/member_directory and ./directories/provider_directory, respectively.
	 */
	public Operator() {
		this("./directories/member_directory", "./directories/provider_directory");
	}

	/**
	 * Constructor
	 *
	 * @param theMemberFileLocation The String of the relative location to the member_directory.
	 * @param theProviderFileLocation The String of the relative location to the provider_directory.
	 */
	public Operator(String theMemberFileLocation, String theProviderFileLocation) {
		memberFileLocation = theMemberFileLocation;
		providerFileLocation = theProviderFileLocation;

		directoryValidation(memberFileLocation);
		directoryValidation(providerFileLocation);
	}

	// directoryValidation will take a path to a directry, and if it isn't there, it will create it with a JSON structure of {}.
	// This will also put a json object in a blank file.
	// This will return false if the directory isn't one of the two approved directories, or if there is an IOException.
	protected boolean directoryValidation(String aDirectoryLocation) {
		if (!(aDirectoryLocation.equals(memberFileLocation) || aDirectoryLocation.equals(providerFileLocation)))
			return false;

		try {
			BufferedWriter outputFileWriter;
			JSONObject blank;
			File theFile = new File(aDirectoryLocation);
			boolean newFile = false;

			if (!theFile.exists()) {
				theFile.createNewFile();
				newFile = true;
			}

			if (!newFile)
				return true;

			outputFileWriter = new BufferedWriter(new FileWriter(theFile));
			blank = new JSONObject();
			outputFileWriter.write(JSONValue.toJSONString(blank));
			outputFileWriter.close();

		} catch (IOException error) {
			error.printStackTrace();
			return false;
		}

		return true;
	}

	public boolean addMember() {
		// reminder to always directoryValidation; see addProvider for an example with the output message I used
		return true;
	}

	// writeToFile will write the passed json object to the file location.
	// This will return false if there is an IOException, if the fileLocation isn't one of two protected file locations.
	protected boolean writeToFile(String fileLocation, JSONObject newDirectory) {
		try {
			if (!directoryValidation(fileLocation))
				return false;

			File outputFile = new File(fileLocation);
			BufferedWriter outputFileWriter = new BufferedWriter(new FileWriter(outputFile));

			outputFileWriter.write(JSONValue.toJSONString(newDirectory));
			outputFileWriter.close();

		} catch (IOException error) {
			error.printStackTrace();
			return false;
		}

		return true;
	}

	public boolean editMember() {
		return true;
	}

	public boolean deleteMember() {
		return true;
	}

	/**
	 * addProvider will prompt the user for the information needed to add another provider to provider_directory, and save that provider.
	 *
	 * @param systemInputScanner The Scanner(System.in) variable.
	 * @return true if add was successfully written, false else.
	 */
	public boolean addProvider(Scanner systemInputScanner) {
		boolean returnCode = false;
		System.out.println("\tAdd a Provider:");
		if (!directoryValidation(providerFileLocation))
			System.out.println("No " + providerFileLocation + " exists; file with empty data created.");

		try (FileReader reader = new FileReader(providerFileLocation)) {

			returnCode = promptForNewProvider(systemInputScanner, reader);
			if (returnCode)
				System.out.println("The new provider was successfully created and saved.");
			else
				System.out.println("The provider was not created or saved.");

		} catch (IOException error) {
			error.printStackTrace();
			return false;
		} catch (ParseException error) {
			error.printStackTrace();
			return false;
		}

		return true;
	}

	// TODO: have this confirm that everything is correct and let them choose which to edit, and then reconfirm
	// 		probably put this/use this helper in editProvider
	// TODO: make the constants for the max sizes protected class vars
	// TODO: refactor/functionize this function when done
	protected boolean promptForNewProvider(Scanner systemInputScanner, FileReader reader) throws IOException, ParseException {
		String repeatLoop = "y";
		String providerNumber = "";              // 9 Digits
		String name = "";                // 25 Characters
		String street = "";              // 25 Characters
		String city = "";                // 25 Characters
		String state = "";               // 2 Characters
		String zip = "";                 // 5 characters
		ArrayList<String> serviceNumbers = new ArrayList<String>(); // 6 Digits per element
		ArrayList<String> serviceNames = new ArrayList<String>();   // 20 Characters per element
		ArrayList<Double> serviceFees = new ArrayList<Double>();    // Up to 99,999.99

		JSONObject providersJson;
		JSONObject newProvider;
		JSONParser jsonParser = new JSONParser();
		Object jsonData = jsonParser.parse(reader);
		providersJson = (JSONObject) jsonData;

		while (true) {
			providerNumber = generateRandomNumber(9);
			// TODO: validate that the number is original
			break;
		}

		// get name
		System.out.println("Please enter the new provider's name (25 characters max): ");
		while (true) {
			System.out.print("> ");
			name = systemInputScanner.nextLine();
			if (name.length() == 0)
				;
			else if (name.length() > 25)
				System.out.println("That data was too long to be stored.");
			else if (validateString(name))
				break;
			else
				System.out.println("Please do not enter anything besides characters and spaces.");
		}
		System.out.println();

		// get street
		System.out.println("Please enter the new provider's street address (25 characters max, where the street number is first and the street name is after): ");
		while (true) {
			String streetNumber = "";
			String streetName = "";
			System.out.print("> ");
			try {
				streetNumber += systemInputScanner.nextInt();
				streetName = systemInputScanner.nextLine();
				street += (streetNumber + streetName);

				if (street.length() == 0)
					;

				else if (streetName.length() == 0)
					System.out.println("Please enter a street number followed by the street name.");

				else if (streetName.charAt(0) != ' ')
					System.out.println("Please enter a street number followed by the street name, with a space between them.");

				else if (streetName.length() == 1 && streetName.charAt(0) == ' ')
					System.out.println("Please enter a street number followed by the street name.");

				else if (street.length() > 25)
					System.out.println("That street name was too long to be stored.");

				else if (validateString(streetName))
					break;
				else
					System.out.println("Please do not enter anything besides characters and spaces.");

			} catch (InputMismatchException error) {
				System.out.println("An example street address would be (ignoring the brackets): [19939 Trade Way].");
				systemInputScanner.nextLine(); // clear the buffer
			}
		}
		System.out.println();

		// get city
		System.out.println("Please enter the new provider's city (25 characters max): ");
		while (true) {
			System.out.print("> ");
			city = systemInputScanner.nextLine();
			if (city.length() == 0)
				;
			else if (city.length() > 25)
				System.out.println("That data was too long to be stored.");
			else if (validateString(city))
				break;
			else
				System.out.println("Please do not enter anything besides characters and spaces.");
		}
		System.out.println();

		// get state
		System.out.println("Please enter the new provider's state (2 characters): ");
		while (true) {
			System.out.print("> ");
			state = systemInputScanner.nextLine().toUpperCase();
			if (state.length() == 0)
				;
			else if (state.length() != 2)
				System.out.println("That data was the wrong size, it must be 2 digits.");
			else if (validateString(state) && Character.isLetter(state.charAt(0)) && Character.isLetter(state.charAt(1)))
				break;
			else
				System.out.println("Please do not enter anything besides characters and spaces.");
		}
		System.out.println();

		// get zip
		System.out.println("Please enter the new provider's ZIP code (5 characters): ");
		while (true) {
			System.out.print("> ");
			zip = systemInputScanner.nextLine();
			if (zip.length() == 0)
				;
			else if (zip.length() != 5)
				System.out.println("That data was the wrong size, it must be 5 digits.");
			else if (validateZipCode(zip))
				break;
			else
				System.out.println("Please do not enter anything besides numbers.");
		}
		System.out.println();

		// get service informations
		while (repeatLoop.equals("y")) {
			String serviceName = "";
			Double serviceFee;
			while (true) {
				String serviceNumber = generateRandomNumber(6);
				// TODO: validate that the number is original
				serviceNumbers.add(serviceNumber);
				break;
			}

			System.out.println("Please enter a service name (20 characters max): ");
			while (true) {
				System.out.print("> ");
				serviceName = systemInputScanner.nextLine();
				if (serviceName.length() == 0)
					;
				else if (serviceName.length() > 20)
					System.out.println("That data was too long to be stored.");
				else if (validateString(serviceName)) {
					serviceNames.add(serviceName);
					break;
				} else
					System.out.println("Please do not enter anything besides characters and spaces.");
			}
			System.out.println();

			System.out.println("Please enter that service's fee (up to $99,999.99; do not include punctuation besides a \".\"): ");
			while (true) {
				System.out.print("> ");
				try {
					serviceFee = systemInputScanner.nextDouble();
					systemInputScanner.nextLine(); // clear the buffer
					if (serviceFee > 99999.99)
						System.out.println("That data was too large to be stored.");
					else if (!validDecimals(serviceFee))
						System.out.println("Up to two decimal places are allowed.");
					else {
						serviceFees.add(serviceFee);
						break;
					}
				} catch (InputMismatchException error) {
					System.out.println("An example price would be (ignoring the brackets): [19.99].");
					systemInputScanner.nextLine(); // clear the buffer
				}
			}
			System.out.println();

			System.out.println("Would you like to enter another service? (Y/n): ");
			while (true) {
				System.out.print("> ");
				repeatLoop = systemInputScanner.next().toLowerCase();
				systemInputScanner.nextLine(); // clear the buffer
				if (!repeatLoop.equals("y") && !repeatLoop.equals("n"))
					repeatLoop = "y";
				else
					break;
			}
			System.out.println();
		}

		System.out.println("The new provider will have the below information:");
		System.out.println();
		System.out.println("Number:   " + providerNumber);
		System.out.println("Name:     " + name);
		System.out.println("Street:   " + street);
		System.out.println("City:     " + city);
		System.out.println("State:    " + state);
		System.out.println("Zip Code: " + zip);
		for (int i = 0; i < serviceNumbers.size(); i++) {
			System.out.println("Service " + (i+1) + ": ");
			System.out.println("\tNumber: " + serviceNumbers.get(i));
			System.out.println("\tName:   " + serviceNames.get(i));
			System.out.println("\tFee:    $" + serviceFees.get(i));
		}
		System.out.println();

		System.out.println("Would you like to continue with this addition or cancel it? (Y/n): ");
		while (true) {
			System.out.print("> ");
			repeatLoop = "x";
			repeatLoop = systemInputScanner.next().toLowerCase();
			systemInputScanner.nextLine(); // clear the buffer
			if (repeatLoop.equals("n"))
				return false;
			else if (repeatLoop.equals("y"))
				break;
		}

		newProvider = buildProviderJson(name, street, city, state, zip, serviceNumbers, serviceNames, serviceFees);
		providersJson.put(providerNumber, newProvider);
		writeToFile(providerFileLocation, providersJson);
		return true;
	}

	// generateRandomNumber will return a string number that is as many digits as passed in to be.
	// NOTE: this function isn't super efficient, so if adding a new member/provider/provider's service
	// starts taking too long to select a number, optimise this function.
	protected String generateRandomNumber(int digits) {
		String randomNumber = "";
		for (int i = 0; i < digits; i++)
			randomNumber += (int) (Math.random() * 10);
		return randomNumber;
	}

	// validateString will test if theString isn't an int or a double, allowing for spaces.
	protected boolean validateString(String theString) {
		for (int i = 0; i < theString.length(); i++)
			if (!Character.isLetter(theString.charAt(i)))
				if(theString.charAt(i) != ' ')
					return false;
		return true;
	}

	// validateZipCode will only validate that the passed string is made entirely of digits.
	protected boolean validateZipCode(String theZip) {
		for (int i = 0; i < theZip.length(); i++)
			if (!Character.isDigit(theZip.charAt(i)))
				return false;
		return true;
	}

	// validDecimals will check if the double has too many decimals to be a money,
	// returning true if is valid.
	protected boolean validDecimals(Double theDouble) {
		int decimalSize = 0;
		String theNumber = "";
		theNumber += theDouble;
		for (int i = 0; i < theNumber.length(); i++) {
			if (theNumber.charAt(i) == '.') {
				decimalSize = theNumber.length() - i-1;
				if (decimalSize <= 2)
					return true;
				else
					return false;
			}
		}

		return true;
	}

	// buildProviderJson will take the passed info needed to make a new entry in the provider
	// json, and returns the value to be the value in the providerNumber-providerInfo pair.
	protected JSONObject buildProviderJson(String name, String street, String city, String state, String zip, ArrayList<String> serviceNumbers, ArrayList<String> serviceNames, ArrayList<Double> serviceFees) {
		JSONObject newProvider = new JSONObject();
		JSONObject providerNumber = new JSONObject();
		JSONObject serviceNumbersJson = new JSONObject();

		for (int i = 0; i < serviceNumbers.size(); i++) {
			JSONObject serviceInfo = new JSONObject();
			serviceInfo.put("name", serviceNames.get(i));
			serviceInfo.put("fee", serviceFees.get(i));
			serviceNumbersJson.put(serviceNumbers.get(i), serviceInfo);
		}
		newProvider.put("serviceNumbers", serviceNumbersJson);

		newProvider.put("name", name);
		newProvider.put("street", street);
		newProvider.put("city", city);
		newProvider.put("state", state);
		newProvider.put("zipCode", zip);

		return newProvider;
	}

	// TODO: this function
	public boolean editProvider() {
		return true;
	}

	// TODO: this function
	public boolean deleteProvider() {
		return true;
	}
}

