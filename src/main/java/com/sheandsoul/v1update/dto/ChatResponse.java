package com.sheandsoul.v1update.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class ChatResponse {

    private String response;

    public ChatResponse(String response) {
        this.response = response;
    }

    

}
