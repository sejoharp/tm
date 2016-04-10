package sejoharp;

public class Match {
	private String team1;
	private String team2;
	private String tableNumber;
	private String disciplin;
	private String round;
	private String notificationEmail;

	public String getTeam1() {
		return team1;
	}

	public void setTeam1(String team1) {
		this.team1 = team1;
	}

	public String getTeam2() {
		return team2;
	}

	public void setTeam2(String team2) {
		this.team2 = team2;
	}

	public String getTableNumber() {
		return tableNumber;
	}

	public void setTableNumber(String tableNumber) {
		this.tableNumber = tableNumber;
	}

	public String getDisciplin() {
		return disciplin;
	}

	public void setDisciplin(String disciplin) {
		this.disciplin = disciplin;
	}

	public String getRound() {
		return round;
	}

	public void setRound(String round) {
		this.round = round;
	}

	@Override
	public String toString() {
		return "Match [team1=" + team1 + ", team2=" + team2 + ", tableNumber="
				+ tableNumber + ", disciplin=" + disciplin + ", round=" + round
				+ "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((disciplin == null) ? 0 : disciplin.hashCode());
		result = prime * result + ((round == null) ? 0 : round.hashCode());
		result = prime * result
				+ ((tableNumber == null) ? 0 : tableNumber.hashCode());
		result = prime * result + ((team1 == null) ? 0 : team1.hashCode());
		result = prime * result + ((team2 == null) ? 0 : team2.hashCode());
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
		Match other = (Match) obj;
		if (disciplin == null) {
			if (other.disciplin != null)
				return false;
		} else if (!disciplin.equals(other.disciplin))
			return false;
		if (round == null) {
			if (other.round != null)
				return false;
		} else if (!round.equals(other.round))
			return false;
		if (tableNumber == null) {
			if (other.tableNumber != null)
				return false;
		} else if (!tableNumber.equals(other.tableNumber))
			return false;
		if (team1 == null) {
			if (other.team1 != null)
				return false;
		} else if (!team1.equals(other.team1))
			return false;
		if (team2 == null) {
			if (other.team2 != null)
				return false;
		} else if (!team2.equals(other.team2))
			return false;
		return true;
	}

	public String getNotificationEmail() {
		return notificationEmail;
	}

	public void setNotificationEmail(String notificationEmail) {
		this.notificationEmail = notificationEmail;
	}
}
