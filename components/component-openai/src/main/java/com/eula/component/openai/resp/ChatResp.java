package com.eula.component.openai.resp;

import com.eula.component.openai.enums.Model;
import lombok.Data;

@Data
public class ChatResp {

    private String id;

    private String object;

    private String created;

    private Model model;

    private ChoiceResp[] choices;

}
