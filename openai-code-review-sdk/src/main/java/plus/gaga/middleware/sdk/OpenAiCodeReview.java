package plus.gaga.middleware.sdk;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import plus.gaga.middleware.sdk.domain.service.impl.OpenAiCodeReviewService;
import plus.gaga.middleware.sdk.infrastructure.git.GitCommand;
import plus.gaga.middleware.sdk.infrastructure.openai.IOpenAI;
import plus.gaga.middleware.sdk.infrastructure.openai.impl.ChatGLM;
import plus.gaga.middleware.sdk.infrastructure.weixin.WeiXin;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class OpenAiCodeReview {

    private static final Logger logger = LoggerFactory.getLogger(OpenAiCodeReview.class);

    // 配置配置
    private String weixin_appid = "wx36ce3405a4d61ad2";
    private String weixin_secret = "97cb157b9bf62510f28b8f58a929722a";
    private String weixin_touser = "ofFSu6FntS0cxXuZnTEj6o10-hZc";
    private String weixin_template_id = "plNJBXbJPrBe1nt9_txDve_-TuuQpB4eBzxuVMdwJUA";

    // ChatGLM 配置
    private String chatglm_apiHost = "https://open.bigmodel.cn/api/paas/v4/chat/completions";
    private String chatglm_apiKeySecret = "";

    // Github 配置
    private String github_review_log_uri;
    private String github_token;

    // 工程配置 - 自动获取
    private String github_project;
    private String github_branch;
    private String github_author;

    public static void main(String[] args) throws Exception {

                 // 获取当前的日期和时间（默认是系统时区）
        LocalDateTime now = LocalDateTime.now();

        // 指定时区为北京时区，即UTC+8
        ZoneId beijingZone = ZoneId.of("Asia/Shanghai");

        // 将当前日期时间转换为北京时间
        ZonedDateTime beijingTime = now.atZone(beijingZone);

        // 定义时间格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
         // 格式化日期时间为时、分、秒
        String formattedTime = beijingTime.format(formatter);
        GitCommand gitCommand = new GitCommand(
                "",
                "",
                "openai",
                "master",
                "zouyp",
                formattedTime
        );


        /**
         * 项目：{{repo_name.DATA}} 分支：{{branch_name.DATA}} 作者：{{commit_author.DATA}} 说明：{{commit_message.DATA}}
         */
        WeiXin weiXin = new WeiXin(
                " ",
                " ",
                " -hZc",
                " "
        );


        IOpenAI openAI = new ChatGLM(" ", " ");

        OpenAiCodeReviewService openAiCodeReviewService = new OpenAiCodeReviewService(gitCommand, openAI, weiXin);
        openAiCodeReviewService.exec();

        logger.info("openai-code-review done!");
    }

    private static String getEnv(String key) {
        String value = System.getenv(key);
        if (null == value || value.isEmpty()) {
            throw new RuntimeException(key + " value is null");
        }
        return value;
    }

}
