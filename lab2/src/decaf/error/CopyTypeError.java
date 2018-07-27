package decaf.error;

import decaf.Location;

public class CopyTypeError extends DecafError {

	private String type1, type2;

	public CopyTypeError(Location location, String type1, String type2) {
		super(location);
		this.type1 = type1;
		this.type2 = type2;
	}

	@Override
	protected String getErrMsg() {
		return "For copy expr, the source " + type1 + " and the destination " + type2 + " are not same";
	}

}
