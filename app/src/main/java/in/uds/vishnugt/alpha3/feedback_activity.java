package in.uds.vishnugt.alpha3;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class feedback_activity extends AppCompatActivity {

    Bundle extras;
    String username;
    String company;
    String companyid;
    Spinner spinattendance,spingrooming,spintoilet,spinfloor,spinpantryroom,spindusting,spinfeedback;
    ArrayList<String> input=new ArrayList<>();
    AlertDialog alertDialog;
    EditText comments;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_activity);

        extras=getIntent().getExtras();
        username=extras.getString("username");
        company=extras.getString("company");
        companyid=extras.getString("companyid");

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
                        Toast.makeText(getApplicationContext(), "You clicked yes button", Toast.LENGTH_LONG).show();
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
}
