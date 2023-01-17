package com.user.app.controller;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.user.app.Model.CsvData;
import com.user.app.commons.utils.EncodeDecode;
import com.user.app.commons.utils.FileUploader;
import com.user.app.commons.utils.ReadCsvFile;
import com.user.app.commons.utils.Utils;
import com.user.app.dto.ExerciseDto;
import com.user.app.dto.QuestionExerciseDto;
import com.user.app.entity.QuestionExercise;
import com.user.app.entity.User;
import com.user.app.repositories.QuestionExerciseRepository;
import com.user.app.repositories.UserRepo;
import com.user.app.service.CourseService;
import com.user.app.service.ExerciseService;
import com.user.app.service.QuestionExerciseService;
import lombok.SneakyThrows;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.security.Principal;
import java.util.*;

import static com.user.app.commons.utils.Utils.getAuthUsername;

@Controller
@RequestMapping("/exercise/")
public class ExerciseController {


    @Autowired
    private UserRepo userRepo;

    @Autowired
    private QuestionExerciseService questionExerciseService;
    @Autowired
    private ModelMapper modelMapper;

    @Value("${project.image}")
    private String path;

    @Autowired
    private ExerciseService exerciseService;
    @Autowired
    private QuestionExerciseRepository questionExerciseRepository;


    @Autowired
    private CourseService courseService;

    @PostMapping("update")
    @ResponseBody
    public ResponseEntity<?> update(@RequestBody Map<String, Object> params) {
//        String username = getAuthUsername();
//        User user = userRepo.findByEmail(username).get();
        String eachQuestionContains = params.get("eachQuestionContains").toString();
        String exerciseName = params.get("exerciseName").toString();
        String id = params.get("id").toString();
        double minimumMarks = Double.parseDouble(params.get("minimumMarks").toString());
        String numberOfQuestionPerStudentUpdate = params.get("numberOfQuestionPerStudentUpdate").toString();
        String time = params.get("time").toString();
        boolean isNegative = Boolean.parseBoolean(params.get("isNegative").toString());
        QuestionExerciseDto questionExerciseDto = new QuestionExerciseDto();
        questionExerciseDto.setExerciseName(exerciseName);
        questionExerciseDto.setTime(time);
        questionExerciseDto.setNumberOfQuestionPerStudent(numberOfQuestionPerStudentUpdate);
        questionExerciseDto.setId(Long.parseLong(id));
        questionExerciseDto.setEachQuestionContains(Integer.parseInt(eachQuestionContains));
        questionExerciseDto.setMinimumMarks(minimumMarks);
        questionExerciseDto.setNegative(isNegative);
        questionExerciseDto.setUpdatedAt(new Date());
        QuestionExerciseDto qer = questionExerciseService.getById(questionExerciseDto.getId());
        questionExerciseDto.setCreatedAt(qer.getCreatedAt());
        questionExerciseDto.setUser(qer.getUser());
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Update successfully");
        response.put("success", true);
        try {
            questionExerciseRepository.save(modelMapper.map(questionExerciseDto, QuestionExercise.class));
        } catch (Exception e) {
            response.put("message", e.getMessage());
            response.put("success", false);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("create")
    @ResponseBody
    public String exerciseCreate(Model model, @RequestBody Map<String, Object> map) {
        System.out.println(map);
        String[] result = map.get("res").toString().split(",");
        String courseId = map.get("courseId").toString();
        String exerciseName = map.get("exercise").toString();
        String numberOfQuestionPerStudent = map.get("numberOfQuestionPerStudent").toString();
        String examTime = map.get("examTime").toString();

        QuestionExerciseDto questionExerciseDto = new QuestionExerciseDto();
        questionExerciseDto.setExerciseName(exerciseName);
        questionExerciseDto.setTime(examTime);
        questionExerciseDto.setNumberOfQuestionPerStudent(numberOfQuestionPerStudent);
        questionExerciseDto.setUser(userRepo.findByEmail(getAuthUsername()).get());
        QuestionExerciseDto questionExerciseDtoResponse = null;
        if (questionExerciseService.findByName(exerciseName).size() > 0) {
            questionExerciseDtoResponse = questionExerciseService.findByName(exerciseName).get(0);
        } else {
            questionExerciseDtoResponse = questionExerciseService.create(questionExerciseDto);
        }

        System.out.println(Arrays.toString(result));
        for (int i = 0; i <= result.length; i++) {
            if (i % 6 == 0 && i > 0) {

                saveQuestion(new CsvData(courseId, result[i - 6],
                                result[i - 5], result[i - 4], result[i - 3],
                                result[i - 2], result[i - 1])
                        , questionExerciseDtoResponse);
            }
        }
        return "Exercise created successfully!";
    }

    @SneakyThrows
    @PostMapping("/export/{courseId}")
    @ResponseBody
    public ResponseEntity<?> export(@RequestParam("file") MultipartFile file,
                                    @PathVariable("courseId") String courseId) {
        User user = userRepo.findByEmail(getAuthUsername()).get();
        FileUploader fileUploader = new FileUploader(path);
        String fileName = fileUploader.uploadMultipartFile(file);
        File file1 = new File(path + File.separator + fileName);
        String p = file1.getAbsolutePath();
        List<CsvData> csvData = ReadCsvFile.readCSV(p, courseId);
//        csvData.forEach(this::saveQuestion);
        fileUploader.deleteFile(path + fileName);
        return new ResponseEntity<>(csvData, HttpStatus.OK);
    }


    public void saveQuestion(CsvData questions, QuestionExerciseDto questionExerciseDto) {
        exerciseService.create(questions(questions, questionExerciseDto),
                userRepo.findByEmail(getAuthUsername()).get());
    }

    private ExerciseDto questions(CsvData questions, QuestionExerciseDto questionExerciseDto) {
        ExerciseDto exerciseDto = new ExerciseDto();
        exerciseDto.setQuestion(questions.getQuestion());
        Map<String, String> options = new HashMap<>();
        options.put("a", questions.getOption1());
        options.put("b", questions.getOption2());
        options.put("c", questions.getOption3());
        options.put("d", questions.getOption4());
        exerciseDto.setCoursesId(Long.parseLong(questions.getCourseId()));
        exerciseDto.setOptions(new Gson().toJson(options));
        exerciseDto.setAnswer(questions.getAnswer());
        if (questionExerciseDto != null) {

            QuestionExercise questionExercise =
                    modelMapper.map(questionExerciseDto, QuestionExercise.class);

            exerciseDto.setQuestionExerciseList(questionExercise);
        }
        return exerciseDto;
    }

    private void updateQuestion(CsvData questions, QuestionExerciseDto questionExerciseDto, String questionId) {
        exerciseService.update(questions(questions, questionExerciseDto), userRepo.findByEmail(getAuthUsername()).get(), questionId);
    }

    @SneakyThrows
    @PostMapping("/confirm-save-question/{exercise}/{courseId}")
    @ResponseBody
    public ResponseEntity<?> confirmSaveQuestion(@RequestBody Map<String, String> map,
                                                 @PathVariable("exercise") String exerciseName,
                                                 @PathVariable("courseId") String courseId) {

        String numberOfQuestionPerStudent = map.get("numberOfQuestionPerStudent").toString();
        String examTime = map.get("examTime").toString();
        QuestionExerciseDto questionExerciseDto = new QuestionExerciseDto();
        questionExerciseDto.setExerciseName(exerciseName);
        questionExerciseDto.setNumberOfQuestionPerStudent(numberOfQuestionPerStudent);
        questionExerciseDto.setTime(examTime);
        questionExerciseDto.setCreatedAt(new Date());
        questionExerciseDto.setUser(userRepo.findByEmail(getAuthUsername()).get());
        QuestionExerciseDto questionExerciseDtoResponse = null;
        if (questionExerciseService.findByName(exerciseName).size() > 0) {
            questionExerciseDtoResponse = questionExerciseService.findByName(exerciseName).get(0);
        } else {
            questionExerciseDtoResponse = questionExerciseService.create(questionExerciseDto);
        }

        JsonArray jsonArray = new Gson().fromJson(map.get("data"), JsonArray.class);
        QuestionExerciseDto finalQuestionExerciseDtoResponse = questionExerciseDtoResponse;
        jsonArray.forEach(j -> saveQuestion(j, finalQuestionExerciseDtoResponse, courseId));
        Map<String, String> response = new HashMap<>();
        response.put("message", "Question saved successfully!");
        response.put("error", "false");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private void saveQuestion(JsonElement jsonElement, QuestionExerciseDto questionExerciseDtoResponse, String courseId) {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        CsvData csvData = new CsvData(courseId, jsonObject.get("question").getAsString(),
                jsonObject.get("option1").getAsString(),
                jsonObject.get("option2").getAsString(),
                jsonObject.get("option3").getAsString(),
                jsonObject.get("option4").getAsString(),
                jsonObject.get("answer").getAsString());


        if (questionExerciseDtoResponse != null) {
            saveQuestion(csvData, questionExerciseDtoResponse);
        }

    }


    @GetMapping("/download")
    public ResponseEntity<Resource> getFile() throws FileNotFoundException {
        String filename = "sample_question_csv.csv";


        InputStreamResource file = new InputStreamResource(new FileUploader(path + "temp/").getResource(filename));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(file);
    }

    @SneakyThrows
    @PostMapping("/all-questions-list/{exerciseId}/{numberOfQuestionPerStudent}")
    @ResponseBody
    public ResponseEntity<?> allQuestionsListWithLimit(@PathVariable("exerciseId") String exerciseId,
                                                       @PathVariable("numberOfQuestionPerStudent") String numberOfQuestionPerStudent) {
        List<ExerciseDto> exercise = exerciseService
                .findAllByQuestionExerciseIdWithLimit(Long.parseLong(EncodeDecode.decode(exerciseId)), numberOfQuestionPerStudent);
        Map<String, List<ExerciseDto>> map = new HashMap<>();
        map.put("data", exercise);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @SneakyThrows
    @PostMapping("/all-questions-list/{exerciseId}")
    @ResponseBody
    public ResponseEntity<?> allQuestionsList(@PathVariable("exerciseId") String exerciseId) {
        List<ExerciseDto> exercise = exerciseService
                .findAllByQuestionExerciseId(Long.parseLong(EncodeDecode.decode(exerciseId)));
        Map<String, List<ExerciseDto>> map = new HashMap<>();
        map.put("data", exercise);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @SneakyThrows
    @PostMapping("delete-single-questions/{questionId}")
    @ResponseBody
    public ResponseEntity<?> deleteSingleQuestion(@PathVariable("questionId") String questionId) {
        exerciseService.delete(questionId);

        return new ResponseEntity<>("Delete successfully!", HttpStatus.OK);
    }

    @SneakyThrows
    @PostMapping("empty-exercise/{exerciseId}")
    @ResponseBody
    public ResponseEntity<?> emptyExerciseQuestion(@PathVariable("exerciseId") String exerciseId) {
        String exerciseID = EncodeDecode.decode(exerciseId);
        exerciseService.emptyExerciseQuestion(exerciseID);

        return new ResponseEntity<>("Empty exercise successfully successfully! ", HttpStatus.OK);
    }

    @SneakyThrows
    @PostMapping("confirm-update-question/{exerciseId}/{questionId}")
    @ResponseBody
    public ResponseEntity<?> updateSingleQuestion(@PathVariable("exerciseId") String exerciseId,
                                                  @PathVariable("questionId") String questionId,
                                                  @RequestBody Map<String, Object> question) {
        String answer = "";
        if (question.get("choose-answer").toString().equals("Choose another answer")) {
            answer = question.get("answer").toString();
        } else {
            answer = question.get("choose-answer").toString();
        }
        CsvData csvData = new CsvData(question.get("c-id").toString(), question.get("question").toString(),
                question.get("option1").toString(), question.get("option2").toString(), question.get("option3").toString(),
                question.get("option4").toString(), answer);
        QuestionExerciseDto questionExerciseDtoResponse = new QuestionExerciseDto();

        String numberOfQuestionPerStudent = question.get("numberOfQuestionPerStudent").toString();
        String examTime = question.get("examTime").toString();
        questionExerciseDtoResponse.setNumberOfQuestionPerStudent(numberOfQuestionPerStudent);
        questionExerciseDtoResponse.setTime(examTime);
        questionExerciseDtoResponse.setId(Long.parseLong(EncodeDecode.decode(exerciseId)));
        questionExerciseDtoResponse.setUpdatedAt(new Date());
        System.out.println("exerciseIdexerciseIdexerciseId " + exerciseId);
        System.out.println("questionIdquestionIdquestionId " + questionId);

        updateQuestion(csvData, questionExerciseDtoResponse, questionId);


        return new ResponseEntity<>("Update successfully!", HttpStatus.OK);
    }

    @SneakyThrows
    @GetMapping("delete/{exerciseId}")
    public String deleteExercise(@PathVariable("exerciseId") String exerciseId) {
        questionExerciseService.delete(EncodeDecode.decode(exerciseId));
        return "redirect:/exercise";
    }

    @SneakyThrows
    @GetMapping("lesson/{courseId}")
    public String getExerciseByCourseId(@PathVariable("courseId") String courseId,
                                        Model model, Principal principal) {
        if (principal == null) return "redirect:/";
        model.addAttribute("user", userRepo.findByEmail(getAuthUsername()).get());
        model.addAttribute("name", principal.getName());
        model.addAttribute("courseId", Long.parseLong(courseId));
        model.addAttribute("encodeDecode", EncodeDecode.class);
//        model.addAttribute("courseList",exerciseService.getExerciseByCourseId(courseId));
        model.addAttribute("courseList", courseService.getCourseList());
        model.addAttribute("questionExerciseList", questionExerciseService.getExerciseByCourseId(courseId));

        return "admin/exercise/exercise";
    }

    @GetMapping("create/{courseId}")
    public String createCourseLesson(Model model, Principal principal,
                                     @PathVariable("courseId") String courseId) {

        model.addAttribute("user",
                userRepo.findByEmail(getAuthUsername()).get());
        model.addAttribute("encodeDecode", EncodeDecode.class);
        model.addAttribute("utils", Utils.class);
        model.addAttribute("name", principal.getName());
        model.addAttribute("courseId", Long.parseLong(courseId));
        model.addAttribute("courseList", courseService.getCourseList());
        model.addAttribute("questionExerciseList",
                questionExerciseService.getExerciseByCourseId(courseId));
        return "admin/exercise/exercise";
    }
}
