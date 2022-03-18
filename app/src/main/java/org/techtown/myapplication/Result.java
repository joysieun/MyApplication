package org.techtown.myapplication;

import android.graphics.Bitmap;

//고객 클래스
public class Result {
    //사용자 닉네임,반려동물 이름, 진단피부사진, 진단결과, 시간
    private String userid; // 사용자 이름
    private String petname;
    private String skinresult;
    private String time;
    private byte[] pet_image;

    public Result(){
        this.userid = userid;
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
