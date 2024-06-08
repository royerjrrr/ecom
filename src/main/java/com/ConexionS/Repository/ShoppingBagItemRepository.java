package com.ConexionS.Repository;

import com.ConexionS.Entities.ShoppingBagItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShoppingBagItemRepository extends JpaRepository<ShoppingBagItem, Long> {
}