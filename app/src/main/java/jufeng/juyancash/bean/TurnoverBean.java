package jufeng.juyancash.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator102 on 2016/10/5.
 */

public class TurnoverBean implements Parcelable {
    private String partnerCode;
    private String employeeId;
    private String type;
    private String ts;

    public TurnoverBean(){

    }

    public TurnoverBean(String param){
        String[] params = param.split("`");
        this.partnerCode = params[0];
        this.employeeId = params[1];
        this.type = params[2];
        this.ts = params[3];
    }

    public String getPartnerCode() {
        return partnerCode;
    }

    public void setPartnerCode(String partnerCode) {
        this.partnerCode = partnerCode;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTs() {
        return ts;
    }

    public void setTs(String ts) {
        this.ts = ts;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.partnerCode);
        dest.writeString(this.employeeId);
        dest.writeString(this.type);
        dest.writeString(this.ts);
    }

    protected TurnoverBean(Parcel in) {
        this.partnerCode = in.readString();
        this.employeeId = in.readString();
        this.type = in.readString();
        this.ts = in.readString();
    }

    public static final Parcelable.Creator<TurnoverBean> CREATOR = new Parcelable.Creator<TurnoverBean>() {
        @Override
        public TurnoverBean createFromParcel(Parcel source) {
            return new TurnoverBean(source);
        }

        @Override
        public TurnoverBean[] newArray(int size) {
            return new TurnoverBean[size];
        }
    };
}
