package com.droid.remoteaccess;

/**
 * Created by nalmir on 19/12/2015.
 */
public class Contato {


    private String email;
    private String token;
    private String device;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    @Override
    public String toString() {
        return this.email;
    }
}
