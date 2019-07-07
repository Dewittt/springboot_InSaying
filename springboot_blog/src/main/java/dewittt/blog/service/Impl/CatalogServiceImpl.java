package dewittt.blog.service.Impl;

import dewittt.blog.entity.Catalog;
import dewittt.blog.entity.User;
import dewittt.blog.repository.CatalogRepository;
import dewittt.blog.service.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CatalogServiceImpl implements CatalogService {

    @Autowired
    private CatalogRepository catalogRepository;

    @Override
    public Catalog saveCatalog(Catalog catalog) {
        List<Catalog> list = catalogRepository.findByUserAndName(catalog.getUser(),catalog.getName());
        if (list!=null&&list.size()>0){
            throw new IllegalArgumentException("分类已存在");
        }
        return catalogRepository.save(catalog);
    }

    @Override
    public void removeCatalog(Long id) {
        catalogRepository.deleteById(id);
    }

    @Override
    public Optional<Catalog> getCatalogById(Long id) {
        return catalogRepository.findById(id);
    }

    @Override
    public List<Catalog> listCatalogs(User user) {
        return catalogRepository.findByUser(user);
    }
}
