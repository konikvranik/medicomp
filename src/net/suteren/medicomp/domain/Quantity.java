package net.suteren.medicomp.domain;

public enum Quantity {
	TEMPERATURE(new Unit[] { Unit.CELSIUS, Unit.FARENHEIT, Unit.KELVIN }), MASS(
			new Unit[] { Unit.KILOGRAM }), LENGTH(new Unit[] { Unit.METER,
			Unit.FEET, Unit.INCH, Unit.CENTIMETER });

	private Unit[] units;

	Quantity(Unit[] u) {
		units = u;
	}

	public Unit[] getUnits() {
		return units;
	}
}
