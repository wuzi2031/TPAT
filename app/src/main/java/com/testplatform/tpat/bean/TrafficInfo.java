package com.testplatform.tpat.bean;

public class TrafficInfo {
   private String packname;
   private Long rxFlow;
   private Long txFlow;

    public String getPackname() {
        return packname;
    }

    public void setPackname(String packname) {
        this.packname = packname;
    }

    public Long getRxFlow() {
        return rxFlow;
    }

    public void setRxFlow(Long rxFlow) {
        this.rxFlow = rxFlow;
    }

    public Long getTxFlow() {
        return txFlow;
    }

    public void setTxFlow(Long txFlow) {
        this.txFlow = txFlow;
    }
}
