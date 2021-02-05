package com.extract;

public class ExtractResult {

    private int count;
    private int successCount;
    private int failCount;
    private StringBuffer outputTextArea;
    private String errorMsg;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(int successCount) {
        this.successCount = successCount;
    }

    public int getFailCount() {
        return failCount;
    }

    public void setFailCount(int failCount) {
        this.failCount = failCount;
    }

    public StringBuffer getOutputTextArea() {
        return outputTextArea;
    }

    public void setOutputTextArea(StringBuffer outputTextArea) {
        this.outputTextArea = outputTextArea;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    @Override
    public String toString() {
        return "ExtractResult{" +
                "count=" + count +
                ", successCount=" + successCount +
                ", failCount=" + failCount +
                ", outputTextArea=" + outputTextArea +
                ", errorMsg='" + errorMsg + '\'' +
                '}';
    }
}
