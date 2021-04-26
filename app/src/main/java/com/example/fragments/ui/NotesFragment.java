package com.example.fragments.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fragments.DescriptionOfNotesActivity;
import com.example.fragments.DescriptionOfNotesFragment;
import com.example.fragments.Navigation;
import com.example.fragments.R;
import com.example.fragments.data.CardData;
import com.example.fragments.data.CardsSource;
import com.example.fragments.data.CardsSourceImpl;

import java.util.Objects;

import observer.Observer;
import observer.Publisher;

public class NotesFragment extends Fragment {
    private static final int MY_DEFAULT_DURATION = 1000;
    public static final String CURRENT_NOTE = "CurrentNotes";
    private int currentPosition = 0;
    private boolean isLandscape;
    public static String description;
    public static CardsSource data;
    public static NotesAdapter adapter;
    public static RecyclerView recyclerView;
    private Navigation navigation;
    private Publisher publisher;

    public static NotesFragment newInstance() {
        NotesFragment fragment = new NotesFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notes, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_lines);
        // Получим источник данных для списка
        data = new CardsSourceImpl(getResources()).init();
        //initRecyclerView(recyclerView, data/*Notes.notes*/);
        initView(view);
        setHasOptionsMenu(true);
        return view;
    }

    private void initView(View view) {
        recyclerView = view.findViewById(R.id.recycler_view_lines);
        // Получим источник данных для списка
        data = new CardsSourceImpl(getResources()).init();
        initRecyclerView();
    }

    @SuppressLint("NewApi")
    private void initRecyclerView() {

        // Эта установка служит для повышения производительности системы
        recyclerView.setHasFixedSize(true);

        // Будем работать со встроенным менеджером
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        // Установим адаптер
        adapter = new NotesAdapter(data, this);
        recyclerView.setAdapter(adapter);

        // Добавим разделитель карточек
        DividerItemDecoration itemDecoration = new
                DividerItemDecoration(Objects.requireNonNull(getContext()), LinearLayoutManager.VERTICAL);
        itemDecoration.setDrawable(getResources().getDrawable(R.drawable.separator,
                null));
        recyclerView.addItemDecoration(itemDecoration);

        // Установим анимацию. А чтобы было хорошо заметно, сделаем анимацию долгой
        DefaultItemAnimator animator = new DefaultItemAnimator();
        animator.setAddDuration(MY_DEFAULT_DURATION);
        animator.setRemoveDuration(MY_DEFAULT_DURATION);
        recyclerView.setItemAnimator(animator);

        // Установим слушателя
        adapter.SetOnItemClickListener((view, currentPosition) -> {
            showDescriptionOfNotes(currentPosition);
        });
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = requireActivity().getMenuInflater();
        inflater.inflate(R.menu.card_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        final int position = adapter.getMenuPosition();
        switch(item.getItemId()) {
            case R.id.action_update:
                navigation.addFragment(CardFragment.newInstance(data.getCardData(position)), true);
                publisher.subscribe(new Observer() {
                    @Override
                    public void updateCardData(CardData cardData) {
                        data.updateCardData(position, cardData);
                        adapter.notifyItemChanged(position);
                    }
                });
                return true;
            case R.id.action_delete:
                data.deleteCardData(position);
                adapter.notifyItemRemoved(position);
                return true;
        }
        return super.onContextItemSelected(item);
    }
    // Сохраним текущую позицию (вызывается перед выходом из фрагмента)
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(CURRENT_NOTE, currentPosition);
        super.onSaveInstanceState(outState);
    }

    // activity создана, можно к ней обращаться. Выполним начальные действия
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isLandscape = getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE;
        // Если это не первое создание, то восстановим текущую позицию
        if (savedInstanceState != null) {
            // Восстановление текущей позиции.
            currentPosition = savedInstanceState.getInt(CURRENT_NOTE, 0);
        }
        if (isLandscape) {
            showLandDescriptionOfNotes(0);
        }
    }

    private void showDescriptionOfNotes(int index) {
        if (isLandscape) {
            showLandDescriptionOfNotes(index);
        } else {
            showPortDescriptionOfNotes(index);
        }
        description(index);
    }

    public void description(int index) {
        description = CardData.noteDescription(index);
    }

    // Показать описание в ландшафтной ориентации
    private void showLandDescriptionOfNotes(int index) {
        // Создаём новый фрагмент с текущей позицией для вывода описания
        DescriptionOfNotesFragment detail = DescriptionOfNotesFragment.newInstance(index);
        // Выполняем транзакцию по замене фрагмента
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentManager.beginTransaction()
                .replace(R.id.description, detail)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    // Показать описание в портретной ориентации.
    private void showPortDescriptionOfNotes(int index) {
        // Откроем вторую activity
        Intent intent = new Intent();
        intent.setClass(getActivity(), DescriptionOfNotesActivity.class);
        // и передадим туда параметры
        intent.putExtra(DescriptionOfNotesFragment.ARG_INDEX, index);
        startActivity(intent);
    }

}