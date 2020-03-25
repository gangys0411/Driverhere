package com.ninefives.driverhere;

public class BusArriveItem {
    private String BusNo; // 버스 번호
    private String BusId; // 노선 id
    private String ArriveTime; // 도착까지 남은 시간
    private String RemainStation; // 남은 정류장 수

    public void setBusNo(String busno) { // 버스 번호 설정
        BusNo=busno;
    }
    public void setBusId(String busid){ // 노선 id 설정
        BusId=busid;
    }
    public void setArriveTime(String arriveTime){ // 기점 설정
        ArriveTime = arriveTime;
    }
    public void setRemainStation(String remainStation){ // 종점 설정
        RemainStation = remainStation;
    }

    public String getArriveTime(){ // 버스 번호 반환
        return this.ArriveTime;
    }
    public String getRemainStation(){ // 노선 id 반환
        return this.RemainStation;
    }
}
