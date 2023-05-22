package com.onicolian.drawer.Entity;

public class Sets {

    private long id;
    private String desk;
    private String list;

    public Sets(long id, String list, String desk) {
        this.id = id;
        this.desk = desk;
        this.list = list;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDesk() {
        return desk;
    }

    public void setDesk(String desk) {
        this.desk = desk;
    }

    public String getList() {
        return list;
    }

    public void setList(String list) {
        this.list = list;
    }
}
