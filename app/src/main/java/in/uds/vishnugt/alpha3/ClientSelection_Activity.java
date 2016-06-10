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
    String username;
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_view);
        setTitle("Client Selection");
        username=getIntent().getExtras().getString("uname");
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
                final String scompany,scompanyid;
                scompany=company.get(position);
                scompanyid=company.get(position);
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(v.getContext());
                alertDialogBuilder.setTitle("Confirm Action");
                alertDialogBuilder.setMessage(company.get(position));
                alertDialogBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        //Toast.makeText(ClientSelection_Activity.this, "You clicked yes button for" + position, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), Delivery_or_Visit.class);
                        intent.putExtra("username",username);
                        intent.putExtra("company",scompany);
                        intent.putExtra("companyid",scompanyid);
                        startActivity(intent);

                    }
                });
                alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog = alertDialogBuilder.create();
                alertDialog.show();            }
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
            String name;
            for(int i=0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                name = jsonObject.optString("wbsDesc").toString();
                String names[]=name.split("-");
                DataObject obj = new DataObject(names[1], names[0]);
                results.add(i, obj);
                company.add(i, names[0]);
                companyid.add(i,names[1]);
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
                URL url = new URL("http://remote.uds.in:8081/xtime/client/details/'UDS200016950001','UDS200016960002'");
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