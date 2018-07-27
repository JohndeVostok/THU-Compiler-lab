package decaf.error;

import decaf.Location;

/**
 * example：incompatible argument 3: int[] given, int/bool/string expected<br>
 * 3表示发生错误的是第三个参数<br>
 * PA2
 */
public class CaseDifferentTypeError extends DecafError {

	private String type1, type2;

	public CaseDifferentTypeError(Location location, String type1, String type2) {
		super(location);
		this.type1 = type1;
		this.type2 = type2;
	}

	@Override
	protected String getErrMsg() {
		return "type: " + type1
				+ " is different with other expr's type " + type2;
	}

}
