package data;

import java.sql.Date;
public class SignUpData {
	private String username;
	private String password;
	private String email;
	private boolean is_active;
	private Date created_at;
	private Date updated_at;
	private int count_mistake;

	public SignUpData(String username, String password, String email, boolean is_active,int count_mistake) {
		this.username = username;
		this.password = password;
		this.email = email;
		this.is_active = is_active;
		this.created_at = new Date(System.currentTimeMillis());
		this.updated_at = new Date(System.currentTimeMillis());
		this.count_mistake = count_mistake;
	}

	public String getEmail() {return this.email;}
	public void setEmail(String email) {this.email = email;}

	public boolean is_active() {return this.is_active;}
	public void set_is_active(boolean is_active) {this.is_active = is_active;}

	public Date getCreated_at() {return this.created_at;}

	public Date getUpdated_at() {return this.updated_at;}
	public void setUpdated_at(Date updated_at) {this.updated_at = updated_at;}

	public int getCount_mistake() {return this.count_mistake;}
	public void setCount_mistake(int count_mistake) {this.count_mistake = count_mistake;}

	public String getUsername() {
		return this.username;
	}
	public void setUserName(String username){this.username= username;}

	public String getPassword() {
		return this.password;
	}
	public void setPassword(String pwd) {
		this.password = pwd;
	}
}
