package com.example.fragments.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class CardData implements Parcelable {
    protected String id;
    protected final String title;
    protected final String description;
    protected Date date;

    public CardData(String title, String description, Date date) {
        this.title = title;
        this.description = description;
        this.date = date;
    }

    static CardData note_1 = new CardData("Заметка 1", "Содержимое первой заметки", null);
    static CardData note_2 = new CardData("Заметка 2", "Содержимое второй заметки", null);
    static CardData note_3 = new CardData("Заметка 3", "Содержимое третьей заметки", null);

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

    protected CardData(Parcel in) {
        title = in.readString();
        description = in.readString();
        date = new Date(in.readLong());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(description);
        dest.writeLong(date.getTime());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CardData> CREATOR = new Creator<CardData>() {
        @Override
        public CardData createFromParcel(Parcel in) {
            return new CardData(in);
        }

        @Override
        public CardData[] newArray(int size) {
            return new CardData[size];
        }
    };

    public String getId(){
        return id;
    }

    public void setId(String id){
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Date getDate() {
        return date;
    }
}
