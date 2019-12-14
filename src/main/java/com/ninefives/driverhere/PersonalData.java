package com.ninefives.driverhere;

public class PersonalData {
    private String member_id; // ID
    private String member_name; // 이름
    private String member_country; // 지역

    public String getMember_id() {
        return member_id;
    }
    public String getMember_name() {
        return member_name;
    }
    public String getMember_country() {
        return member_country;
    }

    public void setBusid(String member_id) {
        this.member_id = member_id;
    }
    public void setStationid(String member_name) {
        this.member_name = member_name;
    }
    public void setOrder(String member_country) {
        this.member_country = member_country;
    }
}
