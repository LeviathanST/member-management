package models;

import lombok.Getter;

public class Guild {
	@Getter
	private Integer id;
	@Getter
	private String name;
	@Getter
	private String code;

	public Guild(int id, String name) {
		this.id = id;
		this.name = name;
	}
}
