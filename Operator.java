import java.util.ArrayList;
import java.util.Map;
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
	// NOTE: maxLength means the item cannot be larger than that length.
	protected int stakeholderNumberSetLength;
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
		System.out.println("\tAdd a Provider:");
		if (!directoryValidation(providerFileLocation))
			System.out.println("No " + providerFileLocation + " exists; file with empty data created.");

		try (FileReader reader = new FileReader(providerFileLocation)) {

			if (addProviderHelper(systemInputScanner, reader)) {
				System.out.println("The provider was successfully updated and saved.");
				return true;
			} else {
				System.out.println("The provider directory was not updated.");
				return false;
			}

		} catch (IOException error) {
			error.printStackTrace();
			return false;
		} catch (ParseException error) {
			error.printStackTrace();
			return false;
		}
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
		printProviderInfo("The new provider will have the below information:", providerNumber, name, street, city, state, zip, serviceNumbers, serviceNames, serviceFees);
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
		System.out.println("Please enter the new provider's ZIP code (" + zipSetLength + " characters): ");
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

	// printProviderInfo will print the provider's information in a nice format, taking either all the info seperately or as a json object.
	protected void printProviderInfo(String message, String providerNumber, String name, String street, String city, String state, String zip, ArrayList<String> serviceNumbers, ArrayList<String> serviceNames, ArrayList<Double> serviceFees) {
		System.out.println(message);
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
	}
	protected void printProviderInfo(String message, JSONObject providersJson, String providerNumber) {
		// verify the key is actually in provider_directory
		if (!providersJson.containsKey(providerNumber)) {
			System.out.println("The provider number " + providerNumber + " could not be found.");
			return;
		}

		ArrayList<String> serviceNumbers = new ArrayList<String>();
		ArrayList<String> serviceNames = new ArrayList<String>();
		ArrayList<Double> serviceFees = new ArrayList<Double>();

		String[] providerInfo = extractProviderInfo(providersJson, providerNumber, serviceNumbers, serviceNames, serviceFees);

		printProviderInfo(message, providerNumber, providerInfo[0], providerInfo[1], providerInfo[2], providerInfo[3], providerInfo[4], serviceNumbers, serviceNames, serviceFees);
	}

	// extractProviderInfo will return an array (see below pseudocode) that will contain the string data of the passed json w/
	// the passed providerNumber, and will edit the passed ArrayLists. It will add services numbers/names/fees to the already
	// created ArrayLists, and not clear or malloc them.
	// 	String[] providerStrings = [name, street, city, state, zip]
	// NOTE: This function assumes the providerNumber is a valid key of providersJson. Don't use w/o first checking, unknown behavior could occur.
	protected String[] extractProviderInfo(JSONObject providersJson, String providerNumber, ArrayList<String> serviceNumbers, ArrayList<String> serviceNames, ArrayList<Double> serviceFees) {
		String[] providerStrings = new String[5];
		JSONObject providerJson = (JSONObject) providersJson.get(providerNumber);
		providerStrings[0] = (String) providerJson.get("name");
		providerStrings[1] = (String) providerJson.get("street");
		providerStrings[2] = (String) providerJson.get("city");
		providerStrings[3] = (String) providerJson.get("state");
		providerStrings[4] = (String) providerJson.get("zipCode");

		JSONObject services = (JSONObject) providerJson.get("serviceNumbers");
		services.forEach((key, value) -> {
			serviceNumbers.add((String) key);
			serviceNames.add((String) ((JSONObject) value).get("name"));
			serviceFees.add((Double) ((JSONObject) value).get("fee"));
		});
		return providerStrings;
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

	/**
	 * editProvider will prompt the user for which aspect of a provider in provider_directory to change, and save that changed provider.
	 *
	 * @param systemInputScanner The Scanner(System.in) variable.
	 * @return true if an edit was successfully written, false else.
	 */
	public boolean editProvider(Scanner systemInputScanner) {
		System.out.println("\tEdit a Provider:");
		if (!directoryValidation(providerFileLocation))
			System.out.println("No " + providerFileLocation + " exists; file with empty data created.");

		try (FileReader reader = new FileReader(providerFileLocation)) {

			if (editProviderHelper(systemInputScanner, reader)) {
				System.out.println("The provider was successfully updated and saved.");
				return true;
			} else {
				System.out.println("The provider directory was not updated.");
				return false;
			}

		} catch (IOException error) {
			error.printStackTrace();
			return false;
		} catch (ParseException error) {
			error.printStackTrace();
			return false;
		}
	}

	protected boolean editProviderHelper(Scanner systemInputScanner, FileReader reader) throws IOException, ParseException {
		JSONParser jsonParser = new JSONParser();
		JSONObject providersJson = (JSONObject) jsonParser.parse(reader);
		int valueToChange;
		boolean doneEditing = false;

		// have the user select the provider to edit
		String providerNumber = selectUniqueNumber(systemInputScanner, providersJson, stakeholderNumberSetLength);

		JSONObject providerJson = (JSONObject) providersJson.get(providerNumber);
		// output the current provider info; have the user select what to edit
		while (!doneEditing) {

			System.out.println();
			printProviderInfo("This is the provider's current information", providersJson, providerNumber);
			System.out.println();
			System.out.println("What aspect would you like to change?");
			System.out.println();
			System.out.println("\t0) Discard changes");
			System.out.println("\t1) Save changes");
			System.out.println("\t2) Randomly generate a new provider key");
			System.out.println("\t3) Name");
			System.out.println("\t4) Street");
			System.out.println("\t5) City");
			System.out.println("\t6) State");
			System.out.println("\t7) Zip Code");
			System.out.println("\t8) Services Offered");
			System.out.println();
			System.out.print("> ");

			valueToChange = systemInputScanner.nextInt();
			systemInputScanner.nextLine(); // clear the buffer
			switch (valueToChange) {
				case 0:
					return false;
				case 1:
					doneEditing = true;
					break;
				case 2:
					// change the provider number and rekey the provider json
					String newKey = generateNewKey(stakeholderNumberSetLength, providersJson);
					providerNumber = rekeyUniqueNumber(providerNumber, newKey, providersJson);
					System.out.println("The new provider number is " + providerNumber);
					break;
				case 3:
					// change the name
					String name = promptForString(systemInputScanner, "name", stakeholderNameMaxLength);
					providerJson.replace("name", name);
					System.out.println("The new provider name is " + name);
					break;
				case 4:
					// change the street
					String street = promptForStreet(systemInputScanner);
					providerJson.replace("street", street);
					System.out.println("The new provider street is " + street);
					break;
				case 5:
					// change the city
					String city = promptForString(systemInputScanner, "city", cityMaxLength);
					providerJson.replace("city", city);
					System.out.println("The new provider city is " + city);
					break;
				case 6:
					// change the state
					String state = promptForState(systemInputScanner);
					providerJson.replace("state", state);
					System.out.println("The new provider state is " + state);
					break;
				case 7:
					// change the zip
					String zip = promptForZip(systemInputScanner);
					providerJson.replace("zipCode", zip);
					System.out.println("The new provider ZIP Code is " + zip);
					break;
				case 8:
					// edit the services
					editProviderServices(systemInputScanner, providerJson);
					break;
				default:
					System.out.println("The input of [" + valueToChange + "] is not valid; try again.");
					break;
			}
		}

		// save changes to disk
		providersJson.replace(providerNumber, providerJson);
		writeToFile(providerFileLocation, providersJson);
		return true;
	}

	// selectUniqueNumber will output the stakeholder or service numbers and names in the passed json,
	// and handle prompting the user for a valid stakeholder's number which will be returned.
	protected String selectUniqueNumber(Scanner systemInputScanner, JSONObject thesJson, int length) {
		String theNumber = "";
		System.out.println("Please select enter the ID number you would like to work with:");
		System.out.println();
		thesJson.forEach((key, value) -> {
				System.out.print((String) ((JSONObject) value).get("name"));
				System.out.print(": ");
				System.out.println((String) key);
		});

		while (true) {
			System.out.print("> ");
			theNumber = systemInputScanner.next();
			systemInputScanner.nextLine(); // clear the buffer
			if (theNumber.length() != length)
				System.out.println("That entry is an incorrect length; it must be " + length + " characters long.");
			else if (!thesJson.containsKey(theNumber))
				System.out.println("Invalid the number. Must be made of integers only and be present on the above list.");
			else
				return theNumber;
		}
	}

	// rekeyUniqueNumber will change the stakeholder key to the new one, and return the new key.
	// NOTE: rekeyUniqueNumber assumes the oldNumber is a valid stakeholder key and newNumber
	// is an unused stakeholder key; use w/ invalid keys may result in undesired behavior.
	// For best calling, use the below example (taken from editProviderHelper):
	// providerNumber = rekeyUniqueNumber(providerNumber, generateNewKey(stakeholderNumberSetLength, providersJson), providersJson);
	protected String rekeyUniqueNumber(String oldNumber, String newNumber, JSONObject theJson) {
		JSONObject valueInfo = (JSONObject) theJson.remove(oldNumber);
		theJson.put(newNumber, valueInfo);
		return newNumber;
	}

	// editProviderServices will allow for editing of the different services offered by the passed provider.
	protected void editProviderServices(Scanner systemInputScanner, JSONObject providerJson) {
		int valueToChange;
		String serviceNumber = selectUniqueNumber(systemInputScanner, (JSONObject) providerJson.get("serviceNumbers"), serviceNumberSetLength);
		JSONObject servicesJson = (JSONObject) providerJson.get("serviceNumbers");
		JSONObject serviceJson = (JSONObject) servicesJson.get(serviceNumber);
		while (true) {
			System.out.println();
			System.out.println("These are the current service's information");
			System.out.println();
			System.out.println("Number: " + serviceNumber);
			System.out.println("Name:   " + (String) serviceJson.get("name"));
			System.out.println("Fee:    " + (Double) serviceJson.get("fee"));
			System.out.println();
			System.out.println("Which service aspect would you like to edit?");
			System.out.println();
			System.out.println("\t0) Done editing service");
			System.out.println("\t1) Delete this service");
			System.out.println("\t2) Randomly generate a new service key");
			System.out.println("\t3) Name");
			System.out.println("\t4) Fee");
			System.out.println();
			System.out.print("> ");

			valueToChange = systemInputScanner.nextInt();
			systemInputScanner.nextLine(); // clear the buffer
			switch (valueToChange) {
				case 0:
					// save changes
					providerJson.replace(serviceNumber, serviceJson);
					return;
				case 1:
					// delete the service
					servicesJson.remove(serviceNumber);
					System.out.println("The service was deleted");
					return;
				case 2:
					// generate a new serive key
					String newKey = generateNewKey(serviceNumberSetLength, servicesJson);
					serviceNumber = rekeyUniqueNumber(serviceNumber, newKey, servicesJson);
					System.out.println("The new service number is " + serviceNumber);
					break;
				case 3:
					// get the new name
					String name = promptForString(systemInputScanner, "service name", serviceNameMaxLength);
					serviceJson.replace("name", name);
					System.out.println("The new service name is " + name);
					break;
				case 4:
					// get the new fee
					Double fee = promptForServiceFee(systemInputScanner);
					serviceJson.replace("fee", fee);
					System.out.println("The new service fee is $" + fee);
					break;
				default:
					System.out.println("The input of [" + valueToChange + "] is not valid; try again.");
					break;
			}
		}
	}

	/**
	 * deleteProvider will prompt the user for provider in provider in provider_directory to delete.
	 *
	 * @param systemInputScanner The Scanner(System.in) variable.
	 * @return true if a provider was deleted and successfully written, false else.
	 */
	public boolean deleteProvider(Scanner systemInputScanner) {
		System.out.println("\tDelete a Provider:");
		if (!directoryValidation(providerFileLocation)) {
			System.out.println("No " + providerFileLocation + " exists; file with empty data created.");
			System.out.println("An empty file has no data; therefore, there are not providers to delete.");
			return false;
		}

		try (FileReader reader = new FileReader(providerFileLocation)) {

			JSONParser jsonParser = new JSONParser();
			JSONObject providersJson = (JSONObject) jsonParser.parse(reader);

			// have the user select the provider to delete
			String providerNumber = selectUniqueNumber(systemInputScanner, providersJson, stakeholderNumberSetLength);

			// delete provider and save changes to disk
			providersJson.remove(providerNumber);
			writeToFile(providerFileLocation, providersJson);

		} catch (IOException error) {
			error.printStackTrace();
			return false;
		} catch (ParseException error) {
			error.printStackTrace();
			return false;
		}

		System.out.println("The provider was successfully deleted.");
		return true;
	}
}

