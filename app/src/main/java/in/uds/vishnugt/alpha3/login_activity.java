package in.uds.vishnugt.alpha3;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

//url http://192.168.0.172:8080/flow/rest/login?loggedIn=false&password=%22cat%22&rememberMe=false&requestList=&transactions=&txnlocations=&userId=0&username=%22cats%22


public class login_activity extends AppCompatActivity {
    EditText username;
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);


    }

    public void signin(View v) {
        new LongOperation().execute("");
        /*if(!(username.getText().toString().equals("admin") && password.getText().toString().equals("admin")))
        {
            Intent intent = new Intent(this, ClientSelection_Activity.class);
            this.startActivity(intent);
        }
        else
        {
            Toast.makeText(this, "Credentials wrong >.<", Toast.LENGTH_SHORT).show();
        }*/
    }


    private class LongOperation extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            InputStream inputStream = null;
            String result = "";

            try {
                URL url = new URL("http://58.68.16.118:8080/flow/rest/login");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("PUT");
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "application/json");
                OutputStreamWriter osw = new OutputStreamWriter(connection.getOutputStream());
                osw.write(String.format("{\"username\":\"cats\",\"password\":\"cats\"}"));
                osw.flush();
                osw.close();
                System.err.println(connection.getResponseCode() + connection.getResponseMessage());
                Log.d("vishnugt", connection.getResponseMessage() + connection.getResponseCode());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "";
        }
    protected void onPostExecute(Boolean result) {

    }


        @Override
        protected void onPostExecute(String result) {
        Log.d("result", result);
            Toast.makeText(getApplicationContext(), "execction complete", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPreExecute() {
            Toast.makeText(getApplicationContext(), "execction started", Toast.LENGTH_SHORT).show();

        }

        @Override
        protected void onProgressUpdate(Void... values) {}
    }
}
