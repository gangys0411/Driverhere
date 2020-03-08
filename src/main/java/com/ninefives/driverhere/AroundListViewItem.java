package com.ninefives.driverhere;

public class AroundListViewItem {
    private String NodeNm; // 정류소 이름
    private String NodeId; // 정류소 id

    public void setNodeNm(String nodeNm) { // 정류소 이름 설정
        NodeNm=nodeNm;
    }
    public void setNodeId(String nodeId){ // 정류소 id 설정
        NodeId=nodeId;
    }

    public String getNodeNm(){ // 정류소 이름 반환
        return this.NodeNm;
    }
    public String getNodeId(){ // 정류소 id 반환
        return this.NodeId;
    }
}