package com.example.alen.shoppinglist.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import com.example.alen.shoppinglist.Database.ORMDataBaseHelper;
import com.example.alen.shoppinglist.R;
import com.example.alen.shoppinglist.model.Items;
import com.example.alen.shoppinglist.model.MainList;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Alen on 18-Apr-17.
 */

public class ListItemAdapter extends BaseAdapter {

    private ORMDataBaseHelper databaseHelper;
    private Context context;
    private ArrayList<Items> itemses;


    public ListItemAdapter(Context context, ArrayList<Items> itemses) {
        this.context = context;
        this.itemses = itemses;
    }

    @Override
    public int getCount() {
        return itemses.size();
    }

    @Override
    public Object getItem(int position) {
        return itemses.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.adapter_item_list, null);
        }
        TextView name = (TextView) convertView.findViewById(R.id.tv_articleName);
        name.setText(itemses.get(position).getName());

        TextView amount = (TextView) convertView.findViewById(R.id.tv_amount);
        amount.setText(itemses.get(position).getAmount());

        TextView purchasedStatus = (TextView) convertView.findViewById(R.id.tv_purchasedOrNot);

        purchasedStatus.setText(itemses.get(position).getPurchasedStatus());
        if (purchasedStatus.getText().toString().equals("Purchased")) {
            purchasedStatus.setText(R.string.purchased);
        } else {
            purchasedStatus.setText(R.string.not_purchased);
        }

        ImageButton deleteButton = (ImageButton) convertView.findViewById(R.id.ib_delete);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                dialog.setTitle("Do you want to delete item?");
                dialog.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            getDatabaseHelper().getmItemsDao().delete(itemses.remove(position));
                            notifyDataSetChanged();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        return convertView;
    }

    public ORMDataBaseHelper getDatabaseHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(context, ORMDataBaseHelper.class);
        }
        return databaseHelper;
    }

    public void updateAdapter(ArrayList<Items> list) {
        this.itemses=list;
        notifyDataSetChanged();
    }

    public void clear() {
        itemses.clear();
    }
}