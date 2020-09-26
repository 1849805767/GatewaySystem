package com.example.processingdisplay;

public class DataSet {
    private String dataType;
    private int dataBit;
    private int dataThrMax;
    private int getDataThrMin;
    public DataSet(){
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public int getDataBit() {
        return dataBit;
    }

    public void setDataBit(int dataBit) {
        this.dataBit = dataBit;
    }

    public int getDataThrMax() {
        return dataThrMax;
    }

    public void setDataThrMax(int dataThrMax) {
        this.dataThrMax = dataThrMax;
    }

    public int getDataThrMin() {
        return getDataThrMin;
    }

    public void setDataThrMin(int getDataThrMin) {
        this.getDataThrMin = getDataThrMin;
    }
}
