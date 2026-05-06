package com.example.learningassistantapp.repository;

import android.content.Context;

import com.example.learningassistantapp.model.LearningTask;
import com.example.learningassistantapp.model.Question;
import com.example.learningassistantapp.model.QuizResult;
import com.example.learningassistantapp.model.User;
import com.example.learningassistantapp.util.AppPreferences;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LearningRepository {
    private final AppPreferences preferences;

    public LearningRepository(Context context) {
        preferences = new AppPreferences(context);
    }

    public boolean login(String username, String password) {
        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            return false;
        }
        preferences.saveUser(new User(username.trim(), username.trim().toLowerCase() + "@example.com", "0400000000"));
        return true;
    }

    public void register(String username, String email, String phone, String password) {
        preferences.saveUser(new User(username.trim(), email.trim(), phone.trim()));
    }

    public User getUser() {
        return preferences.getUser();
    }

    public List<String> getInterestOptions() {
        return Arrays.asList(
                "Algorithms",
                "Data Structures",
                "Web Development",
                "Testing",
                "Android Development",
                "Cloud Computing",
                "Cyber Security",
                "Databases",
                "Machine Learning",
                "Software Engineering"
        );
    }

    public void saveSelectedInterests(Set<String> interests) {
        preferences.saveInterests(interests);
    }

    public Set<String> getSelectedInterests() {
        return new LinkedHashSet<>(preferences.getInterests());
    }

    public List<LearningTask> getLearningTasks() {
        List<LearningTask> tasks = new ArrayList<>();
        tasks.add(new LearningTask("task_1", "Generated Task 1", "Small description for the generated task on algorithms and problem solving.", "Algorithms"));
        tasks.add(new LearningTask("task_2", "Generated Task 2", "Small description for the generated task on Android components and layouts.", "Android Development"));
        tasks.add(new LearningTask("task_3", "Generated Task 3", "Small description for the generated task on testing strategies and quality checks.", "Testing"));
        return tasks;
    }

    public LearningTask getPrimaryTask() {
        return getLearningTasks().get(0);
    }

    public List<Question> getQuestionsForTask(String taskId) {
        if ("task_2".equals(taskId)) {
            return Arrays.asList(
                    new Question("q4", "Android Development", "Which layout is commonly used to position views relative to each other in Android?", Arrays.asList(
                            "ConstraintLayout",
                            "BroadcastReceiver",
                            "SharedPreferences",
                            "IntentFilter"
                    ), 0),
                    new Question("q5", "Android Development", "What does SharedPreferences mainly store?", Arrays.asList(
                            "Large video files",
                            "Small key-value app data",
                            "XML layouts",
                            "Push notifications"
                    ), 1)
            );
        }
        if ("task_3".equals(taskId)) {
            return Arrays.asList(
                    new Question("q6", "Testing", "What is the goal of a unit test?", Arrays.asList(
                            "To test one small piece of logic in isolation",
                            "To publish the app",
                            "To change the database schema",
                            "To design icons"
                    ), 0),
                    new Question("q7", "Testing", "Why is regression testing useful?", Arrays.asList(
                            "It slows delivery on purpose",
                            "It checks that old features still work after changes",
                            "It replaces version control",
                            "It removes the need for requirements"
                    ), 1)
            );
        }
        return Arrays.asList(
                new Question("q1", "Algorithms", "Which option best describes an algorithm?", Arrays.asList(
                        "A random collection of ideas",
                        "A step-by-step method to solve a problem",
                        "A database table",
                        "A mobile device sensor"
                ), 1),
                new Question("q2", "Data Structures", "Which data structure works on a Last-In, First-Out rule?", Arrays.asList(
                        "Queue",
                        "Graph",
                        "Stack",
                        "Tree"
                ), 2),
                new Question("q3", "Android Development", "What is the main role of a ViewModel in Android MVVM?", Arrays.asList(
                        "To replace XML layouts",
                        "To hold UI-related data across configuration changes",
                        "To compile the app",
                        "To manage device hardware"
                ), 1)
        );
    }

    public QuizResult evaluateQuiz(LearningTask task, Map<String, Integer> selections) {
        List<Question> questions = getQuestionsForTask(task.getId());
        int score = 0;
        for (Question question : questions) {
            Integer answer = selections.get(question.getId());
            if (answer != null && answer == question.getCorrectAnswerIndex()) {
                score++;
            }
        }
        preferences.saveLatestQuizResult(task.getTitle(), score, questions.size());
        return new QuizResult(task.getId(), task.getTitle(), score, questions.size(), new LinkedHashMap<>(selections));
    }

    public int getLastScore() {
        return preferences.getLastScore();
    }

    public int getLastTotal() {
        return preferences.getLastTotal();
    }

    public String getLastTaskTitle() {
        return preferences.getLastTaskTitle();
    }

    public String getDummyQuizHistorySummary() {
        return "Quiz history: 2/3 on Algorithms Basics, 3/3 on Android UI, 1/3 on Testing Foundations.";
    }
}
