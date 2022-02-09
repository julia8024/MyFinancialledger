# Info
<table> 
  <tr>
    <td>프로젝트명</td>
    <td>나만의 가계부</td>
    <td>제작기간</td>
    <td>2021.01.25 ~ 2021.02.26 (33일)</td>
  </tr>
  <tr>
    <td>참여인원</td>
    <td>5명</td>
    <td>개발환경</td>
    <td>JAVA, Android Studio, Room DB</td>
  </tr>
  <tr>
    <td>나의 역할</td>
    <td colspan = 3>입출금 내역 기록 기능의 화면 레이아웃과 데이터베이스로의 데이터 전송 및 불러오기</td>
  </tr>
  <tr rowspan = 3>
    <td>목적</td>
    <td colspan = 3>
      1. 동아리 내 신입생들의 앱 개발 학습효과 극대화.<br>
      2. 앱 구현 및 데이터베이스에 대한 이해<br>
      3. 안드로이드 스튜디오 학습<br>
    </td>
  </tr>
  <tr rowspan = 6>
    <td>구현내용</td>
    <td colspan = 3>
      1. 사용자가 입력한 데이터를 데이터베이스로 전송<br>
      2. 데이터베이스에서 데이터 가져오기<br>
      3. 일/월별 지출액 비교<br>
      4. 목표 달성 현황<br>
      5. 목표 금액과 지출유형, 은행 설정<br>
      6. 장르별 사용 내역 그래프로 띄우기<br>
    </td>
  </tr>
</table>
<br><br>

## 서비스 간략소개

### 소개

고령화 사회에서 초고령화 사회로 진입할 것이라는 통계청의 예측에 따라 사실상 연금만으로는 노후를 책임질 수 없다는 것이 현실.<br>
안정적인 미래를 위해서라면 재테크는 현대인들의 평생 숙제임.<br><br>

불필요한 소비는 줄이고 절약하는 습관을 들이는 데에 도움을 주고자 본 어플리케이션을 기획 및 개발.<br><br>

- 나만의 가계부: 사용자가 보다 쉽게 자신의 지출과 소비패턴을 파악할 수 있도록 도움을 주는 가계부 어플리케이션<br>
- 목표고객: 20대 초반 사회초년생
<br>

<table>
  <tr rowspan = 3>
    <td colspan = 4>
      '나만의 가계부'는<br> 
      입출금 기록을 날마다 작성하여 돈의 흐름이 어떻게 흘러가는지 파악할 수 있도록 함.<br>
      -> 목표 고객의 소비 성향에 긍정적인 효과를 줄 것.</td>
  </tr>
</table>
<br><br>

### 앱 구조

### 데이터 흐름

### 데이터 구조

장르/은행 종류, 기타설정: SharedPreference에 저장

#### 입출금 기록: 관계형 DB

<table>
  <tr>
    <td>이름</td>
    <td>ID</td>
    <td>날짜</td>
    <td>금액</td>
    <td>입금/출금</td>
    <td>매체</td>
    <td>장르 or 은행 종류</td>
    <td>메모</td>
  </tr>
  <tr>
    <td>자료형</td>
    <td>int</td>
    <td>String</td>
    <td>int</td>
    <td>String</td>
    <td>String</td>
    <td>String</td>
    <td>String</td>
  </tr>
  <tr>
    <td>입력 예시</td>
    <td>1</td>
    <td>2021-02-26</td>
    <td>50000</td>
    <td>출금</td>
    <td>현금</td>
    <td>쇼핑</td>
    <td>"옷"</td>
  </tr>
</table>
<br>

#### 목표 기록: 관계형 DB
<table>
  <tr>
    <td>이름</td>
    <td>시작일</td>
    <td>종료일</td>
    <td>목표금액</td>
  </tr>
  <tr>
    <td>자료형</td>
    <td>String</td>
    <td>String</td>
    <td>int</td>
  </tr>
  <tr>
    <td>입력예시</td>
    <td>2021-03-01</td>
    <td>2021-03-31</td>
    <td>50000</td>
  </tr>
</table>
<br>

## 레이아웃 및 기능

