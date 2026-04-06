package com.example.quizapp_31c.model;

public class QuizUiState {
    private final boolean welcomeVisible;
    private final boolean quizVisible;
    private final boolean resultVisible;
    private final String greetingText;
    private final String progressText;
    private final String progressPercentText;
    private final int progressValue;
    private final int progressMax;
    private final String questionText;
    private final String feedbackText;
    private final int feedbackColor;
    private final boolean submitEnabled;
    private final boolean nextVisible;
    private final String nextButtonText;
    private final String resultTitle;
    private final String resultMessage;
    private final AnswerOption optionOne;
    private final AnswerOption optionTwo;
    private final AnswerOption optionThree;
    private final AnswerOption optionFour;

    public QuizUiState(
            boolean welcomeVisible,
            boolean quizVisible,
            boolean resultVisible,
            String greetingText,
            String progressText,
            String progressPercentText,
            int progressValue,
            int progressMax,
            String questionText,
            String feedbackText,
            int feedbackColor,
            boolean submitEnabled,
            boolean nextVisible,
            String nextButtonText,
            String resultTitle,
            String resultMessage,
            AnswerOption optionOne,
            AnswerOption optionTwo,
            AnswerOption optionThree,
            AnswerOption optionFour
    ) {
        this.welcomeVisible = welcomeVisible;
        this.quizVisible = quizVisible;
        this.resultVisible = resultVisible;
        this.greetingText = greetingText;
        this.progressText = progressText;
        this.progressPercentText = progressPercentText;
        this.progressValue = progressValue;
        this.progressMax = progressMax;
        this.questionText = questionText;
        this.feedbackText = feedbackText;
        this.feedbackColor = feedbackColor;
        this.submitEnabled = submitEnabled;
        this.nextVisible = nextVisible;
        this.nextButtonText = nextButtonText;
        this.resultTitle = resultTitle;
        this.resultMessage = resultMessage;
        this.optionOne = optionOne;
        this.optionTwo = optionTwo;
        this.optionThree = optionThree;
        this.optionFour = optionFour;
    }

    public boolean isWelcomeVisible() {
        return welcomeVisible;
    }

    public boolean isQuizVisible() {
        return quizVisible;
    }

    public boolean isResultVisible() {
        return resultVisible;
    }

    public String getGreetingText() {
        return greetingText;
    }

    public String getProgressText() {
        return progressText;
    }

    public String getProgressPercentText() {
        return progressPercentText;
    }

    public int getProgressValue() {
        return progressValue;
    }

    public int getProgressMax() {
        return progressMax;
    }

    public String getQuestionText() {
        return questionText;
    }

    public String getFeedbackText() {
        return feedbackText;
    }

    public int getFeedbackColor() {
        return feedbackColor;
    }

    public boolean isSubmitEnabled() {
        return submitEnabled;
    }

    public boolean isNextVisible() {
        return nextVisible;
    }

    public String getNextButtonText() {
        return nextButtonText;
    }

    public String getResultTitle() {
        return resultTitle;
    }

    public String getResultMessage() {
        return resultMessage;
    }

    public AnswerOption getOptionOne() {
        return optionOne;
    }

    public AnswerOption getOptionTwo() {
        return optionTwo;
    }

    public AnswerOption getOptionThree() {
        return optionThree;
    }

    public AnswerOption getOptionFour() {
        return optionFour;
    }
}
