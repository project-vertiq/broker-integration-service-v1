package com.vertiq.broker.integration.service.v1.repository;

import com.vertiq.broker.integration.service.v1.entity.Instrument;
import com.vertiq.broker.integration.service.v1.entity.InstrumentId;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface InstrumentRepository extends JpaRepository<Instrument, InstrumentId> {
    @Query("SELECT i FROM Instrument i WHERE i.symbol IN :symbols")
    List<Instrument> findBySymbolIn(List<String> symbols);
}
