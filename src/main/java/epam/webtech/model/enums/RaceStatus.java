package epam.webtech.model.enums;

import epam.webtech.exceptions.NotFoundException;

public enum RaceStatus {

    WAITING(1),
    IN_PROGRESS(0),
    FINISHED(2);

    private int priority;

    public int getPriority() {
        return priority;
    }

    RaceStatus(int priority) {
        this.priority = priority;
    }

    public static RaceStatus getByPriority(int priority) throws NotFoundException {
        switch (priority) {
            case 0:
                return IN_PROGRESS;
            case 1:
                return WAITING;
            case 2:
                return FINISHED;
            default:
                throw new NotFoundException("RaceStatus with priority " + priority + " not found");
        }
    }

}
