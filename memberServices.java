//Class declaration for memberServices
//sub-architecture 1
//allows the provider to add services provided to member for billing

import java.io.File;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileReader;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.io.FileNotFoundException;

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
  //protected int [] memDirectory = new int[size];
  //protected ChocAnSystem CAS = new ChocAnSystem();
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

  protected boolean createFile(int provider, int service, String fileLocation, JSONObject object)
  {
    if (!(fileLocation.equals(fileLocation)))
        return false;

    DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
    Date date = new Date();
    String currentDate = new String(dateFormat.format(date));

    try {
    File outputFile = new File("./reports/"+currentDate);
    if(!outputFile.exists()){
      outputFile.createNewFile();
    }
    BufferedWriter outputFileWriter = new BufferedWriter(new FileWriter(outputFile,true));
    outputFileWriter.write(JSONValue.toJSONString(object));
        outputFileWriter.close();

    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }

    //will add json object writeToFile(outputFile, JSONobject);
    return true;
  }

public JSONObject returnKey(int number, String providerFileLocation) {
    String ID = String.format ("%09d", number);
    try{
        FileReader reader = new FileReader(providerFileLocation);
        JSONObject member = (JSONObject) parser.parse(reader);

        System.out.println(ID);
        //var.put("test","test"); //
        //System.out.println(var);

        return null;
    }
    catch(FileNotFoundException e){e.printStackTrace();}
    catch(IOException e){e.printStackTrace();}
    catch(ParseException e){e.printStackTrace();}
    catch(Exception e){e.printStackTrace();}
    return null;
  }
public JSONObject returnSub(int number, String providerFileLocation) {
    String ID = String.format ("%09d", number);

    JSONParser parser = new JSONParser();

    try{
        FileReader reader = new FileReader(providerFileLocation);
        JSONObject member = (JSONObject) parser.parse(reader);

        //System.out.println(member);
        JSONObject var = (JSONObject) member.get(ID);
        //System.out.println(var);
        var.put("kanye","west"); //
        //System.out.println(var);

        return var;
    }
    catch(FileNotFoundException e){e.printStackTrace();}
    catch(IOException e){e.printStackTrace();}
    catch(ParseException e){e.printStackTrace();}
    catch(Exception e){e.printStackTrace();}
    return null;
}
public void read(int number, String providerFileLocation)
{
    String ID = String.format ("%09d", number);
    String NESTED = String.format ("%09d", ID);

    JSONParser parser = new JSONParser();
    try{
        Object obj = parser.parse(new FileReader(providerFileLocation));
        JSONObject providerJSON = (JSONObject) obj;
        JSONObject list = (JSONObject) providerJSON.get(ID);

        //JSONObject services = (JSONObject) list.get("serviceNumbers") ;
        //String name = (String) list.get(ID);
        //System.out.println("Provider name: " + myJSON);

        /*String name = (String) list.get("name");
        String street = (String) list.get("street");
        String city = (String) list.get("city");
        String state = (String) list.get("state");
        String zip = (String) list.get("zipCode");

        System.out.println("Provider number: " + ID);
        System.out.println("Provider street: " + street);
        System.out.println("Provider city: " + city);
        System.out.println("Provider state: " + state);
        System.out.println("Provider zip code: " + zip);*/

        //printServices(services);
        //writeToFile();

    }
    catch(FileNotFoundException e){e.printStackTrace();}
    catch(IOException e){e.printStackTrace();}
    catch(ParseException e){e.printStackTrace();}
    catch(Exception e){e.printStackTrace();}
}

protected JSONObject buildProviderFile(int provider, int member, int service, String date) {
    JSONObject output = new JSONObject();
    JSONObject nested = new JSONObject();

    output.put(provider,buildProviderFileSub(member,service,date));

    return output;
}

protected JSONObject buildProviderFileSub( int member, int service, String date) {
    JSONObject output = new JSONObject();

    output.put(date,buildProviderFileSubSub(member, service));

    return output;
}

protected JSONObject buildProviderFileSubSub(int member, int service) {
    JSONObject output = new JSONObject();

    output.put("ass",service);

    return output;
}

protected boolean writeToFile(String fileLocation, JSONObject newDirectory) {
    try {
        //if (!directoryValidation(fileLocation))
            //return false;
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
}
/*Provider file
{
"11-25-2018":					←DATE SERVICE OCCURED
 	{“Provider number”: “0000”,
  "Member number":"44444",
   "Member name":"alex",
 	  "Current data/time":"11-25-2018 HH:MM:SS",
 	  “Service name”: “spa”
 	  "Service code":"999",
 	  "Service fee":"$10",
 	  “Comments”: “comments”
 	},
 "11-30-2018":					←DATE SERVICE OCCURED
 	{“Provider number”: “0000”,
  "Member number":"44444",
   "Member name":"alex",
 	  "Current data/time":"11-25-2018 HH:MM:SS",
 	  “Service name”: “spa”
 	  "Service code":"999",
 	  "Service fee":"$10",
 	  “Comments”: “comments”
 	}
 }*/

/*Member file
 {"11-25-2018":					←DATE SERVICE OCCURED
 	{“member number”: “000”,
  "Provider number":"44444",
   "provider name":"alex",
 	  "Service name":"Spa",
 	},
 "11-30-2018":					←DATE SERVICE OCCURED
 	{“member number”: “000”,
  "Provider number":"44444",
   "provider name":"alex",
 	  "Service name":"Spa",
 	}
 }*/