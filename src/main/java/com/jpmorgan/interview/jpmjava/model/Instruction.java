package com.jpmorgan.interview.jpmjava.model;

import java.time.DayOfWeek;
import java.time.LocalDate;

public class Instruction {
    private final String entity;
    private final Boolean buy;
    private final Double agreedFx;
    private final String currency;
    private final LocalDate instructionDate;
    private final LocalDate settlementDate;
    private final Integer units;
    private final Double pricePerUnit;
    public static final boolean BUY = true;
    public static final boolean SELL = false;

    public Instruction(String entity, Boolean buy, Double agreedFx, String currency,
            LocalDate instructionDate, LocalDate settlementDate, Integer units,
            Double pricePerUnit) {
        super();
        this.entity = entity;
        this.buy = buy;
        this.agreedFx = agreedFx;
        this.currency = currency;
        this.instructionDate = instructionDate;
        this.settlementDate = settlementDate;
        this.units = units;
        this.pricePerUnit = pricePerUnit;
    }

    private boolean isSpecialCurrency() {
        return currency.equals("AED") || currency.equals("SAR");
    }

    public LocalDate calculateActualSettlementDate() {
        DayOfWeek dayOfWeek = settlementDate.getDayOfWeek();
        switch (dayOfWeek) {
        case FRIDAY:
            return (isSpecialCurrency()) ? settlementDate.plusDays(2) /* Sunday */ : settlementDate;
        case SATURDAY:
            return (isSpecialCurrency()) ? settlementDate.plusDays(1)
                    /* Sunday */ : settlementDate.plusDays(2) /* Monday */;
        case SUNDAY:
            return (isSpecialCurrency()) ? settlementDate : settlementDate.plusDays(1) /* Monday */;
        default:
            return settlementDate;
        }
    }

    public Double calculateUsdAmount() {
        return pricePerUnit * (double) units.intValue() * agreedFx;
    }

    public boolean getSell() {
        return !buy;
    }

    public String getEntity() {
        return entity;
    }

    public Boolean getBuy() {
        return buy;
    }

    public Double getAgreedFx() {
        return agreedFx;
    }

    public String getCurrency() {
        return currency;
    }

    public LocalDate getInstructionDate() {
        return instructionDate;
    }

    public LocalDate getSettlementDate() {
        return settlementDate;
    }

    public Integer getUnits() {
        return units;
    }

    public Double getPricePerUnit() {
        return pricePerUnit;
    }

}
