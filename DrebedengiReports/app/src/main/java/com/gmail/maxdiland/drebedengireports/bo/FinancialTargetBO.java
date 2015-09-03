package com.gmail.maxdiland.drebedengireports.bo;

import java.util.List;

/**
 * author Max Diland
 */
public class FinancialTargetBO {

    private int id;
    private String name;
    private boolean root;
    private List<FinancialTargetBO> childs;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isRoot() {
        return root;
    }

    public void setRoot(boolean root) {
        this.root = root;
    }

    public List<FinancialTargetBO> getChilds() {
        return childs;
    }

    public void setChilds(List<FinancialTargetBO> childs) {
        this.childs = childs;
    }

    @Override
    public String toString() {
        return name;
    }
}
