package jufeng.juyancash.eventbus;

/**
 * Created by Administrator102 on 2017/3/10.
 */

public class CouponDialogCloseEvent {
    private boolean isShowMessage;
    private String message;
    private String title;

    public CouponDialogCloseEvent(){
        this.isShowMessage = false;
        this.message = null;
        this.title = null;
    }

    public CouponDialogCloseEvent(boolean isShowMessage, String message, String title) {
        this.isShowMessage = isShowMessage;
        this.message = message;
        this.title = title;
    }

    public boolean isShowMessage() {
        return isShowMessage;
    }

    public void setShowMessage(boolean showMessage) {
        isShowMessage = showMessage;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
