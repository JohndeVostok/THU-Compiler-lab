package decaf.error;

import decaf.Location;

/**
 * can not use this in static function
 * PA2
 */
public class SuperVarError extends DecafError {

	public SuperVarError(Location location) {
		super(location);
	}

	@Override
	protected String getErrMsg() {
		return "super.member_var is not supported";
	}

}
