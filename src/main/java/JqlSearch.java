import org.json.JSONArray;
import org.json.JSONObject;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;

public class JqlSearch {

	public static void main(String[] args) {
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
		System.out.println(a.get("key"));
		}
		}catch(Exception e) {
			e.printStackTrace();
		}
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
