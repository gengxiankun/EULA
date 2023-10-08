package com.eula.component.openai;

import cn.hutool.json.JSONUtil;
import com.eula.component.openai.builder.MessagesBuilder;
import com.eula.component.openai.headers.Header;
import com.eula.component.openai.resp.ChatResp;
import com.eula.component.openai.enums.Model;
import com.eula.component.openai.req.ChatReq;
import com.eula.component.openai.req.MessageReq;
import lombok.Data;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Data
public class OpenaiTemplate {

    private static final Logger logger = LoggerFactory.getLogger(OpenaiTemplate.class);

    private OpenaiProperties properties;

    private OkHttpClient client;

    public OkHttpClient getClient() {
        if (this.client == null) {
            OkHttpClient.Builder builder = new OkHttpClient().newBuilder();

            // 设置读取超时
            builder.readTimeout(this.properties.getReadTimeout(), TimeUnit.SECONDS);
            // 设置连接超时
            builder.connectTimeout(this.properties.getConnectTimeout(), TimeUnit.SECONDS);
            // 设置写入超时
            builder.writeTimeout(this.properties.getWriteTimeout(), TimeUnit.SECONDS);

            this.client = builder.build();
        }
        return this.client;
    }

    public ChatResp chat(String content) {
        List<MessageReq> messages = new MessagesBuilder().ask(content).build();
        return this.chat(messages);
    }

    public ChatResp chat(List<MessageReq> messages) {
        return this.chat(this.properties.getUrl(), this.properties.getModel(), messages);
    }

    public ChatResp chat(String url, Model model, List<MessageReq> messages) {
        ChatReq chatReq = ChatReq.builder().model(model.toValue()).messages(messages).build();
        String body = JSONUtil.toJsonStr(chatReq);
        String result = this.post(url, body);
        return JSONUtil.toBean(result, ChatResp.class);
    }

    public String post(String url, String body) {
        try {
            Header header = new Header(this.properties.getKey(), "application/json");
            MediaType mediaType = MediaType.parse(header.getContentType());
            RequestBody requestBody = RequestBody.create(mediaType, body);
            Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .addHeader("Authorization", header.getAuthorization())
                    .addHeader("Content-Type", header.getContentType())
                    .build();
            Response response = this.getClient().newCall(request).execute();

            if (response.isSuccessful() && Objects.nonNull(response.body())) {
                String result = response.body().string();
                logger.info("执行 post 请求成功，url：{}，返回数据：{}", url, result);
                return result;
            }
        } catch (Exception e) {
            logger.error("执行 post 请求失败，url：{}", url, e);
        }

        return null;
    }

}
