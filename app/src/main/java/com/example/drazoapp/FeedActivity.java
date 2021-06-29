package com.example.drazoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class FeedActivity extends AppCompatActivity {

    FloatingActionButton fabAdd, fabAddHomework, fabAddItem, fabAddGroup;

    ImageView profilePicture;
    Uri imageUri;

    String name, pseudo, imageUrl, email, phoneNo, password;

    BottomNavigationView bottomNavigationView;


    RecyclerView recyclerView;


    List<String> urls = new ArrayList<>();


    DatabaseReference reference;
    DatabaseReference reference2, reference3, reference4, reference5, reference5bis, reference6;

    FirebaseRecyclerOptions<CategoryItem> options;
    FirebaseRecyclerAdapter<CategoryItem, FeedViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        fabAdd = findViewById(R.id.fab);
        fabAddHomework = findViewById(R.id.fab_school);
        fabAddItem = findViewById(R.id.fab_store);
        fabAddGroup = findViewById(R.id.fab_group);


        recyclerView = findViewById(R.id.recycler_view);


        Animation rotateOpen = AnimationUtils.loadAnimation(FeedActivity.this, R.anim.fab_rotate_open_anim);
        Animation rotateClose = AnimationUtils.loadAnimation(FeedActivity.this, R.anim.fab_rotate_close_anim);

        Animation schoolFabMergeUp = AnimationUtils.loadAnimation(FeedActivity.this, R.anim.add_homework_fab_merge_up);
        Animation schoolFabMergeDown = AnimationUtils.loadAnimation(FeedActivity.this, R.anim.add_homework_fab_merge_down);

        Animation itemFabMergeUp = AnimationUtils.loadAnimation(FeedActivity.this, R.anim.add_item_fab_merge_up);
        Animation itemFabMergedown = AnimationUtils.loadAnimation(FeedActivity.this, R.anim.add_item_fab_merge_down);

        Animation groupFabMergeUp = AnimationUtils.loadAnimation(FeedActivity.this, R.anim.group_fab_merg_up);
        Animation groupFabMergeDown = AnimationUtils.loadAnimation(FeedActivity.this, R.anim.group_fab_merge_down);

        fabAddHomework.setVisibility(View.GONE);
        fabAddItem.setVisibility(View.GONE);
        fabAddGroup.setVisibility(View.GONE);

        bottomNavigationView = findViewById(R.id.bottom_nav_view);
        profilePicture = findViewById(R.id.toolbar_profile_image);
        bottomNavigationView.setBackground(null);

        transferUserDataHere();

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (fabAddHomework.getVisibility() == View.VISIBLE && fabAddItem.getVisibility() == View.VISIBLE && fabAddGroup.getVisibility() == View.VISIBLE) {
                    fabAddHomework.setVisibility(View.GONE);
                    fabAddItem.setVisibility(View.GONE);
                    fabAddGroup.setVisibility(View.GONE);
                    fabAdd.startAnimation(rotateClose);
                    fabAddHomework.startAnimation(schoolFabMergeDown);
                    fabAddItem.startAnimation(itemFabMergedown);
                    fabAddGroup.startAnimation(groupFabMergeDown);
                } else {

                    fabAddHomework.setVisibility(View.VISIBLE);
                    fabAddItem.setVisibility(View.VISIBLE);
                    fabAddGroup.setVisibility(View.VISIBLE);
                    fabAdd.startAnimation(rotateOpen);
                    fabAddHomework.startAnimation(schoolFabMergeUp);
                    fabAddItem.startAnimation(itemFabMergeUp);
                    fabAddGroup.startAnimation(groupFabMergeUp);
                }
            }
        });

        fabAddHomework.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), HomeworkMenu.class);
                intent.putExtra("email", email);
                intent.putExtra("fullName", name);
                intent.putExtra("imageURL", imageUrl);
                intent.putExtra("password", password);
                intent.putExtra("phoneNo", phoneNo);
                intent.putExtra("pseudo", pseudo);

                startActivity(intent);

            }
        });

        fabAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), MarketPlace.class);

                intent.putExtra("email", email);
                intent.putExtra("fullName", name);
                intent.putExtra("imageURL", imageUrl);
                intent.putExtra("password", password);
                intent.putExtra("phoneNo", phoneNo);
                intent.putExtra("pseudo", pseudo);

                startActivity(intent);

            }
        });

        reference = FirebaseDatabase.getInstance().getReference("Users").child(phoneNo).child("imageURL");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                String link = snapshot.getValue(String.class);

                if (!link.equals("NO PROFILE PICTURE")) {
                    Picasso.get().load(link).into(profilePicture);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        profilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), UserProfile.class);

                intent.putExtra("email", email);
                intent.putExtra("fullName", name);
                intent.putExtra("imageURL", imageUrl);
                intent.putExtra("password", password);
                intent.putExtra("phoneNo", phoneNo);
                intent.putExtra("pseudo", pseudo);

                startActivity(intent);

            }
        });


        recyclerView.setHasFixedSize(true);

        reference2 = FirebaseDatabase.getInstance().getReference("FeedActivityStorage");

        options = new FirebaseRecyclerOptions.Builder<CategoryItem>()
                .setQuery(reference2, CategoryItem.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<CategoryItem, FeedViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FeedViewHolder holder, int position, @NonNull CategoryItem model) {

                Picasso.get().load(model.getImageLink()).into(holder.itemPhotos, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {

                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

                //TODO fix the localisation snapshot


                holder.itemName.setText(model.getItemName());
                holder.itemPrice.setText(model.getItemPrice());
                holder.itemLocation.setText(model.getLocalisation());
                holder.description.setText(model.getItemDescription());
            }

            @NonNull
            @Override
            public FeedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_items, parent, false);

                return new FeedViewHolder(view);

            }
        };

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        adapter.startListening();
        recyclerView.setAdapter(adapter);


//        reference2.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                String key1;
//                if (snapshot.exists()) {
//                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                        //get the phone number
//
//                        key1 = dataSnapshot.getKey();
//
//                        reference3 = reference2.child(key1);
//
//                        reference3.addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                                String key2;
//
//                                if(snapshot.exists())
//                                {
//                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//
//                                        //get category
//
//                                        key2 = dataSnapshot.getKey();
//
//                                        reference4 = reference3.child(key2);
//
//                                        reference4.addValueEventListener(new ValueEventListener() {
//                                            @Override
//                                            public void onDataChange(@NonNull  DataSnapshot snapshot) {
//
//                                                String key3;
//
//                                                if(snapshot.exists()) {
//
//                                                    //key3 here = vehicule
//
//                                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                                                        // get the posts in the category -->
//
//                                                        key3 = dataSnapshot.getKey();
//
//                                                        //key3 here = time of the post in millisecond
//
//                                                        reference5 = reference4.child(key3);
//
//                                                      //  Toast.makeText(getApplicationContext(), imageHelperClass.getUrls().get(0), Toast.LENGTH_LONG).show();
//
//
//                                                    }
//                                                }
//                                            }
//
//                                            @Override
//                                            public void onCancelled(@NonNull  DatabaseError error) {
//
//                                            }
//                                        });
//                                    }
//                                }
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull  DatabaseError error) {
//
//                            }
//                        });
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });


//        //gridImage view
//
//        String[] postName= {"Nom de l'article"};
//        int[] images = {R.drawable.ic_launcher_background,};
//        int[] itemLocalisationSnapchot = { R.drawable.love};
//
//
//
//        String [] localisation =  {"lagos"};
//        String[] itemPrice = {"200$"};
//
//
//        CustomAdapter customAdapter = new CustomAdapter(postName, images, itemLocalisationSnapchot, localisation, itemPrice, this);
//        gridView.setAdapter(customAdapter);

    }

    @Override
    protected void onStart() {
        super.onStart();

        if (adapter != null){
            adapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        if (adapter != null){
            adapter.stopListening();
        }
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (adapter != null){
            adapter.startListening();
        }
    }


    public static class CustomAdapter extends BaseAdapter {

        private final String[] itemName;
        private final int[] itemPhoto;
        private final String[] desc;
        private final String[] localisation;
        private final String[] itemPrice;


        private final LayoutInflater layoutInflater;

        public CustomAdapter(String[] itemName, int[] itemPhoto, String[] desc, String[] localisation, String[] itemPrice, Context context) {
            this.itemName = itemName;
            this.itemPhoto = itemPhoto;
            this.desc = desc;
            this.localisation = localisation;
            this.itemPrice = itemPrice;
            this.layoutInflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return itemPhoto.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.row_items, parent, false);
            }

            TextView textView1 = convertView.findViewById(R.id.item_name);
            TextView textView2 = convertView.findViewById(R.id.item_localisation);
            TextView textView3 = convertView.findViewById(R.id.item_price);
            TextView textView4 = convertView.findViewById(R.id.desc);


            ImageView imageView1 = convertView.findViewById(R.id.grid_item);

            textView1.setText(itemName[position]);
            textView2.setText(localisation[position]);
            textView3.setText(itemPrice[position]);
            textView4.setText(desc[position]);

            imageView1.setImageResource(itemPhoto[position]);

            return convertView;
        }
    }

    public void transferUserDataHere() {

        Intent intent = getIntent();

        name = intent.getStringExtra("fullName");
        pseudo = intent.getStringExtra("pseudo");
        email = intent.getStringExtra("email");
        phoneNo = intent.getStringExtra("phoneNo");
        password = intent.getStringExtra("password");
        imageUrl = intent.getStringExtra("imageURL");

    }
}