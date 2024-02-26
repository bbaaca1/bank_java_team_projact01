package bank.system;

import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AdminAccManagemeunt extends AccountFileSystem {

	final static Logger logger = LogManager.getLogger(AdminAccManagemeunt.class);
	/*
	 * 메소드 : modifyAccount()
	 * 작성자 : 백동민
	 * 파라미터 : int number Scanner sc
	 * 		   swich 케이스 문을 사용하기 위한 정수형 숫자 변수와 스캐너를 사용받기 위한 파라미터
	 * 반환값 : 없음(void) 
	 * 기능설명 :	매게 변수로 1이 입력되었다면 계좌번호를 입력화면이 출력된다 그 후 관리자 비밀번호를 입력화면이 출력된다
	 * 		  	존재하는 계좌번호와 관리자 비밀번호를 입력하면 계좌 수정 메뉴가 출력되어 이름,계좌번호,주민번호,주소,전화번호,잔액,비밀번호,계좌상태,계좌한도를 수정할 수 있다
	 * 			대부분 수정시 확인을 받지만 잔액은 다시한번 관리자 비밀번호로 확인을 받는다
	 * 			매게 변수로 2가 입력되었다면 사용자 계좌 추가 메뉴가 출력되어 사용자의 이름,주민번호,주소,전화번호,비밀번호를 입력받아 계좌를 생성한다
	 * 			메게 변수로 3이 입력되었다면 사용자 계좌 삭제 메뉴가 출력되어 관리자의 비밀번호를 다시한번 입력받은 후 삭제할 사용자의 계좌번호를 입력받는다 이후 계좌번호와 예금주명이 출력된다
	 * 			이후 다시한번 관리자 비밀번호를 입력하면 입력한 사용자의 계좌를 삭제한다	
	 */
	@Override
	void modifyAccount(int number, Scanner sc) {
		String input = null; // 입력을 받을 변수
		String modify = null; // 수정사항을 저장해둘 변수
		
		int index = 0; // 사용자 계좌의 인덱스번호를 저장할 변수
		
		switch (number) {
		
		// 계좌 수정
			case 1:
				logger.info("Admin Account Modify start");
				System.out.print("수정할 계좌의 계좌번호를 입력하세요 > ");
				input = sc.nextLine();
				
				
				if (!accNumPattern.matcher(input).matches()) {
					System.out.println("잘못 입력하셨습니다. 메뉴로 돌아갑니다.");
					return;
					}
				
				if (BankMain.map.get(input) != null) {// 계좌가 존재할시 실행됨
				try {
					index = BankMain.map.get(input);
					System.out.print("관리자 비밀번호를 입력하세요 > "); // 수정전 비밀번호 재확인
					input = sc.nextLine();
					logger.info("Admin Modify account number" + BankMain.list.get(index).getAccountNumber()+"and name is " + BankMain.list.get(index).getName());
					
					if (Integer.parseInt(input) == getAdminPassword()) {
						
						System.out.println("1.이름 수정 | 2.계좌번호 수정 | 3.주민번호 수정 | 4. 주소 수정 | 5.전화번호 수정 | 6. 잔액 수정 | 7.비밀번호 수정 | 8.계좌상태 수정 | 9.계좌 한도 수정");
						System.out.print("메뉴를 선택하세요 > ");
						input = sc.nextLine();
						
						switch (Integer.parseInt(input)) {
						
						// 계좌 이름 수정
						case 1:
							System.out.println("현재 이름은 : " + BankMain.list.get(index).getName() +" 입니다");
							System.out.print("수정할 이름을 입력하십시오 > ");
							modify = sc.nextLine();
							if (!namePattern.matcher(modify).matches()) {
								System.out.println("잘못 입력하셨습니다. 메뉴로 돌아갑니다.");
								return;
							}
							
							System.out.println("이름을 " + modify +"(으)로 수정합니다.");
							System.out.print("1번을 누르면 계속 진행합니다 나머지 입력시 취소됩니다 > ");
							input = sc.nextLine();
							
							if (Integer.parseInt(input) == 1) {
								BankMain.list.get(index).setName(modify);
								System.out.println("수정이 완료되었습니다.");
								inputFileSystem();
								break;
							} else {
								System.out.println("잘못 입력하셨습니다. 메뉴로 돌아갑니다.");
								return;
							}
							
						// 계좌번호 수정	
						case 2:
							System.out.println("현재 계좌번호는 : " + BankMain.list.get(index).getAccountNumber() +" 입니다");
							if (BankMain.list.get(index).getAccountType().equals("일반 예금")) {
								modify = "012";
							} else {
								modify = "021";
							}
							System.out.print("수정할 일련번호를 입력하십시오 > ");
							modify += "068";
							modify += sc.nextLine();
							if (!accNumPattern.matcher(modify).matches()) {
								System.out.println("잘못 입력하셨습니다. 메뉴로 돌아갑니다.");
								return;
							} else if(BankMain.map.containsKey(modify)) {
								System.out.println("이미 존재하는 계좌 입니다.");
								return;
							}
							
							System.out.println("계좌번호을 " + modify +"로 수정합니다.");
							System.out.print("1번을 누르면 계속 진행합니다 나머지 입력시 취소됩니다 > ");
							input = sc.nextLine();
							
							if (Integer.parseInt(input) == 1) {
								BankMain.map.clear();
								BankMain.multikeyMap.clear();
								BankMain.freezMap.clear();
								BankMain.list.get(index).setAccountNumber(modify);
								System.out.println("수정이 완료되었습니다.");
								inputFileSystem();
								return;
							} else {
								System.out.println("잘못 입력하셨습니다. 메뉴로 돌아갑니다.");
								return;
							}
						
						// 계좌 주민번호 수정	
						case 3:
							System.out.println("현재 주민번호는 : " + BankMain.list.get(index).getSsn() +" 입니다");
							System.out.print("수정할 주민번호을 입력하십시오 > ");
							modify = sc.nextLine();
							if (!ssnPattern.matcher(modify).matches()) {
								System.out.println("잘못 입력하셨습니다. 메뉴로 돌아갑니다.");
								return;
							}
							
							System.out.println("주민번호를 " + modify +"로 수정합니다.");
							System.out.print("1번을 누르면 계속 진행합니다 나머지 입력시 취소됩니다 > ");
							input = sc.nextLine();
							
							if (Integer.parseInt(input) == 1) {
								BankMain.list.get(index).setSsn(modify);
								System.out.println("수정이 완료되었습니다.");
								inputFileSystem();
								break;
							} else {
								System.out.println("잘못 입력하셨습니다. 메뉴로 돌아갑니다.");
								return;
							}
							
							// 계좌 주소 수정	
						case 4:
							System.out.println("현재 주소은 : " + BankMain.list.get(index).getHomeAddress() +" 입니다");
							System.out.print("수정할 주소을 입력하십시오 > ");
							modify = sc.nextLine();
							
							System.out.println("주소를 " + modify +"(으)로 수정합니다.");
							System.out.print("1번을 누르면 계속 진행합니다 나머지 입력시 취소됩니다 > ");
							input = sc.nextLine();
							
							if (Integer.parseInt(input) == 1) {
								BankMain.list.get(index).setHomeAddress(modify);
								System.out.println("수정이 완료되었습니다.");
								inputFileSystem();
								break;
							} else {
								System.out.println("잘못 입력하셨습니다. 메뉴로 돌아갑니다.");
								return;
							}
							
						// 계좌 전화번호 수정
						case 5:
							System.out.println("현재 전화번호는 : " + BankMain.list.get(index).getPhoneNumber() +" 입니다");
							System.out.print("수정할 전화번호을 입력하십시오 > ");
							modify = sc.nextLine();
							if (!phonePattern.matcher(modify).matches()) {
								System.out.println("잘못 입력하셨습니다. 메뉴로 돌아갑니다.");
								return;
							}
							
							System.out.println("전화번호을 " + modify +"로 수정합니다.");
							System.out.print("1번을 누르면 계속 진행합니다 나머지 입력시 취소됩니다 > ");
							input = sc.nextLine();
							
							if (Integer.parseInt(input) == 1) {
								BankMain.list.get(index).setPhoneNumber(modify);
								System.out.println("수정이 완료되었습니다.");
								inputFileSystem();
								break;
							} else {
								System.out.println("잘못 입력하셨습니다. 메뉴로 돌아갑니다.");
								return;
							}
							
							// 계좌 잔액 수정
						case 6:
							System.out.println("현재 잔액은 : " + BankMain.list.get(index).getBalance() +" 입니다");
							System.out.print("수정할 잔액을 입력하십시오 > ");
							modify = sc.nextLine();
							
	                        if (Long.parseLong(modify) < 0) {
	                            System.out.println("음수로는 입력할 수 없습니다.");
	                            return;
	                        }
	
							System.out.println("잔액을 " + modify +"(으)로 수정합니다.");
							System.out.print("비밀번호를 다시한번 입력하면 계속 진행합니다 나머지 입력시 취소됩니다 > "); // 잔액은 중요하므로 비밀번호를 다시한번 물어봄
							input = sc.nextLine();
							
							if (Integer.parseInt(input) == getAdminPassword()) {
								logger.info("!!!admin corrected a"+ BankMain.list.get(index).getAccountNumber()+ "balance from " + BankMain.list.get(index).getBalance()+ "to" + modify);
								BankMain.list.get(index).setBalance(Integer.parseInt(modify));
								System.out.println("수정이 완료되었습니다.");
								inputFileSystem();
								break;
							} else {
								System.out.println("잘못 입력하셨습니다. 메뉴로 돌아갑니다.");
								return;
							}
							
							// 계좌 비밀번호 수정
						case 7:
							System.out.println("현재 비밀번호은 : " + BankMain.list.get(index).getAccountPasswd() +" 입니다");
							System.out.print("수정할 비밀번호을 입력하십시오 > ");
							modify = sc.nextLine();
							if (!passwdPattern.matcher(modify).matches()) {
								System.out.println("잘못 입력하셨습니다. 메뉴로 돌아갑니다.");
								return;
							}
							
							System.out.println("비밀번호을 " + modify +"로 수정합니다.");
							System.out.print("1번을 누르면 계속 진행합니다 나머지 입력시 취소됩니다 > ");
							input = sc.nextLine();
							
							if (Integer.parseInt(input) == 1) {
								BankMain.list.get(index).setAccountPasswd(modify);
								System.out.println("수정이 완료되었습니다.");
								inputFileSystem();
								break;
							} else {
								System.out.println("잘못된 입력입니다. 메뉴로 돌아갑니다.");
								return;
							}
						
							// 계좌 상태 수정
						case 8:
							System.out.println("현재 계좌 상태는 : " + BankMain.list.get(index).getState() +" 입니다 ");
						    System.out.println("계좌상태 번호 : 0 : 정상 1 : 계좌 동결 2 : 휴면 계좌 3 : 출금 제한  4 : 이체 제한  5 : 이체,출금 제한 6 : 출금,계좌 동결 7 : 이체,계좌 동결 8 : 이체,출금,계좌 동결");
							
						    System.out.println("\n수정할 계좌 상태의 번호를 입력하세요");
							System.out.println("0.정상 | 2.계좌 휴면 | 3.출금 제한 | 4.이체 제한 | 5.이체,출금 제한");
							System.out.print("> ");
							modify = sc.nextLine();
							
							if (Integer.parseInt(modify) < 0 || Integer.parseInt(modify) > 5) {
								System.out.println("잘못 입력하셨습니다. 메뉴로 돌아갑니다.");
								return;
							}
							
							System.out.println("계좌 상태를 " + modify +"로 수정합니다.");
							System.out.println("상태를 정상으로 수정 할경우 한도가 초기화 됩니다.");
							System.out.print("1번을 누르면 계속 진행합니다 나머지 입력시 취소됩니다 > ");
							input = sc.nextLine();
							
							if (Integer.parseInt(input) == 1) {
								BankMain.list.get(index).setNonTradingAcc(false);
								BankMain.list.get(index).setTransferLimit(false);
								BankMain.list.get(index).setWithLimit(false);
								BankMain.list.get(index).setState(Integer.parseInt(modify));
								if (BankMain.list.get(index).getState() == 0) {
									BankMain.list.get(index).setWithdrawalLimits(0);
									BankMain.list.get(index).setTransferLimits(0);
								}
								BankMain.list.get(index).accountStatus(BankMain.list.get(index).getState());
								System.out.println("수정이 완료되었습니다.");
								inputFileSystem();
								break;
							} else {
								System.out.println("잘못 입력하셨습니다. 메뉴로 돌아갑니다..");
								return;
							}
							
							
						// 계좌 이체 및 출금 금액 수정
						case 9:
							
							System.out.println("현재 총출금금액은은 : " + BankMain.list.get(index).getWithdrawalLimits() +" 입니다");
							System.out.println("현재 총이체금액은 : " + BankMain.list.get(index).getTransferLimits() +" 입니다");
							
							System.out.println("1. 총출금액, 2.총이체금액");
						    System.out.print("\n수정할 계좌 총금액을 선택하세요 > ");
							input = sc.nextLine();
							
							if (Integer.parseInt(input) == 1) {
								System.out.print("수정할 총출금금액을 입력하십시오 > ");
								modify = sc.nextLine();
								
	                            if (Long.parseLong(modify) < 0) {
	                                System.out.println("음수로는 입력할수 없습니다.");
	                                return;
	                            }
	
	                            
								System.out.println("총출금금액을 " + modify +"로 수정합니다.");
								System.out.print("1번을 누르면 계속 진행합니다 나머지 입력시 취소됩니다 > ");
								
								input = sc.nextLine();
								if (Integer.parseInt(input) == 1) {
									BankMain.list.get(index).setWithdrawalLimits(0);
									long limit = Long.parseLong(modify);
									BankMain.list.get(index).setWithdrawalLimits(limit);
								} else {
									System.out.println("잘못 입력하셨습니다. 메뉴로 돌아갑니다.");
									return;
								}
								
							} else if (Integer.parseInt(input) == 2) {
								System.out.print("수정할 총이채금액을 입력하십시오 > ");
								modify = sc.nextLine();
								
	                            if (Long.parseLong(modify) < 0) {
	                                System.out.println("음수로는 입력할수 없습니다.");
	                                return;
	                            }
	                            
								System.out.println("총이채금액 " + modify +"로 수정합니다.");
								System.out.print("1번을 누르면 계속 진행합니다 나머지 입력시 취소됩니다 > ");
								
									
								input = sc.nextLine();
								if (Integer.parseInt(input) == 1) {
									BankMain.list.get(index).setTransferLimits(0);
									long limit = Long.parseLong(modify);
									BankMain.list.get(index).setTransferLimits(limit);
								} else {
									System.out.println("잘못 입력하셨습니다. 메뉴로 돌아갑니다.");
									return;
								}
								
							} else {
								System.out.println("잘못 입력하셨습니다. 메뉴로 돌아갑니다.");
								return;
							}
							
							BankMain.list.get(index).setState(BankMain.list.get(index).getState());
							System.out.println("수정이 완료되었습니다.");
							inputFileSystem();
							break;
							
						default:
							System.out.println("잘못 입력하셨습니다. 메뉴로 돌아갑니다.");
							return;
					}
				} else {
					System.out.println("잘못 입력하셨습니다. 메뉴로 돌아갑니다.");
				}
			} catch (NumberFormatException e) {
				// 
				System.out.println("잘못 입력하셨습니다. 메뉴로 돌아갑니다.");
			  }	catch (Exception e) {
				// 에러 발생시 실행
				  logger.error("admin accountModfiy");
				  logger.error(e.toString());
				  System.out.println("오류가 발생하였습니다.");
			}
					
				} else {
				System.out.println("존재하지않는 계좌입니다.");
			}
			

			
			break;
		
		// 계좌 생성
		case 2:
			logger.info("Admin Account Create start");
			System.out.print("관리자 비밀번호를 다시 입력하세요 > ");
			input = sc.nextLine();
			if (Integer.parseInt(input) == getAdminPassword()) {
				
				AccountManage am = new AccountManage();
				AccountTime at = new AccountTime();
				BankMain.list.add(new AccountCreate(null, null, null, null, null, 1, null, null, null, 0));
				int accountIndex = BankMain.list.size()-1;
				@SuppressWarnings("unused")
				int[] time;
				try {
					// 이름 입력 시작
					System.out.print("\n이름을 입력해주세요 > ");
					input = sc.nextLine();
					
					if(!namePattern.matcher(input).matches()) {
						BankMain.list.remove(accountIndex);
						System.out.println("잘못 입력하셨습니다. 메뉴로 돌아갑니다.");
						return;
					}
					
					BankMain.list.get(accountIndex).setName(input);
					
					// 주민번호 입력 시작
					System.out.print("\n주민번호를 입력해주세요 > ");
					
					input = sc.nextLine();

					if(!ssnPattern.matcher(input).matches()) {
						BankMain.list.remove(accountIndex);
						System.out.println("잘못 입력하셨습니다. 메뉴로 돌아갑니다.");
						return;
					}
					
					BankMain.list.get(accountIndex).setSsn(input);
					
					// 주소 입력 시작
					System.out.print("\n주소를 입력해주세요 > ");
					input = sc.nextLine();
					
					BankMain.list.get(accountIndex).setHomeAddress(input);
					
					// 전화번호 입력 시작
					System.out.print("\n전화번호를 입력해주세요 > ");
					input = sc.nextLine();
					
					if (!phonePattern.matcher(input).matches()) {
						BankMain.list.remove(accountIndex);
						System.out.println("잘못 입력하셨습니다. 메뉴로 돌아갑니다.");
						return;
					}
					
					BankMain.list.get(accountIndex).setPhoneNumber(input);
					
					// 계좌유형 입력 시작
					System.out.print("\n계좌 유형를 입력해주세요(1.일반 예금, 2.정기 적금) > ");
					input = sc.nextLine();
					switch (Integer.parseInt(input)) {
					case 1:
						BankMain.list.get(accountIndex).setAccountType("일반 예금");
						break;
						
					case 2:
						BankMain.list.get(accountIndex).setAccountType("정기 적금");
						break;

					default:
						System.out.println("잘못 입력하셨습니다. 메뉴로 돌아갑니다.");
						BankMain.list.remove(accountIndex);
						return;
					}
					
					// 비밀번호 설정
					System.out.print("\n\n사용할 계좌 비밀번호 4자리를 입력해주세요 > ");
					String passwd = sc.nextLine();
					
					if (!passwdPattern.matcher(passwd).matches()) {
						BankMain.list.remove(accountIndex);
						System.out.println("잘못 입력하셨습니다. 메뉴로 돌아갑니다.");
						return;
					}
					
					BankMain.list.get(accountIndex).setAccountPasswd(passwd);
					
					// 계좌 생성
					time = at.currentTime(accountIndex, true);
					String accountNumber = am.createAccountNumber(BankMain.list.get(accountIndex).getAccountType());
					BankMain.list.get(accountIndex).setAccountNumber(accountNumber);
					System.out.println("계좌 번호는 "+ BankMain.list.get(accountIndex).getAccountNumber()+" 입니다.");
										
					//종료
					logger.info("Admin account create success");
					System.out.println("계좌 생성에 성공했습니다. 메뉴로 돌아갑니다.");
					inputFileSystem();
					return;
					
				} catch (NumberFormatException e) {
					// 정수외 입력 일때 실행
					logger.info("admin account create failed");
					System.out.println("잘못 입력하셨습니다. 메뉴로 돌아갑니다.");
					BankMain.list.remove(accountIndex);
					return;
					
				} catch (Exception e) {
					// 에러 발생시 실행
					System.out.println("오류가 발생하였습니다. 메뉴로 돌아갑니다.");
					logger.error("account create error");
					logger.error(e.toString());
					BankMain.list.remove(accountIndex);
					return;
				}
				
				
			}
			
			
			break;
		
		// 계좌 삭제	
		case 3:
			logger.info("Admin Account Closure start");
			
			System.out.print("삭제할 계좌의 계좌번호를 입력하세요 > ");
			input = sc.nextLine();
			if (!accNumPattern.matcher(input).matches()) {
				System.out.println("잘못 입력하셨습니다. 메뉴로 돌아갑니다.");
				return;
			}
			
			if (BankMain.map.get(input) != null) {
				
				index = BankMain.map.get(input);
				System.out.print("관리자 비밀번호를 입력하세요 > ");
				input = sc.nextLine();
				
				if (Integer.parseInt(input) == getAdminPassword()) {
					System.out.println("예금주명 : "+BankMain.list.get(index).getName());
					System.out.println("계좌번호 : "+BankMain.list.get(index).getAccountNumber());
					
					System.out.print("삭제할 계좌가 맞다면 1을 입력하세요 그 외의 입력은 취소됩니다 > ");
					input = sc.nextLine();
					if (input.equals("1")) {
						System.out.print("관리자 비밀번호를 다시한번 입력하세요 > ");
						input = sc.nextLine();
						
						if (Integer.parseInt(input) == getAdminPassword()) {
							logger.info("Admin " + BankMain.list.get(index).getAccountNumber()+" Acoount remove");
							BankMain.list.remove(index);
							System.out.println("삭제가 완료 되었습니다.");
							inputFileSystem();
						} else {
							System.out.println("잘못 입력하셨습니다. 메뉴로 돌아갑니다.");
							return;
						}
					} else {
					System.out.println("잘못 입력하셨습니다. 메뉴로 돌아갑니다.");
					return;
					}
				} else {
					System.out.println("비밀번호가 틀렸습니다.");
					return;
				}
			} else {
				System.out.println("존재하지않는 계좌입니다.");
				return;
			}
			break;
		default :
			System.out.println("잘못 입력하셨습니다. 메뉴로 돌아갑니다.");
			break;

		}
		
		
		// 수정 사항 엑셀에 입력
		
		return;
	}
	
	
}
