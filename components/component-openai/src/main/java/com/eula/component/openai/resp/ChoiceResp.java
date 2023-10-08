package com.eula.component.openai.resp;

import lombok.Data;

@Data
public class ChoiceResp {

    private MessageResp message;

    private String finishReason;

    private String index;

}
