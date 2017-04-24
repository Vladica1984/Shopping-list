package com.example.alen.shoppinglist.activities;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
 * Created by Alen on 22-Apr-17.
 */

public class ThirdActivity extends AppCompatActivity {

    private ORMDataBaseHelper databaseHelper;
    private Items i;
    private TextView name;
    private TextView amount;
    private MainList mainList;
    private List<MainList> ml;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.app_name);

        final int key = getIntent().getExtras().getInt(SecondActivity.DETAIL_KEY);

        try {
            i = getDatabaseHelper().getmItemsDao().queryForId(key);
            ml = getDatabaseHelper().getmMainListDao().queryForAll();

            name = (TextView) findViewById(R.id.tv_nameArticleDetail);
            amount = (TextView) findViewById(R.id.tv_amountArticleDetail);
            name.setText(i.getName());
            amount.setText(i.getAmount());

        } catch (SQLException e) {
            e.printStackTrace();
        }

        final CheckBox c = (CheckBox) findViewById(R.id.cb_purchase);
        c.setChecked(c.isChecked());
        c.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (c.isChecked()) {
                    c.setText("Purchased");
                    c.setChecked(true);
                    try {
                        i.setPurchased(true);
                        i.setPurchasedStatus("Purchased");
                        getDatabaseHelper().getmItemsDao().update(i);
                        Log.i("Database after Items", getDatabaseHelper().getmItemsDao().queryForAll().toString());
                        Log.i("Database after MainList", getDatabaseHelper().getmMainListDao().queryForAll().toString());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } else {
                    c.setText("Not purchased");
                    c.setChecked(false);
                    try {
                        i.setPurchased(false);
                        i.setPurchasedStatus("Not purchased");
                        getDatabaseHelper().getmItemsDao().update(i);
                        Log.i("Database after Items", getDatabaseHelper().getmItemsDao().queryForAll().toString());
                        Log.i("Database after MainList", getDatabaseHelper().getmMainListDao().queryForAll().toString());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    refresh();
                }
            }
        });
        if (c.isChecked()) {
            c.setText("Purchased");
        } else {
            c.setText("Not purchased");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_third, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_article_detail:
                final Dialog dialog = new Dialog(ThirdActivity.this);
                dialog.setContentView(R.layout.dialog_edit_articles);

                Button ok = (Button)dialog.findViewById(R.id.button_edit_article);
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText articleName = (EditText) dialog.findViewById(R.id.et_edit_nameArticle);
                        EditText articleAmount = (EditText) dialog.findViewById(R.id.et_edit_amountArticle);

                        i.setName(articleName.getText().toString());
                        i.setAmount(articleAmount.getText().toString());

                        try {
                            getDatabaseHelper().getmItemsDao().update(i);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        dialog.dismiss();
                        finish();
                    }
                });
                dialog.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public ORMDataBaseHelper getDatabaseHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(this, ORMDataBaseHelper.class);
        }
        return databaseHelper;
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

    @Override
    protected void onResume() {
        try {
            Log.i("Database!!!", getDatabaseHelper().getmMainListDao().queryForAll().toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        super.onResume();
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
