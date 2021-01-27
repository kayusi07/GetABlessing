package com.kayushi07.getablessing;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Ayushi on 12-04-2018.
 */

public class GetUserName extends AppCompatActivity {

    String userName = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.name_first_time);

        final EditText name = (EditText) findViewById(R.id.userName);
        Button btn = (Button) findViewById(R.id.btn_get);

        final SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(this);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userName = name.getText().toString();
                int lenOfName = userName.length();
                System.out.println("LENT  " + lenOfName);
                if (lenOfName <= 2){
                    Toast.makeText(GetUserName.this, "Invalid Name!", Toast.LENGTH_SHORT).show();
                }
                else {
                    SharedPreferences.Editor editor = prefs
                            .edit();
                    editor.putString("user_name",
                            userName);
                    editor.commit();

                    Intent i = new Intent(GetUserName.this, MainActivity.class);
                    startActivity(i);

                    finish();
                }
            }
        });



    }
}
