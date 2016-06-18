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


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.CookieManager;
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
    Boolean loginState =false;
    String outputresponse;
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
            if(outputresponse.toCharArray()[12]=='t') {
                boolean havepermission = false;
                int trans = outputresponse.indexOf("transactions");
                int posstart = outputresponse.indexOf("[", trans);
                int posend = outputresponse.indexOf("]", trans);
                posstart++;
                String permissions = outputresponse.substring(posstart, posend);
                String[] permission = permissions.split(",");
                for (String token : permission) {
                    Log.e("Permission", token);
                    if (token.matches("\"SUPERVISOR\"") == true)
                        havepermission = true;
                }
                Log.e("Permission", "" + havepermission);
                if (havepermission == true) {
                    //Log.d("someshit", outputresponse.toString());
                    loginState = true;
                }
            }
            //Toast.makeText(getApplicationContext(), "execction complete", Toast.LENGTH_SHORT).show();
            aftercomplete();
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
        if(loginState)
        {
            //Toast.makeText(getApplicationContext(), "User logged in", Toast.LENGTH_SHORT).show();
            loginState=false;
            Intent intent = new Intent(this, ClientSelection_Activity.class);
            intent.putExtra("uname", usernameintext);
            intent.putExtra("Cookie",cookie);
            this.startActivity(intent);
        }
        else
        {
            //password.getBackground().setColorFilter(getResources().getColor(R.color.errorred), PorterDuff.Mode.SRC_ATOP);
            password.setError("Wrong Password");
            //Toast.makeText(this, "Credentials wrong >.<", Toast.LENGTH_SHORT).show();
        }
    }
}
