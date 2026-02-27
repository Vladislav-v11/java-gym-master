package ru.yandex.practicum.gym;

import java.util.*;

public class Timetable {

    private Map<DayOfWeek, TreeMap<TimeOfDay, List<TrainingSession>>> timetable = new HashMap<>();

    public void addNewTrainingSession(TrainingSession trainingSession) {
        //сохраняем занятие в расписании
        TreeMap<TimeOfDay, List<TrainingSession>> timeTableForDay
                = timetable.getOrDefault(trainingSession.getDayOfWeek(), new TreeMap<>());

        List<TrainingSession> listOfTrainingSession = timeTableForDay.getOrDefault(trainingSession.getTimeOfDay(),
                new ArrayList<>());
        listOfTrainingSession.add(trainingSession);

        timeTableForDay.put(trainingSession.getTimeOfDay(), listOfTrainingSession);
        timetable.put(trainingSession.getDayOfWeek(), timeTableForDay);
    }

    public TreeMap<TimeOfDay, List<TrainingSession>> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        return timetable.getOrDefault(dayOfWeek, new TreeMap<>());
    }

    public List<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        TreeMap<TimeOfDay, List<TrainingSession>> dayTimeTable = timetable.getOrDefault(dayOfWeek, new TreeMap<>());
        return dayTimeTable.getOrDefault(timeOfDay, new ArrayList<>());
    }

    public List<CounterOfTrainings> getCountByCoaches() {
        Map<Coach, Integer> coachCounts = new HashMap<>();
        for (TreeMap<TimeOfDay, List<TrainingSession>> daySessions : timetable.values()) {
            for (List<TrainingSession> sessions : daySessions.values()) {
                for (TrainingSession session : sessions) {
                    Coach coach = session.getCoach();
                    coachCounts.put(coach, coachCounts.getOrDefault(coach, 0) + 1);
                }
            }
        }

        List<CounterOfTrainings> counterList = new ArrayList<>();
        for (Map.Entry<Coach, Integer> entry : coachCounts.entrySet()) {
            counterList.add(new CounterOfTrainings(entry.getKey(), entry.getValue()));
        }

        Collections.sort(counterList);
        return counterList;
    }

}
