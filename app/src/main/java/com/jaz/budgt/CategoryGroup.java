package com.jaz.budgt;

import java.util.ArrayList;

/**
 * Created by jaz on 9/26/17.
 */

public class CategoryGroup {
    private ArrayList<Category> subcategories;
    private String name = "";

    public CategoryGroup(String name) {
        this.subcategories = new ArrayList<>(0);
        this.name = name;
    }

    public CategoryGroup(String name, ArrayList<Category> subcategories) {
        this.subcategories = subcategories;
        this.name = name;
    }

    public CategoryGroup(String name, String[] subcategories) {
        this.subcategories = new ArrayList<>(0);
        for(String c : subcategories) {
            this.subcategories.add(new Category(c));
        }
        this.name = name;
    }



    public ArrayList<Category> getSubcategories() {
        return subcategories;
    }

    public void setSubcategories(ArrayList<Category> subcategories) {
        this.subcategories = subcategories;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
