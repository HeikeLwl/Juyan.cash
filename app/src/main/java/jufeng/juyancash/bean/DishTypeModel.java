package jufeng.juyancash.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator102 on 2016/9/27.
 */
public class DishTypeModel implements Parcelable {
    private String dishTypeModelId;
    private String dishTypeModelTypeName;
    private String dishTypeModelParentId;
    private int dishTypeModelHasParent;
    private int dishTypeModelType;

    public String getDishTypeModelId() {
        return dishTypeModelId;
    }

    public void setDishTypeModelId(String dishTypeModelId) {
        this.dishTypeModelId = dishTypeModelId;
    }

    public int getDishTypeModelHasParent() {
        return dishTypeModelHasParent;
    }

    public void setDishTypeModelHasParent(int dishTypeModelHasParent) {
        this.dishTypeModelHasParent = dishTypeModelHasParent;
    }

    public String getDishTypeModelParentId() {
        return dishTypeModelParentId;
    }

    public void setDishTypeModelParentId(String dishTypeModelParentId) {
        this.dishTypeModelParentId = dishTypeModelParentId;
    }

    public String getDishTypeModelTypeName() {
        return dishTypeModelTypeName;
    }

    public void setDishTypeModelTypeName(String dishTypeModelTypeName) {
        this.dishTypeModelTypeName = dishTypeModelTypeName;
    }

    public int getDishTypeModelType() {
        return dishTypeModelType;
    }

    public void setDishTypeModelType(int dishTypeModelType) {
        this.dishTypeModelType = dishTypeModelType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.dishTypeModelId);
        dest.writeString(this.dishTypeModelTypeName);
        dest.writeString(this.dishTypeModelParentId);
        dest.writeInt(this.dishTypeModelHasParent);
        dest.writeInt(this.dishTypeModelType);
    }

    public DishTypeModel() {
    }

    protected DishTypeModel(Parcel in) {
        this.dishTypeModelId = in.readString();
        this.dishTypeModelTypeName = in.readString();
        this.dishTypeModelParentId = in.readString();
        this.dishTypeModelHasParent = in.readInt();
        this.dishTypeModelType = in.readInt();
    }

    public static final Parcelable.Creator<DishTypeModel> CREATOR = new Parcelable.Creator<DishTypeModel>() {
        @Override
        public DishTypeModel createFromParcel(Parcel source) {
            return new DishTypeModel(source);
        }

        @Override
        public DishTypeModel[] newArray(int size) {
            return new DishTypeModel[size];
        }
    };
}
