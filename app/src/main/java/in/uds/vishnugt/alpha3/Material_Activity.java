package in.uds.vishnugt.alpha3;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
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
import java.util.Date;

public class Material_Activity extends AppCompatActivity {


    private RecyclerView mRecyclerView;
    private RecyclerViewforMaterials mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    AlertDialog alertDialog;
    ArrayList results;
    ArrayList<String> materials=new ArrayList<>();
    ProgressDialog progress,progresssubmit;
    Bundle extras;
    String username;
    String company;
    String companyid;
    String desc;
    String client;
    String cookie;
    String outputresponse;
    String month;
    int year;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_view);

        extras=getIntent().getExtras();
        username=extras.getString("username");
        company=extras.getString("company");
        companyid=extras.getString("companyid");
        cookie=extras.getString("cookie");
        desc=extras.getString("description");
        month=extras.getString("month");
        year=extras.getInt("year");
        client=extras.getString("client");
        new LongOperation().execute("");

        progress = new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
        progress.setCancelable(false);
        progress.show();


        results = new ArrayList<>();
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.getItem(0).setTitle("SUBMIT");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.user:
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                String count;
                count="";
                for(int i=0;i<materials.size();i++)
                {
                    if(mAdapter.countmaterial.get(i).toString().matches(""))
                        count=count.concat(materials.get(i)+" - "+"0"+"\n");
                    else
                        count=count.concat(materials.get(i)+" - "+mAdapter.countmaterial.get(i).toString()+"\n");
                }
                alertDialogBuilder.setMessage(count);
                alertDialogBuilder.setTitle("Are you sure?");
                alertDialogBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        progresssubmit = new ProgressDialog(Material_Activity.this );
                        progresssubmit.setTitle("Loading");
                        progresssubmit.setMessage("Wait while loading...");
                        progresssubmit.setCancelable(false);
                        progresssubmit.show();
                        new LongOperationsubmit().execute("");
                        }
                });
                alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void requeststatus(String os)
    {
        if(os!=null)
        {
            Log.e("outputresponse",os);
            Toast.makeText(this,"Successful",Toast.LENGTH_SHORT).show();
            Intent startintent=new Intent(this,login_activity.class);
            startintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            this.startActivity(startintent);
            finish();
        }
        else
            Toast.makeText(this,"Not Successful",Toast.LENGTH_SHORT).show();

    }

    private class LongOperation extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            URLConnection urlConnection;
            StringBuilder result = new StringBuilder();

            try {
                URL url = new URL("http://remote.uds.in:8081/xtime/client/materials/SHRIRAM");
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
            Log.d("result", result);
            getDataSet(result);
            progress.dismiss();
            mAdapter = new RecyclerViewforMaterials(results);
            mRecyclerView.setAdapter(mAdapter);
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }


    public void getDataSet(String jsonarray)
    {
        JSONObject jsonRootObject;
        try {
            jsonarray="{\"Materials\":"+jsonarray+"}";
            Log.d("Materials",jsonarray);
            jsonRootObject = new JSONObject(jsonarray);
            JSONArray jsonArray = jsonRootObject.optJSONArray("Materials");
            String name;
            int id;
            DataObject empty = new DataObject("", "");
            for(int i=0; i < jsonArray.length(); i++)
            {
                results.add(i,empty);
                materials.add(i, "");
            }
            for(int i=0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                name = jsonObject.optString("materialDesc").toString();
                id = Integer.parseInt(jsonObject.optString("materialId").toString());
                Log.d("materials - id", name+"--"+id);
                //materials.add(id,name);
                materials.set(id - 1, name);
                DataObject obj = new DataObject(name.toString(), "");
                //results.add(id, obj);
                results.set(id - 1, obj);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private class LongOperationsubmit extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            try {
                URL url = new URL("http://remote.uds.in:8081/flow/rest/request");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Connection", "keep-alive");
                //connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Cookie", cookie);
                OutputStreamWriter osw = new OutputStreamWriter(connection.getOutputStream());
                String oswrite="{ \"supervisorList\": [ { ";
                for(int i=0;i<materials.size();i++)
                {
                    oswrite=oswrite.concat("\"material"+(i+1)+"\": \""+mAdapter.countmaterial.get(i).toString()+"\",");
                }
                oswrite=oswrite.substring(0,oswrite.length()-1);
                Date today = new Date();
                String current = today.toString();
                oswrite=oswrite.concat("} ], \"requestFields\": { \"projectId\": \""+companyid+"\", \"projectDesc\": \""+desc+"\", \"recordcreationdate\": \""+current+"\", \"monthandYear\": \""+month+" "+year+"\", \"timeClient\" : \"\", \"timeRegional\" : \"\", \"clientId\" : \"\", \"regionalHeadId\" : \"\", \"date\" : \"\", \"deliveryOrVisit\": \"delivery\"}, \"requestType\": \"supervisor\", \"fresh\": true, \"status\": \"Visited\", \"changed\": true, \"transitions\": { \"1\": \"Visited\" }, \"editReason\": \"Automated from app\" }");
                Log.e("JSON sent to the Server",oswrite);
                osw.write(String.format(oswrite));
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
            progresssubmit.dismiss();
            requeststatus(outputresponse);
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
