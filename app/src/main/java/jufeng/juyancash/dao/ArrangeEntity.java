package jufeng.juyancash.dao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table "ARRANGE_ENTITY".
 */
public class ArrangeEntity {

    private Long id;
    /** Not-null value. */
    private String arrangeId;
    private Integer remainCount;
    private String tel;
    private String arrangeNumber;
    private Long signTime;
    private Integer mealPeople;
    private Integer arrangeStatus;

    public ArrangeEntity() {
    }

    public ArrangeEntity(Long id) {
        this.id = id;
    }

    public ArrangeEntity(Long id, String arrangeId, Integer remainCount, String tel, String arrangeNumber, Long signTime, Integer mealPeople, Integer arrangeStatus) {
        this.id = id;
        this.arrangeId = arrangeId;
        this.remainCount = remainCount;
        this.tel = tel;
        this.arrangeNumber = arrangeNumber;
        this.signTime = signTime;
        this.mealPeople = mealPeople;
        this.arrangeStatus = arrangeStatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /** Not-null value. */
    public String getArrangeId() {
        return arrangeId;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setArrangeId(String arrangeId) {
        this.arrangeId = arrangeId;
    }

    public Integer getRemainCount() {
        return remainCount;
    }

    public void setRemainCount(Integer remainCount) {
        this.remainCount = remainCount;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getArrangeNumber() {
        return arrangeNumber;
    }

    public void setArrangeNumber(String arrangeNumber) {
        this.arrangeNumber = arrangeNumber;
    }

    public Long getSignTime() {
        return signTime;
    }

    public void setSignTime(Long signTime) {
        this.signTime = signTime;
    }

    public Integer getMealPeople() {
        return mealPeople;
    }

    public void setMealPeople(Integer mealPeople) {
        this.mealPeople = mealPeople;
    }

    public Integer getArrangeStatus() {
        return arrangeStatus;
    }

    public void setArrangeStatus(Integer arrangeStatus) {
        this.arrangeStatus = arrangeStatus;
    }

}
