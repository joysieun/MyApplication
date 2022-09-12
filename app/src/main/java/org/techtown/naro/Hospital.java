package org.techtown.naro;

public class Hospital {


    private String hosname;
    private String hosaddress;
    private String hosonoff;


    public double getHoslatitude() {
        return hoslatitude;
    }

    public void setHoslatitude(double hoslatitude) {
        this.hoslatitude = hoslatitude;
    }

    private double hoslatitude;
    private double hoslongitude;
    private String tel;

    public Hospital(){
        this.hosname = hosname;
        this.hosaddress = hosaddress;
        this.hosonoff = hosonoff;
        this.hoslatitude = hoslatitude;
        this.hoslongitude = hoslongitude;
        this.tel =tel;
    }
    public String getHosaddress() {
        return hosaddress;
    }

    public void setHosaddress(String hosaddress) {
        this.hosaddress = hosaddress;
    }

    public String getHosonoff() {
        return hosonoff;
    }

    public void setHosonoff(String hosonoff) {
        this.hosonoff = hosonoff;
    }
    public String getHosname() {
        return hosname;
    }

    public void setHosname(String hosname) {
        this.hosname = hosname;
    }
    public double getHoslongitude() {
        return hoslongitude;
    }

    public void setHoslongitude(double hoslongitude) {
        this.hoslongitude = hoslongitude;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }


}
