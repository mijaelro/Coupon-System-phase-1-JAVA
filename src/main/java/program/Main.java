package program;

import job.ExpirationDailyJob;
import test.Test;

public class Main {
	public static void main(String[] args) {

		ExpirationDailyJob exp = new ExpirationDailyJob();
		exp.start();
		Test.testAll();

	}
}
