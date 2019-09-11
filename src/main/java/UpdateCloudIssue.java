
import java.io.IOException;
import org.json.JSONArray;
import org.json.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.google.gson.JsonObject;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
public class UpdateCloudIssue {
	public static void main(String args[]) {
		try {
		// The payload definition using the Jackson library
		@SuppressWarnings("unused")
		JsonNodeFactory jnf1 = JsonNodeFactory.instance;	
		HttpResponse<JsonNode> responser = null;
		responser = Unirest.get("https://tekdemo.atlassian.net/rest/api/2/issue/RA-1")
				  .basicAuth("manjunathr@crgroup.co.in", "6fxnzTw9DdxRPf8CpxvSB99D")
				  .header("Accept", "application/json")
				  .header("Content-Type", "application/json")
				  .asJson();
		
		 JsonNode node = responser.getBody();
		 System.out.println(node);
		 JSONArray array =  node.getArray();
		 JSONObject jsonObject =(JSONObject) array.getJSONObject(0).get("fields");
		 jsonObject.put("summary","new Summary");
		 @SuppressWarnings("unused")
		JSONArray newArray = new JSONArray();
		 JSONObject a = node.getObject();
		 a.put("Summary","new Summary");
		 System.out.println(a);
		 JsonObject b = new JsonObject();
		 JsonObject c = new JsonObject();
		 JsonObject priority = new JsonObject();
		 JsonObject issuetype = new JsonObject();
		 issuetype.addProperty("id", "10100");
		 priority.addProperty("name", "Low");
		 c.addProperty("duedate", "2019-12-10");
		 c.addProperty("summary","Json Summary");
		 c.addProperty("description", "Json description");
		 c.add("issuetype", issuetype);
		 c.add("priority", priority);
		 b.add("fields", c);
		 System.out.println(b);
		 String json = b.toString();
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
		HttpResponse<JsonNode> response = Unirest.put("https://tekdemo.atlassian.net/rest/api/2/issue/RA-3")
		  .basicAuth("manjunathr@crgroup.co.in", "6fxnzTw9DdxRPf8CpxvSB99D")
		  .header("Accept", "application/json")
		  .header("Content-Type", "application/json")
		  .body(jsonNode)
		  .asJson();

		System.out.println(response.getHeaders());
		System.out.println(response.getBody());
		System.out.println(response.getStatus());
		System.out.println(response.getStatusText());
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
