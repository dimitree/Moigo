package com.naver;


import com.naver.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MakeGroup extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.makegrouppage);
        
        Button btn1=(Button)findViewById(R.id.button2);
		
		btn1.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				Intent intetn1 = new Intent(MakeGroup.this , MapPage.class);
//				startActivityForResult(intetn1,MetrailActivityCode);
				startActivity(intetn1);
			}
		});
        
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.make_group, menu);
        return true;
    }
    
}
