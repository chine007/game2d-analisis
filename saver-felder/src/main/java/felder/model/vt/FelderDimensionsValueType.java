package felder.model.vt;

public class FelderDimensionsValueType {
	private int processing;
	private int perception;
	private int input;
	private int understanding;
	
	public FelderDimensionsValueType() {
	}
	public FelderDimensionsValueType(int processing, int perception, int input,
			int understanding) {
		this.processing = processing;
		this.perception = perception;
		this.input = input;
		this.understanding = understanding;
	}
	public int getProcessing() {
		return processing;
	}
	public int getPerception() {
		return perception;
	}
	public int getInput() {
		return input;
	}
	public int getUnderstanding() {
		return understanding;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + input;
		result = prime * result + perception;
		result = prime * result + processing;
		result = prime * result + understanding;
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
		FelderDimensionsValueType other = (FelderDimensionsValueType) obj;
		if (input != other.input)
			return false;
		if (perception != other.perception)
			return false;
		if (processing != other.processing)
			return false;
		if (understanding != other.understanding)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "FelderDimensionsValueType [processing=" + processing
				+ ", perception=" + perception + ", input=" + input
				+ ", understanding=" + understanding + "]";
	}
}
