package englishPremiershipPackage;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

/*
 * Турнирная таблица по футболу
 */

public class FootballTable extends JFrame implements ActionListener {

    //компоненты формы
    private JPanel infoPanel;
    private JPanel mainPanel;
    private JPanel matchPanel;
    private JPanel tablePanel;
    private JButton jbtnDisplayTeamInfo;
    private JButton jbtnEPLTable;
    private JButton jbtnMatchResult;
    private JButton jbtnSaveTeamInfo;
    private JButton jbtnSetResult;
    private JLabel jlblAwayTeamCaption;
    private JLabel jlblResultCaption;
    private JLabel jlblEPLTableCaption;
    private JLabel jlblHomeTeamCaption;
    private JLabel jlblMatchResultCaption;
    private JLabel jlblTeamInfoCaption;
    private JList jlstAwayTeam;
    private JList jlstHomeTeam;
    private JTable jtblTeamInfo;
    private JTable jtblEPL;
    private JTextField jtxtMatchResult;
    private JScrollPane spAway;
    private JScrollPane spEPLTable;
    private JScrollPane spHome;
    private JScrollPane spTeamInfo;

    //модели для таблиц и списков
    private DefaultTableModel teamInfoModel, eplTableModel;
    private DefaultListModel homeTeamModel, awayTeamModel;

    //константы для панелей
    private final String INFO = "infoPanel";
    private final String TABLE = "tablePanel";
    private final String MATCH = "matchPanel";

    //путь к файлу (может находиться в папке c программой, тогда можно просто указать имя файла)
    private final String FILEPATH = "football_data.txt";

    //типизированный список (коллекция)
    ArrayList<TeamSummary> teams = new ArrayList<>();
    
    //объявдение метода main
    public static void main(String args[]) {
    //лямбда-выражение, альтернатива анонимному классу
        EventQueue.invokeLater(() -> {
            new FootballTable().setVisible(true);
        });
    }

    //конструктор
    public FootballTable() {
        //создание графических компонентов
        initComponents();

        //загрузка данных из файла
        loadFile(FILEPATH);

        //заполнение таблиц
        populateInfoTable();
        populateEPLTable();

        //заполнение списков
        populateLists();
    }

    private void initComponents() {

        //панели
        mainPanel = new JPanel();
        infoPanel = new JPanel();
        tablePanel = new JPanel();
        matchPanel = new JPanel();

        mainPanel.setBackground(new Color(255, 255, 255));
        infoPanel.setBackground(new Color(157, 194, 215));
        tablePanel.setBackground(new Color(166, 215, 157));
        matchPanel.setBackground(new Color(194, 213, 250));

        mainPanel.setLayout(new CardLayout());
        mainPanel.add(infoPanel, INFO);
        mainPanel.add(tablePanel, TABLE);
        mainPanel.add(matchPanel, MATCH);

        //метки
        jlblTeamInfoCaption = new JLabel("Информация о командах");
        jlblEPLTableCaption = new JLabel("Таблица результатов");
        jlblMatchResultCaption = new JLabel("Результат матча");
        jlblHomeTeamCaption = new JLabel("Выберите первую команду");
        jlblAwayTeamCaption = new JLabel("Выберите вторую команду");
        jlblResultCaption = new JLabel("Результаты матча (голы:голы)");

        jlblTeamInfoCaption.setFont(new Font("Arial Black", 1, 18));
        jlblTeamInfoCaption.setHorizontalAlignment(SwingConstants.CENTER);

        jlblEPLTableCaption.setFont(new Font("Arial Black", 1, 18));
        jlblEPLTableCaption.setHorizontalAlignment(SwingConstants.CENTER);

        jlblMatchResultCaption.setFont(new Font("Arial Black", 1, 18));
        jlblMatchResultCaption.setHorizontalAlignment(SwingConstants.CENTER);

        jlblHomeTeamCaption.setFont(new Font("Tahoma", 1, 11));
        jlblAwayTeamCaption.setFont(new Font("Tahoma", 1, 11));
        jlblResultCaption.setFont(new Font("Tahoma", 1, 11));

        //текстовое поле
        jtxtMatchResult = new JTextField();

        //кнопки
        jbtnDisplayTeamInfo = new JButton("Информация о команде");
        jbtnEPLTable = new JButton("Таблица");
        jbtnMatchResult = new JButton("Результаты");
        jbtnSaveTeamInfo = new JButton("Сохранить в файл");
        jbtnSetResult = new JButton("Записать результат");
        jbtnDisplayTeamInfo.addActionListener(this);
        jbtnEPLTable.addActionListener(this);
        jbtnMatchResult.addActionListener(this);
        jbtnSaveTeamInfo.addActionListener(this);
        jbtnSetResult.addActionListener(this);

        //таблицы

        //модели для таблиц для изменения данных
        teamInfoModel = new DefaultTableModel();
        eplTableModel = new DefaultTableModel();

        //установить модели для таблиц
        jtblTeamInfo = new JTable(teamInfoModel);
        jtblEPL = new JTable(eplTableModel);

        //списки

        //модели для списков
        homeTeamModel = new DefaultListModel();
        awayTeamModel = new DefaultListModel();

        //установить модели для списков
        jlstHomeTeam = new JList(homeTeamModel);
        jlstAwayTeam = new JList(awayTeamModel);

        //панели прокрутки
        spTeamInfo = new JScrollPane();
        spEPLTable = new JScrollPane();
        spHome = new JScrollPane();
        spAway = new JScrollPane();
        spTeamInfo.setViewportView(jtblTeamInfo);
        spEPLTable.setViewportView(jtblEPL);
        spHome.setViewportView(jlstHomeTeam);
        spAway.setViewportView(jlstAwayTeam);

        //панель с информацией о командах
        GroupLayout infoPanelLayout = new GroupLayout(infoPanel);
        infoPanel.setLayout(infoPanelLayout);
        infoPanelLayout.setHorizontalGroup(
          infoPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(infoPanelLayout.createSequentialGroup()
              .addGroup(infoPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(infoPanelLayout.createSequentialGroup()
                  .addGap(26)
                    .addComponent(spTeamInfo, GroupLayout.PREFERRED_SIZE, 516, GroupLayout.PREFERRED_SIZE))
                      .addGroup(infoPanelLayout.createSequentialGroup()
                        .addGap(140)
                          .addComponent(jlblTeamInfoCaption)))
                            .addContainerGap(23, Short.MAX_VALUE)));
        infoPanelLayout.setVerticalGroup(
          infoPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(infoPanelLayout.createSequentialGroup()
              .addGap(13)
                .addComponent(jlblTeamInfoCaption)
                  .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(spTeamInfo, GroupLayout.PREFERRED_SIZE, 343, GroupLayout.PREFERRED_SIZE)
                      .addContainerGap(19, Short.MAX_VALUE)));

        
        //турнирная таблица
        GroupLayout tablePanelLayout = new GroupLayout(tablePanel);
        tablePanel.setLayout(tablePanelLayout);
        tablePanelLayout.setHorizontalGroup(
          tablePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(tablePanelLayout.createSequentialGroup()
              .addGroup(tablePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(tablePanelLayout.createSequentialGroup()
                  .addGap(26)
                    .addComponent(spEPLTable, GroupLayout.PREFERRED_SIZE, 516, GroupLayout.PREFERRED_SIZE))
                      .addGroup(tablePanelLayout.createSequentialGroup()
                        .addGap(167)
                          .addComponent(jlblEPLTableCaption)))
                            .addContainerGap(1, Short.MAX_VALUE)));
        tablePanelLayout.setVerticalGroup(
          tablePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(tablePanelLayout.createSequentialGroup()
              .addGap(13)
                .addComponent(jlblEPLTableCaption)
                  .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(spEPLTable, GroupLayout.PREFERRED_SIZE, 343, GroupLayout.PREFERRED_SIZE)
                      .addContainerGap(1, Short.MAX_VALUE)));

        
        //панель для ввода результатов матча
        GroupLayout matchPanelLayout = new GroupLayout(matchPanel);
        matchPanel.setLayout(matchPanelLayout);
        matchPanelLayout.setHorizontalGroup(
          matchPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(matchPanelLayout.createSequentialGroup()
              .addGap(31)
                .addGroup(matchPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                  .addComponent(jlblHomeTeamCaption)
                    .addComponent(spHome, GroupLayout.PREFERRED_SIZE, 227, GroupLayout.PREFERRED_SIZE)
                      .addComponent(jlblResultCaption, GroupLayout.DEFAULT_SIZE, 1, Short.MAX_VALUE))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                          .addGroup(matchPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addGroup(matchPanelLayout.createSequentialGroup()
                              .addComponent(spAway, GroupLayout.PREFERRED_SIZE, 237, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
                                  .addGroup(matchPanelLayout.createSequentialGroup()
                                    .addGroup(matchPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                      .addGroup(matchPanelLayout.createSequentialGroup()
                                        .addComponent(jtxtMatchResult, GroupLayout.PREFERRED_SIZE, 69, GroupLayout.PREFERRED_SIZE)
                                          .addGap(18)
                                            .addComponent(jbtnSetResult))
                                              .addComponent(jlblAwayTeamCaption, GroupLayout.DEFAULT_SIZE, 241, Short.MAX_VALUE))
                                                .addContainerGap())))
                                                  .addGroup(matchPanelLayout.createSequentialGroup()
                                                    .addGap(188)
                                                      .addComponent(jlblMatchResultCaption)
                                                        .addContainerGap(166, Short.MAX_VALUE)));
        matchPanelLayout.setVerticalGroup(
          matchPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(matchPanelLayout.createSequentialGroup()
              .addGap(20)
                .addComponent(jlblMatchResultCaption)
                  .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                    .addGroup(matchPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                      .addComponent(jlblHomeTeamCaption)
                        .addComponent(jlblAwayTeamCaption))
                          .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(matchPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                              .addComponent(spHome, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
                                .addComponent(spAway, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE))
                                  .addGap(9)
                                    .addGroup(matchPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                      .addComponent(jlblResultCaption)
                                        .addComponent(jtxtMatchResult, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                          .addComponent(jbtnSetResult))
                                            .addContainerGap(192, Short.MAX_VALUE)));

        
        //менеджер расположения для главной панели фрейма
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
          layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
              .addComponent(jbtnDisplayTeamInfo)
                .addGap(18)
                  .addComponent(jbtnEPLTable)
                    .addGap(18)
                      .addComponent(jbtnMatchResult)
                        .addGap(18)
                          .addComponent(jbtnSaveTeamInfo)
                            .addGap(37))
                              .addComponent(mainPanel, GroupLayout.DEFAULT_SIZE, 465, Short.MAX_VALUE));
        layout.setVerticalGroup(
          layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
              .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(jbtnDisplayTeamInfo)
                  .addComponent(jbtnEPLTable)
                    .addComponent(jbtnMatchResult)
                      .addComponent(jbtnSaveTeamInfo))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                          .addComponent(mainPanel, GroupLayout.DEFAULT_SIZE, 401, Short.MAX_VALUE)));

        
        //свойства фрейма
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Чемпионат по футболу");
        setResizable(false);
        setLocation(200, 200);
        setSize(575, 470);
    }

    //определение нажатой кнопки
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == jbtnDisplayTeamInfo) {
            //показать информацию о команде
            displayCard(INFO);
        } else if (e.getSource() == jbtnEPLTable) {
            //показать турнирную таблицу
            displayCard(TABLE);
        } else if (e.getSource() == jbtnMatchResult) {
            //показать панель для ввода результатов матча
            displayCard(MATCH);
        } else if (e.getSource() == jbtnSaveTeamInfo) {
            // сохранить таблицу в файл
            saveFile(FILEPATH);
        } else if (e.getSource() == jbtnSetResult) {
            // сохранить результаты в таблицу
            processMatchResult();
        }
    }

    //показ нужной панели
    private void displayCard(String cardName) {
        CardLayout cl = (CardLayout) (mainPanel.getLayout());
        cl.show(mainPanel, cardName);
    }

    //заполнение списка команд
    private void populateLists() {

        String team;// хранит название команды

        //очистка всех данных в модели
        homeTeamModel.clear();
        awayTeamModel.clear();

        //проход по циклу и заполнение названий команд
        for (int i = 0; i < teams.size(); i++) {

            team = teams.get(i).getTeamName();

            homeTeamModel.addElement(team);
            awayTeamModel.addElement(team);
        }
    }

    //заполнение турнирной таблицы
    private void populateInfoTable() {

        String team;
        int played;
        int scored;
        int conceded;
        int points;

        //если нет столбцов - то добавить их
        if (teamInfoModel.getColumnCount() == 0) {
            //add columns
            teamInfoModel.addColumn("Команда");
            teamInfoModel.addColumn("Игры");
            teamInfoModel.addColumn("Голы");
            teamInfoModel.addColumn("Поражения");
            teamInfoModel.addColumn("Очки");
        }
        
        //задание требуемой ширины столбца
        jtblTeamInfo.getColumnModel().getColumn(0).setPreferredWidth(150);
        
        //очистить все данные таблицы
        if (teamInfoModel.getRowCount() > 0) {
            for (int i = teamInfoModel.getRowCount() - 1; i >= 0; i--) {
                teamInfoModel.removeRow(i);
            }
        }

        //создание строк с командами
        for (int i = 0; i < teams.size(); i++) {

            //получить значения
            team = teams.get(i).getTeamName();
            played = teams.get(i).getGamesPlayed();
            scored = teams.get(i).getGoalsScored();
            conceded = teams.get(i).getGoalsConceded();
            points = teams.get(i).getPoints();

            //добавляем в модель новую строку
            teamInfoModel.addRow(new Object[]{team, played, scored, conceded, points});
        }
    }

    //заполнение турнирной таблицы
    private void populateEPLTable() {

        //сортировка списка
        ArrayList<TeamSummary> sortedTeams = getSortedTeams();

        int position;
        String team;
        int played;
        int goalsDifference;
        int points;

        //если нет столбцов
        if (eplTableModel.getColumnCount() == 0) {
            //добавить столбцы
            eplTableModel.addColumn("Позиция");
            eplTableModel.addColumn("Команда");
            eplTableModel.addColumn("Игры");
            eplTableModel.addColumn("Разница голов");
            eplTableModel.addColumn("Очки");
        }
        
        //задание требуемой ширины столбцов
        jtblEPL.getColumnModel().getColumn(0).setPreferredWidth(40);
        jtblEPL.getColumnModel().getColumn(1).setPreferredWidth(150);
        
        //если есть какие то данные
        if (eplTableModel.getRowCount() > 0) {
            //удалить их
            for (int i = eplTableModel.getRowCount() - 1; i >= 0; i--) {
                eplTableModel.removeRow(i);
            }
        }

        //заполнить таблицу данными из списка
        for (int i = 0; i < sortedTeams.size(); i++) {

            //получить данные полей
            position = i + 1;
            team = sortedTeams.get(i).getTeamName();
            played = sortedTeams.get(i).getGamesPlayed();
            goalsDifference = sortedTeams.get(i).getGoalDifference();

            points = sortedTeams.get(i).getPoints();

            //добавление строки через модель
            eplTableModel.addRow(new Object[]{position, team, played, goalsDifference, points});
        }
    }

    //сортировка списка
    public ArrayList<TeamSummary> getSortedTeams() {
        //создать копию
        ArrayList<TeamSummary> copy = new ArrayList<>();

        //скопировать все команды в копию
        TeamSummary objTeam;
        for (int i = 0; i < teams.size(); i++) {
            objTeam = teams.get(i);
            copy.add(objTeam);
        }

        //соритировка
        Collections.sort(copy, Collections.reverseOrder());

        //возвратить отсортированный список
        return copy;
    }

    //добавление результата матча
    private void processMatchResult() {
        //получить индексы выбранных команд
        int homeTeamIndex = jlstHomeTeam.getSelectedIndex();
        int awayTeamIndex = jlstAwayTeam.getSelectedIndex();

        //получить введенный результат матча
        String matchResult = jtxtMatchResult.getText().trim();

        //если не выбрана первая команда
        if (homeTeamIndex == -1) {
            JOptionPane.showMessageDialog(this, "Выберите первую команду", "Ошибка", JOptionPane.WARNING_MESSAGE);

            //если не выбрана вторая команда
        } else if (awayTeamIndex == -1) {
            JOptionPane.showMessageDialog(this, "Выберите вторую команду", "Ошибка", JOptionPane.WARNING_MESSAGE);

            //если выбрана одна и та же команда
        } else if (homeTeamIndex == awayTeamIndex) {
            JOptionPane.showMessageDialog(this, "Команды должны быть разными", "Ошибка", JOptionPane.WARNING_MESSAGE);

            //если не введен результат
        } else if (matchResult.length() < 3) {
            JOptionPane.showMessageDialog(this, "Введите результаты матча в формате A:B\nA - голы, забитые первой командой, B - второй", "Ошибка", JOptionPane.WARNING_MESSAGE);

            //если все нормально - проводим подсчет
        } else {

            try {
                //получить голы
                int homeGoals = Integer.parseInt(matchResult.split(":")[0]);
                int awayGoals = Integer.parseInt(matchResult.split(":")[1]);

                //записать результат для первой команды
                teams.get(homeTeamIndex).processMatch(homeGoals, awayGoals);

                //записать результат для второй команды
                teams.get(awayTeamIndex).processMatch(awayGoals, homeGoals);

                //обновить таблицы
                populateInfoTable();
                populateEPLTable();

                //сообщение пользователю
                String homeTeam = teams.get(homeTeamIndex).getTeamName();
                String awayTeam = teams.get(awayTeamIndex).getTeamName();
                String message = String.format("Результат успешно добавлен:%n%23s %d%n%23s %d", homeTeam, homeGoals, awayTeam, awayGoals);
                JOptionPane.showMessageDialog(this, message, "Результат матча", JOptionPane.INFORMATION_MESSAGE);

                //ошибка NumberFormatException
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, e.getMessage() + "\n\nВведите результаты матча в формате A:B\nA - голы, забитые первой командой, B - второй", "Ошибка", JOptionPane.WARNING_MESSAGE);

                //другие ошибки
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, e.getMessage() + "\n\nВведите результаты матча в формате A:B\nA - голы, забитые первой командой, B - второй", "Ошибка", JOptionPane.WARNING_MESSAGE);
            }

        }
    }

    //загрузка данных из файла в список
    private void loadFile(String filePath) {

        // определяем файл
        File file = new File(filePath);

        //объекты для считывания файла
        FileReader inputStream;
        BufferedReader inputBuffer;
        Scanner inputScanner;

        String line;

        String team;
        int played;
        int scored;
        int conceded;
        int points;

        //если файл существует
        if (file.exists()) {

            // работа с файлами в блоке try-catch
            try {
                //объекты для считывания данных из файла
                inputStream = new FileReader(file);
                inputBuffer = new BufferedReader(inputStream);
                inputScanner = new Scanner(inputBuffer);

                // считать все строки
                while (inputScanner.hasNextLine()) {
                    //считывать построчно
                    line = inputScanner.nextLine();

                    //парсинг строки на поля
                    team = line.split(";")[0];
                    played = Integer.parseInt(line.split(";")[1]);
                    scored = Integer.parseInt(line.split(";")[2]);
                    conceded = Integer.parseInt(line.split(";")[3]);
                    points = Integer.parseInt(line.split(";")[4]);

                    //создание объекта и добавление в список
                    teams.add(new TeamSummary(team, played, scored, conceded, points));
                }

                //закрыть сканер, буфер и поток
                inputScanner.close();
                inputBuffer.close();
                inputStream.close();

                //ошибка ввода вывода
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), "Ошибка ввода/вывода", JOptionPane.WARNING_MESSAGE);

                // все остальные ошибки
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), "Ошибка", JOptionPane.WARNING_MESSAGE);
            }

        } else {
            //ошибка "Файл не существует"
            JOptionPane.showMessageDialog(this, "Файл не существует:\n" + filePath, "Ошибка", JOptionPane.WARNING_MESSAGE);
        }
    }

    //сохранение списка в файл
    private void saveFile(String filePath) {
        //объекты для записи в файл
        FileWriter outputStream;
        BufferedWriter outputBuffer;
        PrintWriter outputPrinter;

        String team;
        int played;
        int scored;
        int conceded;
        int points;

        // работа с файлами в блоке try-catch
        try {

            // поток для записи в файл
            outputStream = new FileWriter(filePath, false);

            // рекомендуется использовать буфер для записи
            outputBuffer = new BufferedWriter(outputStream);
            outputPrinter = new PrintWriter(outputBuffer);

            //цикл по командам
            for (int i = 0; i < teams.size(); i++) {

                //получить значения
                team = teams.get(i).getTeamName();
                played = teams.get(i).getGamesPlayed();
                scored = teams.get(i).getGoalsScored();
                conceded = teams.get(i).getGoalsConceded();
                points = teams.get(i).getPoints();

                //если это не первая строка - поставить пустую строку
                if (i > 0) {
                    outputPrinter.println();
                }

                //запись данных в файл
                outputPrinter.print(team + ";" + played + ";" + scored + ";" + conceded + ";" + points);
            }

            //закрыть printer, buffer и stream
            outputPrinter.close();
            outputBuffer.close();
            outputStream.close();

            //перехват ошибки IOException
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Output file error", JOptionPane.WARNING_MESSAGE);

            //перехват остальных ошибок
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Warning", JOptionPane.WARNING_MESSAGE);
        }

    }
}
