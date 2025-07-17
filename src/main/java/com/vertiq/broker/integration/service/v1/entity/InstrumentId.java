package com.vertiq.broker.integration.service.v1.entity;

import java.io.Serializable;
import lombok.Data;

@Data
public class InstrumentId implements Serializable {
    private String isin;
    private String exchange;
}
