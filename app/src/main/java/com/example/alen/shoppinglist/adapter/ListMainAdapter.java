package com.example.alen.shoppinglist.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.alen.shoppinglist.R;
import com.example.alen.shoppinglist.model.Items;
import com.example.alen.shoppinglist.model.MainList;

import java.util.ArrayList;

/**
 * Created by Alen on 18-Apr-17.
 */

public class ListMainAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<MainList> mainLists;
    private Items items = new Items();

    public ListMainAdapter(Context context, ArrayList<MainList> mainLists) {
        this.context = context;
        this.mainLists = mainLists;
    }

    @Override
    public int getCount() {
        return mainLists.size();
    }

    @Override
    public Object getItem(int position) {
        return mainLists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.adapter_main_list, parent, false);
        }

        TextView nameOfList = (TextView) convertView.findViewById(R.id.tv_listName);
        nameOfList.setText(mainLists.get(position).getNameOfList());

        TextView complete = (TextView) convertView.findViewById(R.id.tv_mainCompleted);
        complete.setText(mainLists.get(position).getComplete());

//        TextView noArticles = (TextView) convertView.findViewById(R.id.tv_noOfArticles);
//        noArticles.setText(mainLists.get(position).getNoOfArticles());

        TextView protect = (TextView) convertView.findViewById(R.id.tv_protected);
        protect.setText(mainLists.get(position).getProtect());

        return convertView;
    }

    public void updateAdapter(ArrayList<MainList> list) {
        this.mainLists = list;
        notifyDataSetChanged();
    }

    public void clear() {
        mainLists.clear();
    }
}
