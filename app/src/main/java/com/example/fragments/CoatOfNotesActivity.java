package com.example.fragments;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class CoatOfNotesActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coat_of_notes);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // Если устройство перевернули в альбомную ориентацию, то надо эту activity закрыть
            finish();
            return;
        }

        if (savedInstanceState == null) {
            // Если эта activity запускается первый раз (с каждым новым гербом первый раз),
            // то перенаправим параметр фрагменту
            CoatOfNotesFragment details = new CoatOfNotesFragment();
            details.setArguments(getIntent().getExtras());
            // Добавим фрагмент на activity
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, details).commit();
        }
    }
}


