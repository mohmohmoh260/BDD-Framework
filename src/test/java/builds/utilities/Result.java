package builds.utilities;

public class Result {

    private static final ThreadLocal<Boolean> success = ThreadLocal.withInitial(() -> Boolean.TRUE);
    private static final ThreadLocal<Exception> exception = ThreadLocal.withInitial(() -> null);

    public void setSuccess(Boolean success){
        Result.success.set(success);
    }
    public Boolean getSuccess() {
        return success.get();
    }

    public void setException(Exception message){
        Result.exception.set(message);
    }

    public Exception getException() {
        return exception.get();
    }

    public void statusCheck(){
        if(!getSuccess()){
            throw new RuntimeException(getException());
        }
    }
}