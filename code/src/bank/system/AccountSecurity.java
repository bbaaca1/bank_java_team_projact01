package bank.system;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AccountSecurity {
	
	
	final static Logger logger = LogManager.getLogger(AccountSecurity.class);
	
	
	/*
	 * 메소드명: certificationbNumber
	 * 파라미터: None
	 * 반환값: String (인증번호를 반환)
	 * 기능 설명:
	 * cfNumber(인증번호)안에 1~9까지의 랜덤숫자 4자리를 받아
	 * 인증번호를 반환한다.
	 */
	public String certificationbNumber() {
		// 인증번호용 메소드입니다. (작성자 백동민 2023.07.07)
		int number = 0;
		String cfNumber = "";
		
		for (int i = 1; i <= 4; i++) {
			number = (int)(Math.random()*10);
			cfNumber += String.valueOf(number);
		}
		return cfNumber;
	}
	
	/*
	 * 메소드명: voicePhishing
	 * 파라미터: String AccountNumber, Boolean isFreez
	 * AccountNumber: freezMap을 사용하기 위한 사용자의 계좌번호
	 * isFreez: 사용자의 계좌 동결 상태 (true: 계좌 동결, false: 계좌 동결 해제)
	 * freezMap: 계좌번호의 대한 동결시간이 저장되어 있는 Map (key: String(계좌번호), value: String(동결이 시작된 시간))
	 * 반환값: void
	 * 기능 설명
	 * 현재 시간을 확인하여 일(Day)과 초(Second) 단위로 변환하여 계산한다.
	 * 동결 상태(isFreez)가 true인 경우, 동결이 시작되었는지 확인한다.
	 * 계좌번호가 freezMap에 존재하지 않는 경우, 동결이 처음 시작되었음을 의미하므로 맵에 계좌번호와 현재 동결 시작 시간을 추가한다.
	 * 동결 상태가 true인 경우, 현재 시간과 동결 시작 시간을 비교하여 동결 시간을 계산한다.
	 * 동결 시간이 24시간(1일) 이상 지난 경우, 하루가 지나 초가 초기화가 되었다는걸 의미하므로 Second에 86400초를 일수만큼 더한다.
	 * 동결 시간이 10분을 초과한 경우, 동결이 해제되었음을 의미하므로 맵에서 해당 계좌번호와 동결 시작 시간을 제거하고 계좌의 동결 상태를 해제한다.
	 */
	public void voicePhishing(String AccountNumber, boolean isFreez) {
		AccountTime at = new AccountTime(); //at.currentTime을 쓰기위한 클래스 선언 (반환값 int[])
		int[] time = at.currentTime(0,false); //현재 연, 월, 일, 시, 분, 초가 저장되어 있는 int[]형 변수
		int day = 0; //월을 일로 치환한 값을 담을 변수
		switch(time[1] - 1) { //time[1]은 월이 들어가 있음. 현재 월 전까지의 일
		case 11:
			day += 30;
		case 10:
			day += 31;
		case 9:
			day += 30;
		case 8:
			day += 31;
		case 7:
			day += 31;
		case 6:
			day += 30;
		case 5:
			day += 31;
		case 4:
			day += 30;
		case 3:
			day += 31;
		case 2:
			if(time[0] % 4 == 0) day += 29; //윤년일 때 day에 29를 더해준다.
			else day += 28;
		case 1:
			day += 30;
		case 0:
			break;
		}
		int Day = time[0] * 365 + day + time[2]; //현재 연, 월, 일을 일로 치환해 합한 값이 들어가는 변수
		int Second = time[3] * 3600 + time[4] * 60 + time[5]; //현재 시, 분, 초를 초로 치환해 합한 값이 들어가는 변수
		String value = Integer.toString(Day) + " " +Integer.toString(Second); //Day와 Second를 공백으로 구분한 값이 들어가는 변수
		
		if(isFreez) { //만약 동결이 되어있다면
			
			//이 구문이 동결시에 단 한번만 실행 되어야 한다.
			if(BankMain.freezMap.get(AccountNumber) == null) { //freezMap에 계좌번호를 넣었을 때 값이 없다면
				logger.info(AccountNumber+"has been freez");
				BankMain.freezMap.put(AccountNumber, value); //freezMap에 (계좌번호, 동결이 시작된 시간)을 넣어준다.
			}
			int i; //반복변수 i
			int resultDay = 0; //결과 일이 저장될 변수
			int resultSecond = 0; //결과 초가 저장될 변수
			String equalsDay = ""; //동결이 시작된 연, 월, 일을 일로 치환해 합한 값
			String equalsSecond = ""; //동결이 시작된 시, 분, 초를 일로 치환해 합한 값
			String Time = BankMain.freezMap.get(AccountNumber); //계좌번호에 대한 동결이 시작된 시간을 담은 변수
			for(i = 0; Time.charAt(i) != ' '; i++) { //값을 공백을 기준으로 나누는 반복문
				equalsDay += Time.charAt(i); //일을 Time에서 분리
			}
			
			equalsSecond = Time.substring(i + 1, Time.length()); //초를 Time에서 분리
			
			
			
			resultDay = Day - Integer.parseInt(equalsDay); //Day(현재 연, 월, 일을 일로 치환해 합한 값) - Integer.parseInt(equalsDay)(동결이 시작된 연, 월, 일을 일로 치환해 합한 값)
			int addSecond = 0;
			
			if(resultDay != 0) { //동결시간이 하루이상 지났을 경우
				addSecond += 86400 * resultDay; //하루가 지나면 0으로 초기화가 되기 때문에 값을 더해준다.
			}
			
			resultSecond = (Second + addSecond) - Integer.parseInt(equalsSecond); //(Second(현재 시, 분, 초를 초로 치환해 합한 값) + addSecond(하루가 지났을 경우의 추가되는 초) - Integer.parseInt(equalsSecond)(동결이 시작된 시, 분, 초를 초로 치환해 합한 값)
			
			if(resultSecond > 600) { //동결시간이 10분을 초과했을 경후
				logger.info(AccountNumber+"has been unfrozen");
				BankMain.freezMap.remove(AccountNumber); //계좌번호에 대한 동결 시작시간 지우기
				BankMain.list.get(BankMain.map.get(AccountNumber)).setFreez(false); //isFreez의 값을 true에서 false로 바꾸기
			}

		}
		return;
		
	}
	
	/*
	 * 메소드명: nonTrading
	 * 파라미터: int account
	 * account: 검증할 계좌번호
	 * 반환값: int (현재날짜에서 마지막 거래날짜를 뺀 값이 반환됨)
	 * 현재 날짜를 일(today)로 바꾸고 마지막 거래 날짜를 일(dateDate)로 바꾼다.
	 * 현재날짜에서 마지막 거래날짜를 뺀 값을 반환한다.
	 */
	
	public int nonTrading(int account) {
		// 휴면계좌 검증용 코드입니다.
		// 계산식 년 * 365 + (저장된 월에서 1월까지의 날짜의 합)- 저장된 날짜
		AccountTime at = new AccountTime();
		int[] time = at.currentTime(0,false);
		int day = 0;
		switch(time[1] - 1) {
		case 11:
			day += 30;
		case 10:
			day += 31;
		case 9:
			day += 30;
		case 8:
			day += 31;
		case 7:
			day += 31;
		case 6:
			day += 30;
		case 5:
			day += 31;
		case 4:
			day += 30;
		case 3:
			day += 31;
		case 2:
			if(time[0] % 4 == 0) day += 29;
			else day += 28;
		case 1:
			day += 30;
		case 0:
			break;
		}
		int toDay = time[0] * 365 + day + time[2];
		// 계산을 위한 오늘 날짜를 toDay에 설정
		
		
		int dateYear = Integer.parseInt(BankMain.list.get(account).getDate().substring(0,4)); //문자열 인덱싱 예) 2023
		int dateMonth = Integer.parseInt(BankMain.list.get(account).getDate().substring(4,6)); //문자열 인덱싱 예) 07
		int dateDay = Integer.parseInt(BankMain.list.get(account).getDate().substring(6,8)); //문자열 인덱싱 예) 13
		
		int Day = 0;
		
		switch(dateMonth -1 ) {
		case 11:
			Day += 30;
		case 10:
			Day += 31;
		case 9:
			Day += 30;
		case 8:
			Day += 31;
		case 7:
			Day += 31;
		case 6:
			Day += 30;
		case 5:
			Day += 31;
		case 4:
			Day += 30;
		case 3:
			Day += 31;
		case 2:
			if(time[0] % 4 == 0) Day += 29;
			else Day += 28;
		case 1:
			Day += 30;
		case 0:
			break;
		}
		
		int dateDate = dateYear * 365 + Day + dateDay;
		// 계산을 위한 저장된 날짜를 toDay에 설정
		
		
		return toDay - dateDate; // 저장된오늘날짜 - 저장된날짜를 리턴
	}
	
	
	/*
	 * 메소드명: cheakState
	 * 파라미터: int index, int num
	 * index: 인덱스값(사용자의 리스트위치)으로 계좌 상태를 보기 위해 사용
	 * num: num이 1이면 이체 제한만 해당, num이 0이라면 출금 제한 나머지 값은 입금에 해당함
	 * 반환값: boolean(계좌의 상태들 중 해당하는 상태가 있다면 true를 반환 아니라면 false를 반환)
	 * 기능설명:
	 * 사용자의 리스트에 들어가 있는 계좌상태 변수들을 확인하여 
	 * 각 상태에 따라 해당하는 문구를 출력 후 true를 반환
	 * 해당하지 않는다면 false를 반환 
	 */
	
	public boolean cheakState(int index, int num) {
		// 계좌 상태를 확인하는 코드
		
		if (BankMain.list.get(index).isNonTradingAcc()) { // isNonTradingAcc가 true라면 실행
			System.out.println("휴면 계좌입니다.");
			return true;
		}
		
		voicePhishing(BankMain.list.get(index).getAccountNumber(), BankMain.list.get(index).isFreez()); //isFreez를 확인하기 전 동결시간 확인 메소드
		if (BankMain.list.get(index).isFreez()) { // isFreez가 true라면 실행
			System.out.println("계좌가 동결중입니다.");
			return true;
		}
		
		if (BankMain.list.get(index).isTransferLimit() && num == 1) { // isTransferLimit가 true라면 실행
			System.out.println("계좌가 이체 제한 상태입니다.");
			return true;
		}
		
		if (BankMain.list.get(index).isWithLimit() && num == 0) { // isWithLimit가 true라면 실행
			System.out.println("계좌가 출금 제한 상태입니다.");
			return true;
		}
		
		return false;
		
	}
	
}
