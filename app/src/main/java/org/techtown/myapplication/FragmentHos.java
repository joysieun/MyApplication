package org.techtown.myapplication;

import static android.content.Context.MODE_PRIVATE;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.util.FusedLocationSource;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class FragmentHos extends Fragment implements OnMapReadyCallback {

    private MapView mapView;
    public SQLiteDatabase database;
    HospitalDB helper;
    String DBname = "Hospital";
    public Cursor cursor;
    String cos_lat;
    String cos_lon;
    String sin_lon;
    String sin_lat;
    private HosAdapter adapter;
    RecyclerView recyclerView;
    Location location;
    List<Hospital> dataList;


    private FusedLocationSource locationSource;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private static final String[] PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
    };
    private double lat, lan;
    String list;
    List<LatLng> latLngs = new ArrayList<>();
    private NaverMap naverMap;

    public FragmentHos() {

    }

    public static FragmentHos newInstance() {
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
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_hos, container, false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        helper = new HospitalDB(getActivity());
        database = helper.getWritableDatabase();

        locationSource = new FusedLocationSource(getActivity(), LOCATION_PERMISSION_REQUEST_CODE);
        mapView = (MapView) rootView.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this); //지도 객체를 얻는것, 함수가 자동으로 호출되면서 매개변수로 naverMap 객체가 전달됨

        recyclerView = rootView.findViewById(R.id.recycleview2);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new HosAdapter();
        recyclerView.setAdapter(adapter);
        setDB(getActivity());
        getList(37.541,126.986,5);




        return rootView;
    }

    //카메라 줌이나 이동은 여기에 구현해주기
    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {


        this.naverMap = naverMap;
        naverMap.setLocationSource(locationSource); // 현재위치
        requestPermissions(PERMISSIONS, LOCATION_PERMISSION_REQUEST_CODE);
        naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);
        naverMap.addOnLocationChangeListener(new NaverMap.OnLocationChangeListener() {
            @Override
            public void onLocationChange(@NonNull Location location) {
                lat = location.getLatitude();
                lan = location.getLongitude();
                Toast.makeText(getActivity().getApplicationContext(), lat + "," + lan+","+dataList.size(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    //퍼미션 허락받기기
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (locationSource.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
            if (!locationSource.isActivated()) {
                naverMap.setLocationTrackingMode(LocationTrackingMode.None);
                return;
            } else {
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

    public static final String ROOT_DIR = "/data/data/org.techtown.myapplication/databases";

    public static void setDB(Context ctx) {
        File folder = new File(ROOT_DIR);
        if (folder.exists()) {

        } else {
            folder.mkdirs();
        }
        AssetManager assetManager = ctx.getResources().getAssets();
        //db파일 이름
        File outfile = new File(ROOT_DIR + "hospital.db");
        InputStream is = null;
        FileOutputStream fo = null;
        long filesize = 0;
        try {
            is = assetManager.open("hospital.db", AssetManager.ACCESS_BUFFER);
            filesize = is.available();
            if (outfile.length() <= 0) {
                byte[] tempdata = new byte[(int) filesize];
                is.read(tempdata);
                is.close();
                outfile.createNewFile();
                fo = new FileOutputStream(outfile);
                fo.write(tempdata);
                fo.close();
            }

        } catch (IOException e) {

        }

    }

    private String buildDistanceQuery(double latitude, double longitude) {
        final double sinLat = Math.sin(Math.toRadians(latitude));
        final double cosLat = Math.cos(Math.toRadians(latitude));
        final double sinLng = Math.sin(Math.toRadians(longitude));
        final double cosLng = Math.cos(Math.toRadians(longitude));
        return "(" + cosLat + "*" + cos_lat
                + "*(" + cos_lon + "*" + cosLng
                + "+" + sin_lon + "*" + sinLng
                + ")+" + sinLat + "*" + sin_lat
                + ")";
    }

    public List<Hospital> getList(double latitude, double longitude, double distance) {
        setDB(getActivity());
        dataList = new ArrayList<Hospital>();

        helper = new HospitalDB(getActivity());
        database = helper.getWritableDatabase();

        String selectQuery = "SELECT *" + "," + buildDistanceQuery(latitude,longitude)
                + " AS partial_distance"
                + " FROM " + DBname
                + " WHERE partial_distance >= "
                + Math.cos(distance/6371);

        Cursor cursor = database.rawQuery(selectQuery,null);
        int count =0;
        while (cursor.moveToNext()){

            Hospital hospital = new Hospital();
            hospital.setHosname(cursor.getString(1));
            hospital.setHosaddress(cursor.getString(2));
            hospital.setHosonoff(cursor.getString(3));
            dataList.add(hospital);
            count++;

        }
        cursor.close();
        return dataList;

    }

}








