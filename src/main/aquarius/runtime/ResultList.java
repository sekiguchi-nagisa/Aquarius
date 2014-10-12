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

	@Override
	public String toString() {
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append('[');
		final int size = this.size();
		for(int i = 0; i < size; i++) {
			if(i > 0) {
				sBuilder.append(", ");
			}
			sBuilder.append(this.get(i));
		}
		sBuilder.append(']');
		return sBuilder.toString();
	}

	@Override
	public boolean equals(Object target) {
		if(target instanceof ResultList) {
			ResultList list = (ResultList) target;
			if(this.size() != list.size()) {
				return false;
			}
			final int size = this.size();
			for(int i = 0; i < size; i++) {
				if(!this.get(i).equals(list.get(i))) {
					return false;
				}
			}
			return true;
		}
		return false;
	}
}
