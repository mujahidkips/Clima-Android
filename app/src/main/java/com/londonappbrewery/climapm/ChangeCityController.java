package com.londonappbrewery.climapm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class ChangeCityController extends AppCompatActivity {

    EditText queryET;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        setContentView(R.layout.change_city_layout);
        queryET=(EditText)findViewById(R.id.queryET);
        ImageButton backBT=(ImageButton) findViewById(R.id.backButton);

        backBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });


        queryET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String city=queryET.getText().toString();
                Intent intent=new Intent(ChangeCityController.this,WeatherController.class);
                intent.putExtra("City",city);
                startActivity(intent);


                return false;
            }
        });
    }
}
