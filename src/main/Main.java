public class Main {
	public static void main(String[] args) {
		try {
			Database.connection();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
