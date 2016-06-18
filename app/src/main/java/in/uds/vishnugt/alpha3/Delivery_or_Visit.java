package in.uds.vishnugt.alpha3;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Delivery_or_Visit extends AppCompatActivity {

    String username;
    String company;
    String outputresponse;
    String companyid;
    String location;
    String cookie;
    TextView companyname,locationname;
    ListView listview;
    ArrayAdapter adapter;
    ArrayList<String> array=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_delivery_supervisor);

        companyname=(TextView)findViewById(R.id.company);
        locationname=(TextView)findViewById(R.id.location);
        listview=(ListView)findViewById(R.id.listview);


        Bundle extras=getIntent().getExtras();
        username=extras.getString("username");
        company=extras.getString("company");
        companyid=extras.getString("companyid");
        location=extras.getString("location");
        cookie=extras.getString("Cookie");


        companyname.setText(company.trim());
        locationname.setText(location.trim());

        Log.e("company",company);
        Log.e("companyid",companyid);
        Log.e("location",location);

        new LongOperation().execute("");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.refreshmenu, menu);
        menu.getItem(1).setTitle(username);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.user:
                return true;
            case R.id.refresh:
                new LongOperation().execute("");
                Toast.makeText(getApplicationContext(),"Refresh",Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void materialdelivery(View v)
    {
        Intent intent = new Intent(getApplicationContext(), Material_Activity.class);
        intent.putExtra("username",username);
        intent.putExtra("company",company);
        intent.putExtra("companyid",companyid);
        startActivity(intent);
    }

    public void feedback(View v)
    {
        Intent intent=new Intent(getApplicationContext(),feedback_activity.class);
        intent.putExtra("username",username);
        intent.putExtra("company",company);
        intent.putExtra("companyid",companyid);
        startActivity(intent);
    }


    public void jsonparse()
    {
        JSONObject jsonRootObject,jsonobj;
        String jsonobject=outputresponse;
        try {
            Log.e("Request List",jsonobject);
            jsonRootObject = new JSONObject(jsonobject);
            String statusdate;
            JSONArray jsonarray=jsonRootObject.optJSONArray("requestList");
            for(int i=0;i<jsonarray.length();i++)
            {
                statusdate="";
                jsonobj=jsonarray.getJSONObject(i);
                statusdate=statusdate.concat(jsonobj.getString("status"));
                statusdate=statusdate.concat(" : "+jsonobj.getString("timestamp"));
                array.add(i,statusdate);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private class LongOperation extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            try {
                URL url = new URL("http://remote.uds.in:8081/flow/rest/search");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Connection", "keep-alive");
                //connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Cookie", cookie);
                OutputStreamWriter osw = new OutputStreamWriter(connection.getOutputStream());
                osw.write(String.format("{\"requestFields\":{\"project\":\"ASCENDAS IT PARK - CHENNAI\",\"projectId\":\"\",\"date\":\"\",\"AttnRecdOn\":\"\",\"SalaryDate\":\"\",\"BillingDate\":\"\"},\"requestType\":\"CATS\",\"fullRequest\":true}"));
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

        @Override
        protected void onPostExecute(String result) {
            Log.d("result", result);
            Log.e("JSON",outputresponse);
            //Toast.makeText(getApplicationContext(), outputresponse, Toast.LENGTH_LONG).show();
            array.clear();
            jsonparse();
            adapter=new ArrayAdapter(getApplicationContext(),R.layout.activity_listview_activity,array);
            listview.setAdapter(adapter);
        }

        @Override
        protected void onPreExecute() {

            //Toast.makeText(getApplicationContext(), "execction started", Toast.LENGTH_SHORT).show();

        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

}
