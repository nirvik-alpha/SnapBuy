package com.snapBuy.project.repositories;

import com.snapBuy.project.model.Category;
import com.snapBuy.project.model.Product;
import com.snapBuy.project.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for Product entity.
 * Provides database access methods for product management,
 * including filtering, searching, pagination, and seller-based retrieval.
 * Extends JpaSpecificationExecutor to support dynamic queries.
 * JpaSpecificationExecutor -> advanced dynamic query,
 * Example:
 * price range filter | category + keyword + sorting combined filter | multiple optional filters
 */

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    Page<Product> findByCategoryOrderByPriceAsc(Category category, Pageable pageDetails);

    Page<Product> findByProductNameLikeIgnoreCase(String keyword, Pageable pageDetails);

    Page<Product> findByUser(User user, Pageable pageDetails);
}