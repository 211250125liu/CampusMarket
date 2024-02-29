package com.example.myapplication.HTTP.gson;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AiGsonData {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("object")
    @Expose
    private String object;
    @SerializedName("created")
    @Expose
    private Integer created;
    @SerializedName("model")
    @Expose
    private String model;
    @SerializedName("choices")
    @Expose
    private List<Choice> choices;
    @SerializedName("usage")
    @Expose
    private Usage usage;

    /**
     * No args constructor for use in serialization
     *
     */
    public AiGsonData() {
    }

    /**
     *
     * @param created
     * @param usage
     * @param model
     * @param id
     * @param choices
     * @param object
     */
    public AiGsonData(String id, String object, Integer created, String model, List<Choice> choices, Usage usage) {
        super();
        this.id = id;
        this.object = object;
        this.created = created;
        this.model = model;
        this.choices = choices;
        this.usage = usage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public Integer getCreated() {
        return created;
    }

    public void setCreated(Integer created) {
        this.created = created;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public List<Choice> getChoices() {
        return choices;
    }

    public void setChoices(List<Choice> choices) {
        this.choices = choices;
    }

    public Usage getUsage() {
        return usage;
    }

    public void setUsage(Usage usage) {
        this.usage = usage;
    }

    public String getMessage(){
        return this.choices.get(0).getMessage().getContent();
    }
}

class Message {

    @SerializedName("role")
    @Expose
    private String role;
    @SerializedName("content")
    @Expose
    private String content;

    /**
     * No args constructor for use in serialization
     *
     */
    public Message() {
    }

    /**
     *
     * @param role
     * @param content
     */
    public Message(String role, String content) {
        super();
        this.role = role;
        this.content = content;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

class Choice {

    @SerializedName("index")
    @Expose
    private Integer index;
    @SerializedName("message")
    @Expose
    private Message message;
    @SerializedName("finish_reason")
    @Expose
    private String finishReason;

    /**
     * No args constructor for use in serialization
     *
     */
    public Choice() {
    }

    /**
     *
     * @param finishReason
     * @param index
     * @param message
     */
    public Choice(Integer index, Message message, String finishReason) {
        super();
        this.index = index;
        this.message = message;
        this.finishReason = finishReason;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public String getFinishReason() {
        return finishReason;
    }

    public void setFinishReason(String finishReason) {
        this.finishReason = finishReason;
    }

}

class Usage {

    @SerializedName("prompt_tokens")
    @Expose
    private Integer promptTokens;
    @SerializedName("total_tokens")
    @Expose
    private Integer totalTokens;
    @SerializedName("completion_tokens")
    @Expose
    private Integer completionTokens;

    /**
     * No args constructor for use in serialization
     *
     */
    public Usage() {
    }

    /**
     *
     * @param promptTokens
     * @param totalTokens
     * @param completionTokens
     */
    public Usage(Integer promptTokens, Integer totalTokens, Integer completionTokens) {
        super();
        this.promptTokens = promptTokens;
        this.totalTokens = totalTokens;
        this.completionTokens = completionTokens;
    }

    public Integer getPromptTokens() {
        return promptTokens;
    }

    public void setPromptTokens(Integer promptTokens) {
        this.promptTokens = promptTokens;
    }

    public Integer getTotalTokens() {
        return totalTokens;
    }

    public void setTotalTokens(Integer totalTokens) {
        this.totalTokens = totalTokens;
    }

    public Integer getCompletionTokens() {
        return completionTokens;
    }

    public void setCompletionTokens(Integer completionTokens) {
        this.completionTokens = completionTokens;
    }

}