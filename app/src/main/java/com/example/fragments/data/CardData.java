package com.example.fragments.data;

import java.util.Date;

public class CardData {
    protected final String title;
    protected final String description;
    protected final Date date;
        public CardData (String title, String description, Date date) {
            this.title = title;
            this.description = description;
            this.date = date;
        }

    static CardData note_1 = new CardData("Заметка 1", "Содержимое первой заметки", null);
    static CardData note_2 = new CardData("Заметка 2", "Содержимое второй заметки", null);
    static CardData note_3 = new CardData("Заметка 3", "Содержимое третьей заметки", null);
    static CardData note_4;

    public static String noteDescription(int index) {
        switch (index) {
            case 0:
                return (note_1.title + "\n" + note_1.description);
            case 1:
                return (note_2.title + "\n" + note_2.description);
            default:
                return (note_3.title + "\n" + note_3.description);
        }
    }

    public String getTitle() {
        return title;
    }

    public Date getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }
}
