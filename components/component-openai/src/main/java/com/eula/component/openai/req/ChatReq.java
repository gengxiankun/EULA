package com.eula.component.openai.req;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ChatReq {

    /**
     * 如果不指定，每次请求会预先冻结 模型支持的最大个数 Token，如果是 3.5 也就是 41P ，在请求完成后按 usage 字段多退少补。如果账户余额少于 41P
     * 就会报错，此时可以指定 max_tokens，这样就会按 max_tokens 来冻结
     */
    private Long maxTokens;

    private List<MessageReq> messages;

    private String model;

    /**
     * 默认为 false，为 true 时会调用文本安全接口对内容进行判定，并将审核结果添加到返回值中的 moderation
     * 字段，开发者可以根据值自行判断如何处理。审核输出的详细解释：https://cloud.tencent.com/document/product/1124/51860 开启后每
     * 9000 字符会增加 10P 的消耗
     */
    private Boolean moderation;

    /**
     * 默认为 false，在 moderation 为 true 且自身也为 true 时，如果审核结果不是 Pass，将自动进行内容拦截，对流也生效
     */
    private Boolean moderationStop;

    /**
     * 默认为 false，为 true 时会尝试让 GPT 自己审查内容，不输出违规结果。由于 GPT
     * 的调性，效果好坏比较随机。总的来说对暴力、色情内容效果较好，政治类效果一般。开启后会每次访问会增加约 1P 的消耗
     */
    private Boolean safeMode;

    /**
     * 流方式返回，兼容官方参数，但因为有计费和审核逻辑，比官方流慢。
     */
    private Boolean stream;

}
