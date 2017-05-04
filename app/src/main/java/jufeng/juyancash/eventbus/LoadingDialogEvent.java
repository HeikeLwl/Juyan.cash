package jufeng.juyancash.eventbus;

/**
 * Created by Administrator102 on 2017/3/10.
 */

public class LoadingDialogEvent {
    private boolean isShow;
    private String message;

    public LoadingDialogEvent(boolean isShow, String message) {
        this.isShow = isShow;
        this.message = message;
    }

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
