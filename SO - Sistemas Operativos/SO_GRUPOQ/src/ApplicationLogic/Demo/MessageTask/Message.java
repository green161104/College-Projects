package ApplicationLogic.Demo.MessageTask;

public class Message {

    /**
     * this variable is the title of the message used in the threads.
     */
    private String title;
    /**
     * this variable is the content of the message used in the threads.
     */
    private String content;

    public Message(String title, String content){
        this.title = title;
        this.content = content;
    }

    /**
     * getter for the content variable in message objects.
     * @return
     */
    public String getContent() {return content;}
    /**
     * setter for the content variable in message objects.
     * @param content
     */
    public void setContent(String content) {this.content = content;}
    /**
     * getter for the title in message objects.
     * @return
     */
    public String getTitle() {return title;}
    /**
     * setter for the title variable in message objects.
     * @param title
     */
    public void setTitle(String title) {this.title = title;}
}
