package soldiSubito.home_banking;

public enum CountType {
	ERROR(-1), STANDARD(0), PREMIUM(1);
	
	private final int value;
    private CountType(int value) {
        this.value = value;
    }

    public static CountType getEnumValue(int value) {
		switch (value) {
		case 0:
			return CountType.STANDARD;
		case 1:
			return CountType.PREMIUM;
		default:
			return CountType.ERROR;
		}
	}
    
    public int getValue() {
        return value;
    }
}