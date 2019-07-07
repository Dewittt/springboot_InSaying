package dewittt.blog.repository;

import dewittt.blog.entity.Catalog;
import dewittt.blog.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CatalogRepository extends JpaRepository<Catalog,Long> {
    List<Catalog> findByUser(User user);
    List<Catalog> findByUserAndName(User user,String name);
}
