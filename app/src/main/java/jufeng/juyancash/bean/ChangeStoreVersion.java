package jufeng.juyancash.bean;

/**
 * Created by Administrator102 on 2017/3/27.
 */

public class ChangeStoreVersion {
    private int version;
    private String partnerCode;

    public ChangeStoreVersion(int version, String partnerCode) {
        this.version = version;
        this.partnerCode = partnerCode;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getPartnerCode() {
        return partnerCode;
    }

    public void setPartnerCode(String partnerCode) {
        this.partnerCode = partnerCode;
    }
}
