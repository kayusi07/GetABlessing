package com.kayushi07.getablessing;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class DialogActivity extends AppCompatActivity{


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        setContentView(R.layout.activity_dialog);

        final SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(this);

        String bless = prefs.getString("blessing_today", null);


        Button done = (Button) findViewById(R.id.btn_done);
        TextView txt = (TextView) findViewById(R.id.txt_bless);
        Intent i = getIntent();
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        txt.setText("" + bless);

    }

}
