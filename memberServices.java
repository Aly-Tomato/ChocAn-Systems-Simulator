//Class declaration for memberServices
//sub-architecture 1
//allows the provider to add services provided to member for billing

import java.io.File;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileReader;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class memberServices{

  
  //data fields
  protected int provider; //9-digit provider# 
  //protected int member;  // 9-digit provider#
  protected String status;
  protected String comments;
  protected int size = 20;
  protected OperatorMode OM = new OperatorMode();
  protected JSONParser parser = new JSONParser();
  //protected JSONObject serviceReport = new JSONObject();

  protected boolean isValid(int number, String memberFileLocation){
      //check if member number is appropriate length
      if(number < 100000000 || number > 999999999){
        return false;
      }

      //Parse file to find member number
      try{
        Object obj = parser.parse(new FileReader(memberFileLocation));
        JSONObject jsonObject = (JSONObject) obj;
        
        String member = new String();
        member = (String)jsonObject.get("memberNumber");
        int value = Integer.parseInt(member);

        
        if(value == number) return true;
      }
      catch(IOException e){
        System.out.println("exception err");
        e.printStackTrace();
      }
      catch(ParseException e){
        System.out.println("parse err");
        e.printStackTrace();
      }

      
      return false;
}

  protected boolean isSuspended(int number, String memberFileLocation){
      //check if member number is appropriate length
      if(number < 100000000 || number > 999999999){
        return false;
      }

      //Parse file to find member number
      try{
        Object obj = parser.parse(new FileReader(memberFileLocation));
        JSONObject jsonObject = (JSONObject) obj;
        status = new String();
        status = (String)jsonObject.get("Status");
        if(status == "Suspended") return true;
      }
      catch(IOException e){
        e.printStackTrace();
      }
      catch(ParseException e){
        e.printStackTrace();
      }
      return false;
    }

  protected boolean writeReport(int provider, int service, String fileLocation)
  {
		/* General Json Structure for provider_report 
		 * 
		 * {
     *  "date" (MM-DD-YYYY):{
		 * 	  "time" (HH:MM:SS), 
		 * 	  "dateServProvided" (MM-DD-YYYY), 
		 * 		"providerNumber": (9digits;string),
		 * 	  "memberNumber" (9 digits; string),
		 * 	  "serviceCode" (6 digits; string),
		 * 		"comments": provider comments(100 characters; string),
		 * 	}
		 */
    DateFormat dateFormat = new SimpleDateFormat("MM-DD-YYYY");
    Date date = new Date();
    String filename = new String(provider+"_"+service+"_"+dateFormat.format(date)); 

    try { 
    File outputFile = new File(filename);
    if(!outputFile.exists()){
      outputFile.createNewFile();
    }
    BufferedWriter outputFileWriter = new BufferedWriter(new FileWriter(outputFile));

    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
    return true;
 }

}
