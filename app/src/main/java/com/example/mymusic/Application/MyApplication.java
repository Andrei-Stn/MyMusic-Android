package com.example.mymusic.Application;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(this.getApplicationContext());
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().name("RealmDatabase.realm").build();
        Realm.setDefaultConfiguration(realmConfiguration);

    }
}
