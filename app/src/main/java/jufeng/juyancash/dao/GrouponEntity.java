package jufeng.juyancash.dao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import jufeng.juyancash.syncdata.GrouponVo;

/**
 * Entity mapped to table "GROUPON_ENTITY".
 */
public class GrouponEntity extends Payment implements Parcelable {

    private Long id;
    /** Not-null value. */
    private String grouponId;
    private String grouponName;
    private String grouponCode;

    public GrouponEntity() {
    }

    public GrouponEntity(Long id) {
        this.id = id;
    }

    public GrouponEntity(Long id, String grouponId, String grouponName, String grouponCode) {
        this.id = id;
        this.grouponId = grouponId;
        this.grouponName = grouponName;
        this.grouponCode = grouponCode;
        setName(grouponName);
        setPayId(grouponId);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /** Not-null value. */
    public String getGrouponId() {
        return grouponId;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setGrouponId(String grouponId) {
        this.grouponId = grouponId;
    }

    public String getGrouponName() {
        return grouponName;
    }

    public void setGrouponName(String grouponName) {
        this.grouponName = grouponName;
    }

    public String getGrouponCode() {
        return grouponCode;
    }

    public void setGrouponCode(String grouponCode) {
        this.grouponCode = grouponCode;
    }

    public GrouponEntity(GrouponVo grouponModel){
        this.grouponId = grouponModel.getId();
        this.grouponName = grouponModel.getName();
        this.grouponCode = grouponModel.getSettled()+"";
        Log.d("###", "团购网站："+toString());
    }

    @Override
    public String toString() {
        return "GrouponEntity{" +
                "id=" + id +
                ", grouponId='" + grouponId + '\'' +
                ", grouponName='" + grouponName + '\'' +
                ", grouponCode='" + grouponCode + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.grouponId);
        dest.writeString(this.grouponName);
        dest.writeString(this.grouponCode);
    }

    protected GrouponEntity(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.grouponId = in.readString();
        this.grouponName = in.readString();
        this.grouponCode = in.readString();
    }

    public static final Parcelable.Creator<GrouponEntity> CREATOR = new Parcelable.Creator<GrouponEntity>() {
        @Override
        public GrouponEntity createFromParcel(Parcel source) {
            return new GrouponEntity(source);
        }

        @Override
        public GrouponEntity[] newArray(int size) {
            return new GrouponEntity[size];
        }
    };
}
