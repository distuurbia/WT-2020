package epam.webtech.model.race;

import epam.webtech.model.Entity;
import epam.webtech.model.enums.RaceStatus;
import epam.webtech.model.horse.Horse;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Data
public class Race extends Entity implements Comparable<Race> {

    @Override
    public int compareTo(Race o) {
        if (status.getPriority() == o.status.getPriority()) {
            return date.compareTo(o.date);
        } else
            return o.status.getPriority() - status.getPriority();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Race race = (Race) o;
        return getDate().equals(race.getDate()) &&
                getHorses().equals(race.getHorses());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getDate(), getHorses());
    }

    private Date date;
    private RaceStatus status;
    private List<Horse> horses = new ArrayList<>();
    private Horse winnerHorse;

}
