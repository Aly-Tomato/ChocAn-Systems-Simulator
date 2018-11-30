//Class declaration for MemberServices
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

public class MemberServices {

    //data fields
    protected int provider; //9-digit provider#
    protected String status;
    protected String comments;
    protected int size = 20;

    protected boolean isValid(int number, String FileLocation) {
        //check if member number is appropriate length
        if (number < 100000000 || number > 999999999) {
            return false;
        }

        String ID = String.format("%09d", number);
        JSONParser parser = new JSONParser();
        try {
            //Object obj = parser.parse(new FileReader("providerDirectory.json"));
            Object obj = parser.parse(new FileReader(FileLocation));
            JSONObject providerJSON = (JSONObject) obj;
            JSONObject list = (JSONObject) providerJSON.get(ID);

            return true;

        }
        //catch(FileNotFoundException e){e.printStackTrace();}
        catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


        return false;

    }

    protected boolean isSuspended(int number, String memberFileLocation) {
        //check if member number is appropriate length
        if (number < 100000000 || number > 999999999) {
            return false;
        }


        String ID = String.format("%09d", number);
        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(new FileReader(memberFileLocation));
            JSONObject providerJSON = (JSONObject) obj;
            JSONObject list = (JSONObject) providerJSON.get(ID);

            String status = new String();
            status = (String) list.get("status");
            return status.equals("Suspended");

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


        return false;
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    protected boolean buildObject(int providerID, int memberID, int serviceID, String date, String comment) {
        JSONObject datekey = new JSONObject();
        JSONObject inner = new JSONObject();

        inner = buildInner(memberID, providerID, serviceID, comment, date);
        datekey.put(date, inner);

        String providerFile = createFile("./services/member_services");
        String memberFile = createFile("./services/provider_services");

        JSONObject providerObject = new JSONObject();
        providerObject = createKey(providerFile, providerID, date, datekey, inner);
        writeToFile(providerFile, providerObject);

        return true;
    }

    protected JSONObject buildInner(int memberID, int providerID, int serviceID, String comment, String date) {
        JSONObject data = new JSONObject();
        String ID1 = String.format("%09d", memberID);
        String ID2 = String.format("%09d", providerID);
        String ID3 = String.format("%06d", serviceID);

        try {
            JSONParser parser = new JSONParser();
            Object obj1 = parser.parse(new FileReader("./services/member_services"));
            Object obj2 = parser.parse(new FileReader("./services/provider_services"));

            JSONObject memberdirectory = (JSONObject) obj1;
            JSONObject providerdirectory = (JSONObject) obj2;


            JSONObject memberfile = (JSONObject) memberdirectory.get(ID1);
            JSONObject providerfile = (JSONObject) providerdirectory.get(ID2);

            JSONObject services = (JSONObject) providerfile.get("serviceNumbers");
            JSONObject serviceInfo = (JSONObject) services.get(ID3);

            String serviceName = (String) serviceInfo.get("name");
            double serviceFee = (double) serviceInfo.get("fee");

            String memberName = (String) memberfile.get("name");

            data.put("MemberID:", ID1);
            data.put("MemberName:", memberName);
            data.put("ServiceID:", ID3);
            data.put("ServiceName:", serviceName);
            data.put("SeviceFee:", serviceFee);
            data.put("Comment", comment);

            String providerName = (String) providerfile.get("name");
            buildMemberObject(date, ID1, ID2, providerName, serviceName);

            return data;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    protected void buildMemberObject(String date, String memberID, String providerID, String providerName, String serviceName) {
        JSONObject datekey = new JSONObject();
        JSONObject inner = new JSONObject();
        String memberFile = createFile("./memberReports/");

        inner = buildInnerMember(memberID, providerID, providerName, serviceName);
        //datekey.put(date,inner);

        JSONObject memberObject = new JSONObject();
        memberObject = createMemberKey(date, inner, memberFile);
        writeToFile(memberFile, memberObject);
    }

    protected JSONObject createMemberKey(String date, JSONObject datekey, String memberFile) {

        try {

            BufferedReader br1 = new BufferedReader(new FileReader(memberFile));

            if (br1.readLine() == null) { //IF FILE IS EMPTY, CREATE NEW PROV KEY
                JSONObject object = new JSONObject();
                object.put(date, datekey);
                //writeToFile(memberFile, object);

                return object;
            } else { //not empty

                JSONParser parser = new JSONParser();
                FileReader reader = new FileReader(memberFile);
                JSONObject object = (JSONObject) parser.parse(reader);

                if (object.containsKey(date)) {

                    object.put(date, datekey);
                    return object;

                } else { //IF THE PROV KEY ISN'T FOUND, ADD IN A NEW PROV KEY
                    object.put(date, datekey);
                    return object;
                }
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
        return null;
    }

    protected JSONObject buildInnerMember(String memberID, String providerID, String providerName, String serviceName) {
        JSONObject data = new JSONObject();

        data.put("MemberID", memberID);
        data.put("ProviderID", providerID);
        data.put("ProviderName", providerName);
        data.put("ServiceName", serviceName);
        return data;

    }

    protected String createFile(String filename) {

        /*DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
        Date date = new Date();
        String currentDate = new String(dateFormat.format(date));*/

        try {

            File outputFile = new File(filename);

            if (!outputFile.exists()) {
                outputFile.createNewFile();
                return filename;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filename;
    }

    protected JSONObject createKey(String providerFile, int providerID, String date, JSONObject dateKey, JSONObject inner) { //Checks to see if a key exists
        String key = String.format("%09d", providerID);

        try {

            BufferedReader br1 = new BufferedReader(new FileReader(providerFile));

            if (br1.readLine() == null) { //IF FILE IS EMPTY, CREATE NEW PROV KEY
                JSONObject object = new JSONObject();
                object.put(key, dateKey);

                return object;
            } else {

                JSONParser parser = new JSONParser();
                FileReader reader = new FileReader(providerFile);
                JSONObject object = (JSONObject) parser.parse(reader);

                if (object.containsKey(key)) {

                    dateKey.put(date, inner);

                    JSONObject prov = (JSONObject) object.get(key);

                    prov.put(date, inner);
                    return object;
                } else { //IF THE PROV KEY ISN'T FOUND, ADD IN A NEW PROV KEY
                    object.put(key, dateKey);

                    return object;
                }
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
        return null;
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

