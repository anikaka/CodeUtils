package com.example.activity;

import com.example.coderesource.R;
import com.example.fragment.MainFragmentActivity;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.widget.TextView;



public class MainActivity extends Activity {
	
	
	private TextView mTxtContact;
	private TextView   txt;
	private Context mContext=this;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);     
        Intent  intent=new Intent(mContext, MainFragmentActivity.class);
        startActivity(intent);
    }
    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
