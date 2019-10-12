package com.ninefives.driverhere;

public class ListViewItem {
    private String BusNo;
    private String BusId;

    public void setBusNo(String routeno) {
        BusNo=routeno;
    }
    public void setBusId(String routeid){
        BusId=routeid;
    }

    public String getBusNo(){
        return this.BusNo;
    }
    public String getBusId(){
        return this.BusId;
    }
}