package plus.gaga.middleware.sdk;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import plus.gaga.middleware.sdk.domain.service.impl.OpenAiCodeReviewService;
import plus.gaga.middleware.sdk.infrastructure.git.GitCommand;
import plus.gaga.middleware.sdk.infrastructure.openai.IOpenAI;
import plus.gaga.middleware.sdk.infrastructure.openai.impl.ChatGLM;
import plus.gaga.middleware.sdk.infrastructure.weixin.WeiXin;

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
        GitCommand gitCommand = new GitCommand(
                "https://github.com/zouyunpeng666/openai-code-review-log",
                getEnv("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"),
                getEnv("COMMIT_PROJECT"),
                getEnv("COMMIT_BRANCH"),
                getEnv("COMMIT_AUTHOR"),
                getEnv("COMMIT_MESSAGE")
        );


        /**
         * 项目：{{repo_name.DATA}} 分支：{{branch_name.DATA}} 作者：{{commit_author.DATA}} 说明：{{commit_message.DATA}}
         */
        WeiXin weiXin = new WeiXin(
                getEnv("wx36ce3405a4d61ad2"),
                getEnv("97cb157b9bf62510f28b8f58a929722a"),
                getEnv("ofFSu6FntS0cxXuZnTEj6o10-hZc"),
                getEnv("plNJBXbJPrBe1nt9_txDve_-TuuQpB4eBzxuVMdwJUA")
        );




        IOpenAI openAI = new ChatGLM(getEnv("https://open.bigmodel.cn/api/paas/v4/chat/completions"), getEnv("3b1359bf7ba3eed763ec9556424eb2d3.1pkaFKfTTf6HCrAP"));

        OpenAiCodeReviewService openAiCodeReviewService = new OpenAiCodeReviewService(gitCommand, openAI, weiXin);
        openAiCodeReviewService.exec();

        logger.info("openai-code-review done!");
    }

    private static String getEnv(String key) {
        String value = System.getenv(key);
        if (null == value || value.isEmpty()) {
            throw new RuntimeException(key+" value is null");
        }
        return value;
    }

}
