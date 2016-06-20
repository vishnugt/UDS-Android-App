package in.uds.vishnugt.alpha3;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.CookieManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//url http://192.168.0.172:8080/flow/rest/login?loggedIn=false&password=%22cat%22&rememberMe=false&requestList=&transactions=&txnlocations=&userId=0&username=%22cats%22


public class login_activity extends AppCompatActivity {
    ProgressDialog progress;
    String cookie;
    EditText username;
    EditText password;
    CheckBox show;
    String passwordintext;
    String usernameintext;
    Boolean havepermission =false;
    Boolean haveaccess=false;
    String outputresponse;
    ArrayList<String> projectIds = new ArrayList<>();
    CookieManager cookieManager=new CookieManager();
    static final String COOKIES_HEADER = "Set-Cookie";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        show=(CheckBox)findViewById(R.id.showpassword);
    }

    private boolean isNetworkConnected() {
        //Only connectivity to a network is checked and not the internet access
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    public void signin(View v)
    {
        username.setError(null);
        password.setError(null);
        if(!isNetworkConnected())
        {
            Toast.makeText(this,"No Network Available", Toast.LENGTH_SHORT).show();
            return ;
        }
        progress = new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
        progress.setCancelable(false);
        progress.show();
        passwordintext = password.getText().toString();
        usernameintext = username.getText().toString();
        new LongOperation().execute("");

    }

    public void showpassword(View v)
    {
        if(show.isChecked())
            password.setTransformationMethod(null);
        else
            password.setTransformationMethod(new PasswordTransformationMethod());
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


                Map<String, List<String>> headerFields = connection.getHeaderFields();
                List<String> cookiesHeader = headerFields.get(COOKIES_HEADER);

                if(cookiesHeader != null)
                {
                    for (String cook : cookiesHeader)
                    {
                        cookie=cook;
                        Log.e("Cookie",cookie);
                        break;
                        //cookieManager.getCookieStore().add(null, HttpCookie.parse(cookie).get(0));
                    }
                }




                //System.err.println(connection.getResponseCode() + connection.getResponseMessage());
                //Log.d("vishnugt", connection.getResponseMessage() + connection.getResponseCode() );
                outputresponse = br.readLine();
                //Log.e("asdf", outputresponse);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d("result", result);
            Log.e("JSON",outputresponse);
            JSONObject jsonRootObject, projectIdjson;
            String jsonobject=outputresponse;
            try {
                Log.e("Login Response",jsonobject);
                jsonRootObject = new JSONObject(jsonobject);
                Log.e("loggedIn",jsonRootObject.getString("loggedIn"));
                if(jsonRootObject.getString("loggedIn").equals("true"))
                {
                    havepermission=true;
                    JSONArray jsonarray = jsonRootObject.optJSONArray("transactions");
                    for (int i = 0; i < jsonarray.length(); i++) {
                        Log.e("transactions",jsonarray.get(i).toString());
                        if(jsonarray.get(i).equals("SUPERVISOR"))
                        {
                            haveaccess=true;
                            break;
                        }
                    }
                    projectIdjson = jsonRootObject.getJSONObject("projectIdMap");
                    //Log.e("projectIdmap", projectIdjson.toString());

                    if(haveaccess)
                    {
                        JSONArray supervisorJSONArray = projectIdjson.getJSONArray("SUPERVISOR");
                        for(int i=0; i<supervisorJSONArray.length(); i++)
                        {
                            projectIds.add(i, supervisorJSONArray.getString(i));
                        }
                    }

                }
                aftercomplete();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onPreExecute() {

            //Toast.makeText(getApplicationContext(), "execction started", Toast.LENGTH_SHORT).show();

        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    public void aftercomplete()
    {
        progress.dismiss();
        if(havepermission&&haveaccess)
        {
            //Toast.makeText(getApplicationContext(), "User logged in", Toast.LENGTH_SHORT).show();
            havepermission=false;
            haveaccess=false;
            Intent intent = new Intent(this, ClientSelection_Activity.class);
            intent.putExtra("uname", usernameintext);
            intent.putExtra("Cookie",cookie);
            intent.putExtra("projectIds", projectIds);
            this.startActivity(intent);
        }
        else
        {
            if(havepermission)
                username.setError("Access Denied");
            else
                password.setError("Wrong Password");
            havepermission=false;
            //Toast.makeText(this, "Credentials wrong >.<", Toast.LENGTH_SHORT).show();
        }
    }
}
