package features.model;

public class TestPreference {
	private Long id;
	private User user;
	private Game game;
	private Integer value;
	private Integer elapsedTime;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Game getGame() {
		return game;
	}
	public void setGame(Game game) {
		this.game = game;
	}
	public Integer getValue() {
		return value;
	}
	public void setValue(Integer value) {
		this.value = value;
	}
	public Integer getElapsedTime() {
		return elapsedTime;
	}
	public void setElapsedTime(Integer elapsedTime) {
		this.elapsedTime = elapsedTime;
	}
}
