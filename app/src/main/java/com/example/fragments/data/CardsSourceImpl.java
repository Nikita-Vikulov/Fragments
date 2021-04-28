package com.example.fragments.data;

import android.content.res.Resources;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class CardsSourceImpl implements CardsSource {
    private List<CardData> dataSource;
    private Resources resources;    // ресурсы приложения

    public CardsSourceImpl(Resources resources) {
        dataSource = new ArrayList<>(7);
        this.resources = resources;
    }

    public CardsSourceImpl init(CardsSourceResponse cardsSourceResponse) {
        // строки заголовков из ресурсов
        String[] titles = {CardData.note_1.title, CardData.note_2.title, CardData.note_3.title,};
        // строки описаний из ресурсов
        String[] descriptions = {CardData.note_1.description, CardData.note_2.description, CardData.note_3.description,};
        // заполнение источника данных
        for (int i = 0; i < descriptions.length; i++) {
            dataSource.add(new CardData(titles[i], descriptions[i], Calendar.getInstance().getTime()));
        }

        if (cardsSourceResponse != null) {
            cardsSourceResponse.initialized(this);
        }

        return this;
    }

    public CardData getCardData(int position) {
        return dataSource.get(position);
    }

    public int size() {
        return dataSource.size();
    }

    @Override
    public void deleteCardData(int position) {
        dataSource.remove(position);
    }

    @Override
    public void updateCardData(int position, CardData cardData) {
        dataSource.set(position, cardData);
    }

    @Override
    public void addCardData(CardData cardData) {
        dataSource.add(cardData);
    }

    @Override
    public void clearCardData() {
        dataSource.clear();
    }
}
