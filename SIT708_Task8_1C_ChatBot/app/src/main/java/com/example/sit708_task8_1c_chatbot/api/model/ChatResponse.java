package com.example.sit708_task8_1c_chatbot.api.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ChatResponse {
    @SerializedName("choices")
    private List<Choice> choices;
    
    @SerializedName("error")
    private Error error;
    
    public List<Choice> getChoices() {
        return choices;
    }
    
    public void setChoices(List<Choice> choices) {
        this.choices = choices;
    }
    
    public Error getError() {
        return error;
    }
    
    public void setError(Error error) {
        this.error = error;
    }
    
    public static class Choice {
        @SerializedName("message")
        private Message message;
        
        @SerializedName("finish_reason")
        private String finishReason;
        
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
    
    public static class Message {
        @SerializedName("role")
        private String role;
        
        @SerializedName("content")
        private String content;
        
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
    
    public static class Error {
        @SerializedName("message")
        private String message;
        
        @SerializedName("code")
        private int code;
        
        public String getMessage() {
            return message;
        }
        
        public void setMessage(String message) {
            this.message = message;
        }
        
        public int getCode() {
            return code;
        }
        
        public void setCode(int code) {
            this.code = code;
        }
    }
}
