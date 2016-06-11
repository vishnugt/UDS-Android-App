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
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class RecyclerViewforMaterials extends RecyclerView
        .Adapter<RecyclerViewforMaterials
        .DataObjectHolder> {
    private static String LOG_TAG = "RecyclerViewforMaterials";
    private ArrayList<DataObject> mDataset;
    public ArrayList<String> countmaterial=new ArrayList<>();
    private static MyClickListener myClickListener;

    public static class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        TextView materialname;
        EditText materialcount;

        public DataObjectHolder(View itemView) {
            super(itemView);
            materialname = (TextView) itemView.findViewById(R.id.materialname);
            materialcount = (EditText) itemView.findViewById(R.id.materialcount);
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

    public RecyclerViewforMaterials(ArrayList<DataObject> myDataset) {
        mDataset = myDataset;
        for(int i=0;i<mDataset.size();i++)
        {
            countmaterial.add(i,0+"");
        }
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.material_cardview, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, final int position) {
        holder.materialname.setText(mDataset.get(position).getmText1());
        //holder.dateTime.setText(mDataset.get(position).getmText2());
        holder.materialcount.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                //setting data to array, when changed
                countmaterial.set(position,s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                countmaterial.set(position,s.toString());

            }

            @Override
            public void afterTextChanged(Editable s) {
                countmaterial.set(position,s.toString());
            }
        });
    }

    public void addItem(DataObject dataObj, int index) {
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