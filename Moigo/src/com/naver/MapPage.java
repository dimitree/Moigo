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
	private NMapPOIdata poiData =null;
	private String placeString = "";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// 네이버 지도를 넣기 위한 LinearLayout 컴포넌트
			
				super.onCreate(savedInstanceState);
	        	setContentView(R.layout.mappage);
	        	super.setMapDataProviderListener(onDataProviderListener);
	        	
				MapContainer = (NMapView) findViewById(R.id.mapView);

				// 네이버 지도 객체 생성
				mMapView = new NMapView(this);
				
				// 지도 객체로부터 컨트롤러 추출
				mMapController = mMapView.getMapController();

				// 네이버 지도 객체에 APIKEY 지정
				mMapView.setApiKey(API_KEY);

				// 생성된 네이버 지도 객체를 LinearLayout에 추가시킨다.
				
				
				// 지도를 터치할 수 있도록 옵션 활성화
				mMapView.setClickable(true);
				
				// 확대/축소를 위한 줌 컨트롤러 표시 옵션 활성화
				mMapView.setBuiltInZoomControls(true, null);	
				
				// 지도에 대한 상태 변경 이벤트 연결
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
				// 표시할 위치 데이터를 지정한다. 마지막 인자가 오버레이를 인식하기 위한 id값
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
			 * 지도가 초기화된 후 호출된다.
			 * 정상적으로 초기화되면 errorInfo 객체는 null이 전달되며,
			 * 초기화 실패 시 errorInfo객체에 에러 원인이 전달된다
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
			 * 지도 레벨 변경 시 호출되며 변경된 지도 레벨이 파라미터로 전달된다.
			 */
			@Override
			public void onZoomLevelChange(NMapView mapview, int level) {}

			/**
			 * 지도 중심 변경 시 호출되며 변경된 중심 좌표가 파라미터로 전달된다.
			 */
			@Override
			public void onMapCenterChange(NMapView mapview, NGeoPoint center) {}

			/**
			 * 지도 애니메이션 상태 변경 시 호출된다.
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
