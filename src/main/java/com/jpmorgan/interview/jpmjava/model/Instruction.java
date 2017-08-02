package com.jpmorgan.interview.jpmjava.model;

import java.time.DayOfWeek;
import java.time.LocalDate;

public class Instruction {
	public String entity;
	private Boolean buy;
	private Double agreedFx;
	private String currency;
	private LocalDate instructionDate;
	private LocalDate settlementDate;
	private Integer units;
	private Double pricePerUnit;
	public static final boolean BUY = true;
	public static final boolean SELL = false;

	public Instruction(String entity, Boolean buy, Double agreedFx, String currency, LocalDate instructionDate,
			LocalDate settlementDate, Integer units, Double pricePerUnit) {
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
					/* Sunday */ : settlementDate.plusDays(2) /* Monday */ ;
		case SUNDAY:
			return (isSpecialCurrency()) ? settlementDate : settlementDate.plusDays(1) /* Monday */ ;
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

	public void setEntity(String entity) {
		this.entity = entity;
	}

	public Boolean getBuy() {
		return buy;
	}

	public void setBuy(Boolean buy) {
		this.buy = buy;
	}

	public Double getAgreedFx() {
		return agreedFx;
	}

	public void setAgreedFx(Double agreedFx) {
		this.agreedFx = agreedFx;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public LocalDate getInstructionDate() {
		return instructionDate;
	}

	public void setInstructionDate(LocalDate instructionDate) {
		this.instructionDate = instructionDate;
	}

	public LocalDate getSettlementDate() {
		return settlementDate;
	}

	public void setSettlementDate(LocalDate settlementDate) {
		this.settlementDate = settlementDate;
	}

	public Integer getUnits() {
		return units;
	}

	public void setUnits(Integer units) {
		this.units = units;
	}

	public Double getPricePerUnit() {
		return pricePerUnit;
	}

	public void setPricePerUnit(Double pricePerUnit) {
		this.pricePerUnit = pricePerUnit;
	}
}
