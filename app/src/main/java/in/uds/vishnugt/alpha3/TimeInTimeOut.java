package in.uds.vishnugt.alpha3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeInTimeOut extends AppCompatActivity {
    TimePicker timeIn;
    TimePicker timeOut;
    Bundle extras;
    String username;
    String company;
    String companyid;
    String desc;
    String client;
    String cookie;
    String outputresponse;
    String month;
    String date;
    Integer year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_in_time_out);

        extras=getIntent().getExtras();
        username=extras.getString("username");
        company=extras.getString("company");
        companyid=extras.getString("companyid");
        cookie=extras.getString("cookie");
        desc=extras.getString("description");
        month=extras.getString("month");
        year=extras.getInt("year");
        client=extras.getString("client");
        date=extras.getString("date");


        timeIn = (TimePicker)findViewById(R.id.timeIn);
        timeOut = (TimePicker)findViewById(R.id.timeOut);
    }
    public void submitbtn(View view)
    {
        SimpleDateFormat sdfDate = new SimpleDateFormat("hh:mm a");//dd/MM/yyyy
        Date now = new Date();
        //now.setHours( timeIn.getCurrentHour() < 12 ? timeIn.getCurrentHour()  : timeIn.getCurrentHour() -12 );
        now.setHours( timeIn.getCurrentHour());
        now.setMinutes(timeIn.getCurrentMinute());
        String timeInStr = sdfDate.format(now);
        Log.e("timeInformat", timeInStr);

        //now.setHours( timeOut.getCurrentHour() < 12 ? timeOut.getCurrentHour()  : timeOut.getCurrentHour() -12 );
        now.setHours( timeOut.getCurrentHour() );
        now.setMinutes(timeOut.getCurrentMinute());
        String timeOutStr = sdfDate.format(now);
        Log.e("timeInformat", timeInStr);

        Log.e("timein", timeInStr);
        Log.e("timeout", timeOutStr);
        Log.e("client", client);

        if(client.equals("SHRIRAM")) {
            Intent intent = new Intent(getApplicationContext(), feedback_activity.class);
            //intent = new Intent(getApplicationContext(), TimeInTimeOut.class);
            intent.putExtra("username", username);
            intent.putExtra("company", company);
            intent.putExtra("companyid", companyid);
            intent.putExtra("description", desc);
            intent.putExtra("cookie", cookie);
            intent.putExtra("client", client);
            intent.putExtra("month",month);
            intent.putExtra("year",year);
            intent.putExtra("timeIn", timeInStr);
            intent.putExtra("timeOut", timeOutStr);
            intent.putExtra("date", date);
            startActivity(intent);
        }
        if(client.equals("BIRLA")) {
            Intent intent = new Intent(getApplicationContext(), BirlaFeedbackActivity.class);
            //intent = new Intent(getApplicationContext(), TimeInTimeOut.class);
            intent.putExtra("username", username);
            intent.putExtra("company", company);
            intent.putExtra("companyid", companyid);
            intent.putExtra("description", desc);
            intent.putExtra("cookie", cookie);
            intent.putExtra("client", client);
            intent.putExtra("month",month);
            intent.putExtra("year",year);
            intent.putExtra("timeIn", timeInStr);
            intent.putExtra("timeOut", timeOutStr);
            intent.putExtra("date", date);
            startActivity(intent);
        }

    }
}
