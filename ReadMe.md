# Arbitrage Calculator

Checks if two sources predictions conflict with each other and allow the possiblity of unbiased arbitrage. 

```java
/* In this example we suppose two different sites propose different payouts 
 * based on their predictions for if Federer or Nadal wins */
Arbitrage a = new Arbitrage.Builder()
		.setHeaders("Federer", "Nadal")
		.addSource("Site 1", 1.30, 3.93)
		.addSource("Site 2", 1.42, 2.90)
		.build();
System.out.println(a);

/* And calculate how much we need to bet for each to allow unbiased 
 * arbitrage (if the total capital we want to bet is 100) */
System.out.println(">>Profits");
System.out.println(a.bet(100));
```

Which outputs:

||Federer|Nadal|Profit %|
|:---|:---:|:---:|:---|
|Site 1|1.3|3.93|1.0236837|
|Site 2|1.42|2.9|1.049053|
|Best|1.42|3.93|0.95867825|

```
>>Profits
Arbitrage Odds: 0.95867825
Capital:	100.0
Win Odds:	[1.42, 3.93]
Weights:	[0.26542056, 0.73457944]
Betting:	[73.45794431397317, 26.542055033879485]
Profits:	[4.310277773368938, 4.310278055019708]
```
