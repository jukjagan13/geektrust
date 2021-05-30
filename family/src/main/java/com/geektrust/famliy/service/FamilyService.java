package com.geektrust.famliy.service;

import com.geektrust.famliy.model.Family;
import com.geektrust.famliy.model.Gender;
import com.geektrust.famliy.model.Person;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class FamilyService {

    private Family protagonist;

    public FamilyService() {
        initializeFamily();
    }

    public String addChild(String motherName, String childName, String gender) {
        List<Family> queue = new LinkedList<>();
        queue.add(this.protagonist);
        while (!queue.isEmpty()) {
            for (int i = 0; i < queue.size(); i++) {
                Family top = queue.remove(0);
                if (top.getWife() != null && top.getWife().getName().equalsIgnoreCase(motherName) && top.getHusband() != null) {
                    if (top.getChildren().stream().noneMatch(c -> (c.getHusband() != null && c.getHusband().getName().equalsIgnoreCase(childName)) || (c.getWife() != null && c.getWife().getName().equalsIgnoreCase(childName)))) {
                        Family newFamily = new Family(new Person(childName, gender.equalsIgnoreCase(Gender.FEMALE.name()) ? Gender.FEMALE : Gender.MALE, true));
                        List<Family> children = new ArrayList<>(top.getChildren());
                        children.add(newFamily);
                        top.setChildren(children);
                    } else {
                        return "CHILD_ADDITION_FAILED";
                    }
                    return "CHILD_ADDITION_SUCCEEDED";
                } else if (top.getHusband() != null && top.getHusband().getName().equalsIgnoreCase(motherName)) {
                    return "CHILD_ADDITION_FAILED";
                }
                queue.addAll(top.getChildren());
            }
        }
        return "PERSON_NOT_FOUND";
    }

    public String getRelationship(String name, String relationship) {
        List<String> response = new ArrayList<>();
        List<Family> queue = new LinkedList<>();
        queue.add(this.protagonist);
        boolean found = false;
        while (!queue.isEmpty() && !found) {
            for (int i = 0; i < queue.size(); i++) {
                if (found)
                    break;
                Family top = queue.remove(0);
                if (relationship.equalsIgnoreCase("Siblings") && top.getChildren().stream().anyMatch(c -> (c.getHusband() != null && c.getHusband().isChild() && c.getHusband().getName().equalsIgnoreCase(name)) || (c.getWife() != null && c.getWife().isChild() && c.getWife().getName().equalsIgnoreCase(name)))) {
                    found = true;
                    top.getChildren().forEach(f -> {
                        if (f.getHusband() != null && f.getHusband().isChild() && !f.getHusband().getName().equalsIgnoreCase(name)) {
                            response.add(f.getHusband().getName());
                        } else if (f.getWife() != null && f.getWife().isChild() && !f.getWife().getName().equalsIgnoreCase(name)) {
                            response.add(f.getWife().getName());
                        }
                    });
                } else if (relationship.equalsIgnoreCase("Son") && ((top.getHusband() != null && top.getHusband().getName().equalsIgnoreCase(name)) || (top.getWife() != null && top.getWife().getName().equalsIgnoreCase(name)))) {
                    found = true;
                    top.getChildren().forEach(f -> {
                        if (f.getHusband() != null && f.getHusband().isChild() && !f.getHusband().getName().equalsIgnoreCase(name)) {
                            response.add(f.getHusband().getName());
                        }
                    });
                } else if (relationship.equalsIgnoreCase("Daughter") && ((top.getHusband() != null && top.getHusband().getName().equalsIgnoreCase(name)) || (top.getWife() != null && top.getWife().getName().equalsIgnoreCase(name)))) {
                    found = true;
                    top.getChildren().forEach(f -> {
                        if (f.getWife() != null && f.getWife().isChild() && !f.getWife().getName().equalsIgnoreCase(name)) {
                            response.add(f.getWife().getName());
                        }
                    });
                } else if (relationship.equalsIgnoreCase("Sister-In-Law") && top.getChildren().stream().anyMatch(c -> (c.getHusband() != null && c.getHusband().getName().equalsIgnoreCase(name)) || (c.getWife() != null && c.getWife().getName().equalsIgnoreCase(name)))) {
                    found = true;
                    top.getChildren().forEach(f -> {
                        if ((f.getWife() == null || (f.getWife() != null && !f.getWife().getName().equalsIgnoreCase(name)) && (f.getHusband() == null || (f.getHusband() != null && !f.getHusband().getName().equalsIgnoreCase(name))))) {
                            if (f.getWife() != null)
                                response.add(f.getWife().getName());
                        }
                    });
                } else if (relationship.equalsIgnoreCase("Brother-In-Law") && top.getChildren().stream().anyMatch(c -> (c.getHusband() != null && c.getHusband().getName().equalsIgnoreCase(name)) || (c.getWife() != null && c.getWife().getName().equalsIgnoreCase(name)))) {
                    found = true;
                    top.getChildren().forEach(f -> {
                        if ((f.getWife() == null || (f.getWife() != null && !f.getWife().getName().equalsIgnoreCase(name)) && (f.getHusband() == null || (f.getHusband() != null && !f.getHusband().getName().equalsIgnoreCase(name))))) {
                            if (f.getHusband() != null)
                                response.add(f.getHusband().getName());
                        }
                    });
                } else if (relationship.equalsIgnoreCase("Paternal-Aunt")) {
                    List<Family> grandchildren = new ArrayList<>();
                    top.getChildren().forEach(f -> {
                        if (f.getHusband() != null && f.getHusband().isChild())
                            grandchildren.addAll(f.getChildren());
                    });
                    found = grandchildren.stream().anyMatch(c -> (c.getHusband() != null && c.getHusband().isChild() && c.getHusband().getName().equalsIgnoreCase(name)) || (c.getWife() != null && c.getWife().isChild() && c.getWife().getName().equalsIgnoreCase(name)));
                    if (found) {
                        top.getChildren().forEach(f -> {
                            if (f.getWife() != null && f.getWife().isChild()) {
                                response.add(f.getWife().getName());
                            }
                        });
                    }
                } else if (relationship.equalsIgnoreCase("Maternal-Aunt")) {
                    List<Family> grandchildren = new ArrayList<>();
                    top.getChildren().forEach(f -> {
                        if (f.getWife() != null && f.getWife().isChild())
                            grandchildren.addAll(f.getChildren());
                    });
                    found = grandchildren.stream().anyMatch(c -> (c.getHusband() != null && c.getHusband().isChild() && c.getHusband().getName().equalsIgnoreCase(name)) || (c.getWife() != null && c.getWife().isChild() && c.getWife().getName().equalsIgnoreCase(name)));
                    if (found) {
                        top.getChildren().forEach(f -> {
                            if (f.getWife() != null && f.getWife().isChild() && f.getChildren().stream().noneMatch(c -> (c.getHusband() != null && c.getHusband().isChild() && c.getHusband().getName().equalsIgnoreCase(name)) || (c.getWife() != null && c.getWife().isChild() && c.getWife().getName().equalsIgnoreCase(name)))) {
                                response.add(f.getWife().getName());
                            }
                        });
                    }
                } else if (relationship.equalsIgnoreCase("Paternal-Uncle")) {
                    List<Family> grandchildren = new ArrayList<>();
                    top.getChildren().forEach(f -> {
                        if (f.getHusband() != null && f.getHusband().isChild())
                            grandchildren.addAll(f.getChildren());
                    });
                    found = grandchildren.stream().anyMatch(c -> (c.getHusband() != null && c.getHusband().isChild() && c.getHusband().getName().equalsIgnoreCase(name)) || (c.getWife() != null && c.getWife().isChild() && c.getWife().getName().equalsIgnoreCase(name)));
                    if (found) {
                        top.getChildren().forEach(f -> {
                            if (f.getHusband() != null && f.getHusband().isChild() && f.getChildren().stream().noneMatch(c -> (c.getHusband() != null && c.getHusband().isChild() && c.getHusband().getName().equalsIgnoreCase(name)) || (c.getWife() != null && c.getWife().isChild() && c.getWife().getName().equalsIgnoreCase(name)))) {
                                response.add(f.getHusband().getName());
                            }
                        });
                    }
                } else if (relationship.equalsIgnoreCase("Maternal-Uncle")) {
                    List<Family> grandchildren = new ArrayList<>();
                    top.getChildren().forEach(f -> {
                        if (f.getWife() != null && f.getWife().isChild())
                            grandchildren.addAll(f.getChildren());
                    });
                    found = grandchildren.stream().anyMatch(c -> (c.getHusband() != null && c.getHusband().isChild() && c.getHusband().getName().equalsIgnoreCase(name)) || (c.getWife() != null && c.getWife().isChild() && c.getWife().getName().equalsIgnoreCase(name)));
                    if (found) {
                        top.getChildren().forEach(f -> {
                            if (f.getHusband() != null && f.getHusband().isChild()) {
                                response.add(f.getHusband().getName());
                            }
                        });
                    }
                }
                queue.addAll(top.getChildren());
            }
        }
        if (found && response.size() > 0) {
            return String.join(" ", response);
        } else if (found) {
            return "NONE";
        }
        return "PERSON_NOT_FOUND";
    }

    public void initializeFamily() {
        try {
            Family vyasFamily = new Family();
            vyasFamily.setHusband(new Person("Vyas", Gender.MALE, true));
            vyasFamily.setWife(new Person("Krpi", Gender.FEMALE));
            vyasFamily.setChildren(Arrays.asList(
                    new Family(new Person("Kriya", Gender.MALE, true)),
                    new Family(new Person("Krithi", Gender.FEMALE, true))
            ));

            Family asvaFamily = new Family();
            asvaFamily.setHusband(new Person("Asva", Gender.MALE, true));
            asvaFamily.setWife(new Person("Satvy", Gender.FEMALE));
            asvaFamily.setChildren(Arrays.asList(new Family(new Person("Vasa", Gender.MALE, true))));

            Family satyaFamily = new Family();
            satyaFamily.setHusband(new Person("Vyan", Gender.MALE));
            satyaFamily.setWife(new Person("Satya", Gender.FEMALE, true));
            satyaFamily.setChildren(Arrays.asList(
                    new Family(new Person("Atya", Gender.FEMALE, true)),
                    asvaFamily,
                    vyasFamily
            ));

            Family jnkiFamily = new Family();
            jnkiFamily.setHusband(new Person("Arit", Gender.MALE));
            jnkiFamily.setWife(new Person("Jnki", Gender.FEMALE, true));
            jnkiFamily.setChildren(Arrays.asList(
                    new Family(new Person("Laki", Gender.MALE, true)),
                    new Family(new Person("Lavnya", Gender.FEMALE, true))
            ));

            Family arasFamily = new Family();
            arasFamily.setHusband(new Person("Aras", Gender.MALE, true));
            arasFamily.setWife(new Person("Chitra", Gender.FEMALE));
            arasFamily.setChildren(Arrays.asList(
                    new Family(new Person("Ahit", Gender.MALE, true)),
                    jnkiFamily
            ));

            Family vichFamily = new Family();
            vichFamily.setHusband(new Person("Vich", Gender.MALE, true));
            vichFamily.setWife(new Person("Lika", Gender.FEMALE));
            vichFamily.setChildren(Arrays.asList(
                    new Family(new Person("Vila", Gender.FEMALE, true)),
                    new Family(new Person("Chika", Gender.FEMALE, true))
            ));

            Family drithaFamily = new Family();
            drithaFamily.setHusband(new Person("Jaya", Gender.MALE));
            drithaFamily.setWife(new Person("Dritha", Gender.FEMALE, true));
            drithaFamily.setChildren(Arrays.asList(
                    new Family(new Person("Yodhan", Gender.MALE, true))
            ));

            Family chitFamily = new Family();
            chitFamily.setHusband(new Person("Chit", Gender.MALE, true));
            chitFamily.setWife(new Person("Amba", Gender.FEMALE));
            chitFamily.setChildren(Arrays.asList(
                    new Family(new Person("Tritha", Gender.FEMALE, true)),
                    new Family(new Person("Vritha", Gender.MALE, true)),
                    drithaFamily
            ));

            Family protagonists = new Family();
            protagonists.setHusband(new Person("Shan", Gender.MALE));
            protagonists.setWife(new Person("Anga", Gender.FEMALE));
            protagonists.setChildren(Arrays.asList(
                    chitFamily,
                    new Family(new Person("Ish", Gender.MALE, true)),
                    vichFamily,
                    arasFamily,
                    satyaFamily
            ));

            this.protagonist = protagonists;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
