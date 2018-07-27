package decaf.error;

import decaf.Location;

/**
 * can not use this in static function
 * PA2
 */
public class SuperNoParentError extends DecafError {

	String type;

	public SuperNoParentError(Location location, String type) {
		super(location);
		this.type = type;
	}

	@Override
	protected String getErrMsg() {
		return "no parent class exist for " + type;
	}

}
