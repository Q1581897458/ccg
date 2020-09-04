package com.mr.config;

public class ApliPayConfig {

    //应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
    public static final String APP_ID = "2016102100731440";
    //应用私钥
    public static final String MERCHANT_PRIVATE_KEY = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCf5dz9Uyjs4sTiLluCwV+536sL5QcYtoLGV7KHaSUO70he7BryVfnox73GjrdjX92SiIz2Te/4JVkeOMFZg9ilHnXpquTwGmTe1s290igRhpT7g6aDFOcPTqlJ+qCNkOhRDRff+JNFpAZ8ceDfWnCTjpyR+6D6yX37PDRYOEOrvCx8W3OEpXOidJNrZ9fZopahbmYQ1LQUT/5kXGIYKnRdxTBmiwM0l/O+LQ7t9fNg2wNfjXOo+uD3APlhFNWk6oQaQ1ReYR0oS5MSL49x/kCnmYvmz1jmFJrJNMwK2FP3IOJ21hpLMU6gfD2vlGPJ5jYC/BnIEHTUGF5dSw9ykw55AgMBAAECggEARZzZXSB7TMZdWOs9w+tUNaP+36rSMBwlSxybCQDptVrPcyPvh1mO950eLfesN4Ng/46bcA/2TV5VMicbZlFUXG6qfxR7n1yeVbXG1MFN2HNzIRzrvV24Sp3AbgjDJV7VBZRhtIyP8V27xSn+8L57DIKl/5hBOW3Hfz1yvIWiUH1nArM50hg2zMS3jH+U3LIaYFtIb3SYhunidcsJFlXKVteDehrcIKkB3nfoCWtZdNVxxdt+KV3yNjEeFAC+TsUswNgcQS6BIaPpCqg3696YhK6Ibbr2Zt6wFGlTnObeMi/dsl9UPbIT945JlujwirWcvSN8t35EkLUNuKXtczVGAQKBgQDfri34oqKJb77ylMzaUWJEXoF8NCwTEv7e2Pybry/PSB+iXG3/E1AzJgydUmj5x70mykrk8y31i/9DFZmUAGUDwF2iTkDlESla1aQQvD7gMuVgCHRyGlqYukoNoF7pg2yg/tz/5t2eaxl58jv4o4+/7cM9/MtljLQYyUYIaYk1CQKBgQC3AGcmWeZpGDozizWXGd1GScP02v+gE5AiBhEmsHsowe+Jyj6yRTINZwzA+714yfjfPsE/UJdN7vp+8XH552NpHM42xVfc7f6zbBZUEokJW8Tiy43vbcIOhR/cLymbCXPe97sG0qqZC43kv0vvFTL4sjNsL84xa0RFvgynMzhZ8QKBgQDUbXb7SnlSTO3rM6XZMLFCfP48FtBEzSWAPlKpCBc/pB9m8qEEPjNMYDZasok/rpHU5Ql7pfdf2zTxYPIZvMYyad6C5g57fXDo8zx9KEoOYke0jFMKijzADyuFBWkeG9Juk50O6xJOASiQx5xs5ej/Pa7Oj+Ya6yWe5Ie+NXw5AQKBgDQWm0WhRSIY0UgmkaRmqkL9cBdb1nLb3qSwkWUvyn55ZRMVS+82Ht8Nu+WaQf4Fjx8MH7lD2S1HnmlPlB+LqxIaLuAMH8w0udCcLWbUt8jAJDRhuA60cQ3s+nqTugX1FNQcM1Hn2MMjNMotYRG2OQHsxxI+MTSD0bUAOV3GCH4hAoGBAKwo/pg8HyP0jfecpil+tOEJDfvCTMtwDVCE/NkVIYoZeIA6z/YeliZZ1rQcPmofCp2VoZzM8u2mRwiPyJAuy1UKJoD12/kc67Yo4SZ1P1FTU5N7FY6hzMCPSoffyEwPvDgJSXtdfOaIOs7QC6g8shn/U4VHQp2FQwpSMF7A+kbH";
    //支付宝公钥
    public static final String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEArDStBHexPe4N/P3w0HuB7ryISAxeR+l97rW8ZEaeMsStcmwCsc7w9+7b5kBxY2EBkcguSQYc91B4WUCYNRtzbJkBmzm+wxuJ2DA4/Wg2qAYfpXQ13318KwLd754iCg587AqQKKODs5Jim91mmK/bkw5VaYgAYrjEUOyYcE1gp0gTHTEGc3XGijEnnCiZLdI9bDNMLb66NLHotwO8UR/C/aKNBR/Mc5gc1ICdDLu14uct6h6i1oPGM8IrAyuQ1kkH66lxH7XwTaSUuNprr573uwLu38mF9TTaAr5EE9nxyonH/QJnhN/5YoSfWEIVjEgBzFtYMVuyf6GFEuIUU0NGCwIDAQAB";
    //通知的回调地址 写一个确实支付成功改订单状态等，//必须为公网地址，
    public static final String NOTIFY_URL = "http://127.0.0.1:8089/paySuccess";
    //用户支付完成回调地址//可以写内网地址
    public static final String RETURN_URL = "http://127.0.0.1:8089/callback";
    // 签名方式
    public static String SIGN_TYPE = "RSA2";
    // 字符编码格式
    public static String CHARSET = "gbk";
    // 支付宝网关
    public static String GATEWAYURL = "https://openapi.alipaydev.com/gateway.do";

}
