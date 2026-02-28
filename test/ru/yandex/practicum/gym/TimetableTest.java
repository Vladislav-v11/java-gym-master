package ru.yandex.practicum.gym;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TimetableTest {
    Timetable timetable;

    @BeforeEach
    void makeTimeTable() {
        timetable = new Timetable();
    }

    @DisplayName("Одна тренировка в день")
    @Test
    void testGetTrainingSessionsForDaySingleSession() {
        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        //Проверить, что за понедельник вернулось одно занятие
        TreeMap<TimeOfDay, List<TrainingSession>> mondaySessions
                = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        assertEquals(1, mondaySessions.size());

        //Проверить, что за вторник не вернулось занятий
        TreeMap<TimeOfDay, List<TrainingSession>> tuesdaySessions
                = timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY);
        assertTrue(tuesdaySessions.isEmpty());
    }

    @DisplayName("Несколько тренировок в день в разное время")
    @Test
    void testGetTrainingSessionsForDayMultipleSessions() {
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");

        Group groupAdult = new Group("Акробатика для взрослых", Age.ADULT, 90);
        TrainingSession thursdayAdultTrainingSession = new TrainingSession(groupAdult, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(20, 0));

        timetable.addNewTrainingSession(thursdayAdultTrainingSession);

        Group groupChild = new Group("Акробатика для детей", Age.CHILD, 60);
        TrainingSession mondayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        TrainingSession thursdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(13, 0));
        TrainingSession saturdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.SATURDAY, new TimeOfDay(10, 0));

        timetable.addNewTrainingSession(mondayChildTrainingSession);
        timetable.addNewTrainingSession(thursdayChildTrainingSession);
        timetable.addNewTrainingSession(saturdayChildTrainingSession);

        // Проверить, что за понедельник вернулось одно занятие
        List<TrainingSession> mondaySessions = timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY,
                new TimeOfDay(13, 0));
        assertEquals(1, mondaySessions.size());

        // Проверить, что за четверг вернулось два занятия
        TreeMap<TimeOfDay, List<TrainingSession>> thursdaySessions
                = timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY);
        assertEquals(2, thursdaySessions.size());

        // Проверить, что за четверг вернулось два занятия в правильном порядке: сначала в 13:00, потом в 20:00
        NavigableSet<TimeOfDay> keys = thursdaySessions.navigableKeySet();
        assertEquals(new TimeOfDay(13, 0), keys.first());
        assertEquals(new TimeOfDay(20, 0), keys.last());

        // Проверить, что за вторник не вернулось занятий
        TreeMap<TimeOfDay, List<TrainingSession>> tuesdaySessions
                = timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY);
        assertTrue(tuesdaySessions.isEmpty());
    }

    @DisplayName("Получение тренировки за конкретное время")
    @Test
    void testGetTrainingSessionsForDayAndTime() {
        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        //Проверить, что за понедельник в 13:00 вернулось одно занятие
        List<TrainingSession> mondaySessions13 = timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY,
                new TimeOfDay(13, 0));
        assertEquals(1, mondaySessions13.size());

        //Проверить, что за понедельник в 14:00 нет занятий
        List<TrainingSession> mondaySessions14 = timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY,
                new TimeOfDay(14, 0));
        assertTrue(mondaySessions14.isEmpty());
    }

    @DisplayName("Подсчет тренировок у одного тренера")
    @Test
    void testGetCountByCoachesSingleCoach() {
        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach, DayOfWeek.MONDAY,
                new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        List<CounterOfTrainings> coachCounts = timetable.getCountByCoaches();
        assertEquals(1, coachCounts.size());
        assertEquals(coach, coachCounts.get(0).getCoach());
        assertEquals(1, coachCounts.get(0).getCount());
    }

    @DisplayName("Подсчет и сортировка тренировок у нескольких тренеров")
    @Test
    void testGetCountByCoachesMultipleCoaches() {
        Coach coach1 = new Coach("Васильев", "Николай", "Сергеевич");
        Coach coach2 = new Coach("Иванов", "Иван", "Иванович");

        Group group1 = new Group("Акробатика для взрослых", Age.ADULT, 90);
        TrainingSession thursdayAdultTrainingSession1 = new TrainingSession(group1, coach1, DayOfWeek.THURSDAY,
                new TimeOfDay(20, 0));
        TrainingSession mondayAdultTrainingSession2 = new TrainingSession(group1, coach2, DayOfWeek.MONDAY,
                new TimeOfDay(14, 0));

        timetable.addNewTrainingSession(thursdayAdultTrainingSession1);
        timetable.addNewTrainingSession(mondayAdultTrainingSession2);

        Group group2 = new Group("Акробатика для детей", Age.CHILD, 60);
        TrainingSession saturdayChildTrainingSession2 = new TrainingSession(group2, coach2, DayOfWeek.SATURDAY,
                new TimeOfDay(10, 0));

        timetable.addNewTrainingSession(saturdayChildTrainingSession2);

        List<CounterOfTrainings> coachCounts = timetable.getCountByCoaches();
        // проверить, что вернулось число занятий для тренеров
        assertEquals(2, coachCounts.size());
        assertTrue(coachCounts.contains(new CounterOfTrainings(coach1, 1)));
        assertTrue(coachCounts.contains(new CounterOfTrainings(coach2, 2)));

        // проверка сортировки по количеству тренировок
        assertTrue(coachCounts.get(0).getCount() >= coachCounts.get(1).getCount());
    }

    @DisplayName("Отсутствие тренировок у тренера")
    @Test
    void testGetCountByCoachesZeroTrainings() {
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");

        List<CounterOfTrainings> coachCounts = timetable.getCountByCoaches();
        assertEquals(0, coachCounts.size());
    }

}
