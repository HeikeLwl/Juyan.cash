package jufeng.juyancash.dao;

/**
 * Created by Administrator102 on 2016/9/22.
 */
public abstract class BillPerson {
    private String name;
    private String personId;

    public String getName(){
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getPersonId() {
        return personId;
    }
}
