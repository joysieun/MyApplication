package org.techtown.myapplication;

import android.graphics.Bitmap;

//고객 클래스
public class PetResult {
    //사용자 아이디, 비밀번호, 이름, 사진, 진단결과, 시간, 반려동물이름
    private String userid;
    private String userpwd;
    private String username;
    private String petname;
    private String skinresult;
    private String time;
    private byte[] pet_image;

    public PetResult(){
        this.userid = userid;
        this.userpwd = userpwd;
        this.username = username;
        this.petname = petname;
        this.skinresult =skinresult;
        this.time = time;
        this.pet_image = pet_image;
    }


    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUserpwd() {
        return userpwd;
    }

    public void setUserpwd(String userpwd) {
        this.userpwd = userpwd;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPetname() {
        return petname;
    }

    public void setPetname(String petname) {
        this.petname = petname;
    }

    public String getSkinresult() {
        return skinresult;
    }

    public void setSkinresult(String skinresult) {
        this.skinresult = skinresult;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public byte[] getPet_image() {
        return pet_image;
    }

    public void setPet_image(byte[] pet_image) {
        this.pet_image = pet_image;
    }
}
