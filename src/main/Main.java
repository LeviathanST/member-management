import config.AppConfig;
import config.Database;
import dto.SignUpDTO;
import exceptions.DataEmptyException;
import exceptions.NotFoundException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import repositories.users.UserAccountRepository;
import repositories.users.UserRoleRepository;
import utils.EnvLoader;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

import static services.AuthService.hashingPassword;

@WebServlet("/")
public class Main extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		try {
			res.setContentType("text/html");
			try (PrintWriter out = res.writer) {
				out.print("Hello");
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
