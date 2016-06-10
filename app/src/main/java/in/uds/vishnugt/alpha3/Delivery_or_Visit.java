package in.uds.vishnugt.alpha3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class Delivery_or_Visit extends AppCompatActivity {

    String username;
    String company;
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
}
