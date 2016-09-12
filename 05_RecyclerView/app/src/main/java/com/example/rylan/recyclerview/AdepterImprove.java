package com.example.rylan.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Rylan on 2016/9/7.
 */


public class AdepterImprove extends RecyclerView.Adapter<AdepterImprove.ViewHolder> {
    private String[] Dataset;
    @Override
    public AdepterImprove.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.itemview, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder temp = new ViewHolder(v);
        return temp;
    }

    public AdepterImprove(String[] myDataset) {
        Dataset = myDataset;
    }

    @Override
    public void onBindViewHolder(AdepterImprove.ViewHolder holder, int position) {
        holder.mTextView.setText(Dataset[position]);
    }

    @Override
    public int getItemCount() {
        return Dataset.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;
        public ViewHolder(View itemView) {
            super(itemView);
            mTextView =(TextView) itemView.findViewById(R.id.textView);

        }
    }
}
