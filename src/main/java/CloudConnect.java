
import com.sun.org.apache.xml.internal.security.utils.Base64;

import javax.json.Json;
import javax.json.JsonObject;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;

public class CloudConnect {
    public static void main(String[] args) {
        try {
            URL jiraREST_URL = new URL("https://tekdemo.atlassian.net/");
            URLConnection urlConnection = jiraREST_URL.openConnection();
            urlConnection.setDoInput(true);

            HttpURLConnection conn = (HttpURLConnection) jiraREST_URL.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);

            String encodedData = URLEncoder.encode(getJSON_Body(),"UTF-8");

            System.out.println(getJSON_Body() + "/" + encodedData);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Basic " + 
            Base64.encode("manjunathr@crgroup.co.in:6fxnzTw9DdxRPf8CpxvSB99D".getBytes(), 0));
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Content-Length", String.valueOf(encodedData.length()));
            conn.getOutputStream().write(encodedData.getBytes());

            try {
                InputStream inputStream = conn.getInputStream();
            }
            catch (IOException e){
                System.out.println(e.getMessage());
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getJSON_Body(){
        JsonObject createIssue = Json.createObjectBuilder()
                .add("fields",
                        Json.createObjectBuilder().add("CRGWebApplication",
                                Json.createObjectBuilder().add("key","RA-2"))
                                .add("summary", "sum")
                                .add("description", "descr")
                                .add("issuetype",
                                        Json.createObjectBuilder().add("id", "10103"))
                ).build();

        return createIssue.toString();
    }
}
