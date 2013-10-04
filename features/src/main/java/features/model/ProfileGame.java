package features.model;

public class ProfileGame {
	private Long id;
	private User user;
	private Game game;
	private Long level;
	private Double time;
	private String won;
	private Long correctAnswers;
	private Long incorrectAnswers;
	private Long movs;
	private Long movsIn;
	private Long movsOut;
	private Long help;
	private Long timeout;
	private Long trace;
	private Long timesPlayed;
	private String log;
	
	public Long getId() {
		return id;
	}
	public User getUser() {
		return user;
	}
	public Game getGame() {
		return game;
	}
	public Long getLevel() {
		return level;
	}
	public Double getTime() {
		return time;
	}
	public String getWon() {
		return won;
	}
	public Long getCorrectAnswers() {
		return correctAnswers;
	}
	public Long getIncorrectAnswers() {
		return incorrectAnswers;
	}
	public Long getMovs() {
		return movs;
	}
	public Long getMovsIn() {
		return movsIn;
	}
	public Long getMovsOut() {
		return movsOut;
	}
	public Long getHelp() {
		return help;
	}
	public Long getTimeout() {
		return timeout;
	}
	public Long getTrace() {
		return trace;
	}
	public Long getTimesPlayed() {
		return timesPlayed;
	}
	public String getLog() {
		return log;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProfileGame other = (ProfileGame) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "ProfileGame [id=" + id + ", user=" + user + ", game=" + game
				+ ", level=" + level + "]";
	}
}
