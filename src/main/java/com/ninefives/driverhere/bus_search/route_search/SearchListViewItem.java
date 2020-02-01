package com.ninefives.driverhere.bus_search.route_search;

public class SearchListViewItem {
    private String BusNo; // 버스 번호
    private String BusId; // 노선 id
    private String StartNode; // 기점
    private String EndNode; // 종점

    public void setBusNo(String routeno) { // 버스 번호 설정
        BusNo=routeno;
    }
    public void setBusId(String routeid){ // 노선 id 설정
        BusId=routeid;
    }
    public void setStartNode(String startnodenm){ // 기점 설정
        StartNode = startnodenm;
    }
    public void setEndNode(String endnodenm){ // 종점 설정
        EndNode = endnodenm;
    }

    public String getBusNo(){ // 버스 번호 반환
        return this.BusNo;
    }
    public String getBusId(){ // 노선 id 반환
        return this.BusId;
    }
    public String getDirection(){ // 버스 방향 반환
        return this.StartNode + " --> " + this.EndNode;
    }
}