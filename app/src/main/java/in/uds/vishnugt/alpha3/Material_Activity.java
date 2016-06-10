package in.uds.vishnugt.alpha3;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

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


    ArrayList<String> materials=new ArrayList<>();
    ProgressDialog progress;
    Bundle extras;
    String username;
    String company;
    String companyid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_material);

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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.getItem(0).setTitle(username);
        return true;
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
            //mRecyclerView.setAdapter(mAdapter);
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
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
