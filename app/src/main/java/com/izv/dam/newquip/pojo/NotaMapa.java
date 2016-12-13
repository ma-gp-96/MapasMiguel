package com.izv.dam.newquip.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "googleLocalizacion")
public class NotaMapa implements Parcelable {

    @DatabaseField
    long id;

    @DatabaseField
    float latitude;

    @DatabaseField
    float longitude;

    @DatabaseField
    String date;

    public NotaMapa(long id, float latitude, float longitude, String date) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.date = date;
    }

    public NotaMapa() {
        this(0,(float)0.0,(float)0.0,"");
    }

    protected NotaMapa(Parcel in) {
        id = in.readLong();
        latitude = in.readFloat();
        longitude = in.readFloat();
        date = in.readString();
    }

    public static final Creator<NotaMapa> CREATOR = new Creator<NotaMapa>() {
        @Override
        public NotaMapa createFromParcel(Parcel in) {
            return new NotaMapa(in);
        }

        @Override
        public NotaMapa[] newArray(int size) {
            return new NotaMapa[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "googleLocalizacion{" +
                "id=" + id +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", date=" + date +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeFloat(latitude);
        dest.writeFloat(longitude);
        dest.writeString(date);
    }
}
