package com.example.sashok.lesssoner;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by sashok on 22.3.17.
 */

public class CardViewAdapter extends RecyclerView.Adapter<CardViewAdapter.MyHolder> {
    private String[] mDataset;
    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view, parent, false);
        MyHolder vh = new MyHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        holder.mTextView.setText(mDataset[position]);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    @Override
    public int getItemCount() {
        return mDataset.length;
    }
    public class MyHolder extends  RecyclerView.ViewHolder {
        public TextView mTextView;
        public MyHolder(View view) {
            super(view);
            this.mTextView=(TextView)view.findViewById(R.id.info_text);
        }
    }
    public CardViewAdapter(String[] myDataset){

        mDataset = myDataset;
    }
}
