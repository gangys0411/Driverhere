package com.ninefives.driverhere;

public class Item {

    String startnodenm; // 기점
    String endnodenm; // 종점
    String startvehicletime; // 첫차 시간
    String endvehicletime; // 막차 시간
    String routeno; // 노선 번호
    String routeid; // 노선 id
    String routetp; // 노선 타입

    public String getStartnodenm(){ // 기점 정보 반환하기
        return startnodenm;
    }
    public String getEndnodenm(){ // 종점 정보 반환하기
        return endnodenm;
    }
    public String getStartvehicletime(){ // 첫차 시간 반환하기
        return startvehicletime;
    }
    public String getEndvehicletime(){ // 막차 시간 반환하기
        return endvehicletime;
    }
    public String getRouteid(){ // 노선 id 반환하기
        return routeid;
    }
    public String getRouteno(){ // 노선 번호 반환하기
        return routeno;
    }
    public String getRoutetp(){ // 노선 타입 반환하기
        return routetp;
    }

    public void setStartnodenm(String startnodenm){
        this.startnodenm = startnodenm;
    }
    public void setEndnodenm(String endnodenm){
        this.endnodenm = endnodenm;
    }
    public void setStartvehicletime(String startvehicletime){
        this.startvehicletime = startvehicletime;
    }
    public void setEndvehicletime(String endvehicletime){
        this.endvehicletime = endvehicletime;
    }
    public void setRouteid(String routeid){
        this.routeid = routeid;
    }
    public void setRouteno(String routeno){
        this.routeno = routeno;
    }
    public void setRoutetp(String routetp){
        this.routetp = routetp;
    }
}
