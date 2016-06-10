package in.uds.vishnugt.alpha3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Delivery_or_Visit extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_delivery_supervisor);
    }

    public void material_delivery_btn_function(View v)
    {
        Intent intent = new Intent(getApplicationContext(), Material_Activity.class);
        startActivity(intent);
    }
}
