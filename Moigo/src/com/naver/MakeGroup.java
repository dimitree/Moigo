package com.naver;


import org.json.JSONException;



import com.naver.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MakeGroup extends Activity {
	static int MakeGroupActivityCode=0;
	private String longitude="";
	private String latitude="";
	private String location="";

	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.makegrouppage);
        
        Button btn1=(Button)findViewById(R.id.button2);
		
		btn1.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				Intent intetn1 = new Intent(MakeGroup.this , MapPage.class);
				//intetn1.putExtra("Name", Value);
				startActivityForResult(intetn1,MakeGroupActivityCode);
				
//				startActivityForResult(intetn1,MetrailActivityCode);
				//startActivity(intetn1);
			}
		});
        
		
		Button btn2=(Button)findViewById(R.id.button1);
		
		btn2.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				setDataToDb();
				Intent intetn1 = new Intent(MakeGroup.this , MessageSendPage.class);
				//intetn1.putExtra("Name", Value);
				//startActivityForResult(intetn1,MakeGroupActivityCode);
				
//				startActivityForResult(intetn1,MetrailActivityCode);
				startActivity(intetn1);
			}
		});
    }

    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	Log.d("fuck","result");
	    if (requestCode == MakeGroupActivityCode && resultCode == Activity.RESULT_OK) {
	    	TextView locationText=(TextView)findViewById(R.id.locationText);
	    	
	    	longitude = data.getStringExtra("longitude");
	    	latitude = data.getStringExtra("latitude");
	    	location = data.getStringExtra("location");
	    	locationText.setText(location);
			
	        //Log.d("epic",metrialName);
	        // do something with B's return values
	    }
	}
    
    private void setDataToDb() {
    	
    }
    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.make_group, menu);
        return true;
    }
    
}
