package decaf.error;

import decaf.Location;

public class CopyClassError extends DecafError {

	private String type;

	public CopyClassError(Location location, String type) {
		super(location);
		this.type = type;
	}

	@Override
	protected String getErrMsg() {
		return "expected class type for copy expr but " + type
				+ " given";
	}

}
