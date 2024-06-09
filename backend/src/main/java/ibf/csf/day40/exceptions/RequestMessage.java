package ibf.csf.day40.exceptions;

import jakarta.json.Json;

public class RequestMessage {
    
    private String errMsg;

    private String toJsonSting(String errMsg) {
        return Json.createObjectBuilder()
            .add("message", errMsg)
            .build()
            .toString();
    
    }

    

    public RequestMessage(String errMsg) {
        this.errMsg = errMsg;
    }



    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    @Override
    public String toString() {
        return toJsonSting(errMsg);
    }

    
}
