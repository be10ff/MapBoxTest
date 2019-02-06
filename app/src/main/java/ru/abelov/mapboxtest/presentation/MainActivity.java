package ru.abelov.mapboxtest.presentation;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.abelov.mapboxtest.R;
import ru.abelov.mapboxtest.data.entity.WeatherEntity;
import ru.abelov.mapboxtest.domain.WeatherInteractor;
import ru.abelov.mapboxtest.presentation.view.CurrentWeatherView;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, PermissionsListener, CurrentWeatherView {

    @BindView(R.id.mvMap)
    MapView mapView;

    private MapboxMap mapboxMap;
    private PermissionsManager permissionsManager;
    private SymbolManager symbolManager;

    private WeatherInteractor weatherInteractor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Mapbox.getInstance(getApplicationContext(), getString(R.string.mapbox_access_token));

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        weatherInteractor = new WeatherInteractor();
        weatherInteractor.setView(this);

        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        weatherInteractor.destroy();
        mapView.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @SuppressWarnings("MissingPermission")
    private void enableLocationComponent(Style style){
        if(PermissionsManager.areLocationPermissionsGranted(this)) {
            LocationComponent locationComponent = mapboxMap.getLocationComponent();
            locationComponent.activateLocationComponent(this, style);
            locationComponent.setLocationComponentEnabled(true);
            locationComponent.setCameraMode(CameraMode.TRACKING);
            locationComponent.setRenderMode(RenderMode.NORMAL);
        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(this, R.string.user_location_permission_explanation, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            mapboxMap.getStyle(this::enableLocationComponent);
        } else {
            Toast.makeText(this, R.string.user_location_permission_explanation, Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        MainActivity.this.mapboxMap = mapboxMap;

        mapboxMap.setStyle(Style.MAPBOX_STREETS, style -> {
                symbolManager = new SymbolManager(mapView, mapboxMap, style);
                enableLocationComponent(style);
        });

        mapboxMap.addOnMapClickListener(p -> {
                weatherInteractor.getCurrentWeather(p.getLatitude(), p.getLongitude());
                return false;
        });
    }

    @Override
    public void onResult(WeatherEntity model) {
        symbolManager.getAnnotations().clear();
        SymbolOptions option = new SymbolOptions().withLatLng(new LatLng(model.location.lat, model.location.lon)).withTextField(model.current.tempC);
        symbolManager.create(option);
    }

    @Override
    public void onError(String message) {
        Toast.makeText(this, R.string.error, Toast.LENGTH_LONG).show();
    }
}
