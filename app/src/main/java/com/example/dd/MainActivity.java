package com.example.dd;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, ColorAdapter.OnColorSelected {

    private PaintView paintView;
    private ImageView imgBackground;
    private ImageView btnPaint;
    private ImageView btnClear;
    private ImageView btnUndo;
    private RecyclerView rvColor;
    private List<Integer> colors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        paintView = findViewById(R.id.paint_view);
        imgBackground = findViewById(R.id.img_background);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        paintView.init(metrics);
        paintView.normal();
        init();
        initRecyclerView();
    }

    private void getColors(){
        colors = new ArrayList<>();
        colors.add(Color.BLUE);
        colors.add(Color.GRAY);
        colors.add(Color.GREEN);
        colors.add(Color.YELLOW);
        colors.add(Color.BLACK);
        colors.add(Color.RED);
        colors.add(Color.CYAN);
        colors.add(Color.WHITE);
    }

    private void init() {
        btnPaint = findViewById(R.id.btn_paint);
        btnClear = findViewById(R.id.btn_clear);
        btnUndo = findViewById(R.id.btn_undo);
        btnPaint.setOnClickListener(this);
        btnClear.setOnClickListener(this);
        btnUndo.setOnClickListener(this);
        rvColor = findViewById(R.id.rv_color);
    }
    private void initRecyclerView(){
        getColors();
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rvColor.setLayoutManager(llm);
        rvColor.setAdapter(new ColorAdapter(this, colors,this));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_paint:
                break;
            case R.id.btn_clear:
                Toast.makeText(this, "Click", Toast.LENGTH_SHORT).show();
                paintView.setColorPaint(Color.WHITE);
                DisplayMetrics metrics = new DisplayMetrics();
                paintView.init(metrics);
                break;
            case R.id.btn_undo:
                paintView.clear();
                break;
        }
    }

    @Override
    public void onSelected(int color) {

    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater menuInflater = getMenuInflater();
//        menuInflater.inflate(R.menu.main, menu);
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch(item.getItemId()) {
//            case R.id.normal:
//                paintView.normal();
//                return true;
//            case R.id.emboss:
//                paintView.emboss();
//                return true;
//            case R.id.blur:
//                paintView.blur();
//                return true;
//            case R.id.clear:
//                paintView.clear();
//                return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}
