package com.example.chat_gps2.fragment.spclass;

import android.Manifest;
import android.app.Activity;

import android.content.Context;
import android.content.pm.PackageManager;

import android.graphics.Bitmap;

import android.graphics.Canvas;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.chat_gps2.R;

import com.example.chat_gps2.fragment.Model.User;

import com.google.android.gms.maps.GoogleMap;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SPMap extends Activity {
    // Get singleton instance

    public static Bitmap img;
    public static View view;
    public static Context context;
    TextView textView;


    public static GoogleMap map;

    private static String userID;

    public static HashMap<String,User> listUser = new HashMap<String, User>();
    public static HashMap<String,Bitmap> bitmaoList = new HashMap<String, Bitmap>();

    public static HashMap<String, Marker> markerList = new HashMap<String, Marker>();

    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 100;

    //https://developer.android.com/training/permissions/requesting.html#java
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    public static boolean check_requires_my_GPS(FragmentActivity activity){
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            return false;
        }
        return true;
    }

    public static void requires_GPS(Activity activity){

        //https://developer.android.com/training/permissions/requesting.html#java
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(activity,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
        }
    }

    public static void addBitMap(final User user){
/*
        Bitmap myLogo = BitmapFactory.decodeResource(context.getResources(), R.drawable.images);
        bitmaoList.put(user.getId(),myLogo);
*/

        Glide.with(context)
                .load(user.getImageURL())
                .asBitmap()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        bitmaoList.put(user.getId(),resource);
                    }
                });




    }

    //https://viblo.asia/p/tim-hieu-ve-google-maps-marker-trong-android-djeZ1bPYlWz
    public static Bitmap  createMaker(User user) throws Exception {

        final View marker = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.marker, null);
        ImageView markerImage =  marker.findViewById(R.id.user_dp);
        Bitmap AVTbitmap = bitmaoList.get(user.getId());
        if (AVTbitmap==null) {
            return null;
        }
        else {
            markerImage.setImageBitmap(AVTbitmap);
        }

        TextView txt_name = (TextView)marker.findViewById(R.id.name);
        txt_name.setText(user.getUsername());

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        marker.setLayoutParams(new ViewGroup.LayoutParams(52, ViewGroup.LayoutParams.WRAP_CONTENT));
        marker.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        marker.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        marker.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(marker.getMeasuredWidth(), marker.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        marker.draw(canvas);


        return bitmap;
    }
    public static Bitmap createMakerDF(User user) {


        View marker = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.marker, null);

        CircleImageView markerImage = (CircleImageView) marker.findViewById(R.id.user_dp);
        markerImage.setImageResource(R.mipmap.ic_launcher);
        TextView txt_name = (TextView)marker.findViewById(R.id.name);

        txt_name.setText(user.getUsername());

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        marker.setLayoutParams(new ViewGroup.LayoutParams(52, ViewGroup.LayoutParams.WRAP_CONTENT));
        marker.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        marker.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        marker.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(marker.getMeasuredWidth(), marker.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        marker.draw(canvas);

        return bitmap;
    }

    static void newMarker(User user) throws Exception {
        if (user == null || user.getLongitude()==null || user.getLatitude() == null)
            return;
        LatLng latLng = new LatLng(user.getLatitude(), user.getLongitude());
        MarkerOptions markerOptions;
        Bitmap icon;


        try{
//            https://pragmaapps.com/add-custom-image-in-google-maps-marker-android/
            icon = createMaker(user);
        }
        catch (Exception e){
            icon = createMakerDF(user);
        }
        if (icon == null){
            return;
        }

        markerOptions = new MarkerOptions().position(latLng)
                .icon(BitmapDescriptorFactory.fromBitmap( createMaker(user)))
                .title("hello");
        Marker m = map.addMarker(markerOptions);
        markerList.put(user.getId(),m);

    }


    static ValueEventListener getUSER = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            User user = dataSnapshot.getValue(User.class);
            listUser.put(userID,user);
            upDatMarker(user);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    public static void getListMember(String groupID){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("usersInGroup").child(groupID);
        reference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                listUser.clear();
                //Duyệt rất cả các ID của các Gr
                for (DataSnapshot snapshot1 : dataSnapshot.getChildren()){

                    DatabaseReference getUser = FirebaseDatabase.getInstance().getReference("Users")
                            .child(snapshot1.getValue().toString());
                    getUser.addValueEventListener(getUSER);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    public static void upDatMarker(final User user){
        if (bitmaoList.get(user.getId())==null){
            addBitMap(user);
        }
        if (markerList.get(user.getId())==null){
            try{
                newMarker(user);
            }
            catch (Exception e){
                Log.v("new mk",e.toString());
            }
        }
        else {
            if (user.getLatitude()==0 || user.getLongitude() == 0)
                return;

            Marker marker = markerList.get(user.getId());
            marker.setPosition(new LatLng(user.getLatitude(),user.getLongitude()));

        }
    }







}
