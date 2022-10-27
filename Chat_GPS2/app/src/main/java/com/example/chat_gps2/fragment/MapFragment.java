package com.example.chat_gps2.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.chat_gps2.R;
import com.example.chat_gps2.fragment.Model.User;

import com.example.chat_gps2.fragment.spclass.SPMap;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class MapFragment extends Fragment implements OnMapReadyCallback {

    private static View view;
    private Spinner spinner;
    private GoogleMap mMap;
    ImageView image_profile;
    StorageReference storageReference;
    FirebaseUser fuser;
    DatabaseReference reference;
    TextView textView;

    //Chữa lỗi dò rỉ bộ nhớ
    //Nguồn https://helpex.vn/question/id-trung-lap-the-null-hoac-id-cha-voi-mot-doan-khac-cho-com-google-android-gms-maps-mapfragment-5cb71a31ae03f62598dde726

    //Thông tin
    private void spinnerTT(){

        image_profile = view.findViewById(R.id.profile_image);
        textView = view.findViewById(R.id.textview);

        storageReference = FirebaseStorage.getInstance().getReference("uploads");

        fuser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user.getImageURL().equals("default")){
                    image_profile.setImageResource(R.mipmap.ic_launcher);
                    textView.setText(user.getUsername());

                } else {
                    try {
                        Glide.with(getContext()).load(user.getImageURL()).into(image_profile);
                        textView.setText(user.getUsername());
                    }
                    catch (Exception e){

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.map_fragment, container, false);
        } catch (InflateException e) {
            /* map is already there, just return view as it is */
        }
        SPMap.context = getActivity();
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        final TextView textView2 = view.findViewById(R.id.main_getGroup);

        DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());
        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("groupOn").getValue() == null){
                    Toast.makeText(getContext(),"Bạn chưa chọn nhóm !",Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (dataSnapshot.child("groupOn").getValue().toString().equals("")){
                    Toast.makeText(getContext(),"Bạn chưa chọn nhóm !",Toast.LENGTH_SHORT).show();
                    return;
                }
                SPMap.getListMember(dataSnapshot.child("groupOn").getValue().toString());
                try {
                    textView2.setText(dataSnapshot.child("nameGroupOn").getValue().toString());
                }
                catch (Exception e){

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        spinnerTT();
        SPMap.view = view;

        SupportMapFragment mapFragment = (SupportMapFragment)
                this.getChildFragmentManager()
                        .findFragmentById(R.id.mapppppppppp);
        mapFragment.getMapAsync(this);

        return view;
    }




    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        SPMap.map = mMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        if(SPMap.check_requires_my_GPS(getActivity())){
            mMap.setMyLocationEnabled(true);

        }


    }
}