package in.uds.vishnugt.alpha3;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

//url http://192.168.0.172:8080/flow/rest/login?loggedIn=false&password=%22cat%22&rememberMe=false&requestList=&transactions=&txnlocations=&userId=0&username=%22cats%22


public class login_activity extends AppCompatActivity {
    EditText username;
    EditText password;
    String passwordintext;
    String usernameintext;
    Boolean loginState =false;
    String outputresponse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);


    }

    public void signin(View v)
    {
        passwordintext = password.getText().toString();
        usernameintext = username.getText().toString();
        new LongOperation().execute("");

    }


    private class LongOperation extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            try {
                URL url = new URL("http://remote.uds.in:8081/flow/rest/login");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("PUT");
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "application/json");
                OutputStreamWriter osw = new OutputStreamWriter(connection.getOutputStream());
                osw.write(String.format("{\"loggedIn\":false,\"password\":\""+ passwordintext +"\",\"username\":\""+ usernameintext +"\",\"rememberMe\":null,\"userId\":0,\"requestList\":null,\"transactions\":null,\"txnlocations\":null}"));
                osw.flush();
                osw.close();
                InputStream stream = connection.getInputStream();
                InputStreamReader isReader = new InputStreamReader(stream );
                BufferedReader br = new BufferedReader(isReader );
                //System.err.println(connection.getResponseCode() + connection.getResponseMessage());
                //Log.d("vishnugt", connection.getResponseMessage() + connection.getResponseCode() );
                outputresponse = br.readLine();
                //Log.e("asdf", outputresponse);
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

            if(outputresponse.toCharArray()[12]=='t')
            {
                //Log.d("someshit", outputresponse.toString());
                loginState=true;
            }
            //Toast.makeText(getApplicationContext(), "execction complete", Toast.LENGTH_SHORT).show();
            aftercomplete();
        }

        @Override
        protected void onPreExecute() {
            //Toast.makeText(getApplicationContext(), "execction started", Toast.LENGTH_SHORT).show();

        }

        @Override
        protected void onProgressUpdate(Void... values) {}
    }

    public void aftercomplete()
    {
        if(loginState)
        {
            Toast.makeText(getApplicationContext(), "User logged in", Toast.LENGTH_SHORT).show();
            loginState=false;
            Intent intent = new Intent(this, ClientSelection_Activity.class);
            this.startActivity(intent);
        }
        else
        {
            Toast.makeText(this, "Credentials wrong >.<", Toast.LENGTH_SHORT).show();
        }
    }
}
