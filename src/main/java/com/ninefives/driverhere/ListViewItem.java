package com.ninefives.driverhere;

public class ListViewItem {
    private int NodeNm; // 정류소 이름
    private int NodeId; // 정류소 id

    public void setNodeNm(int nodenm) { // 버스 번호 설정
        NodeNm=nodenm;
    }
    public void setNodeId(int nodeid){ // 노선 id 설정
        NodeId=nodeid;
    }

    public int getNodeNm(){ // 버스 번호 반환
        return this.NodeNm;
    }
    public int getNodeId(){ // 노선 id 반환
        return this.NodeId;
    }
}