package jufeng.juyancash.dao;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator102 on 2016/9/21.
 */
public class Payment implements Parcelable {
    public String name;
    private String mPayId;
    private int type;

    public String getName(){
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPayId() {
        return mPayId;
    }

    public void setPayId(String payId) {
        mPayId = payId;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.mPayId);
        dest.writeInt(this.type);
    }

    public Payment() {
    }

    protected Payment(Parcel in) {
        this.name = in.readString();
        this.mPayId = in.readString();
        this.type = in.readInt();
    }

    public static final Parcelable.Creator<Payment> CREATOR = new Parcelable.Creator<Payment>() {
        @Override
        public Payment createFromParcel(Parcel source) {
            return new Payment(source);
        }

        @Override
        public Payment[] newArray(int size) {
            return new Payment[size];
        }
    };
}
