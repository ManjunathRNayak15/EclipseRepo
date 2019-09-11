import org.json.JSONArray;
import org.json.JSONObject;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;

public class JiraCloudConnect {

    public static void main(String[] args) {
        try {

        	HttpResponse<JsonNode> response = null;
			response = Unirest.get("https://tekdemo.atlassian.net/rest/api/2/issue/RA-3")
					  .basicAuth("manjunathr@crgroup.co.in", "6fxnzTw9DdxRPf8CpxvSB99D")
					  .header("Accept", "application/json")
					  .header("Content-Type", "application/json")
					  .asJson();
			
				
        		 JsonNode node = response.getBody();
        		 JSONArray array =  node.getArray();
        		 JSONObject jsonObject = array.getJSONObject(0);
        		 System.out.println("Issue key   :"+jsonObject.get("key"));
        		 JSONObject fields =  jsonObject.getJSONObject("fields");
        		 System.out.println("Summary     :"+fields.get("summary"));
        		 System.out.println("Description :"+fields.get("description"));
        		 JSONObject assignee = (JSONObject) fields.get("assignee");
        		 System.out.println("Assignee    :"+assignee.get("name"));
        		 JSONObject issuetype = fields.getJSONObject("issuetype");
        		 System.out.println("Issuetype   :"+issuetype.get("name"));
        		 JSONObject project = fields.getJSONObject("project");
        		 System.out.println("Project     :"+project.get("name"));
        		 JSONObject priority = fields.getJSONObject("priority");
        		 System.out.println("Priority    :"+priority.get("name"));
        		 JSONObject status = fields.getJSONObject("status");
        		 System.out.println("Status      :"+status.get("name"));

        }catch (Exception e) {
			// TODO Auto-generated catch block	
			e.printStackTrace();
		}
    }
}
