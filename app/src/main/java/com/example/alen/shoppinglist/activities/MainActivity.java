package com.example.alen.shoppinglist.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.alen.shoppinglist.Database.ORMDataBaseHelper;
import com.example.alen.shoppinglist.R;
import com.example.alen.shoppinglist.adapter.ListMainAdapter;
import com.example.alen.shoppinglist.model.Items;
import com.example.alen.shoppinglist.model.MainList;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ORMDataBaseHelper databaseHelper;
    public static String MAIN_KEY = "MAIN_KEY";
    private List<MainList> mainList;
    private ListMainAdapter adapter;

    public enum Completed {
        COMPLETED("Completed"),
        NOT_COMPLETED("Not completed"),
        PURCHASED("Purchased"),
        NOT_PURCHASED("Not purchased");

        private String text;

        Completed (final String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.app_name);

        try {
            mainList = getDatabaseHelper().getmMainListDao().queryForAll();
            final ListView listView = (ListView) findViewById(R.id.lv_list);
            adapter = new ListMainAdapter(MainActivity.this, (ArrayList<MainList>) mainList);

            completedMainList();

            Button add = (Button) findViewById(R.id.button_add);
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditText listName = (EditText) findViewById(R.id.et_nameOfList);
                    MainList m = new MainList();
                    m.setNameOfList(listName.getText().toString());
                    m.setComplete(Completed.NOT_COMPLETED.toString());
                    if (m.getNameOfList().equals("")) {
                        Toast.makeText(MainActivity.this, "Enter list name", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    listName.setText("");
                    try {
                        getDatabaseHelper().getmMainListDao().create(m);
                        Log.i("What's in database", getDatabaseHelper().getmMainListDao().queryForAll().toString());
                        refresh();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    MainList m = (MainList) listView.getItemAtPosition(position);
                    Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                    intent.putExtra(MAIN_KEY, m.getIdMainList());
                    startActivity(intent);
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }

        refresh();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about:
                AlertDialog.Builder about = new AlertDialog.Builder(this);
                about.setTitle("O autoru");
                about.setMessage("Vladimir Popovic" + "\nTelefon: +38164 101-98-67" + "\n" + "Email: popovic.b.vladimir@gmail.com");
                about.show();
                about.setCancelable(true);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onResume() {
        refresh();
        super.onResume();
    }

    private void refresh() {
        ListView mainList = (ListView) findViewById(R.id.lv_list);

        if (mainList != null) {
            ListMainAdapter adapter = (ListMainAdapter) mainList.getAdapter();
            if (adapter != null) {
                try {
                    adapter.clear();
                    List<MainList> list = getDatabaseHelper().getmMainListDao().queryForAll();
                    adapter.updateAdapter((ArrayList<MainList>) list);
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

    private void completedMainList() {
        for (MainList main: mainList) {
            for (Items items: main.getItems()) {
                if (!items.isPurchased()) {
                    main.setComplete(Completed.NOT_COMPLETED.toString());
                    break;
                } else {
                    main.setComplete(Completed.COMPLETED.toString());
                    break;
                }
            }
            try {
                getDatabaseHelper().getmMainListDao().update(main);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
