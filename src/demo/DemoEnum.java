package demo;

import com.algonquincollege.megh0011.ictlabschedules.DOW;
import com.algonquincollege.megh0011.ictlabschedules.Periods;

public class DemoEnum {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		for ( Periods period : Periods.values() ) {
			System.out.println( "Period: " + period );
		}

		System.out.println();

		for ( DOW dow : DOW.values() ) {
			System.out.println( "DOW: " + dow );
		}
	}
}