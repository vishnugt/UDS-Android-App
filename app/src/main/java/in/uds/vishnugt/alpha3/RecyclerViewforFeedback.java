package in.uds.vishnugt.alpha3;

/**
 * Created by vishnu.g on 5/21/2016.
 */

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class RecyclerViewforFeedback extends RecyclerView
        .Adapter<RecyclerViewforFeedback
        .DataObjectHolder> {
    private static String LOG_TAG = "RecyclerViewforFeedback";
    private ArrayList<DataObject2> mDataset;

    public ArrayList<String> feedbackconditionArray=new ArrayList<>();
    public ArrayList<String> feedbackremarksArray=new ArrayList<>();
    public ArrayList<String> yesornoArray=new ArrayList<>();

    private static MyClickListener myClickListener;

    public static class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        TextView feedbackname;
        EditText feebackcondition;
        EditText feedbackremarks;
        Spinner spinner;

        public DataObjectHolder(View itemView) {
            super(itemView);
            feedbackname = (TextView) itemView.findViewById(R.id.feedbackname);
            feebackcondition = (EditText) itemView.findViewById(R.id.feedbackresponse);
            feedbackremarks = (EditText) itemView.findViewById(R.id.feedbackremarks);
            spinner = (Spinner) itemView.findViewById(R.id.spinner);
            Log.i(LOG_TAG, "Adding Listener");
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            myClickListener=new MyClickListener() {
                @Override
                public void onItemClick(int position, View v) {
                    return;
                }
            };
        }
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public RecyclerViewforFeedback(ArrayList<DataObject2> myDataset) {
        mDataset = myDataset;
        for(int i=0;i<mDataset.size();i++)
        {
            feedbackconditionArray.add(i,"");
            feedbackremarksArray.add(i,"");
            yesornoArray.add(i, "");
        }
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.feedback_cardview, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, final int position) {
        holder.feedbackname.setText(mDataset.get(position).getTitles());
        //holder.feedbackname.setText(mDataset.get(position).getCondition());
        //holder.dateTime.setText(mDataset.get(position).getmText2());
        holder.feebackcondition.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                //setting data to array, when changed
                feedbackconditionArray.set(position,s.toString());
                Log.e(position+"",s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                feedbackconditionArray.set(position,s.toString());

            }

            @Override
            public void afterTextChanged(Editable s) {
                feedbackconditionArray.set(position,s.toString());
            }
        });

        holder.feedbackremarks.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                //setting data to array, when changed
                feedbackremarksArray.set(position,s.toString());
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                feedbackremarksArray.set(position,s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {
                feedbackremarksArray.set(position,s.toString());
            }
        });


        holder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                yesornoArray.set(position, parentView.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

    }



    public void addItem(DataObject2 dataObj, int index) {
        mDataset.add(index, dataObj);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        mDataset.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }
}