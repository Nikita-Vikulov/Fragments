package com.example.fragments.data;

import android.content.res.Resources;

import java.util.ArrayList;
import java.util.List;


public class CardsSourceImpl implements com.example.fragments.data.CardsSource {
    private final List<CardData> dataSource;
    private Resources resources;    // ресурсы приложения

    public CardsSourceImpl(Resources resources) {
        dataSource = new ArrayList<>(7);
        this.resources = resources;
    }

    public CardsSourceImpl init(){
        // строки заголовков из ресурсов
        String[] titles = {CardData.note_1.title, CardData.note_2.title, CardData.note_3.title,};
        // строки описаний из ресурсов
        String[] descriptions = {CardData.note_1.description, CardData.note_2.description, CardData.note_3.description,};
        String[] dates = {CardData.note_1.date, CardData.note_2.date, CardData.note_3.date,};;
        // заполнение источника данных
        for (int i = 0; i < descriptions.length; i++) {
            dataSource.add(new CardData(titles[i], descriptions[i], dates[i]));
        }
        return this;
    }

    public CardData getCardData(int position) {
        return dataSource.get(position);
    }

    public int size(){
        return dataSource.size();
    }
}
