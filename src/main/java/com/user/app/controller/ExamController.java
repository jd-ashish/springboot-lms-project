package com.user.app.controller;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.user.app.Model.QuestionReview;
import com.user.app.commons.utils.EncodeDecode;
import com.user.app.commons.utils.Utils;
import com.user.app.config.email.EmailService;
import com.user.app.dto.*;
import com.user.app.entity.Course;
import com.user.app.entity.Exam;
import com.user.app.entity.QuestionExercise;
import com.user.app.entity.User;
import com.user.app.repositories.UserRepo;
import com.user.app.service.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static com.user.app.commons.utils.Utils.getAuthUsername;

@Controller
@RequestMapping("/exam/")
public class ExamController {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private CourseService courseService;
    @Autowired
    private ExamService examService;
    @Autowired
    private ExerciseService exerciseService;

    @Autowired
    private ExamReportService examReportService;

    @Autowired
    private QuestionExerciseService questionExerciseService;

    @Autowired
    private CourseProgressService courseProgressService;

    @Autowired
    private EmailService emailService;

    @PostMapping("submit")
    @ResponseBody
    public ResponseEntity<?> submit(@RequestBody Map<String, Object> question) {
        Map<String, Object> map = new Gson().fromJson(String.valueOf(question.get("allQuestionAnswer")),
                new TypeToken<HashMap<String, Object>>() {
                }.getType());
        QuestionExerciseDto questionExerciseDto = questionExerciseService
                .getById(Long.parseLong(EncodeDecode.decode(question.get("exerciseId").toString())));
        StringBuilder questionId = new StringBuilder();
        StringBuilder answer = new StringBuilder();
        StringBuilder optionAnswer = new StringBuilder();
        StringBuilder questionList = new StringBuilder();
        StringBuilder correctAnswer = new StringBuilder();
        StringBuilder studentAnswer = new StringBuilder();
//        AtomicReference<Integer> mark = new AtomicReference<>(0);
        AtomicReference<Integer> marks = new AtomicReference<>(0);
        AtomicReference<Integer> totalCorrectQuestions = new AtomicReference<>(0);
        AtomicReference<Integer> totalIncorrectQuestions = new AtomicReference<>(0);
        map.forEach((key, value) -> {
            questionId.append(key).append(",");
            ExerciseDto exercise = exerciseService.findAllByQuestionById(Long.parseLong(key)).get(0);
            correctAnswer.append(exercise.getAnswer()).append(",");
            studentAnswer.append(value).append(",");
            if (Utils.ABCDto1234(exercise.getAnswer().trim()) == (Utils.ABCDto1234(value.toString().trim()))) {
                optionAnswer.append("1").append(",");
                marks.updateAndGet(v -> v + questionExerciseDto.getEachQuestionContains());
//                mark.updateAndGet(v -> v * questionExerciseDto.getEachQuestionContains());
                totalCorrectQuestions.updateAndGet(v -> v + 1);
            } else {
                if (value.equals("x")) {
                    optionAnswer.append("x").append(",");
                } else {
                    optionAnswer.append("0").append(",");
                    totalIncorrectQuestions.updateAndGet(v -> v + 1);
                }
            }
            answer.append(value).append(",");
        });
        if (questionExerciseDto.isNegative()) {
            marks.updateAndGet(v -> v - Integer.parseInt(totalIncorrectQuestions.toString()));
//                mark.updateAndGet(v -> v-Integer.parseInt(totalIncorrectQuestions.toString()));
        }
        User user = userRepo.findByEmail(getAuthUsername()).get();

        Map<String, String> courseProgressTempData = new HashMap<>();
        courseProgressTempData.put("data", "exercise_" + EncodeDecode.decode(question.get("exerciseId").toString()));
        courseProgressTempData.put("type", "exercise");
        Course course = courseService.getCourseById(Long.parseLong(question.get("course").toString()));
        if (user.getRole().equals("ROLE_STUDENT")) {
            Long course_id = course.getId();
            CourseProgressDto courseProgressDto = new CourseProgressDto();
            courseProgressDto.setCourse(course);
            courseProgressDto.setStudent(user);

            courseProgressDto.setData(courseProgressTempData.get("data"));
            if (!courseProgressService.checkCourseProgressExistOrNot(courseProgressDto)) {
                if (courseProgressTempData.get("type").equals("lesson")) {
                    courseProgressDto.setCompletedLesson(1);
                    courseProgressDto.setCompletedExercise(0);
                } else {
                    courseProgressDto.setCompletedExercise(1);
                    courseProgressDto.setCompletedLesson(0);
                }
                courseProgressService.createCourseProgress(courseProgressDto);
            } else {
                CourseProgressDto courseProgressDto1 = courseProgressService
                        .getCourseProgressExistRecord(courseProgressDto);

                courseProgressDto.setCompletedLesson(courseProgressDto1.getCompletedLesson() + 1);
                courseProgressDto.setCompletedExercise(courseProgressDto1.getCompletedExercise() + 1);
                courseProgressDto.setData((courseProgressDto1.getData() != null) ?
                        courseProgressDto1.getData() + "," + courseProgressTempData.get("data") : courseProgressTempData.get("data"));

                if (!courseProgressDto1.getData().contains(courseProgressTempData.get("data")))
                    courseProgressService.updateCourseProgress(courseProgressDto, courseProgressTempData.get("type"));
            }
        }

        ExamDto examDto = new ExamDto();
        examDto.setCourse(course);
        examDto.setTeacher(userRepo.findById(Long.parseLong(question.get("teacher").toString())).get());
        examDto.setStudent(user);
        examDto.setAnswer(answer.deleteCharAt(answer.length() - 1).toString());
        examDto.setQuestion(questionId.deleteCharAt(questionId.length() - 1).toString());
        examDto.setTotalQuestions(question.get("totalQuestion").toString());
        examDto.setTotalSolvedQuestions(question.get("totalSolvedQuestion").toString());
        examDto.setTotalUnsolvedQuestions(question.get("totalUnSolvedQuestion").toString());
        examDto.setTimeTaken(question.get("msLeft").toString());

        examDto.setExercise(modelMapper.map(questionExerciseDto, QuestionExercise.class));
        examDto.setEachQuestionContains(questionExerciseDto.getEachQuestionContains());
        examDto.setMinimumMarks(questionExerciseDto.getMinimumMarks());
        examDto.setNegative(questionExerciseDto.isNegative());



        ExamDto examResponse = examService.create(examDto);
        ExamReportDto examReportDto = new ExamReportDto();
        examReportDto.setStudent(user);
        examReportDto.setExam(modelMapper.map(examResponse, Exam.class));
        examReportDto.setFinalMarks(marks.toString());
        examReportDto.setTotalCorrectQuestion(totalCorrectQuestions.toString());
        examReportDto.setTotalIncorrectQuestion(totalIncorrectQuestions.toString());
        examReportDto.setQuestions(questionId.toString());
        examReportDto.setAnswers(optionAnswer.deleteCharAt(optionAnswer.length() - 1).toString());
        examReportDto.setCorrectAnswer(correctAnswer.deleteCharAt(correctAnswer.length() - 1).toString());
        examReportDto.setStudentAnswer(studentAnswer.deleteCharAt(studentAnswer.length() - 1).toString());
        ExamReportDto examReport = examReportService.createReport(examReportDto);
        Map<String, String> response = new HashMap<>();
        response.put("message", "exam paper submitted successfully!");
        response.put("error", "false");
        response.put("id", String.valueOf(examReport.getId()));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("response/{examReportId}")
    public String getResponse(Principal principal,
                              Model model, @PathVariable("examReportId") String examReportId) {
        if (principal == null) return "redirect:/";
        User user = userRepo.findByEmail(getAuthUsername()).get();

        /*EmailDetails emailDetails
                = new EmailDetails("ranjanashish253@gmail.com",
                "Congrats your exam submitted successfully", "Exam response submitted successfully", "");
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 00);
        c.set(Calendar.MINUTE, 1);
        c.set(Calendar.SECOND, 00);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                //Call your method here
                emailService.sendSimpleMail(emailDetails);
                timer.cancel();
                //setEmail(emailContent, subject);
            }
        }, c.getTime(), 1000);*/


        model.addAttribute("user", user);
        model.addAttribute("examReportId", examReportId);
        model.addAttribute("utils", Utils.class);
        model.addAttribute("int", Integer.class);
        model.addAttribute("examReport", examReportService.findById(examReportId).get(0));
        model.addAttribute("exam", examReportService.findById(examReportId).get(0).getExam());
        return "admin/questions/exam-response";
    }

    @GetMapping("question-review/{examReportId}")
    public String getQuestionReview(Model model,
                                    @PathVariable("examReportId") String examReportId) {
        ExamReportDto examReport = examReportService.findById(examReportId).get(0);
        List<QuestionReview> questionReviewList = new ArrayList<>();
        Map<String, Object> questionResults = new HashMap<>();

        String[] questionIds = examReport.getQuestions().split(",");
        String[] ans = examReport.getStudentAnswer().split(",");
        System.out.println(Arrays.toString(ans));

        for (int i=0; i<questionIds.length; i++) {
            ExerciseDto question = exerciseService.getById(questionIds[i]);
            Map<String,Object> optionMap = new Gson().fromJson(question.getOptions(),
                    new TypeToken<HashMap<String,Object>>(){}.getType() );
            QuestionReview questionReview = new QuestionReview();
            questionReview.setQuestion(question.getQuestion());
            questionReview.setOptionA(optionMap.get("a").toString());
            questionReview.setOptionB(optionMap.get("b").toString());
            questionReview.setOptionC(optionMap.get("c").toString());
            questionReview.setOptionD(optionMap.get("d").toString());
            questionReview.setCorrectAnswer(question.getAnswer());
            System.out.println(questionIds[i]);
            System.out.println(ans[i]);
            questionReview.setUserSelectAnswer(ans[i]);
            questionReviewList.add(questionReview);
        }
        model.addAttribute("examReport", examReport);
        model.addAttribute("utils", Utils.class);
        model.addAttribute("questionReviewList", questionReviewList);


        return "admin/questions/question-review";
    }

    @GetMapping("configuration")
    public String getConfiguration(){
        return "admin/configuration";
    }
}
