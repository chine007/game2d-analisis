package features.persistence.model;

import java.util.Set;

public class GameCategory {
	private Integer id;
	private String code;
	private Set<Feature> features;
	
	public Integer getId() {
		return id;
	}
	public String getCode() {
		return code;
	}
	public Set<Feature> getFeatures() {
		return features;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
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
		GameCategory other = (GameCategory) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "GameCategory [code=" + code + "]";
	}
}
