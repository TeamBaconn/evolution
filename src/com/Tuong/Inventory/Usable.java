package com.Tuong.Inventory;

public enum Usable {
	EAT,DIG,ATTACK,BREACH,HEAL,PLACE,OTHER;
	
	public Usable getUsable(String s){
		for(Usable us : Usable.values()) if(s.equals(us.toString())) return us;
		return null;
	}
}
