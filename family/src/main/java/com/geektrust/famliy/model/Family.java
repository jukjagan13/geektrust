package com.geektrust.famliy.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Family {
    private Family parent;
    private Person husband;
    private Person wife;
    private List<Family> children = new ArrayList<>();

    public void setParent(Family parent) {
        parent.addChild(this);
        this.parent = parent;
    }

    public Family() {
    }

    public Family(Person person) {
        if (person.getGender().equals(Gender.MALE))
            this.setHusband(person);
        else if (person.getGender().equals(Gender.FEMALE))
            this.setWife(person);
    }

    public Family(Person person, Family parent) {
        if (person.getGender().equals(Gender.MALE))
            this.setHusband(person);
        else if (person.getGender().equals(Gender.FEMALE))
            this.setWife(person);
        this.setParent(parent);
    }

    public Family(Person husband, Person wife) {
        this.setHusband(husband);
        this.setWife(wife);
    }

    public void addChild(Family newChild) {
        newChild.setParent(this);
        this.children.add(newChild);
    }

    public List<Family> getChildren() {
        return children;
    }

//    public List<Family> getSiblings() {
//        String name = this.husband != null ? this.husband.getName() : this.wife.getName();
//        List<Family> siblings = this.parent.getChildren().stream().filter(c -> (c.getHusband() != null && !c.getHusband().getName().equalsIgnoreCase(name)) || (c.getWife() != null && !c.getWife().getName().equalsIgnoreCase(name))).collect(Collectors.toList());
//        return siblings;
//    }
}
