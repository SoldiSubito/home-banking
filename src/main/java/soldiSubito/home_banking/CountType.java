package soldiSubito.home_banking;

public enum CountType {
	STANDARD(0), PREMIUM(1);
	
	private final int value;
    private CountType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}