import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;
import java.io.BufferedWriter;
import java.io.FileWriter;


public class MemberReport {

    protected String memberFileLocation = "./directories/member_directory";                                                         //main member directory
    protected String memberServices = "./services/member_services";      //file with all member services


    //reads the member directory to find a specific member
    public void read(int number) {

        String ID = String.format("%09d", number);                              //convert int to string fro json

        try (FileReader reader = new FileReader(memberFileLocation)) {          //open member directory

            //parse through file
            JSONParser parser = new JSONParser();
            JSONObject member = (JSONObject) parser.parse(reader);

            //if ID is in file
            if (member.containsKey(ID)) {

                JSONObject memberJSON = (JSONObject) member.get(ID);

                parseMemberInfo(memberJSON, ID);

            }

            //if not ID in file
            else {
                System.out.println("ERROR: Member not existing in directory.\n");
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //if an ID is found, parse through that keys details
    private void parseMemberInfo(JSONObject ID, String number) {

        System.out.println("\nHere is the members information:\n");

        String name = (String) ID.get("name");
        System.out.println("Member name: "+ name);

        String street = (String) ID.get("street");
        System.out.println("Street: " + street);

        String city = (String) ID.get("city");
        System.out.println("City: " + city);

        String state = (String) ID.get("state");
        System.out.println("State: " + state);

        String zip = (String) ID.get("zipCode");
        System.out.println("Zip code: " + zip);

        writeMemberInfoFile(name, number, street, city, state, zip);
    }

    //once all info is found, write onto the memberReport file
    private void writeMemberInfoFile(String Name, String Number, String Street, String City, String State, String Zip) {
        JSONObject memberReport = new JSONObject();
        JSONObject info = new JSONObject();

        info.put("ID Number", Number);
        info.put("name", Name);
        info.put("street", Street);
        info.put("city", City);
        info.put("state", State);
        info.put("zipCode", Zip);
        info.put("services", readService(Number));                  //readService returns an entire JSON object of services

        //put all info found into memberReport
        memberReport.put(Number, info);

        if (memberInfoHelper(Name, memberReport, Number))
            System.out.println("\nSuccessfully wrote to file!\n");
        else
            System.out.println("ERROR: File did not write.\n");
    }

    //function that creates the actual new memberReport
    private boolean memberInfoHelper(String Name, JSONObject memberReport, String Number) {

        try {
            Date date = new Date();                     //generates the current date to name the memberReport
            String currentDate = date.func();

            String fileName = Name + "_" + currentDate + ".json";

            File outputFile = new File(fileName);

            //this will work and update under the assumption that each member generates a memberReport only once per day
            if (!outputFile.exists()) {

                outputFile.createNewFile();

                BufferedWriter outFile = new BufferedWriter(new FileWriter(outputFile));
                outFile.write(JSONValue.toJSONString(memberReport));

                outFile.close();

            }

            else {

             //   BufferedWriter outFile = new BufferedWriter(new FileWriter(outputFile));
             //   outFile.write(JSONValue.toJSONString(memberReport));
             //   outFile.close();
            }

        } catch (IOException error) {
            error.printStackTrace();
            return false;
        }

        return true;
    }

    //reads all of the services member has had, returns a JSON object to put into file
    public JSONObject readService(String Number) {

        //open file
        try (FileReader reader = new FileReader(memberServices)) {

            JSONParser parser = new JSONParser();
            JSONObject services = (JSONObject) parser.parse(reader);

            JSONObject serviceReport = new JSONObject();
            JSONObject info = new JSONObject();

            //if ID number is inside file
            if (services.containsKey(Number)) {

                JSONObject serviceDetails = (JSONObject) services.get(Number);
                System.out.println("\nHere are the services the member has had done:\n");

                //loops though all the services done for member
                for (Object key : serviceDetails.keySet()) {
                    String date = (String) key;

                    Object serviceInfo = serviceDetails.get(date);
                    System.out.println("Date of Service: " + date);

                    String providerName = (String) ((JSONObject) serviceInfo).get("Provider name");
                    String serviceName = (String) ((JSONObject) serviceInfo).get("Service name");

                    info.put("Provider name", providerName);
                    info.put("Service name", serviceName);

                    System.out.println("Provider Name: " + providerName);
                    System.out.println("Service: " + serviceName);

                    serviceReport.put(date, info);

                }

                return serviceReport;               //return JSON object created
            }


            else {
                System.out.println("Member has not had any services done.\n");
            }

            return serviceReport;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;

    }
}
