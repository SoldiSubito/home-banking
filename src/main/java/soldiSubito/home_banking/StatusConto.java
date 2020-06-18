package soldiSubito.home_banking;

public enum StatusConto {
	ERROR(-1), DISPONIBILE(0), BLOCCATO(1);

	private final int value;
    private StatusConto(int value) {
        this.value = value;
    }

    public static StatusConto getEnumValue(int value) {
		switch (value) {
		case 0:
			return StatusConto.DISPONIBILE;
		case 1:
			return StatusConto.BLOCCATO;
		default:
			return StatusConto.ERROR;
		}
	}
    
    public int getValue() {
        return value;
    }
}
