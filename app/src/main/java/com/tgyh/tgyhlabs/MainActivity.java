package com.tgyh.tgyhlabs;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.tgyh.widght.Views.SwitchButton;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SwitchButton sb = findViewById(R.id.sb);
        sb.setOnListener(new SwitchButton.OnListener() {
            @Override
            public void switched(SwitchButton button, boolean cheaked) {
                Toast.makeText(MainActivity.this, ":" + cheaked, Toast.LENGTH_SHORT).show();
            }
        });

        /*CircleProgress mCp = findViewById(R.id.cp);
        mCp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ObjectAnimator.ofInt(mCp,"progress",0,100).setDuration(3000).start();
            }
        });*/
    }
}