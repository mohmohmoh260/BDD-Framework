package builds.utilities;

public class Result {

    public static ThreadLocal<Boolean> success = ThreadLocal.withInitial(() -> Boolean.TRUE);
    public static ThreadLocal<String> message = ThreadLocal.withInitial(() -> "");

    public void setSuccess(Boolean success){
        Result.success.set(success);
    }
    public Boolean getSuccess() {
        return success.get();
    }

    public void setMessage(String message){
        Result.message.set(message);
    }

    public String getMessage() {
        return message.get();
    }

    public void statusCheck(){
        if(!getSuccess()){
            throw new RuntimeException(getMessage());
        }
    }
}