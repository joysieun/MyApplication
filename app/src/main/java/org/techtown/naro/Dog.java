package org.techtown.naro;
//강아지 정보 저장하는 클래스(사용자마다)
public class Dog{
    private String userid;
    private String petname;
    private String petage;
    private String petsex;
    private String petbirth;
    private byte[] petface;

    public Dog(){
        this.userid = userid;
        this.petname = petname;
        this.petage = petage;
        this.petsex = petsex;
        this.petbirth = petbirth;
        this.petface = petface;
    }
    public String getUserid(){
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

    public String getPetage() {
        return petage;
    }
    public String getPetsex(){
        return petsex;
    }

    public String getPetbirth() {
        return petbirth;
    }
    public void setPetage(String petage){this.petage = petage;}

    public void setPetbirth(String petbirth) {
        this.petbirth = petbirth;
    }

    public void setPetsex(String petsex) {
        this.petsex = petsex;
    }

    public byte[] getPetface() {
        return petface;
    }

    public void setPetface(byte[] petface) {
        this.petface = petface;
    }
}
