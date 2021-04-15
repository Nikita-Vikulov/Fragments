package com.example.fragments.data;

public class CardData {
    protected final String title;
    protected final String description;
    protected final String date;
        public CardData (String title, String description, String date) {
            this.title = title;
            this.description = description;
            this.date = date;
        }

    static CardData note_1 = new CardData("Заметка 1", "Содержимое первой заметки", "07.04.2021");
    static CardData note_2 = new CardData("Заметка 2", "Содержимое второй заметки", "08.04.2021");
    static CardData note_3 = new CardData("Заметка 3", "Содержимое третьей заметки", "09.04.2021");

    public static String noteDescription(int index) {
        switch (index) {
            case 1:
                return (note_3.title + "\n" + note_3.description);
            case 2:
                return (note_2.title + "\n" + note_2.description);
            default:
                return (note_1.title + "\n" + note_1.description);
        }
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

}
