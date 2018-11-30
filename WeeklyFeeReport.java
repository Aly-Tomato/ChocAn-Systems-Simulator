import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


public class WeeklyFeeReport {

    protected String providerServices = "./services/provider_services";      //file
    //parse json file
    public void read(int number)
    {
        String ID = String.format ("%09d", number);
        JSONParser parser = new JSONParser();
        try{
            FileReader reader = new FileReader(providerServices);
            JSONObject serviceReport = (JSONObject) parser.parse(reader);

            //if the id number exists, parse the info and calculate
            if(serviceReport.containsKey(ID)) {
                JSONObject services = (JSONObject) serviceReport.get(ID);

                double totalFee = 0, fee;
                int totalConsiltation = 0;

                //walk through all the dates a provider provided service
                for (Object key : services.keySet()) {
                    String date = (String) key;

                    Object serviceInfo = services.get(date);
                    String feeString = (String) ((JSONObject) serviceInfo).get("Service fee");

                    //calculate the total fee and total consultations
                    fee = stringToDouble(feeString);
                    totalFee += fee;
                    totalConsiltation++;

                }

                System.out.println("\nTotal number of consultations with members: " + totalConsiltation);
                System.out.println("Total fee to be paid: " + totalFee + "\n");
            }
            else{
                System.out.println("\nCannot find ID number!");
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    //convert string type into double type
    public double stringToDouble(String input){
        String feeString = input.substring(1,input.length());
        double fee = Double.parseDouble(feeString);
        return fee;
    }

}
