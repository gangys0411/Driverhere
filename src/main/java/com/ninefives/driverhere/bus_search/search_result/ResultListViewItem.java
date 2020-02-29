package com.ninefives.driverhere.bus_search.search_result;

public class ResultListViewItem {
    private String NodeNm; // 정류소 이름
    private String NodeId; // 정류소 id
    private int NodeOrd; // 정류소 순서

    public void setNodeNm(String nodenm) { // 버스 번호 설정
        NodeNm=nodenm;
    }
    public void setNodeId(String nodeid){ // 노선 id 설정
        NodeId=nodeid;
    }
    public void setNodeOrd(int nodeord){ // 정류소 순서 설정
        NodeOrd=nodeord;
    }

    public String getNodeNm(){ // 버스 번호 반환
        return this.NodeNm;
    }
    public String getNodeId(){ // 노선 id 반환
        return this.NodeId;
    }
    public int getNodeOrd(){ // 정류소 순서 반환
        return this.NodeOrd;
    }
}