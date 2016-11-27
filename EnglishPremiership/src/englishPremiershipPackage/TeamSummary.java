package englishPremiershipPackage;

/*
 * Данные о команде (для турнирной таблицы)
 */

public class TeamSummary implements Comparable<TeamSummary> {//интерфейс Comparable - для сравнения объектов

    //поля
    private String team;
    private int played;
    private int scored;
    private int conceded;
    private int points;

    //конструктор
    public TeamSummary(String teamName, int gamesPlayed, int goalsScored, int goalsConceded, int points) {
        team = teamName;
        played = gamesPlayed;
        scored = goalsScored;
        conceded = goalsConceded;
        this.points = points;
    }

    //геттеры
    public String getTeamName() {
        return team;
    }

    public int getGamesPlayed() {
        return played;
    }

    public int getGoalsScored() {
        return scored;
    }

    public int getGoalsConceded() {
        return conceded;
    }

    public int getPoints() {
        return points;
    }

    //сервис методы
    //подсчет разницы голов
    public int getGoalDifference() {
        return scored - conceded;
    }

    //обработка результата матча
    public void processMatch(int goalsScored, int goalsConceded) {

        //обновление счетчика матчей и числа забитых/пропущенных голов
        played++;
        scored = scored + goalsScored;
        conceded = conceded + goalsConceded;

        //посчитать очки
        if (goalsScored > goalsConceded) {
            //за победу 3 очка
            points = points + 3;

        } else if (goalsScored == goalsConceded) {
            //за ничью 1 очко
            points = points + 1;
        }
    }

    // метод для сравнения обектов при сортировке команд
    @Override
    public int compareTo(TeamSummary other) {
        // сравнение по очкам и голам

        //получить разницы
        int pointsDifference = this.getPoints() - other.getPoints();
        int goalsDifference = this.getGoalDifference() - other.getGoalDifference();

        //сравние по очкам
        if (pointsDifference > 0) {
            //текущая команда выше
            return 1;

        } else if (pointsDifference < 0) {
            //текущая команда ниже
            return -1;
 
        } else {
            //если очки одинаковые - сравнение по голам
            if (goalsDifference > 0) {
                //текущая команда выше
                return 1;

            } else if (goalsDifference < 0) {
                //текущая команда ниже
                return -1;

            } else {
                // если все показатели равны
                return 0;
            }

        }
    }
}
