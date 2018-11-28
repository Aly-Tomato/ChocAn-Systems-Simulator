import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;



public class AccountsPayableReport {


    protected String providerServices = "/Users/alexandrallamas/IdeaProjects/cs300_teamcinco4/directories/provider_services.json";      //file
    //write nested information into json file
    public JSONObject readInfo(String number, JSONObject serviceReport){

        if(serviceReport.containsKey(number)){
            JSONObject services = (JSONObject) serviceReport.get(number); //in each provider number
            JSONObject info = new JSONObject();

            double totalFee = 0, fee;
            int totalConsultation = 0;

            //in each date
            for(Object key:services.keySet()){
                totalConsultation ++;
                String providerNumber = (String)key;
                JSONObject dateInfo = (JSONObject)services.get(providerNumber);
                String feeString = (String) dateInfo.get("Service fee");
                fee = stringToDouble(feeString);
                totalFee += fee;
            }

            //write info into the file
            info.put("Total consultations",totalConsultation);
            info.put("Total fee",totalFee);

            return info;
        }

        return null;

    }

    //parse json file
    public void read(int number)
    {
        JSONParser parser = new JSONParser();
        try{
            FileReader reader = new FileReader(providerServices);
            JSONObject serviceReport = (JSONObject) parser.parse(reader);

            //output file
            JSONObject payableReport = new JSONObject();

            Date date = new Date();
            String currentDate = date.func();

            //file name contains current date
            String fileName ="Manager_report" + "_" + currentDate + ".json";

            File outputFile = new File(fileName);

            int totalProviders = 0;
            double allFee = 0;

            outputFile.createNewFile();
            BufferedWriter outFile = new BufferedWriter(new FileWriter(outputFile));

            for(Object key:serviceReport.keySet()){
                totalProviders ++;

                double totalFee = 0, fee;
                int totalConsultation = 0;

                String providerNumber = (String)key;
                JSONObject serviceInfo = (JSONObject)serviceReport.get(providerNumber);


                for(Object key1:serviceInfo.keySet()){
                    totalConsultation ++;
                    String date_ = (String) key1;
                    JSONObject dateInfo = (JSONObject)serviceInfo.get(date_);
                    String feeString = (String) dateInfo.get("Service fee");

                    fee = stringToDouble(feeString);
                    totalFee += fee;

                }
                allFee += totalFee;

                System.out.println("\nProvider " + providerNumber + ":" );
                System.out.println("Total consultations: " + totalConsultation);
                System.out.println("Total fee: " + totalFee);
                System.out.println();

                payableReport.put(providerNumber,readInfo(providerNumber,serviceReport));

            }
            payableReport.put("Total providers to be paid: ", totalProviders);
            payableReport.put("Total fee to be payed: ", allFee);
            outFile.write(JSONValue.toJSONString(payableReport));

            System.out.println("\nTotal providers to be paid: " + totalProviders);
            System.out.println("Total fee to be paid: " + allFee + "\n");

            outFile.close();
        } catch (ParseException e1) {
            e1.printStackTrace();
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    //convert string type to double type
    public double stringToDouble(String input){
        String feeString = input.substring(1,input.length());
        double fee = Double.parseDouble(feeString);
        return fee;
    }

}
