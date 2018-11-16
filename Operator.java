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
	// NOTE: stakeholder is a member or a provider.
	// NOTE: setLength means the item must be that length.
	protected int stakeholderNumberSetLength; // it must be this length
	protected int stakeholderNameMaxLength;
	protected int serviceNumberSetLength;
	protected int serviceNameMaxLength;
	protected int streetMaxLength;
	protected int cityMaxLength;
	protected int stateSetLength;
	protected int zipSetLength;
	protected double serviceMaxPrice;

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

		stakeholderNumberSetLength = 9;
		stakeholderNameMaxLength = 25;
		serviceNumberSetLength = 6;
		serviceNameMaxLength = 20;
		streetMaxLength = 25;
		cityMaxLength = 14;
		stateSetLength = 2;
		zipSetLength = 5;
		serviceMaxPrice = 99999.99;

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

			if (addProviderHelper(systemInputScanner, reader))
				System.out.println("The new provider was successfully created and saved.");
			else
				System.out.println("The provider directory was not updated.");

		} catch (IOException error) {
			error.printStackTrace();
			return false;
		} catch (ParseException error) {
			error.printStackTrace();
			return false;
		}

		return true;
	}

	protected boolean addProviderHelper(Scanner systemInputScanner, FileReader reader) throws IOException, ParseException {
		ArrayList<String> serviceNumbers = new ArrayList<String>();
		ArrayList<String> serviceNames = new ArrayList<String>();
		ArrayList<Double> serviceFees = new ArrayList<Double>();
		JSONObject newProvider;
		JSONParser jsonParser = new JSONParser();
		JSONObject providersJson = (JSONObject) jsonParser.parse(reader);

		// get unique provider number
		String providerNumber = generateNewKey(stakeholderNumberSetLength, providersJson);

		// get name
		String name = promptForString(systemInputScanner, "name", stakeholderNameMaxLength);
		System.out.println();

		// get street
		String street = promptForStreet(systemInputScanner);
		System.out.println();

		// get city
		String city = promptForString(systemInputScanner, "city", cityMaxLength);
		System.out.println();

		// get state
		String state = promptForState(systemInputScanner);
		System.out.println();

		// get zip
		String zip = promptForZip(systemInputScanner);
		System.out.println();

		// get service informations
		promptForServices(systemInputScanner, serviceNumbers, serviceNames, serviceFees);
		System.out.println();

		// ask to confirm the data
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

		// ask the user if they want to save this provider or cancel
		if (!promptForBool(systemInputScanner, "Would you like to continue with this addition or cancel it?"))
			return false;

		// build, save, and write the new provider
		newProvider = buildProviderJson(name, street, city, state, zip, serviceNumbers, serviceNames, serviceFees);
		providersJson.put(providerNumber, newProvider);
		writeToFile(providerFileLocation, providersJson);
		return true;
	}

	// generateNewKey will return a string number that is a unique key of the passed json or arraylist.
	protected String generateNewKey(int digits, JSONObject jsonData) {
		String newKey = "";
		while (true) {
			for (int i = 0; i < digits; i++)
				newKey += (int) (Math.random() * 10);

			if (!jsonData.containsKey(newKey))
				break;
		}
		return newKey;
	}
	protected String generateNewKey(int digits, ArrayList<String> currentKeys) {
		String newKey = "";
		while (true) {
			for (int i = 0; i < digits; i++)
				newKey += (int) (Math.random() * 10);

			if (!currentKeys.contains(newKey))
				break;
		}
		return newKey;
	}

	// promptForString is a helper function that will prompt the user for the promptName and validate that it is only made of spaces and letters,
	// and that it is not larger than maxLength. This will not work for something like the address which requires a number and then a string,
	// or the zip/state which have other criteria.
	protected String promptForString(Scanner systemInputScanner, String promptName, int maxLength) {
		String returnValue = "";
		System.out.println("Please enter the " + promptName + " (" + maxLength + " characters max): ");
		while (true) {
			System.out.print("> ");
			returnValue = systemInputScanner.nextLine().trim();
			if (returnValue.length() == 0)
				;
			else if (returnValue.length() > maxLength)
				System.out.println("Data is too long to be stored.");
			else if (validateString(returnValue))
				break;
			else
				System.out.println("Please do not enter anything besides characters and spaces.");
		}

		return returnValue;
	}

	// validateString will test if theString isn't an int or a double, allowing for spaces.
	protected boolean validateString(String theString) {
		for (int i = 0; i < theString.length(); i++)
			if (!Character.isLetter(theString.charAt(i)))
				if(theString.charAt(i) != ' ')
					return false;
		return true;
	}

	// promptForStreet is about the same thing as promptForString, but the street has to have
	// a street number followed by a street name, and this requies additional testing.
	protected String promptForStreet(Scanner systemInputScanner) {
		String street = "";
		System.out.println("Please enter the new provider's street address (" + streetMaxLength + " characters max, where the street number is first and the street name is after): ");
		while (true) {
			street = "";
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
				else if (street.length() > streetMaxLength)
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
		return street;
	}

	// promptForState is about the same thing as promptForString, but the state has to be
	// two letters, and this requies additional testing.
	protected String promptForState(Scanner systemInputScanner) {
		String state = "";
		System.out.println("Please enter the new provider's state (" + stateSetLength + " characters): ");
		while (true) {
			System.out.print("> ");
			state = systemInputScanner.nextLine().toUpperCase().trim();
			if (state.length() == 0)
				;
			else if (state.length() != stateSetLength)
				System.out.println("That data was the wrong size, it must be " + stateSetLength + " digits.");
			else if (validateString(state) && Character.isLetter(state.charAt(0)) && Character.isLetter(state.charAt(1)))
				break;
			else
				System.out.println("Please do not enter anything besides characters and spaces.");
		}
		return state;
	}

	protected String promptForZip(Scanner systemInputScanner) {
		String zip = "";
		System.out.println("Please enter the new provider's ZIP code (" + zipSetLength + "characters): ");
		while (true) {
			System.out.print("> ");
			zip = systemInputScanner.nextLine().trim();
			if (zip.length() == 0)
				;
			else if (zip.length() != zipSetLength)
				System.out.println("That data was the wrong size, it must be " + zipSetLength + " digits.");
			else if (validateZipCode(zip))
				break;
			else
				System.out.println("Please do not enter anything besides numbers.");
		}
		return zip;
	}

	// validateZipCode will only validate that the passed string is made entirely of digits.
	protected boolean validateZipCode(String theZip) {
		for (int i = 0; i < theZip.length(); i++)
			if (!Character.isDigit(theZip.charAt(i)))
				return false;
		return true;
	}

	// promptForServices will handle prompting and validating as many services as the user wants to make, and saves them into the passed arraylists.
	protected void promptForServices(Scanner systemInputScanner, ArrayList<String> serviceNumbers, ArrayList<String> serviceNames, ArrayList<Double> serviceFees) {
		while (true) {
			String serviceName = "";
			Double serviceFee;
			serviceNumbers.add(generateNewKey(serviceNumberSetLength, serviceNumbers));

			// get a new service name to add
			serviceNames.add(promptForString(systemInputScanner, "service name", serviceNameMaxLength));
			System.out.println();

			// get a new service fee to associate with the name
			serviceFees.add(promptForServiceFee(systemInputScanner));
			System.out.println();

			// ask if the user wants to make another service
			if (!promptForBool(systemInputScanner, "Would you like to enter another service?"))
				return;
			System.out.println();
		}
	}

	// promptForServiceFee will handle prompting and validating the price for a service.
	protected Double promptForServiceFee(Scanner systemInputScanner) {
		Double serviceFee;
		System.out.println("Please enter that service's fee (up to $" + serviceMaxPrice + "; do not include punctuation besides a \".\"): ");
		while (true) {
			System.out.print("> ");
			try {
				serviceFee = systemInputScanner.nextDouble();
				systemInputScanner.nextLine(); // clear the buffer
				if (serviceFee > serviceMaxPrice)
					System.out.println("That data was too large to be stored.");
				else if (!validDecimals(serviceFee))
					System.out.println("Up to two decimal places are allowed.");
				else {
					break;
				}
			} catch (InputMismatchException error) {
				System.out.println("An example price would be (ignoring the brackets): [19.99].");
				systemInputScanner.nextLine(); // clear the buffer
			}
		}
		return serviceFee;
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

	// promptForBool will ask the user the message string, append " (Y/n)", and return their response.
	protected boolean promptForBool(Scanner systemInputScanner, String message) {
		System.out.println(message + " (Y/n): ");
		while (true) {
			System.out.print("> ");
			String repeatLoop = systemInputScanner.next().toLowerCase();
			systemInputScanner.nextLine(); // clear the buffer
			if (!repeatLoop.equals("y") && !repeatLoop.equals("n"))
				;
			else if (repeatLoop.equals("y"))
				return true;
			else
				return false;
		}
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

