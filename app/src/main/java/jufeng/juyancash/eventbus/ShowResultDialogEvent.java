package jufeng.juyancash.eventbus;

/**
 * Created by Administrator102 on 2017/3/10.
 */

public class ShowResultDialogEvent {
    private String title;
    private String message;

    public ShowResultDialogEvent(String title, String message) {
        this.title = title;
        this.message = message;
    }

    public String getTitle() {
        if(title == null){
            return "提示";
        }
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        if(message == null){
            return "";
        }
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
