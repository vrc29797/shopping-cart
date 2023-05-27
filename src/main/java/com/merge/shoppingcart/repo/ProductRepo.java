package com.merge.shoppingcart.repo;

import com.merge.shoppingcart.model.Product;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepo extends JpaRepository<Product, UUID> {

  //    boolean existsByIdAndActiveTrue(UUID uuid);
  //
  //    boolean existsByIdAndStockGreaterThanEqual(UUID uuid, int stock);

  //    @Query(
  //            value =
  //                    "select is_active from product where ",
  //            nativeQuery = true)
  //    List<BigDecimal> getTransactionByMerchantId(
  //            @Param("merchant_id") String merchantId,
  //            @Param("start_date") LocalDateTime from,
  //            @Param("end_date") LocalDateTime to);

}
