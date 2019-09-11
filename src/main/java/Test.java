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

public class Test {

	public static final String SAMPLE_XLSX_FILE_PATH = "/home/manjunath/Desktop/Codes/Cloud/TestExcel1.xlsx";
	 
    public static void main(String[] args) {
    	ArrayList<String> keys = issues();
    	System.out.println(keys);
        try {
        	ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
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
        		 issue.add(fields.get("customfield_10112"));
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
    private static void matchIssues(ArrayList<Object> jira, ArrayList<ArrayList<String>> list) {
    	for(int i=1;i<list.size();i++) {
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
		 c.addProperty("summary",a.get(1));
		 c.addProperty("description", a.get(2));
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
		// This code sample uses the  'Unirest' library:
		// http://unirest.io/java.html
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
	public static ArrayList<ArrayList<String>> readExcel() {
    	try {
            Workbook workbook = WorkbookFactory.create(new File(SAMPLE_XLSX_FILE_PATH));
           ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
            Sheet sheet = workbook.getSheetAt(0);
            DataFormatter dataFormatter = new DataFormatter();
            for (Row row: sheet) {
            	ArrayList<String> col = new ArrayList<String>();
                for(Cell cell: row) {
                    String cellValue = dataFormatter.formatCellValue(cell);
                    col.add(cellValue);
                }
                list.add(col);
            }
            workbook.close();
            return list;
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
}
