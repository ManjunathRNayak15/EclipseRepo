import org.json.JSONArray;
import org.json.JSONObject;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;

public class JiraCloudConnect {

    public static void main(String[] args) {
        try {

        	HttpResponse<JsonNode> response = null;
			response = Unirest.get("https://sanjma.atlassian.net/rest/api/2/issue/ISD-1")
					  .basicAuth("sanjmahajan7777@gmail.com", "YBYtbILAE1LK3lfheGYM62EA")
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
