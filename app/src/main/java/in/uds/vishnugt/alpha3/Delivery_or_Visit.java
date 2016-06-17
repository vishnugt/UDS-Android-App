package in.uds.vishnugt.alpha3;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class Delivery_or_Visit extends AppCompatActivity {

    String username;
    String company;
    String outputresponse;
    String companyid;
    String location;
    TextView companyname,locationname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_delivery_supervisor);

        companyname=(TextView)findViewById(R.id.company);
        locationname=(TextView)findViewById(R.id.location);

        Bundle extras=getIntent().getExtras();
        username=extras.getString("username");
        company=extras.getString("company");
        companyid=extras.getString("companyid");
        location=extras.getString("location");

        companyname.setText(company.trim());
        locationname.setText(location.trim());

        Log.e("company",company);
        Log.e("companyid",companyid);
        Log.e("location",location);

        new LongOperation().execute("");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.getItem(0).setTitle(username);
        return true;
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
                connection.setRequestProperty("Cookie", "JSESSIONID=5803F8F7FCB9E4FC3E7B543E5B64F8D2; _ga=GA1.2.1002082324.1463725136");
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
            Toast.makeText(getApplicationContext(), outputresponse, Toast.LENGTH_LONG).show();
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
