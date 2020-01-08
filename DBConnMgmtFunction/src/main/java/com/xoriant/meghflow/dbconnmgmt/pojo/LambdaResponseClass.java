package com.xoriant.meghflow.dbconnmgmt.pojo;

public class LambdaResponseClass {

    public String responseCode;
    public String responseText;

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseText() {
        return responseText;
    }

    public void setResponseText(String responseText) {
        this.responseText = responseText;
    }

    public LambdaResponseClass(String responseCode, String responseText) {
        this.responseCode = responseCode;
        this.responseText = responseText;
    }

    public LambdaResponseClass() {
    }
}
