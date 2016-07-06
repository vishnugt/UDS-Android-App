package in.uds.vishnugt.alpha3;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

public class feedback_activity extends AppCompatActivity {

    Bundle extras;
    String username;
    String company;
    String companyid;
    String outputresponse;
    String desc;
    String cookie;
    String month;
    int year;
    ProgressDialog progresssubmit;
    Spinner spinattendance,spingrooming,spintoilet,spinfloor,spinpantryroom,spindusting,spinfeedback;
    ArrayList<String> input=new ArrayList<>();
    AlertDialog alertDialog;
    EditText comments;
    String birla_shriram;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_activity);

        extras=getIntent().getExtras();
        username=extras.getString("username");
        company=extras.getString("company");
        companyid=extras.getString("companyid");
        cookie=extras.getString("cookie");
        desc=extras.getString("description");
        month=extras.getString("month");
        year=extras.getInt("year");
        birla_shriram=extras.getString("birla_shriram");

        spinattendance=(Spinner)findViewById(R.id.spinattendance);
        spingrooming=(Spinner)findViewById(R.id.spingrooming);
        spintoilet=(Spinner)findViewById(R.id.spintoilet);
        spinfloor=(Spinner)findViewById(R.id.spinfloor);
        spinpantryroom=(Spinner)findViewById(R.id.spinpantryroom);
        spindusting=(Spinner)findViewById(R.id.spindusting);
        spinfeedback=(Spinner)findViewById(R.id.spinfeedback);
        comments=(EditText)findViewById(R.id.comment);
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
                input.add(0, spinattendance.getSelectedItem().toString());
                input.add(1, spingrooming.getSelectedItem().toString());
                input.add(2, spintoilet.getSelectedItem().toString());
                input.add(3, spinfloor.getSelectedItem().toString());
                input.add(4, spinpantryroom.getSelectedItem().toString());
                input.add(5, spindusting.getSelectedItem().toString());
                input.add(6, spinfeedback.getSelectedItem().toString());
                input.add(7, comments.getText().toString());

                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                String count;
                count = "";
                for (int i = 0; i < 8; i++) {
                    count = count.concat(input.get(i) + "\n");
                }
                alertDialogBuilder.setMessage(count);
                alertDialogBuilder.setTitle("Are you sure?");
                alertDialogBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        progresssubmit = new ProgressDialog(feedback_activity.this );
                        progresssubmit.setTitle("Loading");
                        progresssubmit.setMessage("Wait while loading...");
                        progresssubmit.setCancelable(false);
                        progresssubmit.show();
                        new LongOperationsubmit().execute("");
                    }
                });
                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
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
                oswrite=oswrite.concat("\"attendance\": \""+input.get(0).toString()+"\",");
                oswrite=oswrite.concat("\"grooming\": \""+input.get(1).toString()+"\",");
                oswrite=oswrite.concat("\"cleanlinessToilet\": \""+input.get(2).toString()+"\",");
                oswrite=oswrite.concat("\"cleanlinessFloor\": \""+input.get(3).toString()+"\",");
                oswrite=oswrite.concat("\"cleanlinessPantry\": \""+input.get(4).toString()+"\",");
                oswrite=oswrite.concat("\"cleanlinessDusting\": \""+input.get(5).toString()+"\",");
                oswrite=oswrite.concat("\"feedback\": \""+input.get(6).toString()+"\",");
                oswrite=oswrite.concat("\"comments\": \""+input.get(7).toString()+"\"");
                Date today = new Date();
                String current = today.toString();
                oswrite=oswrite.concat("} ], \"requestFields\": { \"projectId\": \""+companyid+"\", \"projectDesc\": \""+desc+"\", \"recordcreationdate\": \""+current+"\", \"monthandYear\": \""+month+" "+year+"\", \"timeClient\" : \"\", \"timeRegional\" : \"\", \"clientId\" : \"\", \"regionalHeadId\" : \"\", \"date\" : \"\", \"deliveryOrVisit\": \"visit\"}, \"requestType\": \"supervisor\", \"fresh\": true, \"status\": \"Visited\", \"changed\": true, \"transitions\": { \"1\": \"Visited\" }, \"editReason\": \"Automated from app\" }");
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
            progresssubmit.dismiss();
            Log.d("result", result);
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
