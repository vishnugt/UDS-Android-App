package in.uds.vishnugt.alpha3;

import android.app.ListActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ClientSelection_Activity extends ListActivity {


    ArrayList<String> array= new ArrayList<>();

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            array.add("Apple");
            array.add("Apple");
            array.add("Apple");
            array.add("Apple");
            array.add("Apple");
            array.add("Apple");

            setListAdapter(new ArrayAdapter<String>(this, R.layout.single_listview_row,array));
            ListView listView = getListView();
            listView.setTextFilterEnabled(true);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    // When clicked, show a toast with the TextView text
                    Toast.makeText(getApplicationContext(),
                            ((TextView) view).getText(), Toast.LENGTH_SHORT).show();
                }
            });

        }

    }