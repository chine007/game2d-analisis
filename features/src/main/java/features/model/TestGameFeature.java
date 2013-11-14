package features.model;

public class TestGameFeature {
	private Long id;
	private User user;
	private Game game;
	private Feature feature;
	private Integer value;
	
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
	public Feature getFeature() {
		return feature;
	}
	public void setFeature(Feature feature) {
		this.feature = feature;
	}
	public Integer getValue() {
		return value;
	}
	public void setValue(Integer value) {
		this.value = value;
	}
}
