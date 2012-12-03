package felder.model;

import java.util.Arrays;
import java.util.Calendar;

import felder.model.vt.FelderDimensionsValueType;

public class QuestionnaireILS {
	private Integer id;
	private String firstName;
	private String lastName;
	private String city;
	private Calendar birthDate;
	private String sex;
	private String mail;
	private String country;
	private String province;
	private FelderDimensionsValueType felder;
	private byte[] questionnaire;
	private Calendar registrationDate;
	
	public Integer getId() {
		return id;
	}
	public String getFirstName() {
		return firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public String getCity() {
		return city;
	}
	public Calendar getBirthDate() {
		return birthDate;
	}
	public String getSex() {
		return sex;
	}
	public String getMail() {
		return mail;
	}
	public String getCountry() {
		return country;
	}
	public String getProvince() {
		return province;
	}
	public FelderDimensionsValueType getFelder() {
		return felder;
	}
	public byte[] getQuestionnaire() {
		return questionnaire;
	}
	public Calendar getRegistrationDate() {
		return registrationDate;
	}
	@Override
	public String toString() {
		return "CuestionarioILS [id=" + id + ", firstName=" + firstName
				+ ", lastName=" + lastName + ", city=" + city + ", birthDate="
				+ birthDate + ", sex=" + sex + ", mail=" + mail + ", country="
				+ country + ", province=" + province + ", felder=" + felder
				+ ", questionnaire=" + Arrays.toString(questionnaire)
				+ ", registrationDate=" + registrationDate + "]";
	}
	
	public FelderDimensionsValueType calculate() {
		int[] felder = new int[4];
		byte[] quest = getQuestionnaire();
		
		int index = 0;
		for (int i = 0; i < quest.length; i++) {
			if (quest[i] == 0) {
				felder[index]++;
			} else {
				felder[index]--;
			}
			index = (index + 1) % 4;
		}
		
		return new FelderDimensionsValueType(felder[0], felder[1], felder[2], felder[3]);
	}
	
}
