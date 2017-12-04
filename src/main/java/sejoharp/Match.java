package sejoharp;

import java.util.Objects;

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

    public String toFormattedString() {
        return String.format("Tisch:%s | Disziplin:%s | Runde:%s | %s vs. %s", tableNumber,
                discipline, round, team1, team2);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Match match = (Match) o;
        return Objects.equals(team1, match.team1) &&
                Objects.equals(team2, match.team2) &&
                Objects.equals(tableNumber, match.tableNumber) &&
                Objects.equals(discipline, match.discipline) &&
                Objects.equals(round, match.round);
    }

    @Override
    public int hashCode() {
        return Objects.hash(team1, team2, tableNumber, discipline, round);
    }

    @Override
    public String toString() {
        return "Match{" +
                "team1='" + team1 + '\'' +
                ", team2='" + team2 + '\'' +
                ", tableNumber='" + tableNumber + '\'' +
                ", discipline='" + discipline + '\'' +
                ", round='" + round + '\'' +
                '}';
    }
}
