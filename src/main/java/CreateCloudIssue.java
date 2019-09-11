import java.io.*;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.ObjectMapper;


public class CreateCloudIssue {
		public static void main(String[] args) {
			// The payload definition using the Jackson library
			try {
				JsonNodeFactory jnf = JsonNodeFactory.instance;
				ObjectNode root = jnf.objectNode();
				{
				  ObjectNode fields = root.putObject("fields");
				  {
				    fields.put("summary", "something's wrong");
				    ObjectNode issuetype = fields.putObject("issuetype");
				    {
				      issuetype.put("id", "10103");
				    }
				    
				    ObjectNode project = fields.putObject("project");
				    {
				      project.put("id", "10003");
				    }
				    fields.put("description", "description");
				  }
				}

				// Connect Jackson ObjectMapper to Unirest

			Unirest.setObjectMapper(new ObjectMapper() {
			private com.fasterxml.jackson.databind.ObjectMapper jacksonObjectMapper = 
			new com.fasterxml.jackson.databind.ObjectMapper();

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
				HttpResponse<JsonNode> response = Unirest.post("https://tekdemo.atlassian.net/rest/api/2/issue")
				  .header("Accept", "application/json")
				  .header("Content-Type", "application/json")
				  .asJson();
				System.out.println("heyheyhey");
				System.out.println(response.getBody());
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
}