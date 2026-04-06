package com.example.quizapp_31c.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.quizapp_31c.R;
import com.example.quizapp_31c.model.AnswerOption;
import com.example.quizapp_31c.model.OptionState;
import com.example.quizapp_31c.model.QuizUiState;
import com.example.quizapp_31c.util.Event;

import java.util.HashSet;
import java.util.Locale;
import java.util.Random;
import java.util.Set;

public class QuizViewModel extends ViewModel {
    private static final int TOTAL_QUESTIONS = 5;
    private static final int SCREEN_WELCOME = 0;
    private static final int SCREEN_QUIZ = 1;
    private static final int SCREEN_RESULT = 2;
    private static final int NO_SELECTION = -1;

    private final MutableLiveData<QuizUiState> uiState = new MutableLiveData<>();
    private final MutableLiveData<String> playerNameInput = new MutableLiveData<>("");
    private final MutableLiveData<Event<String>> messageEvent = new MutableLiveData<>();
    private final MutableLiveData<Event<Boolean>> finishEvent = new MutableLiveData<>();
    private final Random random = new Random();

    private int screen = SCREEN_WELCOME;
    private String playerName = "";
    private int score = 0;
    private int questionIndex = 0;
    private int leftOperand = 0;
    private int rightOperand = 0;
    private char operator = '+';
    private int correctAnswer = 0;
    private boolean answerSubmitted = false;
    private int selectedOptionIndex = NO_SELECTION;
    private final int[] optionValues = new int[4];

    public QuizViewModel() {
        publishState();
    }

    public LiveData<QuizUiState> getUiState() {
        return uiState;
    }

    public MutableLiveData<String> getPlayerNameInput() {
        return playerNameInput;
    }

    public LiveData<Event<String>> getMessageEvent() {
        return messageEvent;
    }

    public LiveData<Event<Boolean>> getFinishEvent() {
        return finishEvent;
    }

    public void onStartQuiz() {
        String enteredName = valueOrEmpty(playerNameInput.getValue()).trim();
        if (enteredName.isEmpty()) {
            messageEvent.setValue(new Event<>("Please enter your name."));
            return;
        }

        playerName = enteredName;
        score = 0;
        questionIndex = 0;
        screen = SCREEN_QUIZ;
        prepareQuestion();
    }

    public void onSelectOption(int index) {
        if (answerSubmitted) {
            return;
        }
        selectedOptionIndex = index;
        publishState();
    }

    public void onSubmitAnswer() {
        if (selectedOptionIndex == NO_SELECTION) {
            messageEvent.setValue(new Event<>("Select an answer before continuing."));
            return;
        }

        answerSubmitted = true;
        if (optionValues[selectedOptionIndex] == correctAnswer) {
            score++;
        }
        publishState();
    }

    public void onNextQuestion() {
        if (!answerSubmitted) {
            messageEvent.setValue(new Event<>("Submit your answer first."));
            return;
        }

        if (questionIndex == TOTAL_QUESTIONS - 1) {
            screen = SCREEN_RESULT;
            publishState();
            return;
        }

        questionIndex++;
        prepareQuestion();
    }

    public void onRetakeQuiz() {
        screen = SCREEN_QUIZ;
        score = 0;
        questionIndex = 0;
        prepareQuestion();
    }

    public void onFinishRequested() {
        finishEvent.setValue(new Event<>(true));
    }

    private void prepareQuestion() {
        leftOperand = random.nextInt(16) + 5;
        rightOperand = random.nextInt(11) + 1;
        int operatorIndex = random.nextInt(3);

        if (operatorIndex == 0) {
            operator = '+';
            correctAnswer = leftOperand + rightOperand;
        } else if (operatorIndex == 1) {
            operator = '-';
            if (rightOperand > leftOperand) {
                int temp = leftOperand;
                leftOperand = rightOperand;
                rightOperand = temp;
            }
            correctAnswer = leftOperand - rightOperand;
        } else {
            operator = 'x';
            leftOperand = random.nextInt(10) + 1;
            rightOperand = random.nextInt(10) + 1;
            correctAnswer = leftOperand * rightOperand;
        }

        answerSubmitted = false;
        selectedOptionIndex = NO_SELECTION;
        generateOptionValues();
        publishState();
    }

    private void generateOptionValues() {
        Set<Integer> values = new HashSet<>();
        int correctIndex = random.nextInt(optionValues.length);
        optionValues[correctIndex] = correctAnswer;
        values.add(correctAnswer);

        for (int i = 0; i < optionValues.length; i++) {
            if (i == correctIndex) {
                continue;
            }

            int candidate;
            do {
                candidate = correctAnswer + random.nextInt(15) - 7;
                if (candidate == correctAnswer) {
                    candidate += i + 2;
                }
            } while (values.contains(candidate));

            optionValues[i] = candidate;
            values.add(candidate);
        }
    }

    private void publishState() {
        uiState.setValue(new QuizUiState(
                screen == SCREEN_WELCOME,
                screen == SCREEN_QUIZ,
                screen == SCREEN_RESULT,
                "Player: " + playerName,
                String.format(Locale.getDefault(), "Question %d of %d", questionIndex + 1, TOTAL_QUESTIONS),
                String.format(Locale.getDefault(), "%d%% complete", progressPercent()),
                answeredQuestions(),
                TOTAL_QUESTIONS,
                String.format(Locale.getDefault(), "%d %s %d = ?", leftOperand, String.valueOf(operator), rightOperand),
                feedbackText(),
                feedbackColor(),
                !answerSubmitted,
                answerSubmitted,
                questionIndex == TOTAL_QUESTIONS - 1 ? "View Result" : "Next Question",
                "Well done, " + playerName,
                String.format(Locale.getDefault(), "You scored %d out of %d.", score, TOTAL_QUESTIONS),
                optionAt(0),
                optionAt(1),
                optionAt(2),
                optionAt(3)
        ));
    }

    private int answeredQuestions() {
        return answerSubmitted ? questionIndex + 1 : questionIndex;
    }

    private int progressPercent() {
        return (int) ((answeredQuestions() * 100f) / TOTAL_QUESTIONS);
    }

    private String feedbackText() {
        if (!answerSubmitted) {
            return "Select one option, then submit.";
        }
        if (selectedOptionIndex != NO_SELECTION && optionValues[selectedOptionIndex] == correctAnswer) {
            return String.format(Locale.getDefault(), "Correct. The answer is %d.", correctAnswer);
        }
        return String.format(Locale.getDefault(), "Incorrect. The correct answer is %d.", correctAnswer);
    }

    private int feedbackColor() {
        if (!answerSubmitted) {
            return R.color.feedback_neutral;
        }
        if (selectedOptionIndex != NO_SELECTION && optionValues[selectedOptionIndex] == correctAnswer) {
            return R.color.feedback_correct;
        }
        return R.color.feedback_incorrect;
    }

    private AnswerOption optionAt(int index) {
        return new AnswerOption(
                String.valueOf(optionValues[index]),
                !answerSubmitted,
                optionStateFor(index)
        );
    }

    private OptionState optionStateFor(int index) {
        if (!answerSubmitted) {
            return selectedOptionIndex == index ? OptionState.SELECTED : OptionState.DEFAULT;
        }
        if (optionValues[index] == correctAnswer) {
            return OptionState.CORRECT;
        }
        if (selectedOptionIndex == index) {
            return OptionState.INCORRECT;
        }
        return OptionState.DEFAULT;
    }

    private String valueOrEmpty(String value) {
        return value == null ? "" : value;
    }
}
