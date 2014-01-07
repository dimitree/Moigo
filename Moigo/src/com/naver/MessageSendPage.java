package com.naver;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.nhn.android.maps.NMapActivity;

import com.nhn.android.maps.NMapController;
import com.nhn.android.maps.NMapLocationManager;
import com.nhn.android.maps.NMapOverlay;
import com.nhn.android.maps.NMapOverlayItem;
import com.nhn.android.maps.NMapView;
import com.nhn.android.maps.NMapView.OnMapStateChangeListener;
import com.nhn.android.maps.maplib.NGeoPoint;
import com.nhn.android.maps.nmapmodel.NMapError;
import com.nhn.android.maps.overlay.NMapPOIdata;
import com.nhn.android.maps.overlay.NMapPOIitem;
import com.nhn.android.mapviewer.overlay.NMapCalloutOverlay;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager.OnCalloutOverlayListener;
import com.nhn.android.mapviewer.overlay.NMapPOIdataOverlay;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Rect;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.telephony.SmsManager;
public class MessageSendPage extends NMapActivity implements OnMapStateChangeListener, OnCalloutOverlayListener {
	private static final String API_KEY = "1f40521fac2fb495cd1b701ead930471";
	// 네이버 맵 객체
	private NMapView mMapView = null;
	// 맵 컨트롤러
	private NMapController mMapController = null;
	private NMapPOIitem poiItem = null;
	private NGeoPoint selectPoint = null;
	// 맵을 추가할 레이아웃
	private NMapView MapContainer;
	private NMapViewerResourceProvider mMapViewerResourceProvider = null;
	private NMapLocationManager mMapLocationManager;
	private NMapOverlayManager mOverlayManager;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.messagesendpage);
		
		setMessageData();
    	   	
		MapContainer = (NMapView) findViewById(R.id.mapView);
		mMapView = new NMapView(this);
		mMapController = mMapView.getMapController();
		mMapView.setApiKey(API_KEY);
		mMapView.setClickable(true);
		mMapView.setBuiltInZoomControls(true, null);	
		mMapView.setOnMapStateChangeListener(this);		
		MapContainer.addView(mMapView);
		mMapViewerResourceProvider = new NMapViewerResourceProvider(this);		
		mOverlayManager = new NMapOverlayManager(this, mMapView, mMapViewerResourceProvider);		
		setPOI();		
		
		Button btn2=(Button)findViewById(R.id.btnSend);		
		btn2.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				sendMessageData() ;
			}
		});
	}
	
	public void sendMessageData() {
		String titleStr;
		String contentStr;
		String placeStr;
		
		EditText titleText=(EditText)findViewById(R.id.title);	
		EditText contentText=(EditText)findViewById(R.id.content);	
		EditText placeText=(EditText)findViewById(R.id.place);	
		
		titleStr = titleText.getText().toString();
		contentStr = contentText.getText().toString();
		placeStr = placeText.getText().toString();
		
		
		
		Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + ""));
	    try {
			intent.putExtra("sms_body", titleStr+"\n\n"+ contentStr+"\n\n"+placeStr+"\n\n"+
			"http://m.map.naver.com/appLink.nhn?app=Y&version=7&appMenu=location&lng="+
			getXData()+"&lat="+getYData()+"&title="+URLEncoder.encode(getNameData(),"UTF-8")+"&dlevel=9");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    //Log.d("uri",Uri.parse("navermaps://?menu=location&amp;lat=120.5654779&amp;lng=60.0322662&amp;title=&amp;pinId=18770067&amp;pinType=spot").toString());
	    startActivity(intent);
	    finish();
	}
	
	private void setMessageData() {
		EditText titleText=(EditText)findViewById(R.id.title);	
		titleText.setText("임시 제목");
		//EditText contentText=(EditText)findViewById(R.id.content);	
		EditText placeText=(EditText)findViewById(R.id.place);	
		placeText.setText(getNameData()+"\n"+getPlaceData());
		
	}

	public void setPOI() {
		int markerId = NMapPOIflagType.PIN;
		NMapPOIdata poiData = new NMapPOIdata(1, mMapViewerResourceProvider);
		poiData.beginPOIdata(1);
		//Log.d("point", Double.toString(clickPoint.getLatitude()));
		poiData.addPOIitem(getXData(),getYData() ,getNameData(), markerId, 0);
		
		poiData.endPOIdata();
		
		NMapPOIdataOverlay poiDataOverlay  = mOverlayManager.createPOIdataOverlay(poiData, null);
		
		mOverlayManager.setOnCalloutOverlayListener(this);	
		
		poiDataOverlay.showAllPOIdata(0);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.message_send_page, menu);
		return true;
	}
	
	public String getNameData() {
		return "봉우화로";
	}
	
	public String getPlaceData() {
		return "서울특별시 강남구 역삼1동 619-14";
	}
	
	public double getXData() {
		return 127.0273389;
	}
	
	public double getYData() {
		return 37.5017583;
	}

	@Override
	public NMapCalloutOverlay onCreateCalloutOverlay(NMapOverlay arg0,
			NMapOverlayItem arg1, Rect arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onAnimationStateChange(NMapView arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMapCenterChange(NMapView arg0, NGeoPoint arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMapCenterChangeFine(NMapView arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMapInitHandler(NMapView arg0, NMapError arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onZoomLevelChange(NMapView arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}
	

}
