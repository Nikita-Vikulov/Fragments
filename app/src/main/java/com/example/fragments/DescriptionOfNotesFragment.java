package com.example.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

import com.example.fragments.ui.NotesFragment;

public class DescriptionOfNotesFragment extends Fragment {
    public static final String ARG_INDEX = "index";
    private int index;

    // Фабричный метод создания фрагмента
    // Фрагменты рекомендуется создавать через фабричные методы.
    public static DescriptionOfNotesFragment newInstance(int index) {
        DescriptionOfNotesFragment fragment = new DescriptionOfNotesFragment();    // создание
        // Передача параметра
        Bundle args = new Bundle();
        args.putInt(ARG_INDEX, index);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_INDEX);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Таким способом можно получить головной элемент из макета
        View view = inflater.inflate(R.layout.fragments_description_of_notes, container, false);
        // найти в контейнере элемент-изображение
        AppCompatTextView textDescription = view.findViewById(R.id.description);
        // Выбрать по индексу подходящий
        textDescription.setText(NotesFragment.description);
        textDescription.setTextSize(30);
        return view;
    }
}


