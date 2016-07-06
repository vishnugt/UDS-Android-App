package in.uds.vishnugt.alpha3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

public class Activity_Client extends AppCompatActivity {

    public String username, cookie;
    ArrayList<String> projectIds = new ArrayList<>();
    Bundle extras;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

        extras=getIntent().getExtras();
        username=extras.getString("uname");
        cookie=extras.getString("Cookie");
        projectIds = extras.getStringArrayList("projectIds");
    }

    public void birlabtn(View v)
    {
        Intent intent = new Intent(this, ClientSelection_Activity.class);
        intent.putExtra("birla_shriram", "BIRLA");
        intent.putExtra("uname", username);
        intent.putExtra("Cookie",cookie);
        intent.putExtra("projectIds", projectIds);
        startActivity(intent);
    }
    public void shrirrambtn(View v)
    {

        Intent intent = new Intent(this, ClientSelection_Activity.class);
        intent.putExtra("birla_shriram", "SHRIRAM");
        intent.putExtra("uname", username);
        intent.putExtra("Cookie",cookie);
        intent.putExtra("projectIds", projectIds);
        startActivity(intent);
    }
}
