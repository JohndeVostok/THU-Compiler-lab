package decaf.error;

import decaf.Location;

public class DOODBoolError extends DecafError {

	private String type;

	public DOODBoolError(Location location, String type) {
		super(location);
		this.type = type;
	}

	@Override
	protected String getErrMsg() {
		return "The condition of Do Stmt requestd type bool but " + type
				+ " given";
	}

}
