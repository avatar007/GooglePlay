package com.studio.googleplay;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private String name = "oneNamess";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView tv = new TextView(this);
        tv.setText("谷歌电子市场");
        tv.setGravity(Gravity.CENTER);
        tv.setTextColor(Color.BLACK);
        tv.setTextSize(30);
        tv.setVisibility(View.GONE);
        setContentView(tv);

    }
}
