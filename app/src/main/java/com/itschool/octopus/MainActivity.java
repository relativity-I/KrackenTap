package com.itschool.octopus;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    /** true = разноцветный урон, false = только красный */
    private static final boolean RAINBOW = true;

    int totalDamage;
    TextView textTop;
    TextView textDown;
    ImageView kraken;
    FrameLayout container;

    private final Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textTop   = findViewById(R.id.textTop);
        textDown  = findViewById(R.id.textDown);
        kraken    = findViewById(R.id.kraken);
        container = findViewById(R.id.container);

        kraken.setOnClickListener(v -> {
            int damage = 1 + random.nextInt(20);
            totalDamage += damage;
            textDown.setText("Урон: " + totalDamage);
            showFloatingDamage(damage);
        });
    }

    /** Цвет числа урона. RAINBOW = true → случайный из палитры, иначе красный. */
    private int pickColor() {
        if (!RAINBOW) return Color.parseColor("#FF2222");
        int[] palette = {
                0xFFFF2222, // красный
                0xFFFF8800, // оранжевый
                0xFFFFDD00, // жёлтый
                0xFF22DD22, // зелёный
                0xFF00CCFF, // голубой
                0xFFAA44FF, // фиолетовый
                0xFFFF44AA, // розовый
                0xFFFFFFFF  // белый
        };
        return palette[random.nextInt(palette.length)];
    }

    /** Всплывающее число — от верхушки кракена вверх под случайным углом. */
    private void showFloatingDamage(int damage) {
        TextView tv = new TextView(this);
        tv.setText("-" + damage);
        tv.setTextSize(32f);
        tv.setTypeface(Typeface.DEFAULT_BOLD);
        tv.setTextColor(pickColor());
        tv.setShadowLayer(8f, 2f, 2f, Color.BLACK);

        tv.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int w = tv.getMeasuredWidth();

        // старт: у верхнего края кракена, чуть со случайным смещением по X
        int offsetX = random.nextInt(80) - 40;
        float startX = (kraken.getWidth() - w) / 2f + offsetX;
        float startY = 0f; // ровно верх кракена

        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
        );
        lp.leftMargin = (int) startX;
        lp.topMargin  = (int) startY;
        tv.setLayoutParams(lp);

        container.addView(tv);

        // угол от -10° до -170° → строго вверх = -90°, широкий веер
        int angleDeg = -10 - random.nextInt(160);
        float distance = 220f;
        float dx = (float) Math.cos(Math.toRadians(angleDeg)) * distance;
        float dy = (float) Math.sin(Math.toRadians(angleDeg)) * distance;

        tv.setScaleX(0.6f);
        tv.setScaleY(0.6f);
        tv.setAlpha(1f);

        tv.animate()
                .scaleX(1.2f)
                .scaleY(1.2f)
                .translationX(dx)
                .translationY(dy)
                .alpha(0f)
                .setDuration(900)
                .setInterpolator(new DecelerateInterpolator())
                .withEndAction(() -> container.removeView(tv))
                .start();
    }
}