/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crowninteractive.smsportal.dto;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author adekanmbi
 */
public class EntityCollection {

    private List<Entity> collection = new ArrayList<>();

    public List<Entity> getCollection() {
        return collection;
    }

    public void setCollection(List<Entity> collection) {
        this.collection = collection;
    }

}
