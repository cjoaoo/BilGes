package business.utils;
import java.time.LocalDate;

public class MockData {
	
	private static LocalDate currentDate = LocalDate.of(2021, 5, 1);

	
	private MockData() {
		//vazio
	}
	
	public static LocalDate getCurrentDate() {
		return currentDate;
	}
}
