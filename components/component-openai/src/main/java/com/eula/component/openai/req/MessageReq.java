package com.eula.component.openai.req;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MessageReq {

    private String role;

    private String content;

}
