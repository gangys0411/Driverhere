package com.ninefives.driverhere;

public class PersonalData {
    private String busid; // ID
    private String stationid; // 이름
    private String order; // 지역

    public String getBusid() {
        return busid;
    }
    public String getStationid() {
        return stationid;
    }
    public String getOrder() {
        return order;
    }

    public void setBusid(String busid) {
        this.busid = busid;
    }
    public void setStationid(String stationid) {
        this.stationid = stationid;
    }
    public void setOrder(String order) {
        this.order = order;
    }
}
