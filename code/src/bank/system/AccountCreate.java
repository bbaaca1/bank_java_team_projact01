package bank.system;

public class AccountCreate {
	// 계좌 생성 및 사용용 클래스입니다.
	//필드(계좌용 변수들)
	private String name; // 예금주명
	private String accountNumber; // 계좌번호
	private String ssn; // 주민등록번호
	private String homeAddress; // 집주소
	private String phoneNumber;  // 전화 번호
	private long balance; // 잔액
	private String accountType;  // 계좌 유형(현재 일반 계좌, 정기 적금)
	private String date; // 최종 거래 일자
	private String accountPasswd; // 거래 비밀 번호
	private int state; // 계좌 상태
	
	
	//필드 (계좌 상태용 변수들)
	private long withdrawalLimits = 0; // 출금 한도 설정 변수
	private long transferLimits = 0; // 이체 한도 설정 변수
	private String freezTime; // 동결 시간 설정 변수 
	
	private boolean isFreezing = false; // 동결상태 확인 변수
	private boolean isNonTradingAccing = false; // 휴면 계좌 확인 변수
	private boolean isWithLimiting = false; // 출금 제한 확인 변수
	private boolean isTransferLimiting = false; // 이체 제한 확인 변수
	

	// 생성자
	public AccountCreate(String name, String accountNumber, String ssn, String homeAddress, String phoneNumber,
			long balance, String accountType, String date,String accountPasswd, int state) {
		// list에 계좌의 정보를 담은 객체를 저장하기 위한 생성자
		this.name = name;
		this.accountNumber = accountNumber;
		this.ssn = ssn;
		this.homeAddress = homeAddress;
		this.phoneNumber = phoneNumber;
		this.balance = balance;
		this.accountType = accountType;
		this.date = date;
		this.accountPasswd = accountPasswd;
		this.state = state;
	}
	
	// getter and setter
	
	public String getAccountPasswd() {
		return accountPasswd;
	}

	public void setAccountPasswd(String accountPasswd) {
		this.accountPasswd = accountPasswd;
	}

	public void setBalance(long balance) {
		this.balance = balance;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getSsn() {
		return ssn;
	}

	public void setSsn(String ssn) {
		this.ssn = ssn;
	}

	public String getHomeAddress() {
		return homeAddress;
	}

	public void setHomeAddress(String homeAddress) {
		this.homeAddress = homeAddress;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public long getBalance() {
		return balance;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
	
	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}
	public boolean isFreez() {
		return isFreezing;
	}
	
	/*
	 *메소드명: setFreez
	 *파라미터: boolean isFreez
	 *isFreez: true라면 계좌 동결, false라면 계좌 동결해제
	 *반환값: void
	 *기능 설명: 
	 *isFreez값에 따라서 isFreezing에 true 또는 false를 넣어주고
	 *이체제한(isTransferLimiting) 또는 출금제한(isWithLimiting) 상태에 따라서
	 *state의 값을 변경해준다.
	 */
	
	public void setFreez(boolean isFreez) {
		this.isFreezing = isFreez;
		
		if (isFreez) {
			state = 1;
			if (isTransferLimiting && isWithLimiting) {
				state = 8;
			} else if (isTransferLimiting) {
				state = 7;
			} else if (isWithLimiting) {
				state = 6;
			}
		} else {
			state = 0;
			if (isTransferLimiting && isWithLimiting) {
				state = 5;
			} else if (isTransferLimiting) {
				state = 4;
			} else if (isWithLimiting) {
				state = 3;
			}
		}
		
	}



	public boolean isNonTradingAcc() {
		return isNonTradingAccing;
	}



	public void setNonTradingAcc(boolean isNonTradingAccing) {
		this.isNonTradingAccing = isNonTradingAccing;
	}



	public boolean isWithLimit() {
		return isWithLimiting;
	}



	public void setWithLimit(boolean isWithLimiting) {
		this.isWithLimiting = isWithLimiting;
	}



	public boolean isTransferLimit() {
		return isTransferLimiting;
	}



	public void setTransferLimit(boolean isTransferLimiting) {
		this.isTransferLimiting = isTransferLimiting;
	}
	
	public long getTransferLimits() {
		return transferLimits;
	}

	/*
	 * 메소드명: setTransferLimits
	 * 파라미터: long transferLimits
	 * transferLimts: 계좌이체를 한 금액을 받는다.
	 * this.transferLimts에는 오늘 계좌이체를 한 금액이 저장된다.
	 * 기능 설명 :
	 * 이체한 금액을 파라미터로 받아 오늘 이체한 금액이 3천만원이 넘어가면
	 * 이체제한을 설정한다.
	 * 
	 */
	
	public void setTransferLimits(long transferLimits) {
		this.transferLimits += transferLimits;
		if (transferLimits == 0) 
		{
			this.transferLimits = 0;
			isTransferLimiting = false;
		}
		if (this.transferLimits > 30000000) {
			isTransferLimiting = true;
			if (state == 3) {
				state = 5;
			} else {
				state = 4;
			}
			
		}
	}

	public long getWithdrawalLimits() {
		return withdrawalLimits;
	}
	
	/*
	 * 메소드명: setWithdrawalLimits
	 * 파라미터: long withdrawalLimits
	 * withdrawalLimits: 출금을 한 금액을 받는다.
	 * this.withdrawalLimits에는 오늘 출금을 한 금액이 저장된다.
	 * 기능 설명 :
	 * 출금한 금액을 파라미터로 받아 오늘 이체한 금액이 6백만원이 넘어가면
	 * 출금제한을 설정한다. 
	 */
	
	public void setWithdrawalLimits(long withdrawalLimits) 
	{
		this.withdrawalLimits += withdrawalLimits;
		if (withdrawalLimits == 0) 
		{
			this.withdrawalLimits = 0;
			isWithLimiting = false;
		}
		if (this.withdrawalLimits > 6000000) {
			isWithLimiting = true;
			if (state == 4) {
				state = 5;
			} else {
				state = 3;
			}
			
		}
	}



	public String getFreezTime() {
		return freezTime;
	}


	public void setFreezTime(String freezTime) {
		this.freezTime = freezTime;
	}


	// 출력용 toString();
	/*
	 * 메소드명: toString
	 * 파라미터: None
	 * 반환값 String(사용자의 이름, 계좌번호, 주민등록번호, 주소, 휴대폰번호, 계좌 잔액, 마지막 거래날짜, 계좌 비밀번호, 계좌 상태를 출력한다.)
	 * 기능 설명:
	 * 사용자의 정보를 출력한다.
	 *
	 */
	@Override
	public String toString() {
		
		String name = String.format("%-10s", getName());
		if (name.length() > 10) {
			name = name.substring(0, 10)+"...";
		}
		
		String value = String.format("%-8s", String.format("%,d", getBalance()));
		if (value.length() > 8) {
			value = value.substring(0, 6)+"...";
		}
		
		String homeAddress = String.format("%-10s", getHomeAddress());
		if(homeAddress.length() > 10) {
			homeAddress = homeAddress.substring(0, 10)+"...";
		}
		
		
		
		return "|" + name +"|"+ getAccountNumber() + " |"+ getSsn() + " |"+ homeAddress + " |"+ getPhoneNumber() + " |" + value + " |"+ getDate() + " |"+ getAccountPasswd() +" |"+ getState() ;
	}
	
	// 상세 출력용 메소드
	/*
	 * 메소드명: printInfo
	 * 파라미터: None
	 * 반환값: void
	 * 기능 설명:
	 * toString에서 생략된 내용들을 
	 * printInfo에서 생략하지 않고 세로로 출력한다.
	 * 
	 */
	public void printInfo() {
		System.out.printf("|이름 : %s", name);
		System.out.printf("\n|계좌 번호 : %s", accountNumber);
		System.out.printf("\n|주민등록번호 : %s", ssn);
		System.out.printf("\n|주소 : %s", homeAddress);
		System.out.printf("\n|전화번호 : %s", phoneNumber);
		System.out.printf("\n|잔액 : %,3d", balance);
		System.out.printf("\n|계좌유형 : %s", accountType);
		System.out.printf("\n|최종거래일 : %s", date);
		System.out.printf("\n|비밀번호 : %s", accountPasswd);
		System.out.printf("\n|계좌상태 : %s", state);
		System.out.printf("\n|총출금액 : %s", withdrawalLimits);
		System.out.printf("\n|총이채액 : %s", transferLimits);
		System.out.println("");
	}	
	
	//계좌 상태 변경용 메소드
	/*
	 * 메소드명: accountStatus
	 * 파라미터: int status
	 * status: 계좌 상태를 입력받는다.
	 * 반환값: void
	 * 기능 설명: 
	 * 
	 * status의 숫자에 따라 상태를 변경함
		   0 : 정상 
		   1 : 계좌 동결 
		   2 : 휴면 계좌 
		   3 : 출금 제한
		   4 : 이체 제한
		   5 : 이체,출금 제한
		   6 : 출금,계좌 동결
		   7 : 이체,계좌 동결
		   8 : 이체,출금,계좌 동결
	 * 
	 * 
	 */
	
	public void accountStatus(int status) {
		switch (status) {
		case 0:
			isFreezing = false;
			isNonTradingAccing = false;
			isWithLimiting = false;
			isTransferLimiting = false;
			break;

		case 1:
			isFreezing = true;
			break;
			
		case 2:
			isNonTradingAccing = true;
			break;
			
		case 3:
			isWithLimiting = true;
			break;
			
		case 4:
			isTransferLimiting = true;
			break;
			
		case 5:
			isWithLimiting = true;
			isTransferLimiting = true;
			break;
		
		case 6:
			isWithLimiting = true;
			isFreezing = true;
			break;
		
		case 7:
			isTransferLimiting = true;
			isFreezing = true;
			break;
			
		case 8:
			isWithLimiting = true;
			isTransferLimiting = true;
			isFreezing = true;
			break;
		}
		
	}
}
