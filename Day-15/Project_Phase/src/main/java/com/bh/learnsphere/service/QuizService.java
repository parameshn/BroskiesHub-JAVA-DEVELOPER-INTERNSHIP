package com.bh.learnsphere.service;

import com.bh.learnsphere.dto.*;
import com.bh.learnsphere.model.*;
import com.bh.learnsphere.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class QuizService {

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private LessonRepository lessonRepository;

    public Quiz createQuiz(QuizDTO quizDTO) {
        Lesson lesson = lessonRepository.findById(quizDTO.getLessonId())
                .orElseThrow(() -> new RuntimeException("Lesson not found"));

        Quiz quiz = Quiz.builder()
                .title(quizDTO.getTitle())
                .description(quizDTO.getDescription())
                .lesson(lesson)
                .passingScore(quizDTO.getPassingScore())
                .timeLimit(quizDTO.getTimeLimit())
                .questions(new ArrayList<>())
                .build();

        return quizRepository.save(quiz);
    }

    public Question addQuestionToQuiz(Long quizId, QuestionDTO questionDTO) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Quiz not found"));

        Question question = Question.builder()
                .questionText(questionDTO.getQuestionText())
                .questionType(questionDTO.getQuestionType())
                .quiz(quiz)
                .points(questionDTO.getPoints())
                .correctAnswer(questionDTO.getQuestionType() == QuestionType.SHORT_ANSWER
                        ? questionDTO.getAnswers().get(0).getAnswerText()
                        : null)
                .answers(new ArrayList<>())
                .build();

        Question savedQuestion = questionRepository.save(question);

        // Add answers for MCQ and TRUE_FALSE questions
        if (questionDTO.getQuestionType() != QuestionType.SHORT_ANSWER) {
            for (AnswerDTO answerDTO : questionDTO.getAnswers()) {
                Answer answer = Answer.builder()
                        .answerText(answerDTO.getAnswerText())
                        .isCorrect(answerDTO.getIsCorrect())
                        .question(savedQuestion)
                        .build();
                savedQuestion.getAnswers().add(answer);
            }
        }

        return questionRepository.save(savedQuestion);
    }

    public QuizResultDTO submitQuiz(QuizSubmissionRequest submissionRequest, Long studentId) {
        Quiz quiz = quizRepository.findById(submissionRequest.getQuizId())
                .orElseThrow(() -> new RuntimeException("Quiz not found"));

        List<QuizResultDTO.QuestionResult> questionResults = new ArrayList<>();
        int totalScore = 0;
        int maxScore = 0;

        for (QuizSubmissionRequest.QuestionAnswer userAnswer : submissionRequest.getAnswers()) {
            Question question = questionRepository.findById(userAnswer.getQuestionId())
                    .orElseThrow(() -> new RuntimeException("Question not found"));

            maxScore += question.getPoints();
            boolean isCorrect = false;
            String userAnswerText = "";
            String correctAnswerText = "";

            switch (question.getQuestionType()) {
                case MCQ:
                case TRUE_FALSE:
                    Answer selectedAnswer = question.getAnswers().stream()
                            .filter(answer -> answer.getId().equals(userAnswer.getSelectedAnswerId()))
                            .findFirst()
                            .orElse(null);

                    if (selectedAnswer != null) {
                        userAnswerText = selectedAnswer.getAnswerText();
                        isCorrect = selectedAnswer.getIsCorrect();
                        correctAnswerText = question.getAnswers().stream()
                                .filter(Answer::getIsCorrect)
                                .map(Answer::getAnswerText)
                                .findFirst()
                                .orElse("");
                    }
                    break;

                case SHORT_ANSWER:
                    userAnswerText = userAnswer.getTextAnswer();
                    correctAnswerText = question.getCorrectAnswer();
                    isCorrect = userAnswerText != null &&
                            userAnswerText.trim().equalsIgnoreCase(question.getCorrectAnswer().trim());
                    break;
            }

            if (isCorrect) {
                totalScore += question.getPoints();
            }

            questionResults.add(QuizResultDTO.QuestionResult.builder()
                    .questionId(question.getId())
                    .questionText(question.getQuestionText())
                    .isCorrect(isCorrect)
                    .userAnswer(userAnswerText)
                    .correctAnswer(correctAnswerText)
                    .points(question.getPoints())
                    .build());
        }

        int finalScore = (int) ((double) totalScore / maxScore * 100);
        boolean passed = finalScore >= quiz.getPassingScore();

        return QuizResultDTO.builder()
                .quizId(quiz.getId())
                .quizTitle(quiz.getTitle())
                .totalQuestions(questionResults.size())
                .correctAnswers(
                        (int) questionResults.stream().filter(QuizResultDTO.QuestionResult::getIsCorrect).count())
                .score(finalScore)
                .passed(passed)
                .passingScore(quiz.getPassingScore())
                .questionResults(questionResults)
                .build();
    }
}