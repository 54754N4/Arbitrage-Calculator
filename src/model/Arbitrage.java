package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Arbitrage {
	private final int rows, cols;
	private final String[] headers, names;
	private final float[][] odds;
	private float[] best, probabilities;
	private float arbitrageOdds;
	private boolean unbiasedArbitrage;
	
	private Arbitrage(String[] headers, String[] names, List<List<Float>> odds) {
		this.headers = headers;
		this.names = names;
		rows = odds.size();
		cols = odds.get(0).size();
		best = new float[cols];
		probabilities = new float[rows];
		// Unbox collection to primitives
		this.odds = new float[rows][cols];
		for (int i=0; i<rows; i++)
			for (int j=0; j<cols; j++)
				this.odds[i][j] = odds.get(i).get(j);
		// Calculate profits per bookmaker
		for (int i=0; i<rows; i++)
			probabilities[i] = totalProbability(this.odds[i]);
		// Get best odds of each column
		for (int i=0; i<cols; i++)
			best[i] = best(i);
		arbitrageOdds = totalProbability(best);
		unbiasedArbitrage = arbitrageOdds < 1;
	}
	
	private final float best(int col) {
		float max = 0;
		for (int i=0; i<rows; i++)
			if (odds[i][col] > max)
				max = odds[i][col];
		return max;
	}
	
	private static final float totalProbability(float...odds) {
		float total = 0;
		for (float odd : odds)
			total += 1/odd;
		return total;
	}
	
	public Profits bet(double capital) {
		if (!unbiasedArbitrage)
			throw new UnbiasedArbitrageNotPossibleException();
		return new Profits(capital, best, arbitrageOdds);
	}
	
	@Override
	public String toString() {
		String line = System.lineSeparator();
		StringBuilder sb = new StringBuilder();
		for (String header : headers)
			sb.append("\t"+header);
		sb.append(line);
		for (int i=0; i<odds.length; i++) {
			sb.append(names[i]+"\t");
			for (int j=0; j<odds[i].length; j++)
				sb.append(odds[i][j]+"\t");
			sb.append(probabilities[i])
				.append(line);
		}
		sb.append(line+"Best\t");
		for (float b : best)
			sb.append(b+"\t");
		sb.append(arbitrageOdds);
		return sb.toString();
	}
	
	public static class Profits {
		private final float arbitrageOdds, totalOdds;
		private final float[] best, weights;
		private final double capital;
		private final double[] bids, profits;

		public Profits(double capital, float[] best, float arbitrageOdds) {
			this.capital = capital;
			this.best = best;
			this.arbitrageOdds = arbitrageOdds;
			int n = best.length;
			// Compute weighted odds
			weights = new float[n];
			totalOdds = sum(best);
			for (int i=0; i<n; i++)
				weights[i] = best[i]/totalOdds;
			// Compute bids
			bids = new double[n];
			for (int i=0; i<n; i++)
				bids[i] = bid(capital, i);
			// Compute profits
			profits = new double[n];
			for (int i=0; i<n; i++)
				profits[i] = bids[i] * best[i] - capital;
		}
		
		private double bid(double capital, int i) {	// bid(i) = capital/(1 + SIGMA[j != i](oi / oj))
			double denominator = 1;
			for (int outcome=0; outcome<best.length; outcome++)
				if (outcome != i)
					denominator += best[i]/best[outcome];
			return capital/denominator;
		}
		
		private float sum(float[] nums) {
			float sum = 0;
			for (float num : nums)
				sum += num;
			return sum;
		}
		
		@Override
		public String toString() {
			String line = System.lineSeparator();
			StringBuilder sb = new StringBuilder();
			sb.append("Arbitrage Odds: "+arbitrageOdds+line);
			sb.append("Capital:\t"+capital+line);
			sb.append("Win Odds:\t"+Arrays.toString(best)+line);
			sb.append("Weights:\t"+Arrays.toString(weights)+line);
			sb.append("Betting:\t"+Arrays.toString(bids)+line);
			sb.append("Profits:\t"+Arrays.toString(profits)+line);
			return sb.toString();
		}
	}
	
	public static class Builder {
		private String[] headers;
		private List<String> names;
		private List<List<Float>> odds;
		
		public Builder() {
			names = new ArrayList<>();
			odds = new ArrayList<>();
		}
		
		public Builder addSource(String name, double...bets) {
			names.add(name);
			List<Float> list = new ArrayList<>();
			for (double bet: bets)
				list.add((float) bet);
			this.odds.add(list);
			return this;
		}
		
		public Builder setHeaders(String...headers) {
			this.headers = headers;
			return this;
		}
		
		private final void setDefaultHeaders() {
			int len = odds.get(0).size();
			String[] names = new String[len];
			for (int i=1; i<=len; i++)
				names[i-1] = "O"+i;
			headers = names;
		}
		
		public Arbitrage build() {
			if (headers == null) 
				setDefaultHeaders();
			if (odds.size() == 0)
				throw new IllegalArgumentException("No odds were given");
			int len = odds.get(0).size();
			for (List<Float> bets : this.odds)
				if (bets.size() != len)
					throw new BetSizesUnequalException();
			return new Arbitrage(headers, names.toArray(String[]::new), odds);
		}
	}
}
