package sejoharp;

public class Match {
    private String team1;
    private String team2;
    private String tableNumber;
    private String discipline;
    private String round;

    private Match(String team1, String team2, String tableNumber, String discipline, String round) {
        this.team1 = team1;
        this.team2 = team2;
        this.tableNumber = tableNumber;
        this.discipline = discipline;
        this.round = round;
    }

    private Match() {
    }

    public static Match emptyMatch() {
        return new Match();
    }

    public String getTeam1() {
        return team1;
    }

    public String getRound() {
        return round;
    }

    public String getTeam2() {
        return team2;
    }

    public String getTableNumber() {
        return tableNumber;
    }

    public String getDiscipline() {
        return discipline;
    }

    public Match withTeam2(String team2) {
        return new Match(team1, team2, tableNumber, discipline, round);
    }

    public Match withTeam1(String team1) {
        return new Match(team1, team2, tableNumber, discipline, round);
    }

    public Match withTableNumber(String tableNumber) {
        return new Match(team1, team2, tableNumber, discipline, round);
    }

    public Match withDiscipline(String discipline) {
        return new Match(team1, team2, tableNumber, discipline, round);
    }

    public Match withRound(String round) {
        return new Match(team1, team2, tableNumber, discipline, round);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((discipline == null) ? 0 : discipline.hashCode());
        result = prime * result + ((round == null) ? 0 : round.hashCode());
        result = prime * result + ((tableNumber == null) ? 0 : tableNumber.hashCode());
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
        if (discipline == null) {
            if (other.discipline != null)
                return false;
        } else if (!discipline.equals(other.discipline))
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

    @Override
    public String toString() {
        return "Match [team1=" + team1 + ", team2=" + team2 + ", tableNumber=" + tableNumber + ", discipline="
                + discipline + ", round=" + round + "]";
    }
}
