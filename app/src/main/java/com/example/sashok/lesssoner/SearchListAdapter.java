package com.example.sashok.lesssoner;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by sashok on 26.3.17.
 */

public class SearchListAdapter extends BaseAdapter implements Filterable {

    private MainActivity activity;
    private FriendFilter friendFilter;
    public ArrayList<Word> friendList;
    private ArrayList<Word> filteredList;


    public SearchListAdapter(MainActivity activity, ArrayList<Word> friendList) {
        this.activity = activity;
        this.friendList = friendList;
        this.filteredList = friendList;
        getFilter();
    }

    @Override
    public int getCount() {
        return filteredList.size();
    }

    @Override
    public Object getItem(int i) {
        return filteredList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    @Override
    public View getView(int position, View view, ViewGroup parent) {
        // A ViewHolder keeps references to children views to avoid unnecessary calls
        // to findViewById() on each row.
        final ViewHolder holder;
        final Word word  = (Word) getItem(position);

        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.search_list_layout, parent, false);
            holder = new ViewHolder();
            holder.name = (TextView) view.findViewById(R.id.search_text_item);
              view.setTag(holder);

        } else {
            // get view holder back
            holder = (ViewHolder) view.getTag();
        }
        holder.name.setText(word.pl_word);
        return view;
    }

    /**
     * Get custom filter
     * @return filter
     */
    @Override
    public Filter getFilter() {
        if (friendFilter == null) {
            friendFilter = new FriendFilter();
        }

        return friendFilter;
    }

    /**
     * Keep reference to children view to avoid unnecessary calls
     */
    static class ViewHolder {
        TextView name;
    }

    private class FriendFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            if (constraint!=null && constraint.length()>0) {
                ArrayList<Word> tempList = new ArrayList<Word>();

                for (Word word : friendList) {
                    if (word.pl_word.toLowerCase().contains(constraint.toString().toLowerCase())) {
                        tempList.add(word);
                    }
                }

                filterResults.count = tempList.size();
                filterResults.values = tempList;
            } else {
                filterResults.count = friendList.size();
                filterResults.values = friendList;
            }

            return filterResults;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredList = (ArrayList<Word>) results.values;
            notifyDataSetChanged();
        }
    }

}