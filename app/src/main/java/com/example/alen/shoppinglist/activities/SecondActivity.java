package com.example.alen.shoppinglist.activities;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.alen.shoppinglist.Database.ORMDataBaseHelper;
import com.example.alen.shoppinglist.R;
import com.example.alen.shoppinglist.adapter.ListItemAdapter;
import com.example.alen.shoppinglist.model.Items;
import com.example.alen.shoppinglist.model.MainList;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alen on 18-Apr-17.
 */

public class SecondActivity extends AppCompatActivity {

    ORMDataBaseHelper databaseHelper;
    MainList mainList;
    Items items = null;
    Items i = new Items();
    int key;
    ListView listView;

    private List<Items> list;
    //private ListItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.app_name);

        key = getIntent().getExtras().getInt(MainActivity.MAIN_KEY);

        try {
            mainList = getDatabaseHelper().getmMainListDao().queryForId(key);

            TextView listName = (TextView) findViewById(R.id.tv_nameListInActivitySecond);
            listName.setText(mainList.getNameOfList());

            list = getDatabaseHelper().getmItemsDao().queryBuilder().
                    where().
                    eq(Items.TABLE_FIELD_MAINLIST, mainList.getIdMainList()).
                    query();
            listView = (ListView) findViewById(R.id.lv_listItem);
            ListItemAdapter adapter = new ListItemAdapter(SecondActivity.this, (ArrayList<Items>) list);
            //final ListView listView = (ListView) findViewById(R.id.list_item);
            //adapter = new ListItemAdapter(SecondActivity.this, (ArrayList<Items>) list);
            listView.setAdapter(adapter);

            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(final AdapterView<?> parent, final View view, final int position, long id) {
                    final Dialog dialog = new Dialog(SecondActivity.this);
                    dialog.setContentView(R.layout.dialog_edit_articles);
                    dialog.setTitle("Edit article details");

                    Button ok = (Button) dialog.findViewById(R.id.button_edit_article);
                    ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            EditText articleName = (EditText)dialog.findViewById(R.id.et_edit_nameArticle);
                            EditText articleAmount = (EditText)dialog.findViewById(R.id.et_edit_amountArticle);
                            i.setName(articleName.getText().toString());
                            i.setAmount(articleAmount.getText().toString());

                            Log.i("ime sa polja",articleName.toString());
                            Log.i("ime",i.getName());
                            Log.i("vrednost",i.getAmount());

                            try {
                                getDatabaseHelper().getmItemsDao().update(i);
                                refresh();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                    return false;
                }
            });
        }
        catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_article:
                final Dialog dialog = new Dialog(this);
                dialog.setContentView(R.layout.dialog_item_list);

                Button add = (Button) dialog.findViewById(R.id.btn_addArticle);
                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText articleName = (EditText)dialog.findViewById(R.id.et_nameOfArticle);
                        EditText articleAmount = (EditText)dialog.findViewById(R.id.et_amountOfArticle);

                        i.setName(articleName.getText().toString());
                        i.setAmount(articleAmount.getText().toString());
                        i.setMainList(mainList);

                        try {
                            Log.i("Sta je u bazi", String.valueOf(getDatabaseHelper().getmItemsDao().queryBuilder().where().eq(Items.TABLE_FIELD_NAME, i.getName())));
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                        try {
                            getDatabaseHelper().getmItemsDao().create(i);
                            refresh();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        dialog.dismiss();
                    }
                });
                dialog.show();
                break;

            case R.id.remove_article:
                try {
                    getDatabaseHelper().getmMainListDao().delete(mainList);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                finish();
                break;
            case R.id.edit_name_list:
                final Dialog dialog2 = new Dialog(this);

                Button add2 = (Button)dialog2.findViewById(R.id.button_edit_list_name_add);
                add2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText listName = (EditText) dialog2.findViewById(R.id.et_edit_list_name);

                        mainList.setNameOfList(listName.getText().toString());
                        try {
                            getDatabaseHelper().getmMainListDao().update(mainList);
                            refresh();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        finish();
                        dialog2.dismiss();
                    }
                });
                dialog2.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void refresh() {
        ListView listItem = (ListView) findViewById(R.id.lv_listItem);

        if (listItem != null) {
            ListItemAdapter adapter = (ListItemAdapter) listItem.getAdapter();
            if (adapter != null) {
                try {
                    adapter.clear();
                    List<Items> list = getDatabaseHelper().getmItemsDao().queryBuilder()
                            .where()
                            .eq(Items.TABLE_FIELD_MAINLIST, mainList.getIdMainList())
                            .query();
                    adapter.updateAdapter((ArrayList<Items>) list);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public ORMDataBaseHelper getDatabaseHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(this, ORMDataBaseHelper.class);
        }
        return databaseHelper;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }
}
