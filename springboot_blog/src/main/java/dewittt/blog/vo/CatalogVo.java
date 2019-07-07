package dewittt.blog.vo;

import dewittt.blog.entity.Catalog;

public class CatalogVo {

    private String username;
    private Catalog catalog;

    public CatalogVo(){}

    public Catalog getCatalog() {
        return catalog;
    }

    public void setCatalog(Catalog catalog) {
        this.catalog = catalog;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
