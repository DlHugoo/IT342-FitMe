package edu.cit.fitme.service;

import edu.cit.fitme.entity.WorkoutDayEntity;
import edu.cit.fitme.entity.WorkoutEntity;
import edu.cit.fitme.repository.WorkoutDayRepository;
import edu.cit.fitme.repository.WorkoutRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WorkoutDayService {

    private final WorkoutDayRepository workoutDayRepository;
    private final WorkoutRepository workoutRepository;
    private final GoogleCalendarService googleCalendarService;

    public WorkoutDayService(
            WorkoutDayRepository workoutDayRepository,
            WorkoutRepository workoutRepository,
            GoogleCalendarService googleCalendarService
    ) {
        this.workoutDayRepository = workoutDayRepository;
        this.workoutRepository = workoutRepository;
        this.googleCalendarService = googleCalendarService;
    }

    public List<WorkoutDayEntity> getAllDays() {
        return workoutDayRepository.findAll();
    }

    public List<WorkoutDayEntity> getDaysByWorkoutId(Long workoutId) {
        return workoutDayRepository.findByWorkoutWorkoutId(workoutId);
    }

    public Optional<WorkoutDayEntity> getDayById(Long id) {
        return workoutDayRepository.findById(id);
    }

    public WorkoutDayEntity createDay(WorkoutDayEntity day, String accessToken) {
        Long workoutId = day.getWorkout().getWorkoutId();
        WorkoutEntity fullWorkout = workoutRepository.findById(workoutId)
                .orElseThrow(() -> new RuntimeException("Workout not found with id: " + workoutId));

        day.setWorkout(fullWorkout);
        day.setRestDay(day.isRestDay());

        WorkoutDayEntity savedDay = workoutDayRepository.save(day);

        // 🔁 Update workout day count
        updateWorkoutDayCount(workoutId);

        // 🔄 Automatically sync to Google Calendar
        try {
            String calendarEventId = googleCalendarService.createCalendarEventForWorkoutDay(accessToken, savedDay);
            savedDay.setCalendarEventId(calendarEventId);
            savedDay = workoutDayRepository.save(savedDay);
        } catch (Exception e) {
            System.err.println("⚠️ Failed to sync with Google Calendar: " + e.getMessage());
        }

        return savedDay;
    }


    public Optional<WorkoutDayEntity> updateDay(Long id, WorkoutDayEntity updatedDay) {
        return workoutDayRepository.findById(id).map(day -> {
            day.setDayNumber(updatedDay.getDayNumber());
            day.setRestDay(updatedDay.isRestDay());

            return workoutDayRepository.save(day);
        });
    }

    public void deleteDay(Long id, String accessToken) {
        Optional<WorkoutDayEntity> maybeDay = workoutDayRepository.findById(id);

        maybeDay.ifPresent(day -> {
            Long workoutId = day.getWorkout().getWorkoutId();

            // 🗑 Delete Google Calendar event if it exists
            if (day.getCalendarEventId() != null) {
                try {
                    googleCalendarService.deleteEvent(accessToken, day.getCalendarEventId());
                } catch (Exception e) {
                    System.err.println("⚠️ Failed to delete Google Calendar event: " + e.getMessage());
                }
            }

            workoutDayRepository.deleteById(id);
            updateWorkoutDayCount(workoutId);
        });
    }


    private void updateWorkoutDayCount(Long workoutId) {
        List<WorkoutDayEntity> allDays = workoutDayRepository.findByWorkoutWorkoutId(workoutId);
        WorkoutEntity workout = workoutRepository.findById(workoutId)
                .orElseThrow(() -> new RuntimeException("Workout not found with id: " + workoutId));

        workout.setDays(allDays.size());
        workoutRepository.save(workout);
    }

    // ✅ Calendar sync
    public Optional<WorkoutDayEntity> syncWorkoutDayToCalendar(Long dayId, String token) {
        return workoutDayRepository.findById(dayId).map(day -> {
            if (day.getCalendarEventId() == null) {
                String calendarEventId = googleCalendarService.createCalendarEventForWorkoutDay(token, day);
                day.setCalendarEventId(calendarEventId);
                return workoutDayRepository.save(day);
            }
            return day;
        });
    }
}
