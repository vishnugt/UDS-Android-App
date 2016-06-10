package in.uds.vishnugt.alpha3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;

public class Delivery_or_Visit extends AppCompatActivity {

    String username;
    String company;
    String companyid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_delivery_supervisor);

        Bundle extras=getIntent().getExtras();
        username=extras.getString("username");
        company=extras.getString("company");
        companyid=extras.getString("companyid");

        Log.e("company",company);
        Log.e("companyid",companyid);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.getItem(0).setTitle(username);
        return true;
    }

    public void material_delivery_btn_function(View v)
    {
        Intent intent = new Intent(getApplicationContext(), Material_Activity.class);
        startActivity(intent);
    }
}
