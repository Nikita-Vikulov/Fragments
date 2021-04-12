package com.example.fragments;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


public class NotesFragment extends Fragment {
    public static final String CURRENT_NOTE = "CurrentCity";
    private int currentPosition = 0;
    private boolean isLandscape;
    public static String description;

    // При создании фрагмента укажем его макет
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notes, container, false);
    }

    // вызывается после создания макета фрагмента, здесь мы проинициализируем список
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initList(view);
    }

    public static class Notes extends NotesFragment {
        final private String name, description, date;

        public Notes(String name, String description, String date) {
            this.name = name;
            this.description = description;
            this.date = date;
        }

        static Notes note_1 = new Notes("Заметка 1", "Содержимое первой заметки", "07.04.2021");
        static Notes note_2 = new Notes("Заметка 2", "Содержимое второй заметки", "08.04.2021");
        static Notes note_3 = new Notes("Заметка 3", "Содержимое третьей заметки", "09.04.2021");
        static String n1 = (note_1.name + "   " + note_1.date);
        static String n2 = (note_2.name + "   " + note_2.date);
        static String n3 = (note_3.name + "   " + note_3.date);
        static String nd1 = (note_1.name + "\n" + note_1.description);
        static String nd2 = (note_2.name + "\n" + note_2.description);
        static String nd3 = (note_3.name + "\n" + note_3.description);
        static String[] notes = {n1, n2, n3};
    }


    // создаём список городов на экране из массива в ресурсах
    private void initList(View view) {
        LinearLayout layoutView = (LinearLayout) view;
        // В этом цикле создаём элемент TextView,
        // заполняем его значениями,
        // и добавляем на экран.
        // Кроме того, создаём обработку касания на элемент
        for (int i = 0; i < NotesFragment.Notes.notes.length; i++) {
            TextView tv = new TextView(getContext());
            tv.setText(NotesFragment.Notes.notes[i]);
            tv.setTextSize(30);
            layoutView.addView(tv);
            final int fi = i;
            tv.setOnClickListener(v -> {
                currentPosition = fi;
                showCoatOfNotes(currentPosition);
            });
        }
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
            showLandCoatOfNotes(0);
        }
    }

    private void showCoatOfNotes(int index) {
        if (isLandscape) {
            showLandCoatOfNotes(index);
        } else {
            showPortCoatOfNotes(index);
        }
        description(index);
    }

    public void description(int index) {
        switch (index) {
            case 0:
                description = NotesFragment.Notes.nd1;
                break;
            case 1:
                description = NotesFragment.Notes.nd2;
                break;
            case 2:
                description = NotesFragment.Notes.nd3;
                break;
        }
    }

    // Показать описание в ландшафтной ориентации
    private void showLandCoatOfNotes(int index) {
        // Создаём новый фрагмент с текущей позицией для вывода описания
        DescriptionOfNotesFragment detail = DescriptionOfNotesFragment.newInstance(index);
        // Выполняем транзакцию по замене фрагмента
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.description, detail);// замена фрагмента
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.commit();

    }

    // Показать описание в портретной ориентации.
    private void showPortCoatOfNotes(int index) {
        // Откроем вторую activity
        Intent intent = new Intent();
        intent.setClass(getActivity(), DescriptionOfNotesActivity.class);
        // и передадим туда параметры
        intent.putExtra(DescriptionOfNotesFragment.ARG_INDEX, index);
        startActivity(intent);
    }
}