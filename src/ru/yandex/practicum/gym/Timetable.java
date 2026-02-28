package ru.yandex.practicum.gym;

import java.util.*;

public class Timetable {

    private Map<DayOfWeek, TreeMap<TimeOfDay, List<TrainingSession>>> timetable = new HashMap<>();
    private HashMap<Coach, Integer> coachesCounter = new HashMap<>();

    public void addNewTrainingSession(TrainingSession trainingSession) {
        //сохраняем занятие в расписании
        TreeMap<TimeOfDay, List<TrainingSession>> timeTableForDay
                = timetable.getOrDefault(trainingSession.getDayOfWeek(), new TreeMap<>());

        List<TrainingSession> listOfTrainingSession = timeTableForDay.getOrDefault(trainingSession.getTimeOfDay(),
                new ArrayList<>());
        listOfTrainingSession.add(trainingSession);

        timeTableForDay.put(trainingSession.getTimeOfDay(), listOfTrainingSession);
        timetable.put(trainingSession.getDayOfWeek(), timeTableForDay);
        // добавляем занятие в расписании конкретного тренера
        Coach currentCoach = trainingSession.getCoach();
        coachesCounter.put(currentCoach, coachesCounter.getOrDefault(currentCoach, 0) + 1);
    }

    public TreeMap<TimeOfDay, List<TrainingSession>> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        return timetable.getOrDefault(dayOfWeek, new TreeMap<>());
    }

    public List<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        TreeMap<TimeOfDay, List<TrainingSession>> dayTimeTable = timetable.getOrDefault(dayOfWeek, new TreeMap<>());
        return dayTimeTable.getOrDefault(timeOfDay, new ArrayList<>());
    }

    public List<CounterOfTrainings> getCountByCoaches() {
        List<CounterOfTrainings> counterOfTrainingsList = new ArrayList<CounterOfTrainings>();
        for (Coach coach : coachesCounter.keySet()) {
            CounterOfTrainings counterOfTrainings = new CounterOfTrainings(coach, coachesCounter.get(coach));
            counterOfTrainingsList.add(counterOfTrainings);
        }
        Collections.sort(counterOfTrainingsList);
        return counterOfTrainingsList;
    }
}

