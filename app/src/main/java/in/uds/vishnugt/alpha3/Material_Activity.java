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
import android.widget.Toast;

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

public class Material_Activity extends AppCompatActivity {


    private RecyclerView mRecyclerView;
    private RecyclerViewforMaterials mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    AlertDialog alertDialog;
    ArrayList results;
    ArrayList<String> materials=new ArrayList<>();
    ProgressDialog progress;
    Bundle extras;
    String username;
    String company;
    String companyid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_view);

        extras=getIntent().getExtras();
        username=extras.getString("username");
        company=extras.getString("company");
        companyid=extras.getString("companyid");

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
                        Toast.makeText(getApplicationContext(), "You clicked yes button", Toast.LENGTH_LONG).show();
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


    private class LongOperation extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            URLConnection urlConnection;
            StringBuilder result = new StringBuilder();

            try {
                URL url = new URL("http://remote.uds.in:8081/xtime/client/materials");
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
            for(int i=0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                name = jsonObject.optString("materialDesc").toString();
                Log.d("materials", name);
                materials.add(i,name);
                DataObject obj = new DataObject(name.toString(), "");
                results.add(i, obj);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
