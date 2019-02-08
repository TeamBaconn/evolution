package com.Tuong.GameManager;

public enum TimeFrame {
	MORNING(360, 660, "Morning"),NOON(661,840,"Noon"),AFTERNOON(841,1080,"Afternoon"),NIGHT(1081,1320,"Night"),MIGNIGHT(1321,120,"Mid night"),EARLYMORNING(121,359,"Early morning");

	public int from, end;
	public String prefix;

	private TimeFrame(int from, int end, String prefix) {
		this.from = from;
		this.end = end;
		this.prefix = prefix;
	}

	public static TimeFrame getType(float tick) {
		for (TimeFrame type : values())
			if (type.from <= tick && tick <= type.end)
				return type;
		return TimeFrame.MIGNIGHT;
	}
	public static int isNight(float tick) {
		if((AFTERNOON.from+AFTERNOON.end)/2 < tick) return (AFTERNOON.from+AFTERNOON.end)/2;
		if(tick < MORNING.from) return MORNING.from;
		return 0;
	}
}
