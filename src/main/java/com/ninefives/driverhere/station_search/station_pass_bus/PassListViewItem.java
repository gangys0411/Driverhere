package com.ninefives.driverhere.station_search.station_pass_bus;

public class PassListViewItem {
    private String BusNo; // 버스 번호
    private String BusId; // 노선 id
    private String Start_Station; // 기점
    private String End_Station; // 종점
    private String Remain_Station = "정보 없음"; // 남은 정류장 수
    private String Arrive_Time = "정보 없음"; // 도착까지 남은 시간

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
    public void setRemain_Station(String remain_station){ // 종점 설정
        Remain_Station = remain_station;
    }
    public void setArrive_Time(String arrive_time){ // 종점 설정
        Arrive_Time = arrive_time;
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
    public String getRemain_Station(){ // 노선 id 반환
        return this.Remain_Station;
    }
    public String getArrive_Time(){ // 노선 id 반환
        return this.Arrive_Time;
    }
}