package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;

public class ReadDBActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_db);

        TextView sjView = findViewById(R.id.read_sj);
        TextView mjView = findViewById(R.id.read_mj);
        TextView mglView = findViewById(R.id.read_mgl);
        TextView smView = findViewById(R.id.read_sm);

        DBHelper helper = new DBHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select sj,mj,mgl,sm from tb_memo order by _id desc limit 1", null);
        while (cursor.moveToNext()) {
            sjView.setText(cursor.getString(0));
            mjView.setText(cursor.getString(1));
            mglView.setText(cursor.getString(2));
            smView.setText(cursor.getString(3));
        }
        db.close();
    }
}