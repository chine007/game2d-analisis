package features.utils;

import java.text.DecimalFormat;

import features.persistence.model.Game;
import features.persistence.model.User;

public class Data implements Comparable<Data> {
	private static final DecimalFormat df = new DecimalFormat("#0.000");
	private String featureCode;
	private Game game;
	private Double preference;
	private User user;
	
	
	public Data(String featureCode, Game game, Double preference, User user) {
		this.game = game;
		this.featureCode = featureCode;
		this.preference = preference;
		this.user = user;
	}
	public Game game() {
		return game;
	}
	public String getFeature() {
		return featureCode;
	}
	public Double getPreference() {
		return preference;
	}
	public String getPreferenceToString() {
		return df.format(preference);
	}
	public User getUser() {
		return user;
	}
	@Override
	public String toString() {
		return featureCode + "\t" + game + "\t" + getPreferenceToString()
		+ "\t" + user;
	}
	@Override
	public int compareTo(Data o) {
		return o.getPreference().compareTo(preference);
	}
}
