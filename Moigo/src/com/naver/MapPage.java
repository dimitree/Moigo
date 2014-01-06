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
	// ���̹� �� ��ü
	private NMapView mMapView = null;
	// �� ��Ʈ�ѷ�
	private NMapController mMapController = null;
	// ���� �߰��� ���̾ƿ�
	private NMapView MapContainer;
	private NMapViewerResourceProvider mMapViewerResourceProvider = null;
	private NMapLocationManager mMapLocationManager;
	private NMapOverlayManager mOverlayManager;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// ���̹� ������ �ֱ� ���� LinearLayout ������Ʈ
		
				super.onCreate(savedInstanceState);
	        	setContentView(R.layout.mappage);
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
				
				// �������̵��� �����ϱ� ���� id�� ����
				int markerId = NMapPOIflagType.PIN;

				// ǥ���� ��ġ �����͸� �����Ѵ�. ������ ���ڰ� �������̸� �ν��ϱ� ���� id��
				NMapPOIdata poiData = new NMapPOIdata(2, mMapViewerResourceProvider);
				poiData.beginPOIdata(2);
				poiData.addPOIitem(127.0630205, 37.5091300, "��ġ1", markerId, 0);
				poiData.addPOIitem(127.061, 37.51, "��ġ2", markerId, 0);
				poiData.endPOIdata();

				// ��ġ �����͸� ����Ͽ� �������� ����
				NMapPOIdataOverlay poiDataOverlay = mOverlayManager.createPOIdataOverlay(poiData, null);
				
				// id���� 0���� ������ ��� �������̰� ǥ�õǰ� �ִ� ��ġ��
				// ������ �߽ɰ� ZOOM�� �缳��
				poiDataOverlay.showAllPOIdata(0);
				
				// �������� �̺�Ʈ ���
				mOverlayManager.setOnCalloutOverlayListener(this);
			}

			/**
			 * ������ �ʱ�ȭ�� �� ȣ��ȴ�.
			 * ���������� �ʱ�ȭ�Ǹ� errorInfo ��ü�� null�� ���޵Ǹ�,
			 * �ʱ�ȭ ���� �� errorInfo��ü�� ���� ������ ���޵ȴ�
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
					
					

					// �������� ������ �߰�
					

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
