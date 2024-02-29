package com.example.myapplication.HTTP.model;

import com.example.myapplication.HTTP.model.Message;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AiRequest {
    @SerializedName("model")
    @Expose
    private String model;
    @SerializedName("temperature")
    @Expose
    private Double temperature;
    @SerializedName("messages")
    @Expose
    private List<Message> messages;
    @SerializedName("max_tokens")
    @Expose
    private Integer maxTokens;
    @SerializedName("stop")
    @Expose
    private Object stop;
    @SerializedName("n")
    @Expose
    private Integer n;
    @SerializedName("top_p")
    @Expose
    private Double topP;

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public Integer getMaxTokens() {
        return maxTokens;
    }

    public void setMaxTokens(Integer maxTokens) {
        this.maxTokens = maxTokens;
    }

    public Object getStop() {
        return stop;
    }

    public void setStop(Object stop) {
        this.stop = stop;
    }

    public Integer getN() {
        return n;
    }

    public void setN(Integer n) {
        this.n = n;
    }

    public Double getTopP() {
        return topP;
    }

    public void setTopP(Double topP) {
        this.topP = topP;
    }

    public AiRequest(String model, Double temperature, List<Message> messages, Integer maxTokens, Object stop, Integer n, Double topP) {
        this.model = model;
        this.temperature = temperature;
        this.messages = messages;
        this.maxTokens = maxTokens;
        this.stop = stop;
        this.n = n;
        this.topP = topP;
    }
}