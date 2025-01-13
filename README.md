# 학습 관리 시스템 (learning-management-system)
본 프로젝트는 **학생과 강사**를 위한 학습 관리 플랫폼으로, 강의 등록, 퀴즈 및 과제 관리, 성적 평가 등 **효율적인 학습 진행 및 평가**를 지원합니다. 학생들은 강의를 수강하고 퀴즈와 과제를 제출할 수 있으며, 강사는 학습 자료를 업로드하고 과제 및 퀴즈 점수를 관리할 수 있습니다.

### 기능 소개
| **기능명**              | **설명**                                                                                          | **연관 테이블 및 컬럼**    |
|------------------------|---------------------------------------------------------------------------------------------------|------------------|
| **회원 관리**           | 학생과 강사의 회원가입 및 로그인 기능 제공                                                        | `student`, `instructor` |
| **강의 등록**           | 강사가 새로운 강의 및 강의 자료(동영상, 문서 등)를 업로드할 수 있는 기능                   | `course`, `lecture`, `content` |
| **강의 수강 신청**       | 학생이 특정 과정을 수강 신청할 수 있는 기능                                                        | `registration`    |
| **수강 현황 관리**       | 학생별 수강 신청 현황 및 수강 상태를 확인할 수 있는 기능                                           | `registration`    |
| **퀴즈 생성 및 관리**    | 강사가 퀴즈를 생성하고 문제를 추가할 수 있는 기능                                                  | `quiz`, `question`, `answer` |
| **퀴즈 제출**           | 학생이 퀴즈 문제에 대한 답변을 제출할 수 있는 기능                                                  | `answer`          |
| **퀴즈 성적 관리**       | 퀴즈 점수를 자동 또는 수동으로 채점하여 성적을 등록할 수 있는 기능                                | `quiz_grade`      |
| **과제 등록 및 관리**    | 강사가 과제를 생성하고 과제 설명, 제출 기한 등을 설정할 수 있는 기능                               | `assignment`      |
| **과제 제출**           | 학생이 과제를 제출할 수 있는 기능 (파일 업로드 가능)                                               | `submission`      |
| **과제 성적 평가**       | 강사가 제출된 과제를 평가하고 점수를 등록할 수 있는 기능                                           | `assignment_grade`|
| **강의 자료 업로드**     | 강사가 강의 자료(파일 등)를 업로드 및 관리할 수 있는 기능                                          | `content`         |
| **강의 시간 및 설명 관리**| 강의 제목, 강의 설명, 강의 시간, 강의 URL 등을 설정 및 수정할 수 있는 기능                         | `lecture`         |
| **코스(과정) 관리**      | 강사가 학습 코스를 생성하고 설명 및 시작/종료 일자를 설정할 수 있는 기능                           | `course`          |
| **학생-강의 성적 조회**  | 학생과 강사가 해당 강의 및 퀴즈, 과제 성적을 확인할 수 있는 기능                                   | `quiz_grade`, `assignment_grade` |
| **로그 기록 및 시간 관리**| 데이터 생성 및 수정 시간을 자동으로 기록                                                          | `created_at`, `updated_at` 컬럼 |
| **수강 상태 변경**       | 수강 신청 상태를 `등록`, `승인`, `취소` 등으로 변경할 수 있는 기능                                 | `registration`    |

## 팀 정보

| Team leader                                                                                                          | member                                                                                                                   | member                                                                                                                  | member                                                                                                               | member                                                                                                               | 
| -------------------------------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------------------ | ----------------------------------------------------------------------------------------------------------------------- | -------------------------------------------------------------------------------------------------------------------- | -------------------------------------------------------------------------------------------------------------------- | 
| <img width="100" alt="이신행" src="" />추가예정 | <img width="100" alt="김효민" src="https://github.com/user-attachments/assets/23165382-a736-4b75-9a4e-2367adc996df" /> | <img width="100" alt="성현아" src="https://github.com/user-attachments/assets/df1445c5-c918-4de3-a504-972fb5cf6171" /> | <img width="100" alt="정태민" src="" /> 추가예정| <img width="100" alt="조영무" src="https://github.com/user-attachments/assets/73009aef-c171-4539-9f60-de46d1585e50" /> |
| [이신행](https://github.com/LeeShinHaeng)                                                                                  | [김효민](https://github.com/Hm-source)                                                                               | [성현아](https://github.com/sha2170)                                                                                  | [정태민](https://github.com/Jung-Taemin)                                                                               | [조영무](https://github.com/fprh13)                                                                             |

---

## 기술 스택

### Tech

<img src="https://img.shields.io/badge/Java-17-FC4C02?style=flat-square&logo=Java&logoColor=white"/> <img src="https://img.shields.io/badge/Spring boot-3.4.0-6DB33F?style=flat-square&logo=Spring boot&logoColor=white"/> <img src="https://img.shields.io/badge/gradle-02303A?style=flat-square&logo=ApacheMaven&logoColor=white"/> <img src="https://img.shields.io/badge/Spring Data JPA-0078D4?style=flat-square&logo=Spring Data JPA&logoColor=white"/> <img src="https://img.shields.io/badge/Mapstruct-C70D2C?style=flat-square&logo=mapstruct&logoColor=white"/> <img src="https://img.shields.io/badge/MySQL-2AB1AC?style=flat-square&logo=MySQL&logoColor=white"/> <img src="https://img.shields.io/badge/Amazon RDS-527FFF?style=flat-square&logo=amazon aws&logoColor=yellow"/> <img src="https://img.shields.io/badge/Junit-25A162?style=flat-square&logo=Junit5&logoColor=white"/> <img src="https://img.shields.io/badge/JWT-000000?style=flat-square&logo=JSONWebTokens&logoColor=white"/>  

### Deploy

<img src="https://img.shields.io/badge/Github Actions-2088FF?style=flat-square&logo=github&logoColor=black"/> <img src="https://img.shields.io/badge/Amazon EC2-FF9900?style=flat-square&logo=amazonec2&logoColor=black"/> <img src="https://img.shields.io/badge/Amazon CodeDeploy-EF2D5E?style=flat-square&logo=amazonaws&logoColor=black"/> <img src="https://img.shields.io/badge/Amazon CodePipeline-4A154B?style=flat-square&logo=amazon aws&logoColor=yellow"/> <img src="https://img.shields.io/badge/Amazon S3-E34F26?style=flat-square&logo=Amazon S3&logoColor=white"/>

### Tool

<img src="https://img.shields.io/badge/IntelliJ IDEA-8A3391?style=flat-square&logo=IntelliJ IDEA&logoColor=black"/> <img src="https://img.shields.io/badge/Notion-FFFFFF?style=flat-square&logo=Notion&logoColor=black"/> <img src="https://img.shields.io/badge/Github-000000?style=flat-square&logo=Github&logoColor=white"/> <img src="https://img.shields.io/badge/Discord-5865F2?style=flat-square&logo=Discord&logoColor=white"/>  

---

## ERD
![학습관리시스템 (1)](https://github.com/user-attachments/assets/75109f32-7479-467b-8c6e-aedcdb8f4965)


## 역할 분담
- 이신행: 팀장, 문서화, 배포, 프로젝트 구성
- 성현아: 퀴즈 도메인
- 김효민: 과정 도메인
- 조영무: 유저 도메인
- 정태민: 과제 도메인


---

# 컨벤션

## 브랜치 전략

Git Flow 을 사용합니다.

- **main**:

  - 프로덕션 배포용 브랜치입니다.
  - 안정된 코드만 병합되며, 실제 서비스에 반영되는 코드입니다.
  - 직접적으로 작업하지 않고, `release` 브랜치 또는 `hotfix` 브랜치에서 병합됩니다.

- **dev**:

  - 개발 중인 브랜치입니다.
  - `feature` 브랜치에서 작업이 완료되면 `dev` 브랜치로 병합합니다.

- **feature**:
  - 기능 개발 브랜치입니다. (\* 큰 기능이 아닌 작은 단위의 기능을 말합니다.)
  - 새로운 기능, 개선사항, 또는 버그 수정을 작업할 때 사용합니다.
  - 작업이 완료되면 `dev` 브랜치로 병합합니다.
  - 브랜치명은 `feature/기능명` 형식으로 작성합니다.

## commit 규칙

| **태그**   | **설명**                | **세부 내용**                                               |
| ---------- | ----------------------- | ----------------------------------------------------------- |
| `feat`     | 기능 (새로운 기능 추가) | 새로운 기능을 추가할 때 사용                                |
| `fix`      | 버그 (버그 수정)        | 버그를 수정할 때 사용                                       |
| `refactor` | 리팩토링                | 비즈니스 로직 변경 없이 코드 구조를 개선할 때 사용          |
| `design`   | 사용자 UI 디자인 변경   | CSS 등 사용자 UI 디자인을 변경할 때 사용                    |
| `comment`  | 주석 추가 및 변경       | 필요한 주석을 추가하거나 변경할 때 사용                     |
| `style`    | 스타일 변경             | 코드 형식, 세미콜론 추가 등 비즈니스 로직에 영향 없는 변경  |
| `docs`     | 문서 수정               | 문서 추가, 수정, 삭제 (README 등 문서 관련 작업)            |
| `test`     | 테스트 코드 작업        | 테스트 코드 추가, 수정, 삭제 (비즈니스 로직 변경 없음)      |
| `chore`    | 기타 변경사항           | 빌드 스크립트 수정, assets 추가, 패키지 매니저 설정 변경 등 |
| `init`     | 초기 생성               | 프로젝트 초기 설정 및 파일 생성 작업                        |
| `rename`   | 파일/폴더명 변경        | 파일 또는 폴더명을 수정하거나 이동했을 때 사용              |
| `remove`   | 파일 삭제               | 파일을 삭제한 경우                                          |

- (ex) feat: 설명 #이슈번호

## merge

- (ex)
  - main에서 feature/기능1 merge할 때 : merge: main <- feature/기능1 #이슈번호
  - feature/기능1에서 main merge할 때 : merge: main -> feature/기능1
 
## Issue
- 이슈 템플릿 양식에 맞추어 작성합니다.(양식 등록 완료)
- 이슈 작성 시 라벨을 넣어 이슈 타입을 표현합니다.

## PR
- pr 템플릿 양식에 맞추어 작성합니다.(양식 등록 완료)


