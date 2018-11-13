/**
 * OperatorMode will allow editing of member_directory and provider_directory.
 *
 * @author  Medina Lamkin and Sawyer Watts
 * @version 1.0
 * @since   2018-11-13 
 */
import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class OperatorMode {
	protected String memberFileLocation;
	protected String providerFileLocation;

	/**
	 * Constructor
	 *
	 * @param theMemberFileLocation The String of the relative location to the member_directory.
	 * @param theProviderFileLocation The String of the relative location to the provider_directory.
	 */
	public OperatorMode(String theMemberFileLocation, String theProviderFileLocation) {
		memberFileLocation = theMemberFileLocation;
		providerFileLocation = theProviderFileLocation;
	}

	/**
	 * Constructor
	 * Sets the file locations to ./directories/member_directory and ./directories/provider_directory, respectively.
	 */
	public OperatorMode() {
		this("./directories/member_directory", "./directories/provider_directory");
	}

	public boolean addMember() {
		return true;
	}

	public boolean editMember() {
		return true;
	}

	public boolean deleteMember() {
		return true;
	}

	public boolean addProvider() {
		return true;
	}

	public boolean editProvider() {
		return true;
	}

	public boolean deleteProvider() {
		return true;
	}


	private boolean writeToFile(String fileLocation, JSONObject newDirectory) {
		if (!(fileLocation.equals(memberFileLocation) || fileLocation.equals(providerFileLocation)))
			return false;

		try {
			File outputFile = new File(fileLocation);
			if (!outputFile.exists())
				outputFile.createNewFile();
			BufferedWriter outputFileWriter = new BufferedWriter(new FileWriter(outputFile));

			outputFileWriter.write(JSONValue.toJSONString(newDirectory));
			outputFileWriter.close();

		} catch (IOException error) {
			error.printStackTrace();
			return false;
		}

		return true;
	}
}

