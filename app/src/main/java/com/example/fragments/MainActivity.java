package com.example.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.fragments.observer.Publisher;
import com.example.fragments.ui.CardFragment;
import com.example.fragments.ui.NotesFragment;
import com.google.android.material.navigation.NavigationView;

import javax.annotation.Nonnull;

import static com.example.fragments.ui.NotesFragment.adapter;
import static com.example.fragments.ui.NotesFragment.data;

public class MainActivity extends AppCompatActivity {

    private final Publisher publisher = new Publisher();
    private Navigation navigation;
    private boolean moveToFirstPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        readSettings();
        initToolbar();
        navigation = new Navigation(getSupportFragmentManager());
        getNavigation().addFragment(NotesFragment.newInstance(), false);
    }

    // регистрация drawer
    private void initDrawer(Toolbar toolbar) {
        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // Обработка навигационного меню
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(item -> {
            int menuItemId = item.getItemId();
            if (onItemSelected(menuItemId)) {
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
            return false;
        });
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        initDrawer(toolbar);
    }

    @Override
    public boolean onOptionsItemSelected(@Nonnull MenuItem item) {
        return onItemSelected(item.getItemId()) || super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        return onItemSelected(item.getItemId()) || super.onContextItemSelected(item);
    }

    private boolean onItemSelected(int menuItemId) {
        switch (menuItemId) {
            case R.id.action_add:
                navigation.addFragment(CardFragment.newInstance(), true);
                publisher.subscribe(cardData -> {
                    data.addCardData(cardData);
                    adapter.notifyItemInserted(data.size() - 1);
                    // это сигнал, чтобы вызванный метод onCreateView
                    // перепрыгнул на начало списка
                    moveToFirstPosition = true;
                });
                return true;
            case R.id.action_delete:
                action_delete();
                return true;
            case R.id.action_clear:
                data.clearCardData();
                adapter.notifyDataSetChanged();
                return true;
            case R.id.action_settings:
                getNavigation().addFragment(new SettingsFragment(), true);
                return true;
            case R.id.action_favorite:
                getNavigation().addFragment(new FavoriteFragment(), true);
                return true;
        }
        return false;
    }

    private void action_delete() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        // В билдере указываем заголовок окна. Можно указывать как ресурс, так
        // и строку
        builder.setTitle(R.string.exclamation)
                // Указываем сообщение в окне. Также есть вариант со строковым
                // параметром
                .setMessage("Delete a note?")
                // Из этого окна нельзя выйти кнопкой Back
                .setCancelable(false)
                // Устанавливаем отрицательную кнопку
                .setNegativeButton(R.string.no,
                        // Ставим слушатель, будем обрабатывать нажатие
                        (dialog, id) -> Toast.makeText(MainActivity.this, "No", Toast.LENGTH_SHORT).show())
                // Устанавливаем нейтральную кнопку
                .setNeutralButton(R.string.dunno,
                        // Ставим слушатель, будем обрабатывать нажатие
                        (dialog, id) -> Toast.makeText(MainActivity.this, "Cancel", Toast.LENGTH_SHORT).show())
                // Устанавливаем кнопку. Название кнопки также можно задавать
                // строкой
                .setPositiveButton(R.string.yes,
                        // Ставим слушатель, будем обрабатывать нажатие
                        (dialog, id) -> {
                            int deletePosition = adapter.getMenuPosition();
                            data.deleteCardData(deletePosition);
                            adapter.notifyItemRemoved(deletePosition);
                            Toast.makeText(MainActivity.this, "Yes", Toast.LENGTH_SHORT).show();
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Здесь определяем меню приложения (активити)
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem search = menu.findItem(R.id.action_search); // поиск пункта меню поиска
        SearchView searchText = (SearchView) search.getActionView(); // строка поиска
        searchText.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // реагирует на конец ввода поиска
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(MainActivity.this, query, Toast.LENGTH_SHORT).show();
                return true;
            }
            // реагирует на нажатие каждой клавиши
            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
        return true;
    }

    // Чтение настроек
    private void readSettings() {
        // Специальный класс для хранения настроек
        SharedPreferences sharedPref = getSharedPreferences(Settings.SHARED_PREFERENCE_NAME, MODE_PRIVATE);
        // Считываем значения настроек
        Settings.IsBackStack = sharedPref.getBoolean(Settings.IS_BACK_STACK_USED, false);
        Settings.IsAddFragment = sharedPref.getBoolean(Settings.IS_ADD_FRAGMENT_USED, true);
        Settings.IsBackAsRemove = sharedPref.getBoolean(Settings.IS_BACK_AS_REMOVE_FRAGMENT, true);
        Settings.IsDeleteBeforeAdd = sharedPref.getBoolean(Settings.IS_DELETE_FRAGMENT_BEFORE_ADD, false);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public Navigation getNavigation() {
        return navigation;
    }

    public Publisher getPublisher() {
        return publisher;
    }
}