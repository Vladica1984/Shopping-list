package com.example.alen.shoppinglist.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.alen.shoppinglist.Database.ORMDataBaseHelper;
import com.example.alen.shoppinglist.R;
import com.example.alen.shoppinglist.adapter.ListMainAdapter;
import com.example.alen.shoppinglist.model.MainList;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ORMDataBaseHelper databaseHelper;
    public static String MAIN_KEY = "MAIN_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            final List<MainList> mainList = getDatabaseHelper().getmMainListDao().queryForAll();
            final ListView listView = (ListView) findViewById(R.id.lv_list);
            final ListMainAdapter adapter = new ListMainAdapter(MainActivity.this, (ArrayList<MainList>) mainList);

            Button add = (Button) findViewById(R.id.button_add);
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditText listName = (EditText) findViewById(R.id.et_nameOfList);
                    MainList m = new MainList();
                    m.setNameOfList(listName.getText().toString());
                    if (m.getNameOfList().equals("")) {
                        Toast.makeText(MainActivity.this, "Enter list name", Toast.LENGTH_SHORT).show();
                        return;
                    }
//                    m.setComplete(getString(R.string.NotCompleted));
//                    m.setProtect("Not protected");
//                    m.setNoOfArticles("0 " + "Articles");
//                    mainList.add(0, m);
                    listName.setText("");
//                    adapter.notifyDataSetChanged();
                    try {
                        getDatabaseHelper().getmMainListDao().create(m);
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
                  //  intent.putExtra("nameOfList", mainList1.getNameOfList());
                    startActivity(intent);
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
}
