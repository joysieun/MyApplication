

package org.techtown.naro;

public class Chat{
    private String id; //사용자 아이디
    private String contents; // 채팅 내용
    private String time; //시간
    private int viewType; //로봇인가 사람인가

    public Chat(String id, String contents, String time, int viewType){
        this.contents = contents;
        this.id = id;
        this.time = time;
        this.viewType =viewType;
    }


    public String getId() {
        return id;
    }

    public String getContents() {
        return contents;
    }


    public String getTime() {
        return time;
    }

    public int getViewType(){return viewType;}


}

