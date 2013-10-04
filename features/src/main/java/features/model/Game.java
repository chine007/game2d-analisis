package features.model;

import java.util.Set;

public class Game {
	private String id;
	private Integer levels;
	private Set<Feature> features;
	private Set<ProfileGame> profileGame;
	
	public String getId() {
		return id;
	}
	public Integer getLevels() {
		return levels;
	}
	public Set<Feature> getFeatures() {
		return features;
	}
	public Set<ProfileGame> getProfileGame() {
		return profileGame;
	}
	@Override
	public String toString() {
		return id;
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
		Game other = (Game) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
