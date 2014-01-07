package com.naver;

import com.nhn.android.maps.NMapActivity;
import com.nhn.android.maps.NMapController;
import com.nhn.android.maps.NMapLocationManager;
import com.nhn.android.maps.NMapOverlay;
import com.nhn.android.maps.NMapOverlayItem;
import com.nhn.android.maps.NMapProjection;
import com.nhn.android.maps.NMapView;
import com.nhn.android.maps.NMapView.OnMapStateChangeListener;
import com.nhn.android.maps.maplib.NGeoPoint;
import com.nhn.android.maps.nmapmodel.NMapError;
import com.nhn.android.maps.nmapmodel.NMapPlacemark;
import com.nhn.android.maps.overlay.NMapPOIdata;
import com.nhn.android.maps.overlay.NMapPOIitem;
import com.nhn.android.mapviewer.overlay.NMapCalloutOverlay;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager.OnCalloutOverlayListener;
import com.nhn.android.mapviewer.overlay.NMapPOIdataOverlay;
import com.nhn.android.mapviewer.overlay.NMapResourceProvider;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MapPage extends NMapActivity implements OnMapStateChangeListener, OnCalloutOverlayListener  {

	// API-KEY
	private static final String API_KEY = "1f40521fac2fb495cd1b701ead930471";
	// ���̹� �� ��ü
	private NMapView mMapView = null;
	// �� ��Ʈ�ѷ�
	private NMapController mMapController = null;
	private NMapPOIitem poiItem = null;
	private NGeoPoint selectPoint = null;
	// ���� �߰��� ���̾ƿ�
	private NMapView MapContainer;
	private NMapViewerResourceProvider mMapViewerResourceProvider = null;
	private NMapLocationManager mMapLocationManager;
	private NMapOverlayManager mOverlayManager;
	private NMapPOIdata poiData =null;
	private String placeString = "";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// ���̹� ������ �ֱ� ���� LinearLayout ������Ʈ
			
				super.onCreate(savedInstanceState);
	        	setContentView(R.layout.mappage);
	        	super.setMapDataProviderListener(onDataProviderListener);
	        	
				MapContainer = (NMapView) findViewById(R.id.mapView);

				// ���̹� ���� ��ü ����
				mMapView = new NMapView(this);
				
				// ���� ��ü�κ��� ��Ʈ�ѷ� ����
				mMapController = mMapView.getMapController();

				// ���̹� ���� ��ü�� APIKEY ����
				mMapView.setApiKey(API_KEY);

				// ������ ���̹� ���� ��ü�� LinearLayout�� �߰���Ų��.
				
				
				// ������ ��ġ�� �� �ֵ��� �ɼ� Ȱ��ȭ
				mMapView.setClickable(true);
				
				// Ȯ��/��Ҹ� ���� �� ��Ʈ�ѷ� ǥ�� �ɼ� Ȱ��ȭ
				mMapView.setBuiltInZoomControls(true, null);	
				
				// ������ ���� ���� ���� �̺�Ʈ ����
				mMapView.setOnMapStateChangeListener(this);
				
				mMapView.setOnMapViewTouchEventListener(onMapViewTouchEventListener);

				
				mMapViewerResourceProvider = new NMapViewerResourceProvider(this);
				
				mOverlayManager = new NMapOverlayManager(this, mMapView, mMapViewerResourceProvider);
			
				
		        Button btn1 = (Button)findViewById(R.id.button1);
		       
				btn1.setOnClickListener(new Button.OnClickListener() {
					public void onClick(View v) {
						Intent resultIntent = new Intent();
					
						
						resultIntent.putExtra("longitude",selectPoint.getLongitude());
						resultIntent.putExtra("latitude",selectPoint.getLatitude());
						resultIntent.putExtra("location",placeString);
						
						
						setResult(Activity.RESULT_OK, resultIntent);
						Log.d("fuck","result pre");
						finish();
					}
				});
			
			
			}
	
	
	
	
			public void onMapClick(NMapView mapView,MotionEvent ev) {
				int markerId = NMapPOIflagType.PIN;
				// ǥ���� ��ġ �����͸� �����Ѵ�. ������ ���ڰ� �������̸� �ν��ϱ� ���� id��
				//ev.getX();
				NMapProjection mapProjection = mapView.getMapProjection(); 
				NGeoPoint clickPoint = mapProjection.fromPixels((int) ev.getX(), (int) ev.getY());
				if(poiData != null)  {
					poiData.removeAllPOIdata();
				}
				
				findPlacemarkAtLocation(clickPoint.getLongitude(), clickPoint.getLatitude());
				poiData = new NMapPOIdata(1, mMapViewerResourceProvider);
				poiData.beginPOIdata(1);
				//Log.d("point", Double.toString(clickPoint.getLatitude()));
				poiItem = poiData.addPOIitem(clickPoint.getLongitude(),clickPoint.getLatitude() ,"asdf", markerId, 0);
				
				poiData.endPOIdata();
				
				selectPoint = clickPoint;
				
			}

			
			public void setOverlayListner() {
				//Log.d("fuck","overlay listener"); 
				mOverlayManager.createPOIdataOverlay(poiData, null);
				
				mOverlayManager.setOnCalloutOverlayListener(this);
			}
			
			private final NMapActivity.OnDataProviderListener onDataProviderListener = new NMapActivity.OnDataProviderListener() {
				@Override
				public void onReverseGeocoderResponse(NMapPlacemark placeMark, NMapError errInfo) {
					if (placeMark != null) {
						poiItem.setTitle(placeMark.toString());
						placeString = placeMark.toString();
						setOverlayListner();
						//mOverlayManager.createPOIdataOverlay(poiData, null);
						//Toast.makeText(getBaseContext(), placeMark.toString(), 10);
					}
				}
			};
			
			
			/**
			 * ������ �ʱ�ȭ�� �� ȣ��ȴ�.
			 * ���������� �ʱ�ȭ�Ǹ� errorInfo ��ü�� null�� ���޵Ǹ�,
			 * �ʱ�ȭ ���� �� errorInfo��ü�� ���� ������ ���޵ȴ�
			 */
			@Override
			public void onMapInitHandler(NMapView mapview, NMapError errorInfo) {
				if (errorInfo == null) { // success
					MapContainer.addView(mMapView);
					//startMyLocation();
//					mMapController.setMapCenter(new NGeoPoint(126.978371, 37.5666091), 11);
				} 
			}

			
			@Override
			public NMapCalloutOverlay onCreateCalloutOverlay(NMapOverlay arg0,NMapOverlayItem arg1, Rect arg2) {
				Log.d("fuck","sdgse   " +  placeString);
				Toast.makeText(this, arg1.getTitle(), Toast.LENGTH_SHORT).show();
				return null;
			}
			
			
			private void startMyLocation() {

				mMapLocationManager = new NMapLocationManager(this);
				//mMapLocationManager.setOnLocationChangeListener(onMyLocationChangeListener);
				mMapLocationManager.enableMyLocation(true);
				
				mMapLocationManager.setOnLocationChangeListener(onMyLocationChangeListener);
				
				
			}
			
			private final NMapView.OnMapViewTouchEventListener onMapViewTouchEventListener = new NMapView.OnMapViewTouchEventListener() {

				@Override
				public void onLongPress(NMapView mapView, MotionEvent ev) {				
				}

				@Override
				public void onLongPressCanceled(NMapView mapView) {
				}

				@Override
				public void onSingleTapUp(NMapView mapView, MotionEvent ev) {
					// TODO Auto-generated method stub
					onMapClick(mapView,ev);
				}

				@Override
				public void onTouchDown(NMapView mapView, MotionEvent ev) {
				}

				@Override
				public void onScroll(NMapView mapView, MotionEvent e1, MotionEvent e2) {
				}

				@Override
				public void onTouchUp(NMapView mapView, MotionEvent ev) {				
				}

			};
			
			
			private final NMapLocationManager.OnLocationChangeListener onMyLocationChangeListener = new NMapLocationManager.OnLocationChangeListener() {

				@Override
				public boolean onLocationChanged(NMapLocationManager locationManager,NGeoPoint myLocation) {


					mMapController.setMapCenter(myLocation,12);
				

					MapContainer.addView(mMapView);
					mMapLocationManager.disableMyLocation();
					return true;
				}

				@Override
				public void onLocationUnavailableArea(NMapLocationManager arg0,
						NGeoPoint arg1) {
				}

				@Override
				public void onLocationUpdateTimeout(NMapLocationManager arg0) {
				}
			};
			/**
			 * ���� ���� ���� �� ȣ��Ǹ� ����� ���� ������ �Ķ���ͷ� ���޵ȴ�.
			 */
			@Override
			public void onZoomLevelChange(NMapView mapview, int level) {}

			/**
			 * ���� �߽� ���� �� ȣ��Ǹ� ����� �߽� ��ǥ�� �Ķ���ͷ� ���޵ȴ�.
			 */
			@Override
			public void onMapCenterChange(NMapView mapview, NGeoPoint center) {}

			/**
			 * ���� �ִϸ��̼� ���� ���� �� ȣ��ȴ�.
			 * animType : ANIMATION_TYPE_PAN or ANIMATION_TYPE_ZOOM
			 * animState : ANIMATION_STATE_STARTED or ANIMATION_STATE_FINISHED
			 */
			@Override
			public void onAnimationStateChange(
							NMapView arg0, int animType, int animState) {}

			@Override
			public void onMapCenterChangeFine(NMapView arg0) {}

			@Override
			public boolean onCreateOptionsMenu(Menu menu) {
				// Inflate the menu; this adds items to the action bar if it is present.
				
				
				getMenuInflater().inflate(R.menu.map_page, menu);
				
				
				return true;
			}

}
