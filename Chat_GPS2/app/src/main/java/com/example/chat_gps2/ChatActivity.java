package com.example.chat_gps2;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.chat_gps2.fragment.Model.ChatMessage;
import com.example.chat_gps2.fragment.Model.User;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ChatActivity extends AppCompatActivity {

    private FirebaseListAdapter<ChatMessage> adapter;
    FirebaseUser mFirebaseUser;
    EditText editText;
    FloatingActionButton fab;
    User user;
    String groupID;
    DatabaseReference messData;

    void findID() {
        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        editText = findViewById(R.id.text_sendd);
        fab = (FloatingActionButton) findViewById(R.id.btn_sendd);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            String text = editText.getText().toString();

            if (messData == null)
            {
                return;
            }
            if (groupID == null){
                return;
            }
            messData.push().setValue(new ChatMessage(text,user.getId(),user.getImageURL()));
            editText.setText("");
        }
    };

    public void isplayChatMessages(){
        //https://stackoverflow.com/questions/47690974/firebaselistadapter-not-working
        ListView listOfMessages = (ListView)findViewById(R.id.listview);

        Query query = FirebaseDatabase.getInstance().getReference("mess").child(groupID);
        FirebaseListOptions<ChatMessage> options = new FirebaseListOptions.Builder<ChatMessage>()
                .setLayout(R.layout.message).setQuery(query, ChatMessage.class)
                .build();
        adapter = new FirebaseListAdapter<ChatMessage>(options) {
            @Override
            protected void populateView(View v, ChatMessage model, int position) {
                TextView textView = v.findViewById(R.id.usernamee);
                ImageView imageView = v.findViewById(R.id.profile_image);
                TextView textViewRight = v.findViewById(R.id.usernamee_right);
                LinearLayout linearLayoutLeft = v.findViewById(R.id.boderLeft);
                LinearLayout linearLayoutRight = v.findViewById(R.id.boderRight);
                if (! user.getId().equals(model.getMessageUser())){
                    linearLayoutRight.setVisibility(View.INVISIBLE);
                    linearLayoutLeft.setVisibility(View.VISIBLE);
                    imageView.setVisibility(View.VISIBLE);
                    textView.setText(model.getMessageText());
                    Glide.with(v.getContext()).load(model.getImgURL()).into(imageView);
                }
                else {
                    imageView.setVisibility(View.INVISIBLE);
                    linearLayoutLeft.setVisibility(View.INVISIBLE);
                    linearLayoutRight.setVisibility(View.VISIBLE);
                    textViewRight.setText(model.getMessageText());
                }

            }
        };
        adapter.startListening();
        listOfMessages.setAdapter(adapter);
        listOfMessages.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);


    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        findID();

        DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference("Users").child(mFirebaseUser.getUid());
        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                try {
                    groupID = dataSnapshot.child("groupOn").getValue().toString();

                }
                catch (Exception e){

                }
                if (groupID == null){
                    Toast.makeText(ChatActivity.this,"Bạn chưa chọn nhóm !",Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (groupID.equals("")){
                    Toast.makeText(ChatActivity.this,"Bạn chưa chọn nhóm !",Toast.LENGTH_SHORT).show();
                    return;
                }
                isplayChatMessages();

                messData = FirebaseDatabase.getInstance().getReference("mess").child(groupID);
                fab.setOnClickListener(onClickListener);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}