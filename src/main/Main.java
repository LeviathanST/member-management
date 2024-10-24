public class Main {
	public static void main(String[] args) {
		try {
			while (true) {
				Database.connection();
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

}
