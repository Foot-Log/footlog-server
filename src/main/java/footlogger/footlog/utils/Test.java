package footlogger.footlog.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import footlogger.footlog.domain.Course;
import footlogger.footlog.repository.CourseRepository;
import footlogger.footlog.repository.SigunguRepository;
import footlogger.footlog.service.*;
import footlogger.footlog.web.controller.UserController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.net.URLDecoder;
import java.util.List;

@Component
public class Test implements CommandLineRunner {

    @Autowired
    private CourseService courseService;
    @Autowired
    private SaveService saveService;
    @Autowired
    private KakaoService kakaoService;
    @Autowired
    private SearchService searchService;
    @Autowired
    private AreaService areaService;
    @Autowired
    private RecommendSystem recommendSystem;
    @Autowired
    private TourApiTest tourApi;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private SigunguRepository sigunguRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private UserController userController;


    @Override
    public void run(String... args) throws Exception {

        String token = "key value";

        String decodedUri = URLDecoder.decode("http%3A%2F%2Flocalhost%3A3000%2Fuser%2Fkakao%2Fcallback", "UTF-8");
        System.out.println(decodedUri);


//        String jsonResponse = tourApi.getTour();
//        System.out.println(jsonResponse);

//        TestResponse testResponse = objectMapper.readValue(jsonResponse, TestResponse.class);
        //개요 변환용도
//        processContent(testResponse);
//        setContent();
//        List<TestResponse.Item> items = testResponse.getResponse().getBody().getItems().getItem();



//        for (TestResponse.Item item : items) {
//            Course course = Course.builder()
//                    .id(Long.valueOf(item.getContentid()))
//                    .image(item.getMainimage())
//                    .address(item.getAddr())
//                    .phoneNum(item.getTel())
//                    .phoneName(item.getTelname())
//                    .name(item.getTitle())
//                    .content(item.getSummary())
//                    .areaCode(Integer.parseInt(item.getAreacode()))
//                    .sigunguCode(Integer.parseInt(item.getSigungucode()))
//                    .build();
//
//            courseRepository.save(course);
//            System.out.println("Course saved: " + course.getName());
//        }

        //                0, 1, 31, 2, 32, 3, 8, 34, 33,
//                6, 7, 36, 35, 4, 5, 38, 37, 39
//        int[] areaCodes = {1, 31, 2, 32, 3, 8, 34, 33, 6, 7, 36, 35, 4, 5, 38, 37, 39};
//
//        for (int areaCode : areaCodes) {
//            String jsonResponse = tourApi.getSiCode(areaCode);
//            ObjectMapper objectMapper = new ObjectMapper();
//
//// JSON 문자열을 TestResponse 클래스로 변환
//            SigunguResponse sigunguResponse = objectMapper.readValue(jsonResponse, SigunguResponse.class);
//
//// items 리스트를 가져옴
//            List<SigunguResponse.Item> items = sigunguResponse.getResponse().getBody().getItems().getItem();
//
//// items 리스트 순회하면서 Course 객체로 변환하여 저장
//            for (SigunguResponse.Item item : items) {
//                // 'code'와 'name'을 각각 사용하여 Course 객체 생성
//                Sigungu sigungu = Sigungu.builder()
//                        .areaCode(areaCode)
//                        .sigunguCode(Integer.parseInt(item.getCode()))
//                        .sigunguName(item.getName())
//                        .build();
//
//                // Course 엔티티를 데이터베이스에 저장
//                sigunguRepository.save(sigungu);
//            }
//        }

//        courseService.getSigunguCourse(token, 6, 2);
    }

    //개용 정보 필터링 후 save
    public void processContent(TestResponse testResponse) {
        List<TestResponse.Item> items = testResponse.getResponse().getBody().getItems().getItem();

        for (TestResponse.Item item : items) {
            // 1. 개행 문자(\n)를 삭제
            System.out.println(item.getSummary());
            String cleanedData = item.getSummary()
                    .replace("\n", "").replace("\r", "")
                    .replace("<br>", "").replace("<br/>", "").replace("</br>", "")
                    .replace("<br >", "").replace("<br />", "")
                    .replace("<p>", "").replace("<p/>", "").replace("</p>", "")
                    .replace("<strong>", "").replace("<strong/>", "").replace("&nbsp;", "");


            // 2. "*문의" 기준으로 이전 내용을 content에 저장
            String content = "";
            if (cleanedData.contains("* 문의")) {
                content = cleanedData.split("\\* 문의")[0].trim();
            } else {
                content = "정보 없음";  // 기본 값 설정
            }

            // 3. "* 관련 홈페이지 :" 기준으로 HTML 태그에서 href와 target을 추출
            String homepage = "";
            String homepageName = "";
            if (cleanedData.contains("href=\"")) {
                homepage = extractBetween(cleanedData, "href=\"", "\"");
            } else {
                homepage = "정보 없음";  // 기본 값 설정
            }
            if (cleanedData.contains("title=\"")) {
                homepageName = extractBetween(cleanedData, "title=\"새창: ", "\"").trim();
            } else {
                homepageName = "정보 없음";  // 기본 값 설정
            }

            // 4. "- 이용요금 :"을 기준으로 내용을 추출
            String charge = "";
            if (cleanedData.contains("- 이용요금 :")) {
                charge = cleanedData.split("- 이용요금 :")[1].trim().split("- 화장실")[0].trim();
            } else {
                charge = "정보 없음";  // 기본 값 설정
            }

            // 출력 또는 저장 (필요에 따라 다른 작업 수행)
            System.out.println("Content: " + content);
            System.out.println("Homepage URL: " + homepage);
            System.out.println("Homepage Name: " + homepageName);
            System.out.println("Charge: " + charge);

            Course course = Course.builder()
                    .id(Long.valueOf(item.getContentid()))
                    .image(item.getMainimage())
                    .address(item.getAddr())
                    .phoneNum(item.getTel())
                    .phoneName(item.getTelname())
                    .name(item.getTitle())
                    .content(content)
                    .areaCode(Integer.parseInt(item.getAreacode()))
                    .sigunguCode(Integer.parseInt(item.getSigungucode()))
                    .homepage(homepage)
                    .charge(charge)
                    .homepageName(homepageName)
                    .build();

            courseRepository.save(course);
            System.out.println("Course saved: " + course.getName());
        }
    }

    //개요 정보 필터링에 필요한 매서드
    private String extractBetween(String text, String start, String end) {
        int startIndex = text.indexOf(start);
        if (startIndex == -1) {
            return "";
        }
        int endIndex = text.indexOf(end, startIndex + start.length());
        if (endIndex == -1) {
            return "";
        }
        return text.substring(startIndex + start.length(), endIndex);
    }

//    private void setContent(){
//        Course course = courseRepository.findById(2549267L).get();
//        course.setContent("구절리는 명주군 왕산면에 속해 있다가 1973년 7월 1일 행정구역 개편때 정선군 북면으로 편입되었다. 구절이란 명칭은 이곳을 흐르는 하천이 유천리 강과 어우러져 구절양장의 형태로 흐른다는 뜻에서 구절리라 이름 붙였다. 이곳은 산간 벽지였으나 석탄개발과 함께 1974년 정선선이 개통되어 한때는 오장광업소, 명주광업소 등 크고 작은 12개의 광산이 성업, 호황을 누렸으나 석탄산업합리화 조치이후 모두 폐쇄됐다.");
//        courseRepository.save(course);
//        course = courseRepository.findById(2549254L).get();
//        course.setContent("사곡리는 2005년부터 무농약농산물 인증(43ha)을 계속해서 받고 있으며 청정자연환경을 잘 간직하고 있는 친환경농업의 최적지로 알려진 마을이다. 우렁이쌀, 찰수수, 고추 등 FTA, DDA 등 어려운 농촌현실에 친환경 농법으로 친환경, 기능성의 고품질 농산물을 생산하며 이러한 생산 과정을 대내외에 알려 안전먹거리를 생산하는 전초기지로 자리매김하는데 혼신의 노력을 하고 있다. 또한 사곡에는 물 맛 좋기로 소문난 약수터가 있으며, 전통문화인 물레방아도 있기 때문에 이러한 점을 볼 때 사곡은 환경 친화적인 마을 조성에 많은 힘을 쏟고 있다.");
//        courseRepository.save(course);
//        course = courseRepository.findById(2549260L).get();
//        course.setContent("고려의 마지막 임금 공양왕이 궁터에서 은거할 시 두리봉의 영험함에 말에서 내려 3번 절함에 마읍이라 칭했다. 마을 생성 초기에는 화전과 약초, 송이를 캐다가 근래에 들어 황기, 당귀, 도라지, 황정 등 약초를 재배하는 전형적 심마니 마을로 발전했다.");
//        courseRepository.save(course);
//        course = courseRepository.findById(2549221L).get();
//        course.setContent("<문경 활공랜드> 항공레포츠의 메카로 자리 잡은 문경 활공랜드는 편리한 교통망과 접근성이 양호한 전국 최고의 입지여건을 갖추고 있으며, 문경읍이 아늑한 분지로 상승기류 형성이 잘되고 서, 남, 북풍이 불어와서 안정적 기류를 유지해 주고 주변에 고압선이 전혀 없다. 주위에 주흘산, 조령산, 성주봉 등 백두대간 명산들이 둘러싸고 있어 활공 시 최상의 경관과 쾌감을 느낄 수 있으며, 활공과 함께 당일 관광코스로는 문경온천, 문경새재, 태조왕건촬영장, 전통도예지, 석탄박물관, 문경팔경 등 다양한 명소가 있다.");
//        courseRepository.save(course);
//        course = courseRepository.findById(2549234L).get();
//        course.setContent("자연생태박물관은 문경새재일원의 생태자원 및 생물자원을 연구하여 전시하는 곳이다. 지상 2층 친환경 목조건물로 되어 있으며 1층은 청정 미래에너지에 대한 학습을 할 수 있는 신재생에너지전시실이 있고, 문경의 자연환경을 영상으로 학습할 수 있는 영상실 등이 있다. 2층은 문경의 생태자원 및 자연사를 학습 및 관람할 수 있는 상설전시관 및 매번 변화된 전시물로 탐방객에게 다가가는 기획전시실, 게임을 통해 자연생태를 학습할 수 있는 생태게임룸으로 구성되어 있다.");
//        courseRepository.save(course);
//        course = courseRepository.findById(2549205L).get();
//        course.setContent("관광도시로 널리 알려진 문경시의 8경 중 중심부에 위치하고 있으며, 대야산(930m)과 둔덕산(970m) 사이로 흐르는 용추계곡, 선유동계곡의 수려하고 청정한 물로 인해 여름철 많은 방문객이 즐겨 찾는다. 휴양림 인근에 신라 9산선문의 봉암사, 견훤 유적지, 운강 이강년 생가지, 문경새재 등 역사적으로 유명한 곳이 자리하고 있어 어린이들의 학습에도 많은 도움을 주며, 도자기 전시관, 생태공원, 클레이 사격장, 래프팅, 드라마 오픈 세트장 등 다양한 체험 학습과 관광 및 레포츠를 누구나 쉽게 즐길 수 있다.");
//        courseRepository.save(course);
//        course = courseRepository.findById(2549201L).get();
//        course.setContent("강원도 평창군 백운산 기슭 절벽에 위치한 석회암 동굴이다. 동굴이 들어선 백운산의 ‘백’과 1976년에 동굴을 발견한 마을 주민의 이름 끝 글자인 ‘룡’을 따 백룡동굴이라 이름 지었다. 약 5억 년 전에 생성된 것으로 보이는 동굴은 천연기념물이다. 오랫동안 사람 출입을 통제하고 원시 그대로의 모습을 보존하다가 2010년부터 일반인에게 제한적 개방을 시작, 탐험형 동굴로 거듭났다. 지역 주민인 해설사의 안내에 따라 동굴을 둘러볼 수 있고, 온라인을 통해 예약해야 한다. 동굴 입구는 동강 수면 위 15m 지점의 절벽에 있어 전용 배를 타고 들어간다. 동굴 안에는 종유석, 석순, 석주 등 억겁의 시간과 물, 석회석이 빚은 온갖 동굴 생성물이 분포한다. 계란 프라이형 석순, 천장에 방패 모양으로 나타나는 동굴방패는 다른 곳에서는 보기 힘든 특이한 형태다. 탐방 중 휴대폰을 소지할 수 없지만, 해설사가 주요 동굴 생성물에서 사진을 촬영해준다.");
//        courseRepository.save(course);
//        course = courseRepository.findById(2549174L).get();
//        course.setContent("운문호반에코트레일은 공암리 복지회관에서 출발하여 공암풍벽 휴게데크까지 약 2km의 거리를 왕복으로 걷는 코스이다. 운문호반의 경치를 만끽하며 걷다 보면 약 1시간 정도 소요된다.");
//        courseRepository.save(course);
//        course = courseRepository.findById(2549177L).get();
//        course.setContent("오름은 주소가 산지로 되어있어, 내비게이션 검색 시 정확한 위치를 알 수 없거나, 주차장이 따로 없는 곳이 많다. 또한 일부 오름은 사유지로, 출입이 제한되거나 통제되어 있는 곳도 있다. 이에, 오름 방문 시에는 사전에 제주관광정보센터(064-740-6000) 등을 통해 정확한 정보를 확인하고 방문하기 바란다. 제주도에는 항상 물이 마르지 않는 화구호(화산 분화구가 막혀 물이 고여 만들어진 호수)를 가진 오름이 10여 개 있는데, 물영아리오름은 이 중 하나이다. 물영아리오름습지는 제주도에서는 2000년에 최초로 환경부 습지보호지역으로 지정되었다. 2006년 10월 18일에 우리나라에서는 5번째, 제주도에서는 첫 번째로 람사르습지에 등록되었다. 등록면적은 309,000㎡이다.");
//        courseRepository.save(course);
//        course = courseRepository.findById(2549191L).get();
//        course.setContent("제1, 2코스는 등산로를 따라 정상을 향하다 보면, 장흥천문과학관이 나오고 조금 더 오르면 보호수로 지정되어 억불산을 상징했던 큰 소나무(수령 200년)가 나온다. 2004년 태풍 때 벼락으로 인해 지금은 고사되어 있지만 이곳이 중간지점이다. 남도대학 옆에서 오르는 코스는 유순한 다른 코스에 비해 가파르고 험하다. 많이 이용하지 않지만 스릴을 원하는 등산객들은 가끔씩 이 길을 택한다. 시작부터 곧게 자란 삼나무숲에서 나오는 피톤치드로 피로를 씻기도 전에 너덜지대로 이어진다. 거의 직선으로 이어지는 너널지대 계곡을 10여 분 오르면 마치 앞을 가로막고 내려보고 있는 듯한 거대한 며느리바위가 나타난다. 그 며느리바위 밑으로 나있는 너덜길을 조금 오르면 옛날 암자터가 나온다. 바위부스러기가 구르기 때문에 조심해야 한다. 여기서 조금 더 오르면 정상으로 이어지는 능선길이 나온다.");
//        courseRepository.save(course);
//        course = courseRepository.findById(2547346L).get();
//        course.setContent("신선이 노닐다 간 자리라고 하여 퇴계 이황 선생이 친히 ‘삼선구곡(三仙九曲)’이라는 이름을 붙여 준 선암계곡은 10km에 이르는 청정계곡으로 도로와 가까이 있어서 시원하게 드라이브를 즐기면서 맑은 물과 눈부시게 하얀 너럭바위가 옹기종기 모인 풍경을 감상할 수 있는 코스이다.");
//        courseRepository.save(course);
//        course = courseRepository.findById(2508606L).get();
//        course.setContent("강원도 양구DMZ는 군사 통제구역으로 잘 보존된 자연경관을 둘러볼 수 있는 생태관광지다. 양구하면 펀치볼을 빼놓을 수 없다. 펀치볼은 해안면에 위치한 해발 400∼500m 고지대에 발달한 분지로 그 주위가 마치 화채(Punch) 그릇(Bowl) 같다고 해서 붙은 이름이다. 펀치볼은 우리나라에서 유일하게 민간인 출입통제선 안에 위치한 면이며, 많은 희귀 동식물이 서식한다. 특히 해발 1,304m의 대암산 정상 부분에 있는 남한 유일의 고층습원인 ‘용늪’은 수천 년의 생태계 변화를 간직하고 있다. 용늪은 대암산·대우산천연보호구역으로 묶어 천연기념물로 지정돼 있다. 50여 년 만에 개방된 두타연도 천연기념물인 산양, 하늘다람쥐, 쇠딱따구리 등이 많이 서식하고 있는 생태의 보고다. 두타연의 생태관광코스는 걷기길로 인기있다. 양구DMZ 안보관광 코스가 잘 짜여 있다. 약 60km의 양구DMZ 코스 중 인기 있는 제4땅굴과 을지전망대를 둘러보기 위해서는 통일관에서 출입신청서를 작성해야 한다. 제4땅굴은 1990년 3월 발견됐으며 땅굴 내부를 관람할 수 있는 전동차를 운행하고 있다. 우리나라 최전방 가칠봉 능선에 위치한 을지전망대에서는 내금강의 4개 봉우리를 전망할 수 있다. 인근에 박수근미술관, 방산자기박물관 등이 있다. ※ 민간인 통제구역 ※ 비무장지대 바깥 남방한계선을 경계로 남쪽 5~20㎞에 있는 민간인통제구역으로, 민간인출입통제선이라고도 부른다. 1953년 이후 남북 군대의 군사분계선으로 해 양쪽이 뒤로 2km씩 물러난 그곳을 DMZ으로 정하고 출입을 통제했다.");
//        courseRepository.save(course);
//        course = courseRepository.findById(2549152L).get();
//        course.setContent("제주세계자연유산센터는 대한민국에서 유일한 유네스코 세계자연유산으로서의 가치를 널리 알리기 위해 조성되었다. 4D영상관에서 제주의 환상적인 자연을 온몸으로 느끼고, 리프트를 타고 제주도 탄생과정 체험, 직접 가 볼 수 없는 용암 동굴 체험 등 교과서에 실린 세계자연유산 제주를 직접 체험해 볼 수 있다. 제주 세계자연유산센터는 2007년 유네스코 세계자연유산 등재, 2009 환경부 선정 생태관광 20선, 2010 한국형 생태관광 10모델에 선정된 대표적인 생태관광지 유네스코 세계자연유산 거문오름에 위치해 있다.");
//        courseRepository.save(course);
//        course = courseRepository.findById(2549151L).get();
//        course.setContent("서남 해안과 해상 지역에 흩어져 있는 우리나라 최대 면적의 국립공원이다. 1981년 14번째 국립공원으로 지정되었으며 면적은 2,266.221㎢(육지 291.023㎢, 해상 1,975.198㎢)에 달한다. 따뜻한 해양성 기후 영향으로 생태적 보전가치가 높은 상록수림이 존재하며 과거 화산활동으로 형성된 섬과 기암괴석들은 그 독특한 아름다움으로 보존의 가치가 높다. 또한 신라시대 장보고(?~846)가 건설한 해상왕국과 조선시대 충무공 이순신(1545~1598)이 왜적을 격파한 전적지가 곳곳에 남아 있다.");
//        courseRepository.save(course);
//        course = courseRepository.findById(2508698L).get();
//        course.setContent("시화방조제가 조성되면서 생긴 인공호수 시화호는 안산시, 시흥시, 화성시 등 3개 지역과 면이 닿아 있다. 규모는 호수면적 4,380만㎡, 방조제 길이 약 1만2,700m, 담수면적 5,650만㎡, 유역면적 4억7,650만㎡로 시화호 인근유역에 100만여 명의 주민이 거주하고 있을 정도다. 드넓은 시화호 유역은 희귀 생물이 찾는 철새 도래지다. 희귀종인 장다리물떼새와 천연기념물인 큰고니, 검은머리물떼새 등이 매년 겨울이면 방문한다. 시화호 갯벌도 생명력이 가득하다. 갯지렁이류와 갑각류, 연체동물, 대형 무척추동물 등 총 214종의 다양한 해양생물이 서식하고 있다. 최근에는 전 세계적으로 희귀종인 저어새 또한 이곳에서 발견되었다. 시화호는 원래 주변 지역의 농업과 공업 단지에 용수를 공급하기 위한 담수호로 조성되었다. 1994년 물막이 공사를 끝내고 방조제를 완공했으나 이후 수질이 악화되고 생태계가 파괴되었다. 결국 정부는 담수화 계획을 폐지하고 2001년 다시 해수를 유입시키면서 해수호로 관리하고 수질 개선을 위한 노력을 다하였다. 이후 시화호의 수질이 이전 수준으로 회복하였고, 해양생물과 철새들이 찾아들기 시작하면서 새로운 생태환경이 조성되었다. 시화호는 국내 최초의 조력발전소인 시화호조력발전소가 설치되고 갈대습지공원이 조성되면서 생태관광지로 부상하고 있다.");
//        courseRepository.save(course);
//        course = courseRepository.findById(2508714L).get();
//        course.setContent("인천광역시 중구에 속하는 영종도는 우리나라에서 일곱 번째로 큰 섬이다. 원래는 제비가 많다 하여 ‘자연도(紫燕島)’라는 이름으로 불렸다. 영종도는 간석지가 발달하여 간조 때면 주위의 운렴도, 용유도와 이어진다. 이처럼 넓은 간석지는 자연생태계 보전에도 유리하게 작용한다. 영종도는 매년 겨울이면 민물도요가 찾아드는 철새 도래지로, 특히 영종도 누남리의 해안은 청다리도요·큰뒷부리도요·중뒷부리도요·재물떼새 등 약 15종의 바닷새가 서식한다. 이곳의 주요 농산물은 고추·딸기·보리·콩·고구마·쌀·마늘 등이며, 염전과 굴, 백합 등의 양식도 이루어지고 있다. 영종도에는 자연생태계를 체계적으로 배우고 싶다면 학생해양탐구학습장을 이용한다. 1984년 개장하여 운영 중으로 바다에 사는 각종 생물과 해양 관련 자연 현상을 관찰할 수 있다. 단체나 학교를 대상으로 인천교육과학연구원(032-880-0755)에서 예약을 받는다. 영종도는 관광지로도 유명세를 얻고 있다. 2001년 동북아 최대 허브 공항인 인천공항의 개항 이후 영종도와 수도권을 연결하는 공항철도가 들어서고 서울에서 김포와 영종도를 잇는 고속도로가 들어섰으며, 2009년 10월에는 송도국제도시와 이어지는 인천대교가 세워지는 등 교통이 편리해지고 볼거리가 풍부해졌기 때문이다. 해변으로는 을왕리해수욕장과 마시안해수욕장, 왕산리해수욕장이 있는데, 이 중 을왕리해수욕장은 아름다운 일몰을 볼 수 있는 곳으로 유명하다.");
//        courseRepository.save(course);
//        course = courseRepository.findById(2547351L).get();
//        course.setContent("신선이 노닐다 간 자리라고 하여 퇴계 이황 선생이 친히 ‘삼선구곡(三仙九曲)’이라는 이름을 붙여 준 선암계곡은 10km에 이르는 청정계곡으로 도로와 가까이 있어서 시원하게 드라이브를 즐기면서 맑은 물과 눈부시게 하얀 너럭바위가 옹기종기 모인 풍경을 감상할 수 있는 코스이다.");
//        courseRepository.save(course);
//        course = courseRepository.findById(2547348L).get();
//        course.setContent("신선이 노닐다 간 자리라고 하여 퇴계 이황 선생이 친히‘삼선구곡(三仙九曲)’이라는 이름을 붙여 준 선암계곡은 10km에 이르는 청정계곡으로 도로와 가까이 있어서 시원하게 드라이브를 즐기면서 맑은 물과 눈부시게 하얀 너럭바위가 옹기종기 모인 풍경을 감상할 수 있는 코스이다.");
//        course.setCharge("무료");
//        course.setHomepage("https://www.danyang.go.kr/tour/527");
//        courseRepository.save(course);
//        course = courseRepository.findById(2508605L).get();
//        course.setPhoneNum("033-680-3365");
//        course.setPhoneName("고성군청 관광문화체육과");
//        courseRepository.save(course);
//        course = courseRepository.findById(2508770L).get();
//        course.setPhoneNum("064-713-9950");
//        course.setPhoneName("한라산 국립공원관리소");
//        courseRepository.save(course);
//
//        course = courseRepository.findById(2508663L).get();
//        course.setPhoneNum("061-850-5200");
//        course.setPhoneName("보성군청 문화관광과");
//        courseRepository.save(course);
//
//        course = courseRepository.findById(2508663L).get();
//        course.setPhoneNum("061-850-5200");
//        course.setPhoneName("보성군청 문화관광과");
//        courseRepository.save(course);
//
//        course = courseRepository.findById(2508698L).get();
//        course.setPhoneNum("031-500-4972");
//        courseRepository.save(course);
//
//    }

//    private void changeNull() {
//        courseRepository.findBy
//    }
}


