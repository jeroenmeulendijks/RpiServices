package nl.meul.rpiservices;

import java.io.IOException;
import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import org.apache.http.Consts;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

public class Kodi extends Service {

    private static final Logger LOGGER = Logger.getLogger(RestInterface.class.getName());

    private int myId;
    private String myUri;

    public Kodi() {
        myId = 0;
        myUri = "http://localhost:8080/jsonrpc";
    }

    /** Get the status of the service. Ex. running or stopped
     * @return String with the status of the service
     */
    @Override
    public String getStatus() {
        return Service.runCommand("sudo service kodi status");
    }

    /** Start the service
     */
    @Override
    public void start() {
        Service.runCommand("sudo service kodi start");
    }

    /** Stop the service
     */
    @Override
    public void stop() {
        Service.runCommand("sudo service kodi stop");
    }

    @Override
    public String getDescription() {
        return "Kodi";
    }

    public JsonObjectBuilder updateLibrary() {
        JsonObjectBuilder resultBuilder = Json.createObjectBuilder();

        if (execute("VideoLibrary.Scan")) {
            resultBuilder.add("VideoLibrary.Scan", "OK");
        }
        else {
            resultBuilder.add("VideoLibrary.Scan", "FAILED");
        }
        if (execute("VideoLibrary.Clean")) {
            resultBuilder.add("VideoLibrary.Clean", "OK");
        }
        else {
            resultBuilder.add("VideoLibrary.Clean", "FAILED");
        }
        return resultBuilder;
    }

    private boolean execute(String method) {
        boolean result = false;

        int timeoutConnection = 1000;
        myId += 1;

        String updateString =  "{\"jsonrpc\": \"2.0\", \"method\": \"" + method + "\", \"id\" : \"" + myId + "\"}" ;
        StringEntity entity = new StringEntity(updateString, Consts.UTF_8);
        HttpPost httpPost = new HttpPost(myUri);
        entity.setContentType("application/json");
        httpPost.setEntity(entity);
        RequestConfig.Builder requestBuilder = RequestConfig.custom();
        requestBuilder = requestBuilder.setConnectTimeout(timeoutConnection);
        requestBuilder = requestBuilder.setConnectionRequestTimeout(timeoutConnection);
        HttpClientBuilder builder = HttpClientBuilder.create();
        builder.setDefaultRequestConfig(requestBuilder.build());

        try (CloseableHttpClient client = builder.build()) {
            HttpResponse response = client.execute(httpPost);

            String responseString = new BasicResponseHandler().handleResponse(response);
            JsonObject jsonResponse;
            try (JsonReader reader = Json.createReader(new StringReader(responseString))) {
                jsonResponse = reader.readObject();
            }

            result = (response.getStatusLine().getStatusCode() == 200 &&
                    jsonResponse.getString("result").equals("OK"));
        }

        catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Could not send update request: {0}", e);
        }

        return result;
    }
}
