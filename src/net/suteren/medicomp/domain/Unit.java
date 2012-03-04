package net.suteren.medicomp.domain;

public enum Unit {
	CELSIUS("°C", new String[] { "c", "C" }), FARENHEIT("°F", new String[] {
			"F", "f" }), KELVIN("K", null), METER("m", new String[] { "M" }), CENTIMETER(
			"cm", new String[] { "CM" }), FEET("ft", new String[] { "FT",
			"feet", "'" }), INCH("in", new String[] { "inch", "\"" }), KILOGRAM(
			"kg", new String[] { "Kg", "KG" });

	private String unit;
	private String[] alternativeUnits;

	Unit(String unit, String[] alternativeUnits) {
		this.unit = unit;
		if (alternativeUnits == null)
			alternativeUnits = new String[] {};
		this.alternativeUnits = alternativeUnits;
	}

	public String[] getAlternativeUnits() {
		return alternativeUnits;
	}

	public String getUnit() {
		return unit;
	}

}
