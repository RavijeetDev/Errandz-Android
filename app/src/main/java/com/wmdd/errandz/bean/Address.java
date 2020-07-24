package com.wmdd.errandz.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class Address implements Parcelable {

    private int addressID;
    private String fullAddress;
    private String streetAddress;
    private String city;
    private String province;
    private String postalCode;
    private String country;
    private String latitude;
    private String longitude;

    public Address() {

    }

    protected Address(Parcel in) {
        addressID = in.readInt();
        fullAddress = in.readString();
        streetAddress = in.readString();
        city = in.readString();
        province = in.readString();
        postalCode = in.readString();
        country = in.readString();
        latitude = in.readString();
        longitude = in.readString();
    }

    public static final Creator<Address> CREATOR = new Creator<Address>() {
        @Override
        public Address createFromParcel(Parcel in) {
            return new Address(in);
        }

        @Override
        public Address[] newArray(int size) {
            return new Address[size];
        }
    };

    public int getAddressID() {
        return addressID;
    }

    public void setAddressID(int addressID) {
        this.addressID = addressID;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(addressID);
        dest.writeString(fullAddress);
        dest.writeString(streetAddress);
        dest.writeString(city);
        dest.writeString(province);
        dest.writeString(postalCode);
        dest.writeString(country);
        dest.writeString(latitude);
        dest.writeString(longitude);
    }
}
