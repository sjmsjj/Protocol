package jmsang.umich.edu.protocol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jianmingsang on 8/30/16.
 */
public class Manager {
    private final static int GET = 1;
    private final static int POST = 2;
    private final static String clientID = "rijxlNmkvTQmXKqAgbVQGr9kKExj9oZax7bVAL0a";
    private final static String clientKey = "5iTT9a2bYafqjJAZmEiRw6YsT8d1OYAzltkDeFVsoai3OiSlQboZd6ZIqhfzegaOMEiapc0jOQsELhq3anH9GhkoSm1BdUZhmKfCLo8LPy0AsBLVKlH4nx5CajK8DTdk";
    private String baseAddress = "localhost:8000/o/token/";
    private String tokenAddress="http://" + clientID + ":" + clientKey + "@" + baseAddress;
    private String grantType = "password";

    private String accessToken;

    private List<String> protocols;
    private final static Manager INSTANCE = new Manager();

    private Manager(){
        protocols = new ArrayList<>();
    }

    public static Manager getInstance(){
        return Manager.INSTANCE;
    }

    private void print(String message){
        System.out.println(message);
    }

    public String getAccessToken(String username, String password){
        print("in get access token function");
        HttpURLConnection urlConnection = null;
        String response = null;
        try{
            URL url = new URL(tokenAddress);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            print("add request post type");
            urlConnection.addRequestProperty("grant_type", grantType);
//            urlConnection.addRequestProperty("client_id", clinentID);
//            urlConnection.addRequestProperty("client_secret", clientKey);
            urlConnection.addRequestProperty("username", username);
            urlConnection.addRequestProperty("password", password);
            urlConnection.addRequestProperty("scope", "read");
            print("finish setting the header information");

            print(username + ' ' + password);
            print(urlConnection.getURL().toString());

            print("begin connect");
            urlConnection.connect();
            print("after connect");
            int status = urlConnection.getResponseCode();
            print("get status: " + status);
            switch (status){
                case 200:
                case 201:
                    response = getConnectionContent(urlConnection);
            }
        }
        catch(MalformedURLException e){
            System.out.println("error in ulr.");
        }
        catch(ProtocolException e){
            System.out.println("error in protocol");
        }
        catch(IOException e){
            System.out.println("error in connecting");
        }
        finally {
            if(urlConnection != null){
                urlConnection.disconnect();
            }
        }
        return response;

    }

    public String getConnectionContent(HttpURLConnection urlConnection){
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;
        try{
            bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line;
            while((line=bufferedReader.readLine()) != null) {
                stringBuilder.append(line + "\n");
            }
            bufferedReader.close();

        }
        catch (IOException e){
            System.out.println("error in reading content from urlconnection.");
        }

        return stringBuilder.toString();
    }

    public void constructFullUrl(String username, String password){
        try {
            tokenAddress = URLEncoder.encode("grant_type", "UTF-8" ) + "=" + URLEncoder.encode("password", "UTF-8");
            tokenAddress += "&" + URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");
            tokenAddress += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
            tokenAddress += "&" + URLEncoder.encode( "client_id", "UTF-8" ) + "=" + URLEncoder.encode(clientID, "UTF-8" );
            tokenAddress += "&" + URLEncoder.encode( "client_secret", "UTF-8" ) + "=" + URLEncoder.encode(clientKey, "UTF-8" );
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }
}

//    String data = URLEncoder.encode( "grant_type", "UTF-8" ) + "=" + URLEncoder.encode( "password", "UTF-8" );
//
//data += "&" + URLEncoder.encode( "username", "UTF-8" ) + "=" + URLEncoder.encode( USERNAME, "UTF-8" );
//
//        data += "&" + URLEncoder.encode( "password", "UTF-8" ) + "=" + URLEncoder.encode( PASSWORD, "UTF-8" );
//
//        data += "&" + URLEncoder.encode( "client_id", "UTF-8" ) + "=" + URLEncoder.encode( CLIENT_ID, "UTF-8" );
//
//        data += "&" + URLEncoder.encode( "client_secret", "UTF-8" ) + "=" + URLEncoder.encode( CLIENT_SECRET, "UTF-8" );
//
