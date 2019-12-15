package com.ninefives.driverhere;

public class PassListViewItem {
    private String BusNo; // 버스 번호
    private String BusId; // 노선 id
    private String Start_Station; // 기점
    private String End_Station; // 종점

    public void setBusNo(String busno) { // 버스 번호 설정
        BusNo=busno;
    }
    public void setBusId(String busid){ // 노선 id 설정
        BusId=busid;
    }
    public void setStart_Station(String start_station){ // 기점 설정
        Start_Station = start_station;
    }
    public void setEnd_Station(String end_station){ // 종점 설정
        End_Station = end_station;
    }

    public String getBusNo(){ // 버스 번호 반환
        return this.BusNo;
    }
    public String getBusId(){ // 노선 id 반환
        return this.BusId;
    }
    public String getDirection(){ // 버스 방향 반환
        return this.Start_Station + " --> " + this.End_Station;
    }
}