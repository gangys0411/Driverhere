package com.ninefives.driverhere.Map;

public class BusMapItem {
    private String NodeNm; // 정류장 이름
    private String NodeId; // 정류장 id
    private Double NodeLati; // 정류장 위도
    private Double NodeLong; // 정류장 경도

    public void setNodeNm(String nodenm) { // 버스 번호 설정
        NodeNm=nodenm;
    }
    public void setNodeId(String nodeid){ // 노선 id 설정
        NodeId=nodeid;
    }
    public void setNodeLati(Double nodelati){ // 기점 설정
        NodeLati = nodelati;
    }
    public void setNodeLong(Double nodelong) {
        NodeLong = nodelong;
    }

    public String getNodeNm(){ // 버스 번호 반환
        return this.NodeNm;
    }
    public String getNodeId(){ // 노선 id 반환
        return this.NodeId;
    }
    public Double getNodeLati(){ // 버스 번호 반환
        return this.NodeLati;
    }
    public Double getNodeLong(){ // 버스 번호 반환
        return this.NodeLong;
    }
}
