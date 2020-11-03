package com.iiht.evaluation.eloan.dto;

public class LoanDto {
	private String applno;
	private int amtRequested;
	private int emi;
	public LoanDto() {
		
	}
	public LoanDto(String applno, int amtRequested, int emi) {
		super();
		this.applno = applno;
		this.amtRequested = amtRequested;
	}
	public String getApplno() {
		return applno;
	}
	public void setApplno(String applno) {
		this.applno = applno;
	}
	public int getAmtRequested() {
		return amtRequested;
	}
	public void setAmtRequested(int amtRequested) {
		this.amtRequested = amtRequested;
	}
	public int getEmi() {
		return emi;
	}
	public void setEmi(int emi) {
		this.emi = emi;
	}
	
	
	

}
