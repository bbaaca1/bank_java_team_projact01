package bank.system;


import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AccountWithdrawal extends AccountFileSystem {
	
	final static Logger logger = LogManager.getLogger(AccountWithdrawal.class);
	
	/*
	 * 메소드명: withdrawalMeun
	 * 파라미터: Scanner sc
	 * 스캐너를 통한 입력을 받기 위해 받은 파라미터
	 * 반환값: void
	 * 기능 설명:
	 * 출금 메뉴화면을 보여주고 사용자로부터 계좌번호, 출금 금액, 출금 계좌 비밀번호 입력을 받는다.
	 * 입력받은 정보를 확인하고, 출금 가능한지 여부를 판단하여 출금을 수행한다.
	 * 출금 후에는 거래 완료 메시지와 명세표 출력 여부를 선택할 수 있다.
	 */
	
	public void withdrawalMeun(Scanner sc) {
		
		long inputNumber = 0;
		String inputAccountNumber = null; // 계좌 번호 담아줄 변수
		long withdrawalAmount = 0; // 출금 금액을 담아줄 변수(= 사용자가 입력한 금액을 알려줄 변수)
		String withdrawalAccountPW = null; // 출금 계좌 비밀번호 
		AccountTime at = new AccountTime(); // 시간 반환해주는 클래스
		AccountSecurity as = new AccountSecurity();
		int[] time; // 년/월/일을 담아줄 배열
		long balance = 0; // 잔액을 담아줄 변수
		long accountNumberCheck = 0; // 계좌 번호 체킹용 변수
		String lastTradeYear = null; // 마지막 거래 연도
		String lastTradeMonth = null; // 마지막 거래 달
		String lastTradeDay = null; // 마지막 거래 일
		long withdrawalLimit = 0;
		
		logger.info("Withdrawl Start");
		
			System.out.println("##########################################################");
			System.out.println(" 예금 출금 메뉴 입니다 잘못들어오셨다면 0번을 입력하면 메인메뉴로 돌아갑니다. ");
			System.out.println("##########################################################");
			System.out.print("\n하이픈(-)을 제외한 계좌 번호를 12 자리를 입력해주세요 > ");
			
			inputAccountNumber = sc.nextLine(); // 계좌 번호 받고 넣기
			
			try {
				accountNumberCheck = Long.parseLong(inputAccountNumber); // 계좌 번호를 넣고 정수로 변환
				
			} catch (NumberFormatException e) {
				// 숫자(정수)외 입력 들어왔을 때, 오류 메시지 출력 후 해당 오류를 로그로 남김
				System.out.println(" ");
				System.out.println("잘못 입력하셨습니다. 메인으로 돌아갑니다.");
				logger.error(e.toString()); // logger.error(e.toString())는 로깅 시스템의 error 레벨에 해당하는 메시지를 기록하는 것을 의미, e.toString()은 예외 객체 e의 문자열 표현을 반환
				return;
				
			}
				
			if (inputAccountNumber.length() != 12) {
				System.out.println(" ");
				System.out.println("잘못 입력하셨습니다. 메인으로 돌아갑니다.");
				return;
				
			} else if (!accNumPattern.matcher(inputAccountNumber).matches()) {
				System.out.println(" ");
				System.out.println("잘못 입력하셨습니다. 메인으로 돌아갑니다.");
				return;
				
			} else if (accountNumberCheck < 0 ) {
				System.out.println(" ");
				System.out.println("잘못 입력하셨습니다. 메인으로 돌아갑니다.");
				return;
			} else if (accountNumberCheck == 0) {
				System.out.println(" ");
				System.out.println("잘못 입력하셨습니다. 메인으로 돌아갑니다.");
				return;
				
			} else {
				
				if (inputAccountNumber.contains("021")) {
					System.out.println("적금 통장은 출금 하실 수 없습니다. 메뉴로 돌아갑니다.");
					return;
				}
				
				try {
					
					
					int index = BankMain.map.get(inputAccountNumber);
					
					// 마지막 거래 년, 월, 일 불러오고, 업데이트는 하지 않음
					lastTradeYear = String.valueOf(BankMain.list.get(index).getDate()).substring(0,4);
					lastTradeMonth = String.valueOf(BankMain.list.get(index).getDate()).substring(4,6);
					lastTradeDay = String.valueOf(BankMain.list.get(index).getDate()).substring(6,8);
					time = at.currentTime(index,false);
					
					
					if ((Integer.parseInt(lastTradeYear))-time[0] != 0 || (Integer.parseInt(lastTradeMonth))-time[1] != 0 || (Integer.parseInt(lastTradeDay))-time[2] != 0) {
						// 년, 월, 일 중 어느 하나라도 음수가 되면 하루가 지난 것이므로 출금 제한 초기화
						BankMain.list.get(index).setWithdrawalLimits(0);
						BankMain.list.get(index).setWithLimit(false);
						if(BankMain.list.get(index).getState() == 3) {
							BankMain.list.get(index).setState(0);
						}
						else if(BankMain.list.get(index).getState() == 5) {
							BankMain.list.get(index).setState(4);
						}
						else if(BankMain.list.get(index).getState() == 6) {
							BankMain.list.get(index).setState(1);
						}
						else if(BankMain.list.get(index).getState() == 8) {
							BankMain.list.get(index).setState(7);
						}
						BankMain.list.get(index).accountStatus(BankMain.list.get(index).getState());
						
						if(as.cheakState(index, 0)) {
							System.out.println("메인메뉴로 돌아갑니다.");
							return;
						}
					
					}
					
				}catch (Exception e) { 
					// 입력 부분이므로 우리가 모르는 어떠한 에러 발생 시, 오류 메시지 출력 후 해당 오류를 로그로 남김
					System.out.println(" ");
					System.out.println("잘못 입력하셨습니다. 메인으로 돌아갑니다.");
					logger.error(e.toString()); // logger.error(e.toString())는 로깅 시스템의 error 레벨에 해당하는 메시지를 기록하는 것을 의미, e.toString()은 예외 객체 e의 문자열 표현을 반환
					return;
					
				} 
				
				
				
				
				logger.info("Account " + inputAccountNumber+ " is select");
				System.out.print("출금 금액을 입력해 주세요 > ");
				try {
					
					withdrawalAmount = (long)Integer.parseInt(sc.nextLine());
					
				} catch (NumberFormatException e) {
					// 숫자(정수)외 입력 들어왔을 때, 오류 메시지 출력후 해당 오류를 로그로 남기는 역할
					System.out.println(" ");
					System.out.println("잘못 입력하셨습니다. 메인으로 돌아갑니다.");
					logger.error(e.toString()); // logger.error(e.toString())는 로깅 시스템의 error 레벨에 해당하는 메시지를 기록하는 것을 의미, e.toString()은 예외 객체 e의 문자열 표현을 반환
					return;
					
				} catch (Exception e) {
					// 입력 부분이므로 우리가 모르는 어떠한 에러 발생시
					System.out.println(" ");
					System.out.println("잘못 입력하셨습니다. 메인으로 돌아갑니다.");
					logger.error(e.toString()); // logger.error(e.toString())는 로깅 시스템의 error 레벨에 해당하는 메시지를 기록하는 것을 의미, e.toString()은 예외 객체 e의 문자열 표현을 반환
					return;
					
				}
				
				if (withdrawalAmount < 10000) {
					// 출금 금액 1만 원 미만 입력 시
					System.out.println(" ");
					System.out.println("1회 최소 출금 금액은 1만 원입니다.");
					System.out.println("메인으로 돌아갑니다.");
					return;
					
				} else if (withdrawalAmount > 1000000) {
					// 출금 금액 1백만 원 초과 입력 시
					System.out.println(" ");
					System.out.println("1회 최대 출금 금액은 1백만 원입니다.");
					System.out.println("메인으로 돌아갑니다.");
					return;
					
				} else if (withdrawalAmount % 10000 != 0) {
					// 출금 1만 원 권 단위로 입력하지 않았을 시
					System.out.println(" ");
					System.out.println("1만 원 단위만 입력 가능합니다.");
					System.out.println("메인으로 돌아갑니다.");
					return;
					
				} else if (withdrawalAmount >= 10000 && withdrawalAmount <= 1000000 && withdrawalAmount % 10000 == 0) {
					// 출금 금액 정상 입력 시
					System.out.println(" ");
					System.out.println("입력하신 계좌 번호: " + inputAccountNumber);
					System.out.println("입력하신 금액: " + withdrawalAmount);
					System.out.println(" ");
					System.out.print("비밀번호 4자리를 입력해 주세요 > ");
					withdrawalAccountPW = sc.nextLine();
											
					}
					
					if (isValidPassword(sc, withdrawalAccountPW)) {
						// 0000부터 9999 사이에 속하는지 확인하기 위해 isValidPassword() 메서드 사용
						System.out.println(" ");
						System.out.println("입력하신 정보가 맞다면 1을 입력해 주세요");
						System.out.print("그 외의 값은 메인으로 돌아갑니다 > ");
						inputNumber = Integer.parseInt(sc.nextLine());
						
						try {

							
							withdrawalLimit = (BankMain.list.get(BankMain.multikeyMap.get(inputAccountNumber, withdrawalAccountPW)).getWithdrawalLimits());							
							
							if (withdrawalLimit + withdrawalAmount > 6000000) {
								// 출금 한도 초과 시, 남은 한도 출력
								System.out.println(" ");
								System.out.println("출금 한도 초과합니다 출금할 계좌의 남은 한도: " + (6000000-withdrawalLimit));
								System.out.println(" ");
								System.out.println("메인으로 돌아갑니다.");
								return;
							}
							
							balance = BankMain.list.get(BankMain.multikeyMap.get(inputAccountNumber, withdrawalAccountPW)).getBalance();
					
							if (balance >= withdrawalAmount) {
								// 잔액이 출금 금액보다 크거나 같을 때, 원래 잔액에서 출금 금액을 뺀 후 리스트에 저장하고, 명세표를 출력할 사람을 위해 바뀐 정보를 다시 불러옴
								BankMain.list.get(BankMain.multikeyMap.get(inputAccountNumber, withdrawalAccountPW)).setBalance(balance-withdrawalAmount);
								balance = BankMain.list.get(BankMain.multikeyMap.get(inputAccountNumber, withdrawalAccountPW)).getBalance();
								
							} else { 
								// 계좌에 잔액이 부족할 때
								System.out.println(" ");
								System.out.println("계좌에 잔액이 부족합니다");
								System.out.println("메인으로 돌아갑니다.");
								return;
								
							}
				
							
						} catch (Exception e) {
							// 우리가 모르는 어떠한 에러 발생 시, 오류 메시지 출력 후 해당 오류를 로그로 남김
							System.out.println(" ");
							System.out.println("잘못 입력하셨습니다. 메인으로 돌아갑니다.");
							logger.error(e.toString()); // logger.error(e.toString())는 로깅 시스템의 error 레벨에 해당하는 메시지를 기록하는 것을 의미, e.toString()은 예외 객체 e의 문자열 표현을 반환
							return;
							
						}
						
						if (inputNumber == 1) {
							// 사용자가 1을 눌렀을 때, 거래 완료 메시지 출력
							time = at.currentTime(BankMain.multikeyMap.get(inputAccountNumber, withdrawalAccountPW),true); // currentTime 메서드는 index값과 boolean타입을 필요로 함. 이때, true면 기록에 거래날짜 저장
							BankMain.list.get(BankMain.multikeyMap.get(inputAccountNumber, withdrawalAccountPW)).setBalance(balance);
							BankMain.list.get(BankMain.multikeyMap.get(inputAccountNumber, withdrawalAccountPW)).setWithdrawalLimits(withdrawalAmount);
							withdrawalLimit = BankMain.list.get(BankMain.multikeyMap.get(inputAccountNumber, withdrawalAccountPW)).getWithdrawalLimits();
							
							if (BankMain.list.get(BankMain.multikeyMap.get(inputAccountNumber, withdrawalAccountPW)).isWithLimit()) {
							logger.info("Account" + inputAccountNumber +"is WithLimit ");
							}
							
							System.out.println(" ");
							System.out.println("거래가 완료되었습니다");
							System.out.println("명세표를 출력하시겠다면 1을 입력해 주세요");
							System.out.print("그 외의 값은 메인으로 돌아갑니다 > ");
							inputFileSystem();
							try {
								
								inputNumber = Integer.parseInt(sc.nextLine());
								
							} catch (NumberFormatException e) {
								// 숫자(정수)외 입력 들어왔을 때, 오류 메시지 출력후 해당 오류를 로그로 남기는 역할
								System.out.println(" ");
								System.out.println("잘못 입력하셨습니다. 메인으로 돌아갑니다.");
								return;
								
							} catch (Exception e) {
								// 입력 부분이므로 우리가 모르는 어떠한 에러 발생시
								System.out.println(" ");
								System.out.println("에러가 발생하였습니다. 메인으로 돌아갑니다.");
								logger.error(e.toString()); // logger.error(e.toString())는 로깅 시스템의 error 레벨에 해당하는 메시지를 기록하는 것을 의미, e.toString()은 예외 객체 e의 문자열 표현을 반환
								return;
								
							}
							
							logger.info("Account" + inputAccountNumber +"has been successfully wthdrwal");
							if (inputNumber == 1) {
								// 사용자가 1을 눌렀을 때, 명세표 출력
								System.out.println(" ");
								System.out.println("거래 날짜: " + time[0] + "/" + time[1] + "/" + time[2]);
								System.out.println("계좌 번호: " + inputAccountNumber);
								System.out.printf("금액: %,3d" ,withdrawalAmount);
								System.out.printf("\n남은 출금 한도: %,3d / 6,000,000",(6000000-withdrawalLimit));
								System.out.println("\n통장 잔액: " + balance);
								System.out.println(" ");
								System.out.println("메인으로 돌아갑니다");
								
							} else {
								// 다른 것을 눌렀을 때, 메인메뉴로 이동
								System.out.println(" ");
								System.out.println("잘못 입력하셨습니다. 메인으로 돌아갑니다.");
								return;
							}
						}
					}
				}
			
			
			
			logger.info("Wthdrwal end");
			// 거래 끝! 메인으로 이동~
			return; 
						
	}
	
	// 0000부터 9999 사이에 속하는지 확인하기 위해 isValidPassword() 메서드 사용
	private static boolean isValidPassword(Scanner sc, String withdrawalAccountPW) {
		
	    /* isValidPassword() 메서드에서는 비밀번호의 길이가 4자리인지 확인한 후, Integer.parseInt()를 사용하여 숫자로 변환 가능한지 확인
	     * 변환 가능한 경우 해당 숫자가 0000부터 9999 사이인지 확인하고, 올바른 비밀번호로 판단
	     * 입력된 비밀번호가 유효한 범위에 속하지 않거나 변환할 수 없는 경우, 적절한 오류 메시지를 출력하고 false를 반환.
	     * isValidPassword() 메서드에서 true가 반환될 때까지 사용자에게 다시 비밀번호를 입력받음.
	     */

	    if (withdrawalAccountPW.length() != 4) { // 비밀번호 자리 수가 4자리인 지 판단 4자리가 아니라면 문구 출력 후 메인으로 이동
			System.out.println(" ");
			System.out.println("잘못 입력하셨습니다. 메인으로 돌아갑니다.");
	        return false;
	        
	    }

	    try {
	        int num = Integer.parseInt(withdrawalAccountPW);
	        // 
	        if (num >= 0 && num <= 9999) { // 비밀번호에 음수가 들어올 수 없으므로 범위 지정
	            return true;
	            
	        } else { // 범위를 벗어난 수가 들어왔을 경우 문구 출력 후 메인으로 이동
				System.out.println(" ");
				System.out.println("잘못 입력하셨습니다. 메인으로 돌아갑니다.");
	            return false;
	            
	        }
	    } catch (NumberFormatException e) { // 비밀번호에 문자가 들어왔을 때
	        // 숫자로 변환할 수 없는 경우
			System.out.println(" ");
			System.out.println("잘못 입력하셨습니다. 메인으로 돌아갑니다.");
	        return false;   
	    }
	    catch (Exception e) { // 비밀번호에 문자가 들어왔을 때
	        // 숫자로 변환할 수 없는 경우
			System.out.println(" ");
			System.out.println("에러가 발생하였습니다. 메인으로 돌아갑니다.");
			logger.error(e.toString()); // logger.error(e.toString())는 로깅 시스템의 error 레벨에 해당하는 메시지를 기록하는 것을 의미, e.toString()은 예외 객체 e의 문자열 표현을 반환
	        return false;
	    }
	}
	@Override
	void modifyAccount(int number, Scanner sc) {
		// TODO Auto-generated method stub
		
	}
	
}