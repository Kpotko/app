package places.app.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;

import places.app.R;
import places.app.storage.DatabaseHelper;
import places.app.storage.PlaceModel;
import places.app.ui.activities.MainActivity;


public class FragmentMap extends Fragment implements OnMapReadyCallback {


    private GoogleMap googleMap;
    private MapView mapView;
    private Vibrator vibrator;

    private ArrayList<PlaceModel> placeModels = new ArrayList<>();
    private HashMap<Integer, Marker> markers = new HashMap<>();

    public static FragmentMap newInstance() {
        return new FragmentMap();
    }

    public FragmentMap() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vibrator = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        mapView = view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);
        return view;
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

        if (getActivity() != null) {
            getActivity().setTitle(R.string.map_title);
        }
    }

    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (getActivity() != null) {
                getActivity().setTitle(R.string.map_title);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        setupMap();
    }

    private void setupMap() {
        googleMap.getUiSettings().setRotateGesturesEnabled(true);
        googleMap.getUiSettings().setMapToolbarEnabled(false);
        googleMap.getUiSettings().setCompassEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(false);
        googleMap.getUiSettings().setIndoorLevelPickerEnabled(false);
        googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                vibrator.vibrate(50);
                ((MainActivity) getActivity()).addToBackStack(PlaceEditFragment.newInstance(latLng.latitude, latLng.longitude), true);
            }
        });
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                int pid = (int) marker.getTag();
                ((MainActivity) getActivity()).addToBackStack(PlaceEditFragment.newInstance(pid), true);
                return true;
            }
        });
        loadPlaces();
    }

    public void loadPlaces() {

        googleMap.clear();
        markers.clear();

        placeModels = DatabaseHelper.getInstance(getActivity()).getAll();
        for (PlaceModel place : placeModels) {
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.title(place.getDescription());
            markerOptions.position(new LatLng(place.getLatitude(), place.getLongitude()));
            Marker marker = googleMap.addMarker(markerOptions);
            marker.setTag(place.getPid());
            markers.put(place.getPid(), marker);

        }

    }

}
