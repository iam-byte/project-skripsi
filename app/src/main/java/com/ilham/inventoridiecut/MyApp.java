package com.ilham.inventoridiecut;

import android.app.Application;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class MyApp extends Application {


	@Override
	public void onCreate() {
		super.onCreate();
		FirebaseDatabase.getInstance().setPersistenceEnabled(true);

		DatabaseReference base1 = FirebaseDatabase.getInstance().getReference("customer");
		DatabaseReference base2 = FirebaseDatabase.getInstance().getReference("login");
		DatabaseReference base3 = FirebaseDatabase.getInstance().getReference("mesin");
		DatabaseReference base4 = FirebaseDatabase.getInstance().getReference("pembuatan-die-cut");
		DatabaseReference base5 = FirebaseDatabase.getInstance().getReference("penarikan");
		DatabaseReference base6 = FirebaseDatabase.getInstance().getReference("pengambilan");
		DatabaseReference base7 = FirebaseDatabase.getInstance().getReference("perbaikan");
		DatabaseReference base8 = FirebaseDatabase.getInstance().getReference("user");

		base1.keepSynced(true);
		base2.keepSynced(true);
		base3.keepSynced(true);
		base4.keepSynced(true);
		base5.keepSynced(true);
		base6.keepSynced(true);
		base7.keepSynced(true);
		base8.keepSynced(true);
	}
}
