package com.example.fragments.data;

public interface CardsSource {
    CardData getCardData(int position);
    int size();
}
