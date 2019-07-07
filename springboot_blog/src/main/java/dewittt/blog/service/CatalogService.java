package dewittt.blog.service;

import dewittt.blog.entity.Catalog;
import dewittt.blog.entity.User;

import java.util.List;
import java.util.Optional;

public interface CatalogService {

    Catalog saveCatalog(Catalog catalog);
    void removeCatalog(Long id);
    Optional<Catalog> getCatalogById(Long id);
    List<Catalog> listCatalogs(User user);
}
