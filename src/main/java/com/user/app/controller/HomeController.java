package com.user.app.controller;

import com.user.app.Model.NoticeBoard;
import com.user.app.commons.dynamic.Javascript;
import com.user.app.commons.utils.EncodeDecode;
import com.user.app.commons.utils.Utils;
import com.user.app.dto.*;
import com.user.app.entity.Course;
import com.user.app.entity.User;
import com.user.app.repositories.QuestionExerciseRepository;
import com.user.app.repositories.UserRepo;
import com.user.app.service.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static com.user.app.commons.utils.Utils.*;

@Controller
public class HomeController {

    public HomeController() {
    }

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private QuestionExerciseService questionExerciseService;

    @Autowired
    private CourseService courseService;
    @Autowired
    private LessonService lessonService;
    @Autowired
    private QuestionExerciseRepository questionExerciseRepository;
    @Autowired
    private CourseProgressService courseProgressService;


    @Autowired
    private ExerciseService exerciseService;

    @Autowired
    private ExamService examService;

    @Autowired
    private ExamReportService examReportService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private EnrollCourseService enrollCourseService;

    @ModelAttribute
    public void commonMethod(Model model, Principal principal) {
        model.addAttribute("utils", Utils.class);

    }

    @GetMapping({"/", "/logins"})
    public String home(Model model, HttpServletResponse response, Principal principal) {
        if (principal != null) return "redirect:/dashboard";

        System.out.println("jsgdfjgjsgdfjgjhsgdjkf " + getAuthUsername());

        model.addAttribute("app_name", (principal == null) ? "Welcome to app , please login" : principal.getName());

        model.addAttribute("name", new UserDto());
        model.addAttribute("success", false);

        return "index";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, Principal principal) {
        if (principal == null) return "redirect:/";

        User user = userRepo.findByEmail(getAuthUsername()).get();
        model.addAttribute("title", "dashboard");
        model.addAttribute("user", user);
        model.addAttribute("name", principal.getName());

        List<NoticeBoard> noticeBoardList = new ArrayList<>();

        if (user.getRole().equals("ROLE_ADMIN")) {
            List<User> users = userRepo.findAll();
            List<CourseDto> courseList = courseService.getCourseList();
            List<LessonDto> lesson = lessonService.getLessonList();
            AtomicInteger todayLesson = new AtomicInteger(0);
            lesson.forEach(e -> {
                if (Utils.isCurrentDate(new Date(), e.getCreatedAt()) == 0) todayLesson.getAndIncrement();
            });

            AtomicInteger teachersCount = new AtomicInteger();
            AtomicInteger studentCount  = new AtomicInteger();
            users.forEach(u ->{
                if(u.getRole().equals("ROLE_TEACHER")){
                    teachersCount.getAndIncrement();
                }else if(u.getRole().equals("ROLE_STUDENT")){
                    studentCount.getAndIncrement();
                }
            });
            AtomicInteger todayCourse = new AtomicInteger(0);
            courseList.forEach(e -> {
                if (Utils.isCurrentDate(new Date(), e.getCreatedAt()) == 0) todayCourse.getAndIncrement();
            });

            List<ExerciseDto> question = exerciseService.getList();
            AtomicInteger todayQuestion = new AtomicInteger(0);
            question.forEach(e -> {
                if (Utils.isCurrentDate(new Date(), e.getCreatedAt()) == 0) todayQuestion.getAndIncrement();
            });
            model.addAttribute("courseList", courseList);
            model.addAttribute("todayCourse", todayCourse);
            model.addAttribute("teachersCount", teachersCount);
            model.addAttribute("studentCount", studentCount);
            model.addAttribute("lesson", lesson);
            model.addAttribute("todayLesson", todayLesson);
            model.addAttribute("question", question);
            model.addAttribute("todayQuestion", todayQuestion);

        }
        if (user.getRole().equals("ROLE_TEACHER")) {
            List<EnrollCourseDto> enrollCourse = enrollCourseService.findByTeacherId(user.getId());
            List<CourseDto> course = courseService.getCourseByTeacherId(user.getId());
            List<LessonDto> lesson = lessonService.getLessonByTeacher(user.getId());
            List<QuestionExerciseDto> exercise = questionExerciseService.getCountQuestionExerciseByTeacher(user.getId());
            List<ExerciseDto> question = exerciseService.getByTeacher(user.getId());


            AtomicInteger todayLesson = new AtomicInteger(0);
            AtomicInteger todayEnrollCourse = new AtomicInteger(0);
            AtomicInteger todayExercise = new AtomicInteger(0);
            AtomicInteger todayQuestion = new AtomicInteger(0);
            AtomicInteger todayCourse = new AtomicInteger(0);
            course.forEach(e -> {
                if (Utils.isCurrentDate(new Date(), e.getCreatedAt()) == 0) todayCourse.getAndIncrement();
            });
            enrollCourse.forEach(e -> {
                if (Utils.isCurrentDate(new Date(), e.getCreatedAt()) == 0) todayEnrollCourse.getAndIncrement();
            });
            lesson.forEach(e -> {
                if (Utils.isCurrentDate(new Date(), e.getCreatedAt()) == 0) todayLesson.getAndIncrement();
            });
            exercise.forEach(e -> {
                if (Utils.isCurrentDate(new Date(), e.getCreatedAt()) == 0) todayExercise.getAndIncrement();
            });
            question.forEach(e -> {
                if (Utils.isCurrentDate(new Date(), e.getCreatedAt()) == 0) todayQuestion.getAndIncrement();
            });


            model.addAttribute("todayEnrollCourse", todayEnrollCourse);
            model.addAttribute("todayLesson", todayLesson);
            model.addAttribute("todayExercise", 0);
            model.addAttribute("todayQuestions", todayQuestion);
            model.addAttribute("todayCourse", todayCourse);
            model.addAttribute("question", question);
            model.addAttribute("exercise", exercise);
            model.addAttribute("lesson", lesson);
            model.addAttribute("course", course);
            model.addAttribute("enrollCourse", enrollCourse);

        }

        if (user.getRole().equals("ROLE_STUDENT")) {
            List<EnrollmentCoursesDto> enrolledCourseByStudent = enrollCourseService.getEnrollmentCoursesByStudentId(user.getId());

            List<Object[]> examReportServiceMaxFinalMarks = examReportService.getMaxFinalMarks(user);


            AtomicInteger totalEnrollmentCourseToday = new AtomicInteger(0);
            enrolledCourseByStudent.forEach(e -> {
                System.out.println(new Date().toString() + " ====> " + e.getCreatedAt());
                if (Utils.isCurrentDate(new Date(), e.getCreatedAt()) == 0)
                    totalEnrollmentCourseToday.getAndIncrement();
            });

            System.out.println(examReportServiceMaxFinalMarks.get(0)[1]);

            if (examReportServiceMaxFinalMarks.size() > 0) {
                //TODO : get exam details via exam id and pass to dashboard and view
//                examReportService.getGetById();
            }


            model.addAttribute("examReportServiceMaxFinalMarks", examReportServiceMaxFinalMarks.size() > 0 ? String.valueOf(examReportServiceMaxFinalMarks.get(0)[0]) : "0");
            model.addAttribute("totalEnrollmentCourse", enrolledCourseByStudent);
            model.addAttribute("totalEnrollmentCourseToday", totalEnrollmentCourseToday);
        }
        courseService.getLatestCourse(2).forEach(course -> {
            NoticeBoard noticeBoard = new NoticeBoard();
            noticeBoard.setMessage("Here is latest course is launch by " + course.getUser().getName());
            noticeBoard.setDate(Utils.timeAgo(new Date(), course.getCreatedAt()));
            noticeBoard.setUser(course.getUser());
            noticeBoard.setHref("/student/all-course");
            noticeBoardList.add(noticeBoard);
        });
        lessonService.getLatestNotice(2).forEach(lessonDto -> {
            NoticeBoard noticeBoard = new NoticeBoard();
            noticeBoard.setMessage("Here is latest lesson is launch by " + lessonDto.getUser().getName());
            noticeBoard.setDate(Utils.timeAgo(new Date(), lessonDto.getCreatedAt()));
            noticeBoard.setUser(lessonDto.getUser());
            noticeBoard.setHref("/course/" + Utils.uri(lessonDto.getCourse().getCourse_name(), false) + "/" + lessonDto.getCourse().getId());
            noticeBoardList.add(noticeBoard);
        });
        exerciseService.getLatestNotice(2).forEach(lessonDto -> {
            Course course = courseService.getCourseById(lessonDto.getCoursesId());
            NoticeBoard noticeBoard = new NoticeBoard();
            noticeBoard.setMessage("Here is latest lesson is launch by " + lessonDto.getUser().getName());
            noticeBoard.setDate(Utils.timeAgo(new Date(), lessonDto.getCreatedAt()));
            noticeBoard.setUser(modelMapper.map(lessonDto.getUser(), User.class));
            noticeBoard.setHref("/course/" + Utils.uri(course.getCourse_name(), false) + "/" + lessonDto.getCoursesId());
            noticeBoardList.add(noticeBoard);
        });
        model.addAttribute("noticeBoardList", noticeBoardList);
        return "admin/dashboard";
    }


    @GetMapping("/course")
    public String course(Model model, Principal principal,
                         @RequestParam(required = false) String editCourse,
                         @RequestParam(required = false) String name,
                         @RequestParam(required = false) String topic
    ) {
        if (principal == null) return "redirect:/";
        User user = userRepo.findByEmail(getAuthUsername()).get();
        try {
            if (principal != null) {
                model.addAttribute("user", user);
                model.addAttribute("name", principal.getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        model.addAttribute("edit", editCourse);
        model.addAttribute("name", name);
        model.addAttribute("topic", topic);
        model.addAttribute("title", "Course list");
        model.addAttribute("courseList", courseService.getCourseByTeacherId(user.getId()));
        model.addAttribute("utils", Utils.class);
        return "admin/course/index";
    }

    @GetMapping("/course/{courseName}/{courseId}")
    public String createCourseLesson(Model model, Principal principal,
                                     @PathVariable("courseName") String courseName,
                                     @PathVariable("courseId") String courseId) {
        if (principal == null) return "redirect:/";
        User user = userRepo.findByEmail(getAuthUsername()).get();
        if (principal != null) {
            model.addAttribute("user", user);
            model.addAttribute("name", principal.getName());
        }
        Course course = courseService.getCourseById(Long.parseLong(courseId));
        List<LessonDto> lessonList = lessonService.getLessonListByCourseId(courseId);

        List<Long> exerciseId = new ArrayList<>();
        List<Long> lessonId = new ArrayList<>();
        Map<Long, ExamReportDto> exerciseExam = new HashMap<>();
        if (user.getRole().equals("ROLE_STUDENT")) {
            CourseProgressDto courseProgress = courseProgressService.getByCourseId(course.getId(), user.getId());
            int CompleteProgress = 0;
            if (courseProgress != null) {
                double tc = courseProgress.getCompletedLesson() + courseProgress.getCompletedExercise();
                double tt = Utils.isNullToX(course.getTotal_lesson(), 0L) + Utils.isNullToX(course.getTotal_questions(), 0L);
                CompleteProgress = (int) ((tc / tt) * 100);
            }
            String[] progressData = new String[0];
            if (courseProgress != null) {
                progressData = courseProgress.getData().split(",");
            }

            for (String data : progressData) {
                if (data.contains("exercise")) exerciseId.add(Long.parseLong(data.split("_")[1]));
                if (data.contains("lesson")) lessonId.add(Long.parseLong(data.split("_")[1]));
                if (data.contains("exercise")) {
                    List<ExamDto> exam = examService
                            .getExamsWithReports(String.valueOf(user.getId()),
                                    String.valueOf(course.getUser().getId()), courseId, data.split("_")[1]);

                    if (exam.size() > 0) {
                        exerciseExam.put(Long.parseLong(data.split("_")[1]), examReportService
                                .findByExamId(String.valueOf(exam.get(0).getId())).get(0));
                    }
                }
            }
            model.addAttribute("totalProgress", 100);
            model.addAttribute("completeProgress", CompleteProgress);
        }


        model.addAttribute("exerciseExamReport", exerciseExam);
        model.addAttribute("exerciseId", exerciseId);
        model.addAttribute("lessonId", lessonId);
        model.addAttribute("title", "Lesson list");
        model.addAttribute("isCourse", true);
        model.addAttribute("isContent", false);
        model.addAttribute("isCreateLesson", false);
        model.addAttribute("lessonList", lessonList);
        model.addAttribute("course", course);
        model.addAttribute("utils", Utils.class);
        model.addAttribute("courseList", courseService.getCourseList());
        model.addAttribute("encodeDecode", EncodeDecode.class);
        model.addAttribute("exerciseQuestion",
                questionExerciseService.getExerciseByCourseId(courseId));
        model.addAttribute("courseId", Long.parseLong(courseId));
        return "admin/lesson/index";
    }

    @GetMapping("/course/{courseName}/{courseId}/{lessonName}/view-content-details/{isPaginationEnabled}/{lessonId}")
    public String readLessonCourse(Model model, Principal principal,
                                   @PathVariable("courseName") String courseName,
                                   @PathVariable("courseId") String courseId,
                                   @PathVariable("lessonName") String lessonName,
                                   @PathVariable("isPaginationEnabled") Boolean isPaginationEnabled,
                                   @PathVariable("lessonId") String lessonId
    ) {
        if (principal == null) return "redirect:/";
        User user = userRepo.findByEmail(getAuthUsername()).get();
        Long nextId = lessonService.getNextId(courseId, lessonId, 1);
        if (isPaginationEnabled) {
            Map<String, String> map = new HashMap<>();
            map.put("courseId", courseId);
            map.put("data", "lesson_" + lessonId);
            map.put("type", "lesson");
            updateProgress(map);
        }


        lessonService.getRangeLesson(courseId, lessonId, String.valueOf(nextId));
        model.addAttribute("user", user);
        model.addAttribute("isPaginationEnabled", isPaginationEnabled);
        model.addAttribute("nextId", nextId);
        model.addAttribute("isContent", true);
        model.addAttribute("lesson", lessonService.getLessonByLessonId(lessonId).get(0));
        return "admin/lesson/index";
    }

    public void updateProgress(Map<String, String> map) {

        User user = userRepo.findByEmail(getAuthUsername()).get();
        Course course = courseService.getCourseById(Long.parseLong(map.get("courseId")));

        if (user.getRole().equals("ROLE_STUDENT")) {
            Long course_id = course.getId();
            CourseProgressDto courseProgressDto = new CourseProgressDto();
            courseProgressDto.setCourse(course);
            courseProgressDto.setStudent(user);

            courseProgressDto.setData(map.get("data"));
            if (!courseProgressService.checkCourseProgressExistOrNot(courseProgressDto)) {
                if (map.get("type").equals("lesson")) {
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
                        courseProgressDto1.getData() + "," + map.get("data") : map.get("data"));

                if (!courseProgressDto1.getData().contains(map.get("data")))
                    courseProgressService.updateCourseProgress(courseProgressDto, map.get("type"));
            }
        }
    }

    @GetMapping("/lesson/create/{courseName}/{courseId}")
    public String createLessonLesson(Model model, Principal principal,
                                     @PathVariable("courseName") String courseName,
                                     @PathVariable("courseId") String courseId) {
        if (principal == null) return "redirect:/";
        if (principal != null) {
            model.addAttribute("user", userRepo.findByEmail(getAuthUsername()).get());
            model.addAttribute("name", principal.getName());
        }


        model.addAttribute("isContent", false);
        model.addAttribute("title", "Lesson list");
        model.addAttribute("isCourse", false);
        model.addAttribute("isCreateLesson", true);
        model.addAttribute("getLastFiveLessonList", lessonService.getLastFiveLessonList());
        model.addAttribute("lessonList", lessonService.getLessonListByCourseId(courseId));
        model.addAttribute("course", courseService.getCourseById(Long.parseLong(courseId)));
        model.addAttribute("courseList", courseService.getCourseList());
        model.addAttribute("scripts",
                Javascript.getScripts("<script src=\"https://cdn.tiny.cloud/1/g3sx7jb3sg9275s4803ucyciwa9rjalod6swv736swez3vzi/tinymce/6/tinymce.min.js\" referrerpolicy=\"origin\"></script>\n"));
        model.addAttribute("encodeDecode", EncodeDecode.class);
        model.addAttribute("exerciseQuestion", questionExerciseService.getExerciseByCourseId(courseId));
        model.addAttribute("courseId", Long.parseLong(courseId));
        return "admin/lesson/index";
    }

    @GetMapping("/lesson")
    public String lesson(Model model, Principal principal,
                         @RequestParam(required = false) String edit,
                         @RequestParam(name = "search", required = false) String search) {
        if (principal == null) return "redirect:/";
        User user = userRepo.findByEmail(getAuthUsername()).get();
        model.addAttribute("user", user);
        model.addAttribute("name", principal.getName());

        model.addAttribute("edit", edit);
        if (edit != null) {
            model.addAttribute("editLesson", lessonService.getLessonByLessonId(edit).get(0));
        }
        model.addAttribute("isContent", false);


        model.addAttribute("search", search);
        model.addAttribute("scripts",
                Javascript.getScripts("<script src=\"https://cdn.tiny.cloud/1/g3sx7jb3sg9275s4803ucyciwa9rjalod6swv736swez3vzi/tinymce/6/tinymce.min.js\" referrerpolicy=\"origin\"></script>\n"));
        List<LessonDto> searchList = new ArrayList<>();
        if (search != null && !search.trim().equals("")) {
            searchList = lessonService.searchLessonList(search, user);
        }

        model.addAttribute("title", "Lesson list");
        model.addAttribute("isSearch", search != null && !search.trim().equals(""));
        model.addAttribute("lessonList", lessonService.getLessonList());
        model.addAttribute("searchList", searchList);
        model.addAttribute("getLastFiveLessonList", lessonService.getLastFiveLessonListByTeacher(user.getId()));
        model.addAttribute("courseList", courseService.getCourseList());
        return "admin/lesson/index";
    }

    @GetMapping("/students")
    public String getStudent(Model model, Principal principal) {
        if (principal == null) return "redirect:/";
        User user = userRepo.findByEmail(getAuthUsername()).get();
        model.addAttribute("user", user);
        model.addAttribute("name", principal.getName());
        model.addAttribute("title", "Student list");
        model.addAttribute("userList", enrollCourseService.findByTeacherId(user.getId()));
        return "admin/student/student";
    }

    @GetMapping("/exercise")
    public String exercise(Model model, Principal principal,
                           @RequestParam(name = "search", required = false) String search) {
        if (principal == null) return "redirect:/";


        model.addAttribute("search", search);

        User user = userRepo.findByEmail(getAuthUsername()).get();
        List<QuestionExerciseDto> searchList = new ArrayList<>();
        if (search != null && !search.trim().equals("")) {
            searchList = questionExerciseService.searchQuestionExerciseList(search, user);
        }
        model.addAttribute("isSearch", search != null && !search.trim().equals(""));
        model.addAttribute("searchList", searchList);

        model.addAttribute("user", user);
        model.addAttribute("name", principal.getName());
        model.addAttribute("courseId", Long.parseLong("0"));
        model.addAttribute("encodeDecode", EncodeDecode.class);
        model.addAttribute("courseList", courseService.getCourseList());
        model.addAttribute("questionExerciseList",
                questionExerciseService.getQuestionExerciseListLimit(5, user));
        return "admin/exercise/exercise";
    }

    @GetMapping("/questions/test/{exerciseId}/{exerciseName}/{courseId}")
    public String testQuestions(Model model, Principal principal,
                                @PathVariable("exerciseId") String exerciseId,
                                @PathVariable("exerciseName") String exerciseName,
                                @PathVariable("courseId") String courseId) {
        if (principal == null) return "redirect:/";

        User user = userRepo.findByEmail(getAuthUsername()).get();
        Course course = courseService.getCourseById(Long.parseLong(courseId));

        if (user.getRole().equals("ROLE_STUDENT")) {
            model.addAttribute("questionExercise",
                    questionExerciseService.getById(Long.parseLong(EncodeDecode.decode(exerciseId))));
        }

        questionExerciseService.findByName(exerciseName, userRepo.findByEmail(getAuthUsername()).get());
        model.addAttribute("user", user);
        model.addAttribute("name", principal.getName());
        model.addAttribute("exerciseId", exerciseId);
        model.addAttribute("courseId", Long.parseLong(courseId));
        model.addAttribute("encodeDecode", EncodeDecode.class);
        model.addAttribute("utils", Utils.class);
        model.addAttribute("exerciseName", exerciseName);
        model.addAttribute("courseList", courseService.getCourseList());
        model.addAttribute("questionExerciseList", questionExerciseService.getExerciseByCourseId(courseId));

        model.addAttribute("questionList", exerciseService
                .findAllByQuestionExerciseId(Long.parseLong(EncodeDecode.decode(exerciseId))));
        return "admin/questions/exam-papers";
    }

    @GetMapping("/questions/{exerciseId}/{exerciseName}/{courseId}")
    public String questions(Model model, Principal principal,
                            @PathVariable("exerciseId") String exerciseId,
                            @PathVariable("exerciseName") String exerciseName,
                            @PathVariable("courseId") String courseId) {
        if (principal == null) return "redirect:/";

        User user = userRepo.findByEmail(getAuthUsername()).get();
        Course course = null;
        if (!courseId.equals("0")) course = courseService.getCourseById(Long.parseLong(courseId));

        Map<String, String> map = new HashMap<>();
        map.put("data", "exercise_" + EncodeDecode.decode(exerciseId));
        map.put("type", "exercise");
        if (user.getRole().equals("ROLE_STUDENT")) {
            Long course_id = course.getId();
            CourseProgressDto courseProgressDto = new CourseProgressDto();
            courseProgressDto.setCourse(course);
            courseProgressDto.setStudent(user);

            courseProgressDto.setData(map.get("data"));
            if (!courseProgressService.checkCourseProgressExistOrNot(courseProgressDto)) {
                if (map.get("type").equals("lesson")) {
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
                        courseProgressDto1.getData() + "," + map.get("data") : map.get("data"));

                if (!courseProgressDto1.getData().contains(map.get("data")))
                    courseProgressService.updateCourseProgress(courseProgressDto, map.get("type"));
            }
        }

        questionExerciseService.findByName(exerciseName, userRepo.findByEmail(getAuthUsername()).get());
        model.addAttribute("user", user);
        model.addAttribute("name", principal.getName());
        model.addAttribute("exerciseId", exerciseId);
        model.addAttribute("courseId", Long.parseLong(courseId));
        model.addAttribute("encodeDecode", EncodeDecode.class);
        model.addAttribute("utils", Utils.class);
        model.addAttribute("exerciseName", exerciseName);
        model.addAttribute("questionExercise", questionExerciseService.getById(Long.parseLong(EncodeDecode.decode(exerciseId))));
        model.addAttribute("courseList", courseService.getCourseList());
        model.addAttribute("questionExerciseList", questionExerciseService.getExerciseByCourseId(courseId));

        model.addAttribute("questionList", exerciseService
                .findAllByQuestionExerciseId(Long.parseLong(EncodeDecode.decode(exerciseId))));
        return "admin/questions/questions";
    }


}