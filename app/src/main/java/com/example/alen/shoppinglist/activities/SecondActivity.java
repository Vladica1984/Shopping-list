package com.example.alen.shoppinglist.activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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
import android.widget.Toast;

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
    int key;
    ListView listView;
    Toolbar toolbar;
    Items iAdd = new Items();
    public static String DETAIL_KEY = "DETAIL_KEY";
    private List<Items> list;

    //private ListItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.app_name);

        key = getIntent().getExtras().getInt(MainActivity.MAIN_KEY);

        try {
            mainList = getDatabaseHelper().getmMainListDao().queryForId(key);

            final TextView listName = (TextView) findViewById(R.id.tv_nameListInActivitySecond);
            listName.setText(mainList.getNameOfList());

            list = getDatabaseHelper().getmItemsDao().queryBuilder().
                    where().
                    eq(Items.TABLE_FIELD_MAINLIST, mainList.getIdMainList()).
                    query();
            listView = (ListView) findViewById(R.id.lv_listItem);
            final ListItemAdapter adapter = new ListItemAdapter(SecondActivity.this, (ArrayList<Items>) list);
            //final ListView listView = (ListView) findViewById(R.id.list_item);
            //adapter = new ListItemAdapter(SecondActivity.this, (ArrayList<Items>) list);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Items i = (Items) listView.getItemAtPosition(position);
                    Intent intent = new Intent(SecondActivity.this, ThirdActivity.class);
                    intent.putExtra(DETAIL_KEY, i.getIdItems());
                    startActivity(intent);

//                    final Dialog dialog = new Dialog(SecondActivity.this);
//                    dialog.setContentView(R.layout.dialog_edit_articles);
//                    dialog.setTitle("Edit article details");
//
//                    Button ok = (Button) dialog.findViewById(R.id.button_edit_article);
//                    ok.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            EditText articleName = (EditText)dialog.findViewById(R.id.et_edit_nameArticle);
//                            EditText articleAmount = (EditText)dialog.findViewById(R.id.et_edit_amountArticle);
//                            i.setName(articleName.getText().toString());
//                            i.setAmount(articleAmount.getText().toString());
//
//                            Log.i("ime sa polja",articleName.toString());
//                            Log.i("ime",i.getName());
//                            Log.i("vrednost",i.getAmount());
//
//                            try {
//                                getDatabaseHelper().getmItemsDao().update(i);
//                                refresh();
//                            } catch (SQLException e) {
//                                e.printStackTrace();
//                            }
//                            dialog.dismiss();
//                        }
//                    });
//                    dialog.show();
//                    return false;
                }
            });
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        refresh();
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_second, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_article:
                final Dialog dialog = new Dialog(this);
                dialog.setContentView(R.layout.dialog_edit_articles);
                Button add = (Button) dialog.findViewById(R.id.button_edit_article);
                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText articleName = (EditText)dialog.findViewById(R.id.et_edit_nameArticle);
                        EditText articleAmount = (EditText)dialog.findViewById(R.id.et_edit_amountArticle);

                        iAdd.setName(articleName.getText().toString());
                        iAdd.setAmount(articleAmount.getText().toString());
                        if (iAdd.getName().equals("") && iAdd.getAmount().equals("")) {
                            Toast.makeText(SecondActivity.this, "Enter name and amount", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        iAdd.setMainList(mainList);
                        try {
                            getDatabaseHelper().getmItemsDao().create(iAdd);
                            refresh();
                            Log.i("Sta je u bazi", getDatabaseHelper().getmItemsDao().queryForAll().toString());
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        dialog.dismiss();
                    }
                });
                dialog.show();
                break;

            case R.id.remove_list:
                final AlertDialog.Builder removeListDialog = new AlertDialog.Builder(SecondActivity.this);
                removeListDialog.setTitle("Do you want to delete list ?");
                removeListDialog.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            iAdd = getDatabaseHelper().getmItemsDao().queryForId(key);
                            getDatabaseHelper().getmItemsDao().delete(iAdd);
                            getDatabaseHelper().getmMainListDao().delete(mainList);
                            refresh();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        finish();
                    }
                });
                removeListDialog.show();
                break;
            case R.id.edit_name_list:
                final Dialog dialog2 = new Dialog(this);
                dialog2.setContentView(R.layout.dialog_edit_name_list);

                Button add2 = (Button)dialog2.findViewById(R.id.button_edit_list_name_add);
                add2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText listName = (EditText) dialog2.findViewById(R.id.et_edit_list_name);

                        try {
                            MainList mlEdit = getDatabaseHelper().getmMainListDao().queryForId(key);
                            mlEdit.setNameOfList(listName.getText().toString());
                            getDatabaseHelper().getmMainListDao().update(mlEdit);
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

    @Override
    protected void onResume() {
        refresh();
        super.onResume();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
