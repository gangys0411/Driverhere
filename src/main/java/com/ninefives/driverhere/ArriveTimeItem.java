package com.ninefives.driverhere;

public class ArriveTimeItem {
    private String Remain_Station; // 남은 정류장 수
    private String Arrive_Time; // 도착까지 남은 시간
    private String RouteNo; // 도착까지 남은 시간

    public void setRemain_Station(String remain_station){ // 종점 설정
        Remain_Station = remain_station;
    }
    public void setRemain_Time(String arrive_time){ // 종점 설정
        Arrive_Time = arrive_time;
    }
    public void setRouteNo(String routeNo){ // 종점 설정
        RouteNo = routeNo;
    }

    public String getRemain_Station(){ // 노선 id 반환
        return this.Remain_Station;
    }
    public String getRemain_Time(){ // 노선 id 반환
        return this.Arrive_Time;
    }
    public String getRouteNo(){ // 노선 id 반환
        return this.RouteNo;
    }
}