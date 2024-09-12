package footlogger.footlog.service;

import footlogger.footlog.converter.AreaConverter;
import footlogger.footlog.converter.CourseConverter;
import footlogger.footlog.converter.NaverBlogConverter;
import footlogger.footlog.domain.Course;
import footlogger.footlog.domain.Log;
import footlogger.footlog.domain.SaveCourse;
import footlogger.footlog.domain.User;
import footlogger.footlog.repository.CourseRepository;
import footlogger.footlog.repository.LogRepository;
import footlogger.footlog.repository.SaveRepository;
import footlogger.footlog.repository.UserRepository;
import footlogger.footlog.utils.NaverBlog;
import footlogger.footlog.utils.RecommendSystem;
import footlogger.footlog.web.dto.response.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CourseService {
    private final CourseRepository courseRepository;
    private final CourseConverter courseConverter;
    private final AreaConverter areaConverter;
    private final SaveRepository saveRepository;
    private final UserRepository userRepository;
    private final NaverBlog naverBlog;
    private final NaverBlogConverter naverBlogConverter;
    private final LogRepository logRepository;
    private final RecommendSystem recommendSystem;
    private final SaveService saveService;

    //지역기반으로 코스를 받아 옴
    public List<CourseResponseDTO> getByAreaName(String token, Long areaCode) {
        User user = userRepository.findByAccessToken(token)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        List<Course> courses = courseRepository.findByAreaCode(areaCode);



        return courses.stream()
                .map(course -> courseConverter
                        .toResponseDTO(course, saveService.getSaveStatus(course.getId(), user.getId())))
                .collect(Collectors.toList());
    }

    //클릭 시 상세 조회
    public CourseDetailDTO getCourseDetail(String token, Long courseId) {
        User user = userRepository.findByAccessToken(token)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("코스를 찾을 수 없습니다."));
        Boolean isSave = saveService.getSaveStatus(course.getId(), user.getId());

        return courseConverter.toDetailDTO(course, isSave);
    }

    //클릭 때 마다 코스를 저장하고 취소하는 토글 기능
    public SaveStatusDTO toggleSaveCourse(String token, Long courseId) {
        User user = userRepository.findByAccessToken(token)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("코스를 찾을 수 없습니다."));
        Optional<SaveCourse> saveCourse = saveRepository.findByCourseIdAndUserId(courseId, user.getId());

        //존재할 경우 테이블에서 삭제
        if (saveCourse.isPresent()) {
            saveRepository.delete(saveCourse.get());
        } else { //존재하지 않을 경우 추가
            SaveCourse addSaveCourse = new SaveCourse(null, course, user);
            saveRepository.save(addSaveCourse);
        }

        return SaveStatusDTO.builder()
                .isSave(saveService.getSaveStatus(courseId, user.getId()))
                .build();
    }

    //네이버 블로그 포스팅 가져오기
    public List<NaverBlogDTO> getNaverBlogs(Long courseId) {

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("코스를 찾을 수 없습니다."));

        String courseName = course.getName();
        List<NaverBlogDTO> postDTOs = new ArrayList<>();

        String naverString = naverBlog.search(courseName);
        JSONObject jsonObject = new JSONObject(naverString);
        JSONArray jsonArray = jsonObject.getJSONArray("items");

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject JSONPost = (JSONObject) jsonArray.get(i);
            NaverBlogDTO postDTO = naverBlogConverter.JSONToDTO(JSONPost);

            postDTOs.add(postDTO);
        }

        return postDTOs;
    }

    //코스 완주 할 경우 저장
    public Long completeCourse(String token, Long courseId) {
        User user = userRepository.findByAccessToken(token)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("코스를 찾을 수 없습니다."));

        Log log = Log.builder()
                .course(course)
                .user(user)
                .build();

        return logRepository.save(log).getId();
    }

    //플라스크 서버로 분석요청 보낸 후 코스id 받아옴
    public List<CourseResponseDTO> analyzePreference(String token, PreferenceRequestBody request) {
        User user = userRepository.findByAccessToken(token)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        List<Long> courseIds = recommendSystem.getRecommendations(request);

        List<Course> courses = courseIds.stream()
                .map(courseRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();

        //서버에 저장
        saveCourses(courses, user);

        return courses.stream()
                .map(course -> courseConverter
                        .toResponseDTO(course, saveService.getSaveStatus(course.getId(), user.getId())))
                .collect(Collectors.toList());
    }

    //플라스크 서버에서 받아온 코스 저장
    public void saveCourses(List<Course> courses, User user) {
        for(Course course : courses) {
            SaveCourse saveCourse = new SaveCourse(null, course, user);
            saveRepository.save(saveCourse);
        }
    }

    //저장되어 있는 추천 코스 반환
    public List<CourseResponseDTO> getRecommendCourse(String token) {
        User user = userRepository.findByAccessToken(token)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        return saveRepository.findByUserId(user.getId()).stream()
                .map(SaveCourse::getCourse)
                .map(course -> courseConverter
                        .toResponseDTO(course, saveService.getSaveStatus(course.getId(), user.getId())))
                .collect(Collectors.toList());
    }
}
