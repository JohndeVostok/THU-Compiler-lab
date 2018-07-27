package decaf.error;

import decaf.Location;

/**
 * example：incompatible argument 3: int[] given, int/bool/string expected<br>
 * 3表示发生错误的是第三个参数<br>
 * PA2
 */
public class CaseTypeError extends DecafError {

	private String type;

	public CaseTypeError(Location location, String type) {
		super(location);
		this.type = type;
	}

	@Override
	protected String getErrMsg() {
		return "incompatible case expr: " + type
				+ " given, but int expected";
	}

}
