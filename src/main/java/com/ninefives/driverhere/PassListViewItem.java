package com.ninefives.driverhere;

public class PassListViewItem {
    private String BusNo; // 버스 번호
    private String BusId; // 노선 id

    public void setBusNo(String routeno) { // 버스 번호 설정
        BusNo=routeno;
    }
    public void setBusId(String routeid){ // 노선 id 설정
        BusId=routeid;
    }

    public String getBusNo(){ // 버스 번호 반환
        return this.BusNo;
    }
    public String getBusId(){ // 노선 id 반환
        return this.BusId;
    }
}