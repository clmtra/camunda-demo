package com.clmtra.demo.domain;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Warrior implements Serializable {

    @JsonAlias("name.findName")
    private String name;

    @JsonAlias("name.title")
    private String title;

    @JsonAlias("random.number")
    private Integer hp;

    private Boolean isAlive;

    private static final long serialVersionUID = 1L;

    public Warrior() {
    }

    public Warrior(String name, String title, Integer hp, Boolean isAlive) {
        this.name = name;
        this.title = title;
        this.hp = hp;
        this.isAlive = isAlive;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getHp() {
        return hp;
    }

    public void setHp(Integer hp) {
        this.hp = hp;
    }

    public Boolean getAlive() {
        return isAlive;
    }

    public void setAlive(Boolean alive) {
        isAlive = alive;
    }
}
