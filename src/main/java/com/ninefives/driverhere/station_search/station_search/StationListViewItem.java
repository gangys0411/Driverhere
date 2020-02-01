package com.ninefives.driverhere.station_search.station_search;

public class StationListViewItem {
    private String NodeNm; // 정류소 이름
    private String NodeId; // 정류소 id

    public void setNodeNm(String nodenm) { // 버스 번호 설정
        NodeNm=nodenm;
    }
    public void setNodeId(String nodeid){ // 노선 id 설정
        NodeId=nodeid;
    }

    public String getNodeNm(){ // 버스 번호 반환
        return this.NodeNm;
    }
    public String getNodeId(){ // 노선 id 반환
        return this.NodeId;
    }
}
