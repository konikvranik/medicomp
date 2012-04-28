package net.suteren.medicomp.enums;

public enum Quantity {
	TEMPERATURE(new Unit[] { Unit.CELSIUS, Unit.FARENHEIT, Unit.KELVIN }), MASS(
			new Unit[] { Unit.KILOGRAM, Unit.GRAM, Unit.POUND, Unit.STONE,
					Unit.QUARTER, Unit.HUNDREDWEIGHT, Unit.OUNCE, Unit.DRACHM }), LENGTH(
			new Unit[] { Unit.METER, Unit.FEET, Unit.INCH, Unit.CENTIMETER });

	private Unit[] units;

	Quantity(Unit[] u) {
		units = u;
	}

	public Unit[] getUnits() {
		return units;
	}
}
