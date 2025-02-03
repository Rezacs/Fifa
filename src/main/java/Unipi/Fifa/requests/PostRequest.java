package Unipi.Fifa.requests;

public class PostRequest {
    private String subject;
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String comment) {
        this.text = comment;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
