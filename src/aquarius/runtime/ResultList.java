package aquarius.runtime;

import java.util.ArrayList;

public class ResultList extends ArrayList<ParsedResult> implements ParsedResult {
	private static final long serialVersionUID = 7488882427514006370L;

	public ResultList() {
		super();
	}

	public ResultList(int size) {
		super(size);
	}
}
