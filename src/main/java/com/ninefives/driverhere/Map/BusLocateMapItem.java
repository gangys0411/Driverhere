package com.ninefives.driverhere.Map;

public class BusLocateMapItem {
    private String VehicleNo; // 정류장 이름
    private Double BusLati; // 정류장 위도
    private Double BusLong; // 정류장 경도

    public void setVehicleNo(String vehicleNo){ // 노선 id 설정
        VehicleNo=vehicleNo;
    }
    public void setBusLati(Double buslati){ // 기점 설정
        BusLati = buslati;
    }
    public void setBusLong(Double buslong) {
        BusLong = buslong;
    }

    public String getVehicleNo(){ // 노선 id 반환
        return this.VehicleNo;
    }
    public Double getBusLati(){ // 버스 번호 반환
        return this.BusLati;
    }
    public Double getBusLong(){ // 버스 번호 반환
        return this.BusLong;
    }
}
