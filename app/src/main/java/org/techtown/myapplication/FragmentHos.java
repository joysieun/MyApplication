package org.techtown.myapplication;

import android.Manifest;
import android.content.Context;
import android.graphics.PointF;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.GoogleMap;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.util.FusedLocationSource;

import java.util.ArrayList;
import java.util.List;


public class FragmentHos extends Fragment implements OnMapReadyCallback{

    private MapView mapView;
    private FusedLocationSource locationSource;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private static final int LOCATION_PERMISSION_REQUEST_CODE =1000;
    private static final String[] PERMISSIONS={
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
    };
    List<LatLng> latLngs = new ArrayList<>();
    private NaverMap naverMap;

    public FragmentHos(){

    }
    public static FragmentHos newInstance(){
        FragmentHos fragmentHos = new FragmentHos();
        return fragmentHos;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_hos,container,false);

        locationSource = new FusedLocationSource(getActivity(),LOCATION_PERMISSION_REQUEST_CODE);
        mapView = (MapView) rootView.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this); //지도 객체를 얻는것, 함수가 자동으로 호출되면서 매개변수로 naverMap 객체가 전달됨


        return rootView;
    }

    //카메라 줌이나 이동은 여기에 구현해주기
    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {

        
        this.naverMap = naverMap;
        naverMap.setLocationSource(locationSource); // 현재위치
        requestPermissions(PERMISSIONS,LOCATION_PERMISSION_REQUEST_CODE);

    }
//퍼미션 허락받기기
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (locationSource.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
            if (!locationSource.isActivated()) {
                naverMap.setLocationTrackingMode(LocationTrackingMode.None);
                return;
            }
            else {
                naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();

    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mapView.onDestroy();
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

}






