//Class declaration for memberServices
//sub-architecture 1
//allows the provider to add services provided to member for billing

import java.io.File;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileReader;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;



public class memberServices{

  
  //data fields
  protected int provider; //9-digit provider# 
  protected int member;  // 9-digit provider#
  protected String comments;
  protected int size = 20;
  //protected int [] memDirectory = new int[size];
  //protected ChocAnSystem CAS = new ChocAnSystem();
  protected OperatorMode OM = new OperatorMode();
  protected JSONParser parser = new JSONParser();
  protected JSONObject serviceReport = new JSONObject();


  /*
   *Move these into user class

  public String getString(){
    String returnThis = new String();
    returnThis = CAS.inputScanner.nextLine();
    return returnThis;
  }

  public int getInt(){
    int return_this;
    return_this = CAS.inputScanner.nextInt();
    return return_this;
  }
  */

  protected boolean isValid(int number, String memberFileLocation){
    //check if member number is appropriate length
    if(number < 100000000 || number > 999999999){
      return false;
    }

    try{
    Object obj = parser.parse(new FileReader(memberFileLocation));
    }
    catch(IOException e){
      e.printStackTrace();
    }
    catch(ParseException e){
      e.printStackTrace();
    }

    member = (int) serviceReport.get("memberNumber");
    if(member == number) return true;
    
    return false;

  }
}
