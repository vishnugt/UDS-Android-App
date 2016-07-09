package in.uds.vishnugt.alpha3;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.math.*;

public class Delivery_or_Visit extends AppCompatActivity {

    String username;
    String company;
    String outputresponse;
    String companyid;
    String location;
    String cookie;
    String desc;
    String enddate;
    TextView companyname,locationname;
    ListView listview;
    ArrayAdapter adapter;
    Intent intent;
    String time;
    ArrayList<String> array=new ArrayList<>();
    MonthYearPicker datepick;
    ProgressDialog progress;
    String client;
    Button materialbtn;
    Integer month;
    Integer year;
    Integer date;

    List<String> monthstrings = Arrays.asList( "", "January", "February", "March", "April", "May", "June", "July", "August", "September",
            "October", "November", "December");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_delivery_supervisor);

        Bundle extras=getIntent().getExtras();
        username=extras.getString("username");
        company=extras.getString("company");
        companyid=extras.getString("companyid");
        location=extras.getString("location");
        cookie=extras.getString("Cookie");
        desc=extras.getString("description");
        enddate=extras.getString("enddate");
        client=extras.getString("client");



        progress = new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
        progress.setCancelable(false);
        progress.show();

        companyname=(TextView)findViewById(R.id.company);
        locationname=(TextView)findViewById(R.id.location);
        listview=(ListView)findViewById(R.id.listview);
        materialbtn=(Button)findViewById(R.id.material);


        new LongOperationtime().execute("");

        Log.e("End date","--"+datepick.MAX_YEAR+"--");



        companyname.setText(company.trim());
        locationname.setText(location.trim());

        Log.e("company",company);
        Log.e("companyid",companyid);
        Log.e("location",location);
        Log.e("description",desc);

        new LongOperation().execute("");
        if(client.equals("BIRLA"))
        {
            Log.e("btn", "conditionmatches");
            materialbtn.setVisibility(View.GONE);
        }


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
        intent = new Intent(getApplicationContext(), Material_Activity.class);
        intent.putExtra("username",username);
        intent.putExtra("company",company);
        intent.putExtra("companyid",companyid);
        intent.putExtra("description",desc);
        intent.putExtra("cookie",cookie);
        intent.putExtra("client", client);
        datepick.show();
    }

    public void feedback(View v)
    {
        if(client.equals("SHRIRAM")) {
            //intent = new Intent(getApplicationContext(), feedback_activity.class);
            intent = new Intent(getApplicationContext(), TimeInTimeOut.class);
            intent.putExtra("username", username);
            intent.putExtra("company", company);
            intent.putExtra("companyid", companyid);
            intent.putExtra("description", desc);
            intent.putExtra("cookie", cookie);
            intent.putExtra("client", client);
            datepick.show();
        }
        if(client.equals("BIRLA")) {
            //intent = new Intent(getApplicationContext(), BirlaFeedbackActivity.class);
            intent = new Intent(getApplicationContext(), TimeInTimeOut.class);
            intent.putExtra("username", username);
            intent.putExtra("company", company);
            intent.putExtra("companyid", companyid);
            intent.putExtra("description", desc);
            intent.putExtra("cookie", cookie);
            intent.putExtra("client", client);
            datepick.show();
        }
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
                osw.write(String.format("{\"requestFields\":{\"project\":\"\",\"projectId\":\""+companyid+"\",\"date\":\"\",\"AttnRecdOn\":\"\",\"SalaryDate\":\"\",\"BillingDate\":\"\"},\"requestType\":\"SUPERVISOR\",\"fullRequest\":true}"));
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



    private class LongOperationtime extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            URLConnection urlConnection;
            StringBuilder result = new StringBuilder();

            try {
                URL url = new URL("http://remote.uds.in:8081/xtime/client/time");
                urlConnection = url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

            }catch( Exception e) {
                e.printStackTrace();
            }
            finally {
            }
            return result.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            //Log.d("result", result);
            //result = "2016-04-09 10:37:04.077";
            time = result;
            month = Integer.parseInt((result.split(" ")[0]).split("-")[1]);
            year = Integer.parseInt(result.split(" ")[0].split("-")[0]);
            date = Integer.parseInt(result.split(" ")[0].split("-")[2]);
            Log.e("month from server", month+ "" );
            Log.e("year from server", year+ "" );
            if(month == 12)
            {   datepick.MIN_YEAR = year;
                datepick.MAX_YEAR = year + 1;
            }
            else if(month == 1)
            {
                datepick.MIN_YEAR=year - 1;
                datepick.MAX_YEAR = year;
            }
            else
            {
                datepick.MIN_YEAR=year;
                datepick.MAX_YEAR=year;

            }
            dateset();
            progress.dismiss();
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    public void dateset()
    {
        datepick = new MonthYearPicker(this);
        datepick.build(new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which==-1) {
                    Log.e("getSelectedMonthName", datepick.getSelectedMonthName());
                    int temp1 = Math.max(monthstrings.indexOf(datepick.getSelectedMonthName()), month);
                    int temp2 = Math.min(monthstrings.indexOf(datepick.getSelectedMonthName()), month);
                    if(!((temp1 - temp2 < 2 && (year == datepick.getSelectedYear())) || (temp1 - temp2 == 11 && (year != datepick.getSelectedYear()))))
                    {
                        Toast.makeText(getApplicationContext(), "Month you have selected is not valid", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        intent.putExtra("month",datepick.getSelectedMonthName());
                        intent.putExtra("year",datepick.getSelectedYear());
                        intent.putExtra("date", time.split(" ")[0]);
                        startActivity(intent);

                    }
                }
            }
        }, null);

    }

}
