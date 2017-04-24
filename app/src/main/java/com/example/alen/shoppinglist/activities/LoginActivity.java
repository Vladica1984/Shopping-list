package com.example.alen.shoppinglist.activities;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.alen.shoppinglist.R;

/**
 * Created by Alen on 24-Apr-17.
 */

public class LoginActivity extends MainActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final EditText passWord = (EditText) findViewById(R.id.editpassword);
        final Button loginButton = (Button) findViewById(R.id.button_submit);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (passWord.getText().toString().equals("1234")) {
                    final Intent myIntent = new Intent();
                    myIntent.setComponent(new ComponentName(LoginActivity.this, MainActivity.class));
                    startActivity(myIntent);
                    finish();
                }
            }
        });
    }

}
