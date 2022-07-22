package com.example.bettominator.service.implementation;

import com.example.bettominator.model.Bilten;
import com.example.bettominator.model.SportsGame;
import com.example.bettominator.service.*;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class FillDataBaseServiceImpl implements FillDataBaseService {

    private final CategoryService categoryService;
    private final CountryService countryService;
    private final GameService gameService;
    private final BiltenService biltenService;
    private final ShoppingCartService shoppingCartService;
    private final BetInfoService betInfoService;
    private final BetLogsService betLogsService;
    private static Random random = new Random();

    public FillDataBaseServiceImpl(CategoryService categoryService, CountryService countryService, GameService gameService, BiltenService biltenService, ShoppingCartService shoppingCartService, BetInfoService betInfoService, BetLogsService betLogsService) {
        this.categoryService = categoryService;
        this.countryService = countryService;
        this.gameService = gameService;
        this.biltenService = biltenService;
        this.shoppingCartService = shoppingCartService;
        this.betInfoService = betInfoService;
        this.betLogsService = betLogsService;
    }

    @Override
    public void FillDataBase() throws IOException {
        this.betLogsService.deleteAll(this.betLogsService.findAll());
        this.shoppingCartService.deleteAll(this.shoppingCartService.findAll());
        this.betInfoService.deleteAll(this.betInfoService.findAll());
        this.biltenService.deleteAll(this.biltenService.findAll());
        this.gameService.deleteAll(this.gameService.findAll());
        Set<SportsGame> games = generateGames();
        List<Bilten> bilten = generateBilten(games);
        for(SportsGame sportsGame: games){
            this.gameService.saveInDataBase(sportsGame);
        }
        for(Bilten b: bilten){
            this.biltenService.saveInDataBase(b);
        }
    }

    @Override
    public List<LocalDateTime> crateDate(int i) {
        List<LocalDateTime> startTimes = new ArrayList<>();
        List<LocalDateTime> endTimes = new ArrayList<>();

        startTimes.add(LocalDateTime.now().minusHours(10));
        int rand1 = random.nextInt(10, 20);
        startTimes.add(LocalDateTime.now().minusMinutes(rand1));
        startTimes.add(LocalDateTime.now().plusHours(10));

        endTimes.add(LocalDateTime.now().minusHours(8));
        int rand2 = random.nextInt(90, 110);
        endTimes.add(LocalDateTime.now().plusMinutes(rand2));
        endTimes.add(LocalDateTime.now().plusHours(12));


        List<LocalDateTime> dates = new ArrayList<>();
        dates.add(startTimes.get(i));
        dates.add(endTimes.get(i));

        return dates;
    }

    @Override
    public Set<SportsGame> generateGames() throws IOException {
        Comparator<SportsGame> comparator = Comparator.comparing(SportsGame::getCompetitorOne).thenComparing(SportsGame::getCompetitorTwo);

        List<String> teams = new ArrayList<>();
        Set<SportsGame> gameList = new TreeSet<>(comparator);
        BufferedReader bf = new BufferedReader(new FileReader("C:\\Users\\Kenii\\Desktop\\bt5functional\\testdata.txt"));

        for(int i=0; i<6; i++){
            String line = bf.readLine();
            String [] parts = line.split("/");

            String country = parts[0].trim();

            String [] parts2 = parts[1].split(";");

            for(String s: parts2){
                String [] parts3 = s.split("~");

                String category = parts3[0];
                String team1 = parts3[1];
                String team2 = parts3[2];
                String team3 = parts3[3];
                String team4 = parts3[4];
                String team5 = parts3[5];
                teams = new ArrayList<>();
                teams.add(team1);
                teams.add(team2);
                teams.add(team3);
                teams.add(team4);
                teams.add(team5);

                for(int j=1; j<4; j++){
                    SportsGame g = new SportsGame();

                    String randTeam1 = teams.get(random.nextInt(teams.size()));
                    g.setCompetitorOne(randTeam1);
                    String randTeam2 = teams.get(random.nextInt(teams.size()));
                    while(randTeam1.equals(randTeam2)){
                        randTeam2=teams.get(random.nextInt(teams.size()));
                    }
                    g.setCompetitorTwo(randTeam2);

                    g.setCategory(categoryService.findByName(category));
                    g.setCountry(countryService.findByName(country));

                    int rand = random.nextInt(3);
                    List<LocalDateTime> times = crateDate(rand);
                    g.setStartOfMatch(times.get(0));
                    g.setEndOfMatch(times.get(1));

                    int competitorOnePoints = random.nextInt(5);
                    int competitorTwoPoints = random.nextInt(5);
                    g.setCompetitorOnePoints(competitorOnePoints);
                    g.setCompetitorTwoPoints(competitorTwoPoints);

                    int competitorOnePointsFH = random.nextInt(0, competitorOnePoints+1);
                    int competitorTwoPointsFH = random.nextInt(0, competitorTwoPoints+1);
                    g.setCompetitorOnePointsFH(competitorOnePointsFH);
                    g.setCompetitorTwoPointsFH(competitorTwoPointsFH);

                    gameList.add(g);
                }
            }

        }
        return gameList;
    }

    @Override
    public List<Bilten> generateBilten(Set<SportsGame> games) {
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        List<Bilten> biltens = new ArrayList<>();
        List<String> betTypes = new ArrayList<>();
        betTypes.add("PICKWINNER");
        betTypes.add("FIRSTHALF");
        betTypes.add("SECONDHALF");
        betTypes.add("ODDEVEN");
        for(SportsGame g:games){
            for(String s : betTypes){
                Bilten b = new Bilten();
                b.setBetType(s);
                b.setSportsGame(g);
                if(s.equals("ODDEVEN")){
                    double evencoef = random.nextDouble()*random.nextInt(2)+1;
                    if(evencoef<1) evencoef+=1;
                    b.setEvenCoef(Double.parseDouble(decimalFormat.format(evencoef)));
                    double oddcoef = random.nextDouble()*random.nextInt(2)+1;
                    if(oddcoef<1) oddcoef+=1;
                    b.setOddCoef(Double.parseDouble(decimalFormat.format(oddcoef)));
                }else{
                    double onecoef = random.nextDouble()*random.nextInt(8)+1;
                    if(onecoef<1) onecoef+=1;
                    b.setOneCoef(Double.parseDouble(decimalFormat.format(onecoef)));
                    double twocoef = random.nextDouble()*random.nextInt(8)+1;
                    if(twocoef<1) twocoef+=1;
                    b.setTwoCoef(Double.parseDouble(decimalFormat.format(twocoef)));
                    double drawcoef = random.nextDouble()*random.nextInt(8)+1;
                    if(drawcoef<1) drawcoef+=1;
                    b.setDrawCoef(Double.parseDouble(decimalFormat.format(drawcoef)));
                }
                biltens.add(b);
            }

        }
        return biltens;
    }
}
