import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.FileNotFoundException;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;



public class ProviderReport {

    protected String providerFileLocation = "./directories/provider_directory";                                                         //main member directory
    protected String providerServices = "./services/provider_services";      //file with all memb

    //function for writing out to the file
    private void writeProviderInfoFile(String Name, String Number, String Street, String City, String State, String Zip) {
        //output to file
        JSONObject providerReport = new JSONObject();
        JSONObject info = new JSONObject();

        //input info to the file
        info.put("ID Number", Number);
        info.put("name", Name);
        info.put("street", Street);
        info.put("city", City);
        info.put("state", State);
        info.put("zipCode", Zip);

        readServices(Number);
        info.put("Services: ", readServices(Number));

        providerReport.put(Number, info);

        //check if the file has been written successfully
        if (providerInfoHelper(Name, providerReport, Number))
            System.out.println("Success\n");
        else
            System.out.println("FAIL\n");

    }

    private  boolean providerInfoHelper(String Name, JSONObject providerReport, String Number) {

        try {
            //name the file with provider name and current date
            Date date = new Date();
            String currentDate = date.func();

            String fileName = Name + "_" + currentDate + ".json";

            File outputFile = new File(fileName);

            if (!outputFile.exists()) {

                outputFile.createNewFile();

                BufferedWriter outFile = new BufferedWriter(new FileWriter(outputFile));
                outFile.write(JSONValue.toJSONString(providerReport));

                outFile.close();
            }

            else {
                //memberfile already exists, do they have more services?
            }

        } catch (IOException error) {
            error.printStackTrace();
            return false;
        }

        return true;
    }

    //read the .json file and parse info
    public void read(int number)
    {
        String ID = String.format ("%09d", number);

        try(FileReader reader = new FileReader(providerFileLocation)){
            JSONParser parser = new JSONParser();
            JSONObject providerJSON = (JSONObject) parser.parse(reader);

            if(providerJSON.containsKey(ID)){
                //get the value object with the key: ID
                JSONObject list = (JSONObject) providerJSON.get(ID);

                //get the value information with keys
                String name = (String) list.get("name");
                String street = (String) list.get("street");
                String city = (String) list.get("city");
                String state = (String) list.get("state");
                String zip = (String) list.get("zipCode");
                //echo on the screen
                System.out.println("\nProvider name: " + name);
                System.out.println("Provider number: " + ID);
                System.out.println("Provider street: " + street);
                System.out.println("Provider city: " + city);
                System.out.println("Provider state: " + state);
                System.out.println("Provider zip code: " + zip + "\n");

                //write to an output file
                writeProviderInfoFile(name,ID,street,city,state,zip);
            }
            else{
                System.out.println("\nCannot find ID number!\n");
            }

        }
        catch(FileNotFoundException e){e.printStackTrace();}
        catch(IOException e){e.printStackTrace();}
        catch(ParseException e){e.printStackTrace();}
        catch(Exception e){e.printStackTrace();}
    }

    //function for  changing string type into double type
    public double stringToDouble(String input){
        //get rid of the first character, which is $
        String feeString = input.substring(1,input.length());
        double fee = Double.parseDouble(feeString);
        return fee;
    }

    //open another file to parse the service info being provided by eaech provider
    public JSONObject readServices(String number){
        JSONParser parser = new JSONParser();

        try{
            FileReader reader = new FileReader(providerServices);
            //Json parser
            JSONObject providerServices = (JSONObject) parser.parse(reader);

            //check if the number is valid
            if(providerServices.containsKey(number)){
                JSONObject services = (JSONObject) providerServices.get(number);
                JSONObject serviceReport = new JSONObject();
                JSONObject info = new JSONObject();

                double totalFee = 0, fee; //value for storing the total fee each provider should be paid
                int totalConsultation = 0;  //value for  storing the total consultations each provider has provided

                //walk through each key no matter what it is
                for(Object key:services.keySet()){
                    String date = (String) key;

                    Object serviceInfo = services.get(date);
                    System.out.println("\nDate of service: " + date);

                    String currentDateTime = (String) ((JSONObject) serviceInfo).get("Current dateTime");
                    String memberName = (String) ((JSONObject) serviceInfo).get("Member name");
                    String memberNumber = (String) ((JSONObject) serviceInfo).get("Member number");
                    String serviceCode = (String) ((JSONObject) serviceInfo).get("Service code");
                    String feeString = (String) ((JSONObject) serviceInfo).get("Service fee");

                    //change the type for calculating total fee
                    fee = stringToDouble(feeString);
                    totalFee += fee;;
                    totalConsultation ++;

                    System.out.println("Current date and time: " + currentDateTime);
                    System.out.println("Member name: " + memberName);
                    System.out.println("Member number: " + memberNumber);
                    System.out.println("Service code: " + serviceCode);
                    System.out.println("Fee to be paid: " + fee + "\n");

                    info.put("Date of service: ",date);
                    info.put("Current date and time: ",currentDateTime);
                    info.put("Member name: ",memberName);
                    info.put("Member number: ",memberNumber);
                    info.put("Service code: ",serviceCode);
                    info.put("Fee to be paid: ",fee);

                    //write a nested json into the file
                    serviceReport.put(date, info);

                }

                System.out.println("\nTotal number of consultations with members: " + totalConsultation);
                System.out.println("Total fee to be paid: " + totalFee + "\n");

                serviceReport.put("Total number of consultations with members: ",totalConsultation);
                serviceReport.put("Total fee to be paid: ",totalFee);

                return serviceReport;
            }
            //Output


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;

    }

}
