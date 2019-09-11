import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;

public class CloudCreate {
	public static void main(String args[]) {
		try {

			 JsonObject b = new JsonObject();
			 JsonObject c = new JsonObject();
			 JsonObject project = new JsonObject();
			 JsonObject issuetype = new JsonObject();
			 JsonObject assignee = new JsonObject();
			 issuetype.addProperty("id", "10103");
			 project.addProperty("id", "10003");
			 assignee.addProperty("accountId", "5c6271bacefe97640e699b34");
			 c.addProperty("summary","Test Bug");
			 c.addProperty("description", "test desc");
			 c.add("project", project);
			 c.add("issuetype", issuetype);
			 c.add("assignee", assignee);
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
			HttpResponse<JsonNode> response = Unirest.post("https://tekdemo.atlassian.net/rest/api/2/issue/")
			  .basicAuth("manjunathr@crgroup.co.in", "6fxnzTw9DdxRPf8CpxvSB99D")
			  .header("Accept", "application/json")
			  .header("Content-Type", "application/json")
			  .body(jsonNode)
			  .asJson();

			System.out.println(response.getHeaders());
			System.out.println(response.getBody());
			System.out.println(response.getStatus());
		}catch(Exception e) {
			e.printStackTrace();
		}				
	}
}
