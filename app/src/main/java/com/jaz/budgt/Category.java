package com.jaz.budgt;

import java.util.ArrayList;

/**
 * Created by jaz on 9/26/17.
 */

public class Category {
    private String name = "";

    public Category(String name) {
        this.name = name;
    }

    public Category(String name, boolean need) {
        this.name = name;
    }

    public String toString() {
        return this.name;
    }

    public void setName(String n) {this.name = n;}

}
