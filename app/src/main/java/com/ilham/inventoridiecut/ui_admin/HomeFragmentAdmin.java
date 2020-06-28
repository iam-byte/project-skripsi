package com.ilham.inventoridiecut.ui_admin;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ilham.inventoridiecut.R;
import com.ilham.inventoridiecut.adapter.TabAdapter;
import com.ilham.inventoridiecut.preference.preferences;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeFragmentAdmin extends Fragment {
    ViewPager pager;
    TabAdapter adapter;
    TabLayout layout;
    RelativeLayout head;
    CircleImageView imageView;
    TextView title,
            sebagai;
    Context context;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home_admin, container, false);

        context = v.getContext();

        pager = v.findViewById(R.id.pager);
        layout = v.findViewById(R.id.tabLayout);
        head = v.findViewById(R.id.head);
        imageView = v.findViewById(R.id.imageView);
        title = v.findViewById(R.id.title);
        sebagai = v.findViewById(R.id.sebagai);

        if (!preferences.getUser(context).equals("admin")) {
            title.setText(preferences.getKey(context));
            sebagai.setText(preferences.getUser(context));
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

        } else {
            head.setVisibility(View.GONE);
        }

        adapter = new TabAdapter(getChildFragmentManager());
        pager.setAdapter(adapter);
        layout.setupWithViewPager(pager);
        layout.getTabAt(0).setText("Perbaikan");
        layout.getTabAt(1).setText("Pengambilan");
        layout.getTabAt(2).setText("Penarikan");

        showProfile();

        return v;
    }

    private boolean isValidContextForGlid(Context context) {
        if (context == null) {
            return false;
        }
        if (context instanceof Activity) {
            final Activity activity = (Activity) context;
            if (activity.isDestroyed() || activity.isFinishing()) {
                return false;
            }
        }
        return true;
    }

    private void showProfile() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("login").child(preferences.getId(context)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String image = dataSnapshot.child("image").getValue(String.class);
                if (isValidContextForGlid(context)) {
                    Glide.with(context).load(image).placeholder(R.drawable.ic_account).into(imageView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}