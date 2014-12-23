package aquarius.example;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import aquarius.CommonStream;
import aquarius.ParsedResult;
import aquarius.ParserFactory;
import aquarius.misc.Utils;
import aquarius.misc.Utils.MemoryInfo;

public class Bench {
	public static void main(String[] args) throws IOException {
		System.out.println("run json bench mark");
		JSONParser parser = ParserFactory.createParser(JSONParser.class);
		CommonStream input = new CommonStream(args[0]);

		MemoryInfo beforeInfo = null;
		MemoryInfo afterInfo = null;

		BenchResultRecord record = new BenchResultRecord();

		final int count = 30;
		for(int i = 0; i < count; i++) {
			input.setPosition(0);

			beforeInfo = Utils.getMemoryInfo();
			long start = System.currentTimeMillis();
			ParsedResult<JSON> result = parser.json().parse(input);
			long stop = System.currentTimeMillis();
			afterInfo = Utils.getMemoryInfo();

			record.add(stop - start);
			System.out.println((i + 1) + "/" + count);
			result.isSucess();
		}

		System.err.println("before parsing");
		System.err.println(beforeInfo);
		System.err.println();
		System.err.println("after parsing");
		System.err.println(afterInfo);

		System.err.println();
		System.err.println("  mean time: " + record.getMean() + "ms");
		System.err.println("median time: " + record.getMedian() + "ms");
	}

	public static class BenchResultRecord {
		private final List<Long> results;

		public BenchResultRecord() {
			this.results = new ArrayList<>();
		}

		public void add(long result) {
			this.results.add(result);
		}

		public long getMean() {
			int size = results.size();
			long sum = 0;
			for(Long result: this.results) {
				sum += result.longValue();
			}
			return Math.round(sum / (double) size);
		}

		public long getMedian() {
			Collections.sort(this.results);
			int medianIndex = Math.round(this.results.size() / (float) 2);
			return this.results.get(medianIndex);
		}
	}
}
