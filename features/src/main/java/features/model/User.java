package features.model;

public class User {
	private String username;
	private String userGroup;
	private String firstName;
	private String lastName;
	private Integer processing;
	private Integer perception;
	private Integer input;
	private Integer understanding;

	public String getUsername() {
		return username;
	}
	public String getUserGroup() {
		return userGroup;
	}
	public String getFirstName() {
		return firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public Integer getProcessing() {
		return processing;
	}
	public Integer getPerception() {
		return perception;
	}
	public Integer getPerceptionByCategory() {
		if (perception > 3) {
			return 1;
		} else if (perception > -5) {
			return 2;
		} else {
			return 3;
		}
//		return perception;
	}
	public Integer getInput() {
		return input;
	}
	public Integer getUnderstanding() {
		return understanding;
	}
	public void setProcessing(Integer processing) {
		this.processing = processing;
	}
	public void setPerception(Integer perception) {
		this.perception = perception;
	}
	public void setInput(Integer input) {
		this.input = input;
	}
	public void setUnderstanding(Integer understanding) {
		this.understanding = understanding;
	}
	@Override
	public String toString() {
		return username + "\t" + firstName + "\t" + lastName + "\t"
				+ getPerceptionByCategory();
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((username == null) ? 0 : username.hashCode());
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
		User other = (User) obj;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}
}
