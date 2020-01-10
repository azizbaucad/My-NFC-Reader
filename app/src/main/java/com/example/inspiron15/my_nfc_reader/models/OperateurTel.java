package com.example.inspiron15.my_nfc_reader.models;

import android.os.Parcel;
import android.os.Parcelable;

public class OperateurTel implements Parcelable {
   public String name;
   public String solde;
   public int imageId;

    public OperateurTel() {
    }

    public OperateurTel(String name, String solde, int imageId) {
        this.name = name;
        this.solde = solde;
        this.imageId = imageId;
    }

    protected OperateurTel(Parcel in) {
        name = in.readString();
        solde = in.readString();
        imageId = in.readInt();
    }

    public static final Creator<OperateurTel> CREATOR = new Creator<OperateurTel>() {
        @Override
        public OperateurTel createFromParcel(Parcel in) {
            return new OperateurTel(in);
        }

        @Override
        public OperateurTel[] newArray(int size) {
            return new OperateurTel[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSolde() {
        return solde;
    }

    public void setSolde(String solde) {
        this.solde = solde;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(solde);
        dest.writeInt(imageId);
    }
}
