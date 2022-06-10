package com.example.fit5046_lab5_groupe.fragment;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.fit5046_lab5_groupe.R;

import androidx.fragment.app.Fragment;

import com.example.fit5046_lab5_groupe.databinding.MapFragmentBinding;
import com.google.android.gms.maps.model.LatLng;
import com.mapbox.geojson.Point;
import com.mapbox.maps.CameraOptions;
import com.mapbox.maps.MapView;
import com.mapbox.maps.Style;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.DrawableRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import com.mapbox.geojson.Point;
import com.mapbox.maps.MapView;
import com.mapbox.maps.Style;
import com.mapbox.maps.plugin.Plugin;
import com.mapbox.maps.plugin.annotation.*;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotation;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions;
import com.mapbox.maps.plugin.delegates.MapPluginProviderDelegate;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;

public class MapFragment extends Fragment {
    private MapView mapView;
    private MapFragmentBinding mapBinding;


    public MapFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the View for this fragment
        mapBinding = MapFragmentBinding.inflate(inflater, container, false);
        View view = mapBinding.getRoot();

        LatLng latLng = getLocationFromAddress(MapFragment.this.getActivity(), "900 Dandenong Rd, Caulfield East");
        final Point point = Point.fromLngLat(latLng.longitude, latLng.latitude);
        mapView = mapBinding.mapView;
        CameraOptions cameraPosition = new CameraOptions.Builder()
                .zoom(13.0)
                .center(point)
                .build();

        mapView.getMapboxMap().loadStyleUri(Style.MAPBOX_STREETS);
        mapView.getMapboxMap().setCamera(cameraPosition);
        addAnnotationToMap();

        return view;
    }

    private void addAnnotationToMap() {
        mapView = mapBinding.mapView;
        Bitmap bitmap = bitmapFromDrawableRes(MapFragment.this.getActivity(), R.drawable.red_marker);

        LatLng latLng = getLocationFromAddress(MapFragment.this.getActivity(), "900 Dandenong Rd, Caulfield East");
        AnnotationPlugin annotationApi = mapView.getPlugin(Plugin.MAPBOX_ANNOTATION_PLUGIN_ID);
        AnnotationManager pointAnnotationManager = annotationApi.createAnnotationManager(AnnotationType.PointAnnotation,null);
        PointAnnotationOptions pointAnnotationOptions = new
                PointAnnotationOptions().withPoint(Point.fromLngLat(latLng.longitude, latLng.latitude)).withIconImage(bitmap);
        pointAnnotationManager.create(pointAnnotationOptions);
    }


    private Bitmap bitmapFromDrawableRes(Context context, @DrawableRes int resourceId) {
        return convertDrawableToBitmap(AppCompatResources.getDrawable(context, resourceId));
    }

    private Bitmap convertDrawableToBitmap(Drawable sourceDrawable) {
        if (sourceDrawable == null) {
            return null;
        }
        if (sourceDrawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) sourceDrawable).getBitmap();
        }
        else {
            Drawable.ConstantState constantState = sourceDrawable.getConstantState();
            if (constantState == null) {
                return null;
            }
            else {
                Drawable drawable = constantState.newDrawable().mutate();
                Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                        drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                drawable.setBounds(0, 0, canvas.getWidth(), canvas.getWidth());
                drawable.draw(canvas);
                return bitmap;
            }
        }
    }

    public LatLng getLocationFromAddress(Context context, String strAddress) {
        Geocoder geocoder = new Geocoder(context);
        List<Address> addressList;
        LatLng latLng = null;
        try {
            addressList = geocoder.getFromLocationName(strAddress, 1);
            if (strAddress!=null) {
                Address location = addressList.get(0);
                latLng = new LatLng(location.getLatitude(), location.getLongitude());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return latLng;
    }

}
