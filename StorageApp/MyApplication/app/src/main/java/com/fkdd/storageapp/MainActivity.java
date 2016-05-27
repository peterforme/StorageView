package com.fkdd.storageapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.Formatter;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    StorageView storageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        storageView = (StorageView)findViewById(R.id.storageView);
        long mTotleSize = StorageSizeUtil.getExternalStorageSizeTotleSize(this, false);
        long mAvailableSize = StorageSizeUtil.getExternalStorageSizeAvailableSize(this, false);
        long mUsedSize = mTotleSize - mAvailableSize;
        int percent =(int) (mUsedSize * 100f / mTotleSize );

        String availableStr = getString(R.string.storage_available) + Formatter.formatFileSize(this, mAvailableSize);
        String usedStr = getString(R.string.storage_already_used) + Formatter.formatFileSize(this, mUsedSize);
        String totalStr =  Formatter.formatFileSize(this, mTotleSize);

        storageView.setUsedRate(percent);
        storageView.setLeftText(usedStr);
        storageView.setRightText(availableStr);
        storageView.setTotalText(totalStr);
        storageView.setRefresh();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
