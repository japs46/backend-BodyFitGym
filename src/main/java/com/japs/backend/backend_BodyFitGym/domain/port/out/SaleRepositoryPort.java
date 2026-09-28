package com.japs.backend.backend_BodyFitGym.domain.port.out;

import com.japs.backend.backend_BodyFitGym.application.dto.SaleSearchCriteria;
import com.japs.backend.backend_BodyFitGym.domain.model.Sale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface SaleRepositoryPort {

    Sale save(Sale sale);

    Optional<Sale> findById(Long id);

    List<Sale> findByAffiliateMembershipId(Long affiliateMembershipId);

    Page<Sale> search(SaleSearchCriteria saleSearchCriteria, Pageable pageable);
}
