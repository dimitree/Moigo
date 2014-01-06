package com.naver;

import com.nhn.android.maps.NMapActivity;
import com.nhn.android.maps.NMapController;
import com.nhn.android.maps.NMapLocationManager;
import com.nhn.android.maps.NMapView;
import com.nhn.android.maps.NMapView.OnMapStateChangeListener;
import com.nhn.android.maps.maplib.NGeoPoint;
import com.nhn.android.maps.nmapmodel.NMapError;
import com.nhn.android.maps.overlay.NMapPOIdata;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager;
import com.nhn.android.mapviewer.overlay.NMapPOIdataOverlay;
import com.nhn.android.mapviewer.overlay.NMapResourceProvider;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.widget.LinearLayout;

public class MapPage extends NMapActivity implements OnMapStateChangeListener {

	// API-KEY
	private static final String API_KEY = "1f40521fac2fb495cd1b701ead930471";
	// 네이버 맵 객체
	private NMapView mMapView = null;
	// 맵 컨트롤러
	private NMapController mMapController = null;
	// 맵을 추가할 레이아웃
	private NMapView MapContainer;
	private NMapViewerResourceProvider mMapViewerResourceProvider = null;
	private NMapLocationManager mMapLocationManager;
	private NMapOverlayManager mOverlayManager;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// 네이버 지도를 넣기 위한 LinearLayout 컴포넌트
		
				super.onCreate(savedInstanceState);
	        	setContentView(R.layout.mappage);
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
				
				// 오버레이들을 관리하기 위한 id값 생성
				int markerId = NMapPOIflagType.PIN;

				// 표시할 위치 데이터를 지정한다. 마지막 인자가 오버레이를 인식하기 위한 id값
				NMapPOIdata poiData = new NMapPOIdata(2, mMapViewerResourceProvider);
				poiData.beginPOIdata(2);
				poiData.addPOIitem(127.0630205, 37.5091300, "위치1", markerId, 0);
				poiData.addPOIitem(127.061, 37.51, "위치2", markerId, 0);
				poiData.endPOIdata();

				// 위치 데이터를 사용하여 오버레이 생성
				NMapPOIdataOverlay poiDataOverlay = mOverlayManager.createPOIdataOverlay(poiData, null);
				
				// id값이 0으로 지정된 모든 오버레이가 표시되고 있는 위치로
				// 지도의 중심과 ZOOM을 재설정
				poiDataOverlay.showAllPOIdata(0);
				
				// 오버레이 이벤트 등록
				mOverlayManager.setOnCalloutOverlayListener(this);
			}

			/**
			 * 지도가 초기화된 후 호출된다.
			 * 정상적으로 초기화되면 errorInfo 객체는 null이 전달되며,
			 * 초기화 실패 시 errorInfo객체에 에러 원인이 전달된다
			 */
			@Override
			public void onMapInitHandler(NMapView mapview, NMapError errorInfo) {
				if (errorInfo == null) { // success
					startMyLocation();
//					mMapController.setMapCenter(new NGeoPoint(126.978371, 37.5666091), 11);
				} else { // fail
					android.util.Log.e("NMAP", "onMapInitHandler: error=" 
								+ errorInfo.toString());
				}
			}

			
			private void startMyLocation() {

				mMapLocationManager = new NMapLocationManager(this);
				//mMapLocationManager.setOnLocationChangeListener(onMyLocationChangeListener);
				mMapLocationManager.enableMyLocation(true);
				
				mMapLocationManager.setOnLocationChangeListener(onMyLocationChangeListener);
				
				/*
				Log.d("asd",Boolean.toString(isMyLocationEnabled));
				NGeoPoint curPoint =  mMapLocationManager.getMyLocation();
				Log.d("asd",Double.toString(curPoint.getLatitude()));
				*/
				
			}
			
			private final NMapView.OnMapViewTouchEventListener onMapViewTouchEventListener = new NMapView.OnMapViewTouchEventListener() {

				@Override
				public void onLongPress(NMapView mapView, MotionEvent ev) {
					
					

					// 오버레이 관리자 추가
					

				}

				@Override
				public void onLongPressCanceled(NMapView mapView) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onSingleTapUp(NMapView mapView, MotionEvent ev) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onTouchDown(NMapView mapView, MotionEvent ev) {

				}

				@Override
				public void onScroll(NMapView mapView, MotionEvent e1, MotionEvent e2) {
				}

				@Override
				public void onTouchUp(NMapView mapView, MotionEvent ev) {
					// TODO Auto-generated method stub

				}

			};
			
			
			private final NMapLocationManager.OnLocationChangeListener onMyLocationChangeListener = new NMapLocationManager.OnLocationChangeListener() {

				@Override
				public boolean onLocationChanged(NMapLocationManager locationManager,NGeoPoint myLocation) {


					mMapController.setMapCenter(myLocation);
				

					MapContainer.addView(mMapView);
					mMapLocationManager.disableMyLocation();
					return true;
				}

				@Override
				public void onLocationUnavailableArea(NMapLocationManager arg0,
						NGeoPoint arg1) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onLocationUpdateTimeout(NMapLocationManager arg0) {
					// TODO Auto-generated method stub
					
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
