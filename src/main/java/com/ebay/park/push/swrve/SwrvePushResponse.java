package com.ebay.park.push.swrve;

/**
 * A Swrve push response, for example:
 * <pre>
 * {
 *   "code": 200,
 *   "message": "OK"
 * }
 * </pre>
 * More information: https://docs.swrve.com/swrves-apis/api-guides/swrve-push-api-guide/
 *
 * @author gervasio.amy
 * @since 14/12/2016.
 */
public class SwrvePushResponse {

    private Integer code;

    private String message;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{ code: ")
                .append(code)
                .append(", message: '")
                .append(message)
                .append("' }");
        return sb.toString();
    }
}
