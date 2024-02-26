package bank.system;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AccountTime {
	
	private int[] dateTime = new int[6];
	
	//작성자 김용욱
	
	/*
	 * 메소드명: currentTime
	 * 파라미터: int index, boolean isTimeDate
	 * index: list 내의 setDate를 사용하기 위한 인덱스 값
	 * isTimeDate: true일 때 list에 setDate를 사용하여 현재 시간을 저장, false일 때 저장하지 않음.
	 * 반환값: int[] (연, 월, 일, 시, 분, 초가 저장된 배열)
	 * 
	 * 기능 설명:
	 * https.protocols 시스템 속성을 설정하여 SSL/TLS 버전의 호환성 문제를 해결한다.
	 * "https://worldtimeapi.org/api/timezone/Asia/Seoul" URL을 이용하여 아시아 서울의 시간 정보를 가져온다.
	 * HttpURLConnection을 사용하여 HTTP 연결을 생성하고 GET 요청을 수행한다.
	 * 연결의 응답 코드가 HTTP_OK인 경우, 데이터를 읽어온다.
	 * LocalDateTime.now()를 사용하여 현재 시간 정보를 가져온다.
	 * dateTime 배열에 현재 연, 월, 일, 시, 분, 초를 저장한다.
	 * isTimeDate가 true인 경우, DateTimeFormatter를 사용하여 현재 시간을 "yyyyMMdd" 형식으로 변환하고 해당 인덱스의 list에 setDate로 저장한다.
	 * dateTime 배열을 반환한다.
	 */
	
	//메소드
	public int[] currentTime(int index, boolean isTimeDate) {
	
		System.setProperty("https.protocols", "TLSv1.2");//서버와 클라이언트의 SSL/TLS 버전이 맞지 않아 발생
		
		try {
            // URL 설정
            URL url = new URL("https://worldtimeapi.org/api/timezone/Asia/Seoul");

            // HTTP 연결 생성
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            // 응답 코드 확인
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                // 응답 데이터 읽기
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line;
                StringBuilder response = new StringBuilder();
                while ((line = br.readLine()) != null) {
                    response.append(line);
                }
                br.close();

                // 예시로 현재 시간을 출력
                LocalDateTime now = LocalDateTime.now();
                
                dateTime[0] = now.getYear();
                dateTime[1] = now.getMonthValue();
                dateTime[2] = now.getDayOfMonth();
                dateTime[3] = now.getHour();
                dateTime[4] = now.getMinute();
                dateTime[5] = now.getSecond();
                
                if (isTimeDate) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
                    String truncatedDateTime = now.format(formatter);
                    BankMain.list.get(index).setDate(truncatedDateTime);
				}
                
                return dateTime;
                
            } else {
                System.out.println("HTTP Request Error: " + conn.getResponseCode());
                
            }

            // 연결 종료
            conn.disconnect();
            return dateTime;
        } catch (Exception e) {
            e.printStackTrace();
            return dateTime;
        }
		
	}
}
