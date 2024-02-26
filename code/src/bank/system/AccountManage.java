package bank.system;


import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AccountManage extends AccountFileSystem {
	
	//필드
	final static Logger logger = LogManager.getLogger(AccountManage.class);
	
	/*
	 * 메소드명: manageMeun
	 * 파라미터: Scanner sc
	 * 입력받기 위해 스캐너를 사용
	 * 반환값: void
	 * 기능 설명:
	 * 계좌 관리 메뉴화면에는 4가지의 메뉴 
	 * 계좌 생성, 계좌 조회, 계좌 해지, 메인 메뉴 중
	 * 해당하는 메뉴를 선택하는 화면이다 
	 * */
	
	//메소드
	public void manageMeun(Scanner sc) {
		// 계좌 관리 메뉴화면 메소드입니다.
		
		int inputNumber = 0;
		String inputAccountNumber = null;
			
		// 계좌 메뉴 반복문 
		do {
			System.out.println("\n\n\n계좌 관리 메뉴 입니다.");
			System.out.println("1.계좌 생성 | 2. 계좌 조회 | 3.계좌 해지 | 0.메인메뉴");
			System.out.print("메뉴를 선택해주세요(숫자만 입력해주세요) > ");
			
			//계좌 메뉴 출력 (작성자 백동민)
			try {
				inputAccountNumber = sc.nextLine();
				inputNumber = Integer.parseInt(inputAccountNumber);
				
					switch (inputNumber) {
					case 0:
						// 메뉴로 복귀
						logger.info("return to main meun");
						System.out.println("메인메뉴로 돌아갑니다.");
						return;
						
					case 1:
						// 계좌 생성
						modifyAccount(inputNumber, sc);
						logger.info("end of account create");
						return;
						
					case 2:
						// 계좌 조회
						modifyAccount(inputNumber, sc);
						logger.info("end of account search");
						return;
						
					case 3:
						// 계좌 해지
						modifyAccount(inputNumber, sc);
						logger.info("end of account closure");
						return;
						
					default:
						// 오입력(숫자)
						System.out.println("잘못입력하셨습니다.");
						return;
					}
			} catch (NumberFormatException e) {
				// 숫자외 입력시
				System.out.println("잘못입력하셨습니다.");
				logger.info("return to main meun");
				return;
			} catch (Exception e) {
				// 오류 발생시 실행
				System.out.println("오류가 발생하였습니다.");
				logger.error("account menu error");
				logger.error(e.toString());
			}
	
			
		} while (true);
	}
	
	// 계좌 번호 생성용 메소드 입니다 (작성자 백동민)
		/*
		 * 메소드명: createAccountNumber
		 * 파라미터: String type
		 * type: type변수 안에는 "일반 예금" 또는 "정기 적금"의 값을 가진다.
		 * 반환값: String (완성된 계좌번호를 반환)
		 * 기능 설명: 
		 * 일반 예금일시 계좌번호 앞자리는 012
		 * 정기 적금일시 계좌번호 앞자리는 021로 시작한다.
		 * 다음 3자리는 대우은행번호 068로 고정한다.
		 * 마지막 6자리는 랜덤숫자 1에서 9사이의 숫자를 6자리 받는다.
		 * 계좌번호 : (일반예금-012, 정기적금-021) + (대우은행 번호-068) + (랜덤숫자 6자리)로 구성된다.
		 * 12자리의 계좌번호가 생성되었다면 값을 반환한다.
		 */
	
	public String createAccountNumber(String type) {
		String account = "";
		String serial = "";
		if (type.equals("일반 예금")) {
			// 일반 예금일때 첫 3자리는 012
			account += "012";
		} else {
			// 정기 적금일때 첫 3자리는 012
			account += "021";
		}
		
		account += "068";
		
		for (int i = 0; i < 6; i++) {
			serial += String.valueOf(((int)(Math.random()*9))+1);
			if (i == 5) {
				for (int j = 0; j < BankMain.list.size()-1; j++) {
					if ( BankMain.list.get(j).getAccountNumber().equals(account)) {
						serial = "";
						i = -1;
					}
				}
			}
		}
		account += serial;
		return account;
	}
	
	// 추상 메소드
	/*
	 * 메소드명: modifyAccount
	 * 파라미터: int number, Scanner sc
	 * number: 1이라면 계좌 생성, 2라면 계좌 조회, 3이라면 계좌 해지 상태를 말한다.
	 * 입력받기 위해 스캐너를 사용
	 * 반환값: void
	 * 기능 설명:
	 * 계좌 생성, 계좌 조회, 계좌 해지, 메인 메뉴 들의 코드가 적혀있는 메소드이다.
	 * 자세한 설명들은 아래 기록함
	 * 
	 */
	
	@Override
	void modifyAccount(int number, Scanner sc) {
		// 계좌 관리 기능용 추상 메소드
		
		AccountSecurity accountsecurity = new AccountSecurity();
		AccountTime at = new AccountTime();
		
		String inputStr = null; // 입력을 받을 변수
		String certificationBNumber = null; // 인증범호를 담을 변수
		int inputNum = 0; // 
		boolean isModifyacc = false; // 계좌 수정시 시간을 list에 집어넣을건지 체크하는 변수
		
		@SuppressWarnings("unused")
		int[] time;
		
		switch (number) {
		
		case 1:
			// 계좌 생성 화면(작성자 백동민 2023.07.10)
			/*
			 * 기능설명(계좌 생성) : BankMain의 list에 이름,주민등록번호,주소,전화번호,계좌유형비밀번호,최종거래일이 null이고 잔액은 1 그리고 계좌상태번호는 0인 AccountCreate 객체를 생성한다
			 * 					사용자가 이름,주민번호,주소,전화번호,입력한뒤 그걸 생성된 객체에 필드로 할당한다
			 * 					인증번호가 발급되고 발급된 인증번호를 와 입력한 인증번호가 동일할시 계좌유형(1 일반 계좌 2 정기 적금)와 비밀번호를 설정하고 같은 계좌유형의 계좌번호와 중복없는 계좌번호가 생성된다
			 *					만약 위 작업중 취소하거나 잘못입력시 생성된 객체를 삭제한후 메인으로 돌아간다
			 */
			logger.info("account create start");
			
			BankMain.list.add(new AccountCreate(null, null, null, null, null, 1, null, null, null, 0)); // 초기 잔액이 1인 빈 계좌를 생성함

			isModifyacc = true;
			
			System.out.println("#######################################################");
			System.out.println("계좌 생성 메뉴 입니다. 잘못들어오셨다면 0번을 입력하면 메인메뉴로 돌아갑니다.");
			System.out.println("#######################################################");
			
			int accountIndex = BankMain.list.size()-1; // 현재 생성한 리스트의 인덱스 번호는 list의 현재 크기 -1
			String accountNumber = null;
			
			/*
			 * 이후 코드에서  반드시 계좌가 생성이 완료되는 상황이 아니면 BankMain.list.remove(accountIndex)를 해야합니다
			 */
			
			try {
				// 이름 입력 시작
				System.out.print("\n이름을 입력해주세요 > ");
				inputStr = sc.nextLine();
				
				if (inputStr.equals("0")) {
					BankMain.list.remove(accountIndex);
					System.out.println("취소하셨습니다. 메인으로 돌아갑니다.");
					return;
				} else if(!namePattern.matcher(inputStr).matches()) {
					BankMain.list.remove(accountIndex);
					System.out.println("잘못 입력하셨습니다. 메인으로 돌아갑니다.");
					return;
				}
				
				BankMain.list.get(accountIndex).setName(inputStr);
				
				// 주민번호 입력 시작
				System.out.print("\n하이픈(-)을 제외한 주민번호를 입력해주세요 > ");
				inputStr = sc.nextLine();
				
				if (inputStr.equals("0")) {
					System.out.println("취소하셨습니다. 메인으로 돌아갑니다.");
					BankMain.list.remove(accountIndex);
					return;
				} else if(!ssnPattern.matcher(inputStr).matches()) {
					BankMain.list.remove(accountIndex);
					System.out.println("잘못 입력하셨습니다. 메인으로 돌아갑니다.");
					return;
				}
				
				BankMain.list.get(accountIndex).setSsn(inputStr);
				
				// 주소 입력 시작
				System.out.print("\n주소를 입력해주세요 > ");
				inputStr = sc.nextLine();
								
				if (inputStr.equals("0")) {
					System.out.println("취소하셨습니다. 메인으로 돌아갑니다.");
					BankMain.list.remove(accountIndex);
					return;
				}
				
				BankMain.list.get(accountIndex).setHomeAddress(inputStr);
				
				// 전화번호 입력 시작
				System.out.print("\n하이픈(-)을 제외한 전화번호를 입력해주세요 > ");
				inputStr = sc.nextLine();
				
				if (inputStr.equals("0")) {
					System.out.println("취소하셨습니다. 메인으로 돌아갑니다.");
					BankMain.list.remove(accountIndex);
					return;
				} else if (!phonePattern.matcher(inputStr).matches()) {
					BankMain.list.remove(accountIndex);
					System.out.println("잘못 입력하셨습니다. 메인으로 돌아갑니다.");
					return;
				}
				
				BankMain.list.get(accountIndex).setPhoneNumber(inputStr);
				
				// 인증번호 인증
				certificationBNumber = accountsecurity.certificationbNumber();
				System.out.println("\n인증번호 : "+ certificationBNumber);
				
				System.out.print("인증번호를 입력해 주세요 > ");
				if (certificationBNumber.equals(sc.nextLine())) {
					System.out.println("인증되었습니다.");
				} else {
					System.out.println("잘못 입력하셨습니다. 메인으로 돌아갑니다.");
					BankMain.list.remove(accountIndex);
					return;
				}
				
				// 계좌유형 입력 시작
				System.out.print("\n계좌 유형를 입력해주세요(1.일반 예금, 2.정기 적금) > ");
				inputNum = Integer.parseInt(sc.nextLine());
				
				switch (inputNum) {
				case 1:
					BankMain.list.get(accountIndex).setAccountType("일반 예금");
					break;
					
				case 2:
					BankMain.list.get(accountIndex).setAccountType("정기 적금");
					break;
					
				case 0:
					System.out.println("취소하셨습니다. 메인으로 돌아갑니다.");
					BankMain.list.remove(accountIndex);
					return;

				default:
					System.out.println("잘못 입력하셨습니다. 메인으로 돌아갑니다.");
					BankMain.list.remove(accountIndex);
					return;
				}
				
				// 비밀번호 설정
				System.out.print("\n사용하실 계좌 비밀번호 4자리를 입력해주세요 > ");
				String passwd = sc.nextLine();
				
				if (!passwdPattern.matcher(passwd).matches()) {
					BankMain.list.remove(accountIndex);
					System.out.println("잘못 입력하셨습니다. 메인으로 돌아갑니다.");
					return;
				}
				
				System.out.print("비밀번호를 다시한번 입력해주세요 > ");
				String rePasswd = sc.nextLine();
				
				if (passwd.equals(rePasswd)) {
					System.out.println("확인되었습니다.");
					BankMain.list.get(accountIndex).setAccountPasswd(passwd);
				}else {
					System.out.println("잘못 입력하셨습니다. 메인으로 돌아갑니다.");
					BankMain.list.remove(accountIndex);
					return;
				}
				
				// 계좌 생성
				time = at.currentTime(accountIndex, isModifyacc);
				accountNumber = createAccountNumber(BankMain.list.get(accountIndex).getAccountType());
				BankMain.list.get(accountIndex).setAccountNumber(accountNumber);
				
				System.out.println("\n계좌 번호는 "+ BankMain.list.get(accountIndex).getAccountNumber()+" 입니다.");
				System.out.println("확인하셨으면 아무키나 입력하세요");
				sc.nextLine();
				// 계좌 입력
				inputFileSystem();
				
				//종료
				logger.info("account create success");
				System.out.println("계좌 생성에 성공했습니다. 메인으로 돌아갑니다.");
				return;
				
			} catch (NumberFormatException e) {
				// 정수외 입력 일때 실행
				logger.info("account create failed");
				System.out.println("잘못 입력하셨습니다. 메인으로 돌아갑니다.");
				BankMain.list.remove(accountIndex); // 에러 발생시 생성된 계좌를 삭제
				return;
				
			} catch (Exception e) {
				// 에러 발생시 실행
				System.out.println("오류가 발생하였습니다. 메인으로 돌아갑니다.");
				logger.error("account create error");
				logger.error(e.toString());
				BankMain.list.remove(accountIndex); // 에러 발생시 생성된 계좌를 삭제
				return;
			}
			
			
			// 계좌 조회 및 계좌 해지 부분 입니다 (작성자 : 김종연)
			/*
			 * 기능 설명:
			 * 사용자의 계좌 번호를 입력받아
			 * 계좌 번호에 해당하는 잔액과 계좌 유형을 보여주는 곳
			 */
			
		case 2:
			// 계좌 조회 화면입니다.
			String accountCheck = null; // 계좌번호  
			inputStr = ""; // 이름
			System.out.println();
			System.out.println("#######################################################");
			System.out.println("계좌 조회입니다. 잘못들어오셨다면 0번을 입력하면 메인메뉴로 돌아갑니다.");
			System.out.println("#######################################################");
			System.out.println();
			System.out.print("하이픈(-)을 제외한 계좌 번호 12자리를 입력해주세요 > ");
			accountCheck = sc.nextLine();
			
			if (!accNumPattern.matcher(accountCheck).matches()) {
				System.out.println("잘못 입력하셨습니다. 메인으로 돌아갑니다.");
				break;
			}
			
			if (accountCheck.length() != 12) { // 12글자 이내 예외처리
				System.out.println();
				System.out.println("잘못 입력하셨습니다. 메인으로 돌아갑니다.");
				break;
			}   
			// 입력받은 문자열 숫자 검열
			System.out.println();
			System.out.print("예금주: ");
			inputStr = sc.nextLine();
			if (!namePattern.matcher(inputStr).matches()) {
				System.out.println("잘못 입력하셨습니다. 메인으로 돌아갑니다.");
				break;
			}
			
			
			for (int i = 0; i < BankMain.list.size(); i++) {
				if (accountCheck.equals(BankMain.list.get(i).getAccountNumber())) {
					if (inputStr.equals(BankMain.list.get(i).getName())) {
						System.out.println();
						System.out.print("계좌 번호: ");
						//substring 문자열로 끊기
						System.out.print(accountCheck.substring(0,3));
						System.out.print("-");
						System.out.print(accountCheck.substring(3,6));
						System.out.print("-");
						System.out.println(accountCheck.substring(6));
						System.out.printf("잔액: %,3d",BankMain.list.get(i).getBalance());
						System.out.println("\n계좌 유형: " + BankMain.list.get(i).getAccountType());
						break;
					} else {
						System.out.println("잘못 입력하셨습니다. 메인으로 돌아갑니다.");
						break;
					}
					
				} else if(i == BankMain.list.size()-1) {
					System.out.println();
					System.out.println("계좌가 존재하지 않습니다.");
					return;
				} 
			}
			
			System.out.println();
			
			return;
		
		case 3:
			/*
			 * 기능 설명:
			 * 사용자의 계좌 번호와 비밀 번호를 입력 받고
			 * 인증 번호까지 입력을 받으면 계좌를 해지해주는 곳
			 */	
			accountCheck = null; // 계좌변호확인
			String passwd = null; // 계좌비밀번호
			String CheckNum = null; // 인증번호 확인
			String verifyNum = null;
			boolean isAccountvalid = false;
			System.out.println();
			System.out.println("#######################################################");
			System.out.println("계좌 해지 메뉴 입니다. 잘못들어오셨다면 0번을 입력하면 메인메뉴로 돌아갑니다.");
			System.out.println("#######################################################");
			System.out.println();
			System.out.print("하이픈(-)을 제외한 계좌 번호 12자리를 입력해주세요 > ");
			accountCheck = sc.nextLine();
			System.out.println();
			
			if (!accNumPattern.matcher(accountCheck).matches()) {
				System.out.println("잘못 입력하셨습니다. 메인으로 돌아갑니다.");
				break;
			}
			
			if (accountCheck.length() != 12) {
				System.out.println("잘못 입력하셨습니다. 메인으로 돌아갑니다.");
				break;
			}
			
			System.out.print("비밀 번호를 입력해 주세요 > ");
			passwd = sc.nextLine();
			System.out.println();
			if (!passwdPattern.matcher(passwd).matches()) {
				System.out.println();
				System.out.println("잘못 입력하셨습니다. 메인으로 돌아갑니다.");
				break;
			}
			
			
			
			for (int i = 0; i < BankMain.list.size(); i++) {
				if (accountCheck.equals(BankMain.list.get(i).getAccountNumber())) {
					if (passwd.equals(BankMain.list.get(i).getAccountPasswd())) {
						System.out.println("인증 번호를 발급했습니다");
						CheckNum = accountsecurity.certificationbNumber();
						System.out.println(CheckNum);
						System.out.println();
						System.out.println("인증 번호를 입력해 주세요.");
						verifyNum = sc.nextLine();
						System.out.println();
						if (!verifyNum.equals(CheckNum)) {
							System.out.print("잘못 입력하셨습니다. 메인으로 돌아갑니다.");
							return;
						} else {
							System.out.println("인증 번호가 확인되었습니다.");
						}
						
						System.out.println();
						if (BankMain.list.get(i).getBalance() > 0) {
							System.out.println("계좌에 남은 금액을 정리해 주세요.");
							return;
						}
						int index = BankMain.multikeyMap.get(accountCheck, passwd);
						
						BankMain.list.remove(index);
						inputFileSystem();
						System.out.println("계좌 해지가 완료되었습니다.");
						
						isAccountvalid = true;
						break;
					}  
				}
				
			}
			if (!isAccountvalid) {
			    System.out.println("잘못 입력하셨습니다. 메인으로 돌아갑니다.");
			    return;
			}
		}
	}
		
	public void startTheSystem() {
		runSystem();
		return;
	}
}
