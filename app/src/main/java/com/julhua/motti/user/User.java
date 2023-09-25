package com.julhua.motti.user;

public class User {
//    private int id;
    private String names;
    private String surnames;
    private boolean isDriver;
    private boolean isPassenger;

//    public User(int id, String names, String surnames, boolean isDriver, boolean isPassenger) {
//        this.id = id;
//        this.names = names;
//        this.surnames = surnames;
//        this.isDriver = isDriver;
//        this.isPassenger = isPassenger;
//    }

    public User(String names, String surnames, boolean isDriver, boolean isPassenger) {
        this.names = names;
        this.surnames = surnames;
        this.isDriver = isDriver;
        this.isPassenger = isPassenger;
    }

//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }

    public String getSurnames() {
        return surnames;
    }

    public void setSurnames(String surnames) {
        this.surnames = surnames;
    }

    public boolean isDriver() {
        return isDriver;
    }

    public void setDriver(boolean driver) {
        isDriver = driver;
    }

    public boolean isPassenger() {
        return isPassenger;
    }

    public void setPassenger(boolean passenger) {
        isPassenger = passenger;
    }

    @Override
    public String toString() {
        return "User{" +
                "names='" + names + '\'' +
                ", surnames='" + surnames + '\'' +
                ", isDriver=" + isDriver +
                ", isPassenger=" + isPassenger +
                '}';
    }
}
