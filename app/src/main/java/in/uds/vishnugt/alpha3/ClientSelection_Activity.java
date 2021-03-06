package in.uds.vishnugt.alpha3;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import org.apache.http.util.ByteArrayBuffer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class ClientSelection_Activity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static String LOG_TAG = "CardViewActivity";
    ArrayList results;
    AlertDialog alertDialog;
    ArrayList<String> company=new ArrayList<>();
    ArrayList<String> companyid=new ArrayList<>();
    ArrayList<String> location=new ArrayList<>();
    ArrayList<String> projectIds = new ArrayList<>();
    ArrayList<String> desc = new ArrayList<>();
    ArrayList<String> enddates=new ArrayList<>();
    String username;
    String cookie;
    ProgressDialog progress;
    Bundle extras;
    String client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_view);
        setTitle("Site Selection");

        extras=getIntent().getExtras();
        username=extras.getString("uname");
        cookie=extras.getString("Cookie");
        projectIds = extras.getStringArrayList("projectIds");
        client = extras.getString("client");

        Toast.makeText(this,"Welcome "+username,Toast.LENGTH_SHORT).show();
        results = new ArrayList<>();
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MyRecyclerViewAdapter(results);
        new LongOperation().execute("");
        progress = new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
        progress.setCancelable(false);
        progress.show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.getItem(0).setTitle(username);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.user) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ((MyRecyclerViewAdapter) mAdapter).setOnItemClickListener(new MyRecyclerViewAdapter
                .MyClickListener() {
            @Override
            public void onItemClick(final int position, View v) {
                Log.i(LOG_TAG, " Clicked on Item " + position);
                final String scompany,scompanyid,senddate,slocation,sdesc;
                scompany=company.get(position);
                scompanyid=companyid.get(position);
                slocation=location.get(position);
                sdesc=desc.get(position);
                senddate=enddates.get(position);
                Intent intent = new Intent(getApplicationContext(), Delivery_or_Visit.class);
                //Intent intent = new Intent(getApplicationContext(), TimeInTimeOut.class);
                intent.putExtra("username",username);
                intent.putExtra("company",scompany);
                intent.putExtra("companyid",scompanyid);
                intent.putExtra("location",slocation);
                intent.putExtra("description",sdesc);
                intent.putExtra("Cookie",cookie);
                intent.putExtra("enddate",senddate);
                intent.putExtra("client", client);
                startActivity(intent);

                }
        });
    }

    private void getDataSet(String jsonarray)
    {
        JSONObject  jsonRootObject;
        try {
            jsonarray="{\"Clients\":"+jsonarray+"}";
            Log.e("Clients",jsonarray);
            jsonRootObject = new JSONObject(jsonarray);
            JSONArray jsonArray = jsonRootObject.optJSONArray("Clients");
            String name,id,enddate;
            for(int i=0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                name = jsonObject.optString("wbsDesc").toString();
                id=jsonObject.optString("wbsId").toString();
                enddate=jsonObject.optString("endDate");
                desc.add(i,name);
                String names[]=name.split("-");
                DataObject obj = new DataObject(names[1], names[0]);
                results.add(i, obj);
                company.add(i, names[0]);
                companyid.add(i,id);
                location.add(i,names[1]);
                enddates.add(i,enddate);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }




    private class LongOperation extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {


            URLConnection urlConnection;
            StringBuilder result = new StringBuilder();

            try {
                String urlpart= "";
                for (String temp : projectIds)
                {
                    urlpart = urlpart.concat("'" + temp + "',");
                }
                Log.e("client", client);
                urlpart = urlpart.substring(0, urlpart.length()-1);
                URL url = new URL("http://remote.uds.in:8081/xtime/client/client/" + client + "-" + urlpart);
                Log.e("urlcheck", url.toString());
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
            mRecyclerView.setAdapter(mAdapter);
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

}