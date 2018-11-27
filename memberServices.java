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
  protected Operator OM = new Operator();
  protected JSONParser parser = new JSONParser();

  protected boolean isValid(int number, String FileLocation){
      //check if member number is appropriate length
     if(number < 100000000 || number > 999999999){
       return false;
     }

     String ID = String.format("%09d", number);
     JSONParser parser = new JSONParser();
     try{
         //Object obj = parser.parse(new FileReader("providerDirectory.json"));
         Object obj = parser.parse(new FileReader(FileLocation));
         JSONObject providerJSON = (JSONObject) obj;
         JSONObject list = (JSONObject) providerJSON.get(ID);

         return true;

     }
     //catch(FileNotFoundException e){e.printStackTrace();}
     catch(IOException e){e.printStackTrace();}
     catch(ParseException e){e.printStackTrace();}
     catch(Exception e){e.printStackTrace();}


     return false;

  }

  protected boolean isSuspended(int number, String memberFileLocation){
      //check if member number is appropriate length
      if(number < 100000000 || number > 999999999){
        return false;
      }

     String ID = String.format("%09d", number);
     JSONParser parser = new JSONParser();
     try{
         Object obj = parser.parse(new FileReader(memberFileLocation));
         JSONObject providerJSON = (JSONObject) obj;
         JSONObject list = (JSONObject) providerJSON.get(ID);

         String status = (String) list.get("status");

         System.out.println(status);

         return status.equals("Suspended");

     }
     catch(IOException e){e.printStackTrace();}
     catch(ParseException e){e.printStackTrace();}
     catch(Exception e){e.printStackTrace();}


     return false;
  }
/*
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
  */

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
     * 	*/

      if (!(fileLocation.equals(fileLocation)))
          return false;

      DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
      Date date = new Date();
      String filename = new String(provider+"_"+service+"_"+dateFormat.format(date));
        //System.out.println(filename);

      try {
      File outputFile = new File("./reports/"+filename);
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
