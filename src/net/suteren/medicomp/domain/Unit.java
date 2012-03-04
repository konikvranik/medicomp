package net.suteren.medicomp.domain;

public enum Unit {
	CELSIUS("°C", new String[] { "c", "C" }, Quantity.TEMPERATURE), FARENTHEIT(
			"F", null, Quantity.TEMPERATURE), KELVIN("K", null,
			Quantity.TEMPERATURE), METER("m", new String[] { "M" },
			Quantity.LENGTH), CENTIMETER("cm", new String[] { "CM" },
			Quantity.LENGTH), FEET("ft", new String[] { "FT", "feet", "'" },
			Quantity.LENGTH), INCH("in", new String[] { "inch", "\"" },
			Quantity.LENGTH), KILOGRAM("kg", new String[] { "Kg", "KG" },
			Quantity.MASS);

	private Quantity quantity;
	private String unit;
	private String[] alternativeUnits;

	Unit(String unit, String[] alternativeUnits, Quantity quantity) {
		this.quantity = quantity;
	}

	public Quantity getQuantity() {
		return quantity;
	}

	public String[] getAlternativeUnits() {
		return alternativeUnits;
	}

	public String getUnit() {
		return unit;
	}

}
