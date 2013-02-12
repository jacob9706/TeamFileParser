package jacob.program.utils;

public class Input {
	
	private String matchNumber, teamNumber, color;
	
	public Input(String matchNumber, String teamNumber, String color) {
		this.setMatchNumber(matchNumber);
		this.setTeamNumber(teamNumber);
		this.setColor(color);
	}

	public String getMatchNumber() {
		return matchNumber;
	}

	public void setMatchNumber(String matchNumber) {
		this.matchNumber = matchNumber;
	}

	public String getTeamNumber() {
		return teamNumber;
	}

	public void setTeamNumber(String teamNumber) {
		this.teamNumber = teamNumber;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}
	
	public String toString() {
		return matchNumber + "," + teamNumber + "," + color;
	}
	
}
