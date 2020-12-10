package epam.webtech.model.bet;

import epam.webtech.model.Entity;
import epam.webtech.model.horse.Horse;
import epam.webtech.model.race.Race;
import epam.webtech.model.user.User;
import lombok.Data;

@Data
public class Bet extends Entity implements Comparable<Bet> {

    private int amount;
    private Race race;
    private Horse horse;
    private User user;

    @Override
    public int compareTo(Bet o) {
        return amount - o.getAmount();
    }
}
