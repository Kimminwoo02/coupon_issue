
## **프로젝트 구조**
<div align="center">
  <img width="1033" alt="스크린샷 2024-08-16 오후 11 26 53" src="https://github.com/user-attachments/assets/f198d2c7-5d13-4e45-9a1a-557c8749a4f2">

</div>

## **ERD**
<img width="524" alt="스크린샷 2024-08-16 오후 10 45 45" src="https://github.com/user-attachments/assets/3330ab51-9d68-4ef4-a35e-f56f9af04085">


## **기술 스택 🛠️**

<div>

  <img width="450" alt="스크린샷 2024-08-16 오후 11 29 49" src="https://github.com/user-attachments/assets/5f5bcd89-315b-4a39-aa73-aeca415fe8d4">
  <br>
 <img width="450" alt="스크린샷 2024-08-16 오후 11 32 14" src="https://github.com/user-attachments/assets/4d61f849-8c68-4081-a114-9938bad346db">
<br>
<img width="450" alt="스크린샷 2024-08-18 오후 6 08 29" src="https://github.com/user-attachments/assets/7fc7644f-5fda-4880-b75a-bf7c90f987b0">

<br>
</div>


## **이슈️**

<div>
1. 최적화
<br>
대규모 트래픽이 발생할 경우 API서버를 확장함으로써 부하를 해소할 수 있다고 생각했지만 병목지점이 생기는 DB는 부하 분산으로 해결할 수 없었습니다.
해결책으로 캐시, 데이터베이스 서버 확장(Master, Slave), 샤딩 등이 있었고, 캐시를 통해 해결하였습니다.
<br>
<br>
 <img width="700" alt="스크린샷 2024-08-18 오후 6 21 50" src="https://github.com/user-attachments/assets/e599f2d6-0550-4bab-8bc1-622415ebfc18">
<br>
<br>

2.동시성 문제
<br>
선착순으로 쿠폰을 발급해주다보니 순차적으로 쿠폰을 발급하는 경우, 문제없이 쿠폰을 발급할 수 있었지만 동시에 요청이 들어온 경우 발급수량이 맞지않는 문제가 발생했습니다.
이를 해결하기 위해 먼저 들어온 요청부터 Lock 획득을 시도하여 Lock을 걸어준 다음 요청을 처리하였습니다.
<br><p>순차적으로 접근한 경우</p>
<img width="700" alt="스크린샷 2024-08-18 오후 6 30 45" src="https://github.com/user-attachments/assets/3c7219a4-f707-427c-855b-1c3ae35321cd">

<p>동시에 접근한 경우</p>
<img width="700" alt="스크린샷 2024-08-18 오후 6 33 45" src="https://github.com/user-attachments/assets/0c734687-2f9b-4922-90f7-760ab7843936">

<p>분산환경을 대비한 Redis Lock</p>
<img width="700" alt="스크린샷 2024-08-18 오후 6 42 21" src="https://github.com/user-attachments/assets/741a0a12-e568-4cd1-896f-dab0edea7750">

</div>









