package com.itschool.octopus;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    int nTaps;
    TextView textTop;
    TextView textDown;
    ImageView kraken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        textTop = findViewById(R.id.textTop);
        textDown = findViewById(R.id.textDown);
        kraken = findViewById(R.id.kraken);
        loadKraken();
    }

    @Override
    protected void onStop() {
        super.onStop();
        savekraken();
    }

    public void tap(View view) {
        int randomNtaps = (int) (Math.random() * 6) + 2;
        nTaps = nTaps + randomNtaps;
        textDown.setText("Вы нанесли: " + nTaps + " / 1000");
    }


    public void savekraken() {
        SharedPreferences prefs = getSharedPreferences("AttackPrefs", Context.MODE_PRIVATE);
        prefs.edit().putInt("num taps", nTaps).apply();
    }
    public void loadKraken() {
        SharedPreferences prefs = getSharedPreferences("AttackPrefs", Context.MODE_PRIVATE);
        nTaps = prefs.getInt("num taps", 0);
    }
}