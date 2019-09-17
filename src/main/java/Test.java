import org.json.JSONArray;
import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import org.apache.poi.ss.usermodel.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Test {

	public static final String SAMPLE_XLSX_FILE_PATH = "/home/manjunath/Desktop/Codes/Cloud/Comm sheet.xlsx";
	 
    public static void main(String[] args) {
    	ArrayList<String> keys = issues();
    	System.out.println(keys);
        try {
        	ArrayList<ArrayList> list = new ArrayList<ArrayList>();
        	ArrayList<Object> jira = new ArrayList<Object>();
        	for(int n=0;n<keys.size();n++) {
        		ArrayList<Object> issue = new ArrayList<Object>();
        		String url = "https://tekdemo.atlassian.net/rest/api/2/issue/";
        		url = url + keys.get(n) ;
        	HttpResponse<JsonNode> response = null;
			response = Unirest.get(url)
					  .basicAuth("manjunathr@crgroup.co.in", "6fxnzTw9DdxRPf8CpxvSB99D")
					  .header("Accept", "application/json")
					  .header("Content-Type", "application/json")
					  .asJson();
        		 JsonNode node = response.getBody();
        		 JSONArray array =  node.getArray();
        		 JSONObject jsonObject = array.getJSONObject(0);
        		 JSONObject fields =  jsonObject.getJSONObject("fields");
        		 issue.add(fields.get("customfield_10115"));
        		 issue.add(jsonObject.get("key"));
        		 issue.add(url);
        		 jira.add(issue);
        	}
        		System.out.println(jira);
        		list =  readExcel();
        		System.out.println(list);
        		matchIssues(jira,list);
        }catch (Exception e) {
			e.printStackTrace();
		}
    }
    private static void matchIssues(ArrayList<Object> jira, ArrayList<ArrayList> list) {
    	System.out.println("action");
    	for(int i=0;i<list.size();i++) {
    		ArrayList<String> a= list.get(i);
    		for(int j=0;j<jira.size();j++) {
    			ArrayList<String> b= (ArrayList<String>) jira.get(j);
    			if(a.get(0).equals(b.get(0))) {
    				System.out.println(list.get(i) +" "+ jira.get(j));
    				updateIssue(a,b);
    			}
    		}
    	}
	}
	private static void updateIssue(ArrayList<String> a, ArrayList<String> b) {
		try {
			String url = b.get(2);
		 JsonObject body = new JsonObject();
		 JsonObject c = new JsonObject();
		 c.addProperty("customfield_10116",a.get(1));
		 body.add("fields", c);
		 System.out.println(body);
		 String json = body.toString();
		 System.out.println(json);
		    // Parse a JSON string into a JsonNode
		 ObjectMapper objectMapper = new ObjectMapper();
		 com.fasterxml.jackson.databind.JsonNode jsonNode =objectMapper.readTree(json);		     
		    // Connect Jackson ObjectMapper to Unirest
		    Unirest.setObjectMapper(new com.mashape.unirest.http.ObjectMapper() {
		       private com.fasterxml.jackson.databind.ObjectMapper jacksonObjectMapper
		               = new com.fasterxml.jackson.databind.ObjectMapper();
		       public <T> T readValue(String value, Class<T> valueType) {
		           try {
		               return jacksonObjectMapper.readValue(value, valueType);
		           } catch (IOException e) {
		               throw new RuntimeException(e);
		           }
		       }
		       public String writeValue(Object value) {
		           try {
		               return jacksonObjectMapper.writeValueAsString(value);
		           } catch (JsonProcessingException e) {
		               throw new RuntimeException(e);
		           }
		       }
		    });
		HttpResponse<JsonNode> response = Unirest.put(url)
		  .basicAuth("manjunathr@crgroup.co.in", "6fxnzTw9DdxRPf8CpxvSB99D")
		  .header("Accept", "application/json")
		  .header("Content-Type", "application/json")
		  .body(jsonNode)
		  .asJson();

		
		System.out.println(response.getStatus());
		}catch(Exception e) {
			e.printStackTrace();
		}	
	}
	public static ArrayList<ArrayList> readExcel() {
    	try {
    		Workbook workbook = WorkbookFactory.create(new File(SAMPLE_XLSX_FILE_PATH));
            Sheet sheet = workbook.getSheetAt(0);
            DataFormatter dataFormatter = new DataFormatter();
            Map<String, Integer> map = new HashMap<String,Integer>(); //Create map
            Row row = sheet.getRow(0);
            short minColIx = row.getFirstCellNum(); //get the first column index for a row
            short maxColIx = row.getLastCellNum(); //get the last column index for a row
            for(short colIx=minColIx; colIx<maxColIx; colIx++) { //loop from first to last index
               Cell cell = row.getCell(colIx); //get the cell
               map.put(cell.getStringCellValue(),cell.getColumnIndex()); //add the cell contents (name of column) and cell index to the map
             }  
            int ordercol = map.get("Tek Order #");
            int Netprice = map.get("Net price");
            int Commn = map.get("Commn %");
            int Commnpayable = map.get("Commn payable");

            	
                   ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
                    for (Row row1: sheet) {
                    	ArrayList<String> col = new ArrayList<String>();
                        for(Cell cell: row1) {
                            String cellValue = dataFormatter.formatCellValue(cell);
                            col.add(cellValue);
                        }
                        list.add(col);
                    }
                    ArrayList<ArrayList> det = new ArrayList<ArrayList>();
            	int len = list.size();
            	for(int i=1;i<list.size();i++) {
                    ArrayList info = new ArrayList();
            		ArrayList<String> a= list.get(i);
            				String ord = a.get(ordercol);
            				String netprice = a.get(Netprice);
            				String comm = a.get(Commn);
            				double net = convert(netprice);
            				double com = convert(comm);
            				double commPayable =  (net * com /100);
            				String payable = Double.toString(commPayable);
            				info.add(ord);
            				info.add(payable);
            				det.add(info);
            			}
            workbook.close();
            return det;
    	}catch(Exception e) {
    		e.printStackTrace();
    		return null;
    	}	
    }
    public static ArrayList<String> issues() {
    	ArrayList<String> issueKeys = new ArrayList<String>();
    	try {
    		
    		HttpResponse<JsonNode> response  = Unirest.get("https://tekdemo.atlassian.net/rest/api/2/search?jql=project=RA&maxResults=-1&fields=key")
    				  .basicAuth("manjunathr@crgroup.co.in", "6fxnzTw9DdxRPf8CpxvSB99D")
    				  .header("Accept", "application/json")
    				  .header("Content-Type", "application/json")
    				  .asJson();

    		
    		JsonNode result = response.getBody();
    		JSONArray issues = result.getArray();
    		JSONObject issue = (JSONObject) issues.get(0);
    		JSONArray v = issue.getJSONArray("issues");
    		JSONObject a = (JSONObject) v.get(0);
    		String key = (String) a.get("key");
    		
    		int d = getCount(key);
    		for(int i=0;i<d;i++) {
    		a = (JSONObject) v.get(i);
    		String k = (String) a.get("key");
    		issueKeys.add(k);
    		}
    		return issueKeys;
    		}catch(Exception e) {
    			e.printStackTrace();
    		}
		return issueKeys;
    }
    private static int getCount(String key) {
		int len = key.length()-1;
		String lastissue = "";
		for(int i=len;i>0;i--) {
			if(key.charAt(i)==45) {
				break;
			}
			lastissue = key.charAt(i) + lastissue;
		}
		int no = Integer.parseInt(lastissue);
		return no;
	}
    private static double convert(String string) {
		int len = string.length();
		double value;
		String val = "";
		for(int i=0;i<len;i++) {
			if(string.charAt(i)>=48 && string.charAt(i)<=57 || string.charAt(i)==46) {
				val = val + string.charAt(i);
			}
		}
		value=Double.valueOf(val);
		return value;
		// TODO Auto-generated method stub
		
	}
}
