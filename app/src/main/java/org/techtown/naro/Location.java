package org.techtown.naro;

public class Location {

    private String name;
    private String tel; // 사용자 이름
    private String onoff;
    private String place1;
    private Double Latitude;
    private Double Longitude;

    public Location(){
        this.name = name;
        this.tel = tel;
        this.onoff = onoff;
        this.place1 = place1;
        this.Latitude = Latitude;
        this.Longitude = Longitude;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getOnoff() {
        return onoff;
    }

    public void setOnoff(String onoff) {
        this.onoff = onoff;
    }

    public String getPlace1() {
        return place1;
    }

    public void setPlace1(String place1) {
        this.place1 = place1;
    }

    public Double getLatitude() {
        return Latitude;
    }

    public void setLatitude(Double latitude) {
        Latitude = latitude;
    }

    public Double getLongitude() {
        return Longitude;
    }

    public void setLongitude(Double longitude) {
        Longitude = longitude;
    }


}

