package in.uds.vishnugt.alpha3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

//url http://192.168.0.172:8080/flow/rest/login?loggedIn=false&password=%22cat%22&rememberMe=false&requestList=&transactions=&txnlocations=&userId=0&username=%22cats%22


public class login_activity extends AppCompatActivity {
    EditText username;
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);


    }
    public void signin(View v)
    {
        if(!(username.getText().toString().equals("admin") && password.getText().toString().equals("admin")))
        {
            Intent intent = new Intent(this, ClientSelection_Activity.class);
            this.startActivity(intent);
        }
        else
        {
            Toast.makeText(this, "Credentials wrong >.<", Toast.LENGTH_SHORT).show();
        }
    }
}
