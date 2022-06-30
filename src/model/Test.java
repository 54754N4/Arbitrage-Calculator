package model;

public class Test {
	public static void main(String[] args) {
		test2();
//		testWiki();
//		test3();
//		test6();
//		testReal();
	}
	
	public static void test2() {
		Arbitrage a = new Arbitrage.Builder()
				.setHeaders("Federer", "Nadal")
				.addSource("Site 1", 1.30, 3.93)
				.addSource("Site 2", 1.42, 2.90)
				.build();
		System.out.println(a);
		System.out.println(">>Profits");
		System.out.println(a.bet(100));
	}
	
	public static void testWiki() {	// https://en.wikipedia.org/wiki/Arbitrage_betting
		Arbitrage a = new Arbitrage.Builder()
				.setHeaders("O1", "O2")
				.addSource("BM1", 1.25, 3.90)
				.addSource("BM2", 1.43, 2.85)
				.build();
		System.out.println(a);
		System.out.println(">>Profits");
		System.out.println(a.bet(136.67));
	}
	
	public static void test3() {
		Arbitrage a = new Arbitrage.Builder()
				.setHeaders("Federer", "Nadal", "Rocky")
				.addSource("Site 1", 2.80, 3.00, 2.62)
				.addSource("Site 2", 2.25, 3.10, 3.25)
				.addSource("Site 3", 2.00, 3.50, 3.50)
				.build();
		System.out.println(a);
		System.out.println(">>Profits");
		System.out.println(a.bet(100));
	}
	
	public static void test6() {
		Arbitrage a = new Arbitrage.Builder()
				.setHeaders("Federer", "Nadal", "Rocky", "Tarzan", "Zilean", "Fairy")
				.addSource("Site 1", 11.00, 5.50, 4.33, 7.50, 7.50, 2.50)
				.addSource("Site 2", 4.00, 3.50, 2.70, 4.00, 1.95, 1.95)
				.addSource("Site 3", 3.30, 2.30, 300, 100, 2.25, 2.25)
				.build();
		System.out.println(a);
		System.out.println(">>Profits");
		System.out.println(a.bet(100));
	}
	
	public static void testReal() {
		Arbitrage a = new Arbitrage.Builder()
				.addSource("feeling", 5.00, 1.29, 3.80)
				.addSource("unibet", 6.10, 1.33, 5.10)
				.addSource("betway", 6.10, 1.37, 5.00)
				.build();
		System.out.println(a);
		System.out.println(a.bet(100));
	}
}