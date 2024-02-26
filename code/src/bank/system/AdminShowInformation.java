package bank.system;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AdminShowInformation {
	
	final static Logger logger = LogManager.getLogger(AdminShowInformation.class);
	
	/*
	 * 메소드명: informationMeun
	 * 작성자 : 백동민
	 * 파라미터: Scanner sc
	 * 스캐너를 통한 입력을 받기 위해 받은 파라미터
	 * 반환값: 없음(void)
	 * 기능설명:	관리자의 서버 정보확인을 메뉴를 출력하는 메소드
	 * 			1번이 입력되면 userInformation메소드를 호출하고
	 * 			2번이 입력되면 findLog메소드를 호출한다
	 */
	
	protected void informationMeun(Scanner sc) {
		
		int input = 0;
		logger.info("admin moved from the Admin menu to the information menu");
		try {
			System.out.println("1.사용자 확인 | 2.로그 확인 | 0.관리자메뉴");
			System.out.print("메뉴를 선택해 주세요 > ");
			input = Integer.parseInt(sc.nextLine());
				
				switch (input) {
				case 1:

					userInformation(sc);
					return;

				case 2:
					findLog(sc);
					return;
					
				case 0:
					return;
					
				default:
					System.out.println("잘못 입력하셨습니다. 메뉴로 돌아갑니다.");
					return;
				}
		} catch (NumberFormatException e) {
			System.out.println("잘못 입력하셨습니다. 메뉴로 돌아갑니다.");
			
		} catch (Exception e) {
			// 메뉴에서 에러 발생시 실행
			logger.error("admin informationMeun error");
			System.out.println("오류발생 메뉴로 돌아갑니다.");
			return;
		}
			
	}

	/*
	 * 메소드명: userInformation
	 * 작성자 : 백동민
	 * 파라미터: Scanner sc
	 * 스캐너를 통한 입력을 받기 위해 받은 파라미터
	 * 반환값: 없음(void)
	 * 기능설명:	관리자가 저장된 사용자의 데이터를 출력하는 메소드
	 * 			전체확인(1)을 입력한 경우 BankMain에 있는 list에 저장된 객체에 toString메소드를 호출한다.
	 * 			상세확인(2)을 입력한 경우 계좌번호를 입력하여 해당 계좌번호에 해당하는 BankMain에 있는 list에 저장된 객체에 printInfo() 메소드를 호출한다.
	 */
	
	private void userInformation(Scanner sc) {
		
		logger.info("admin check users");
		
		int number = 0;
		int input = 0;
		String inputAccountnumber = null;
		
		System.out.println("1.전체확인 2.상세확인");
		System.out.print("메뉴를 선택해 주세요 > ");
		try {		
			input = Integer.parseInt(sc.nextLine());
			switch (input) {
			case 1:
				logger.info("admin check all users");
				for (AccountCreate account : BankMain.list) {
						
					System.out.println(account.toString());
					
					if (number == 10) { //계좌를 10개씩 출력
						System.out.print("\n 계속 출력 할려면 1을 누르세요 > ");
						input = Integer.parseInt(sc.nextLine());
						if (input == 1) {
							number = 0;
						} else {
							return;
						}
					}
					number++;
				}
				System.out.print("\n 나갈려면 아무키나 누르세요");
				sc.nextLine();
				return;
				
			case 2:
				System.out.print("확인할 사용자의 계좌번호를 입력하세요 > ");
				inputAccountnumber = sc.nextLine();
				if (BankMain.map.containsKey(inputAccountnumber)) {
					BankMain.list.get(BankMain.map.get(inputAccountnumber)).printInfo();
					return;
				} else {
					System.out.println("존재하지 않는 계좌입니다.");
				}
				
			default :
				System.out.println("잘못 입력하셨습니다. 메뉴로 돌아갑니다.");
				return;
			}
		} catch (NumberFormatException e) {
			System.out.println("잘못 입력하셨습니다. 메뉴로 돌아갑니다.");
			
		} catch (Exception e) {
			// 사용자 찾기에서 에러 발생시
			logger.error("admin check error");
			System.out.println("오류발생 메뉴로 돌아갑니다.");
			return;
		}
	}
	
	/*
	 * 메소드명: findLog
	 * 작성자 : 백동민
	 * 파라미터: Scanner sc
	 * 스캐너를 통한 입력을 받기 위해 받은 파라미터
	 * 반환값: 없음(void)
	 * 기능설명:	자동 생성되는 로그폴더(상대경로 \logs)에 저장된 로그 파일의 파일명들에 번호를 붙여 출력하는 메소드
	 * 			오래된 순서부터 로그 파일의 파일명을 출력한다 이때 가장 오래된 파일이 1번이며 최신에 가까워질수록 1식증가한다
	 * 			만약 로그 파일의 숫자가 10개가 넘어가면 로그파일 파일명을 10개씩 보여준다
	 * 			만약 사용자가 특정 로그파일의 번호로 입력했을때 showLog메소드를 호출한다.
	 */
	
	private void findLog(Scanner sc) {
		// 로그 파일들을 보여주는 메소드
		
		Map<Integer,String> log = new HashMap<>(); // 로그파일들을 담아둘 맵
		File directory = new File("logs"); // 로그폴더를 상대경로로 지정함
		File[] files = directory.listFiles(); // 로그파일이름을 출력하기 위한 배열
		
		String input = null;
		logger.info("admin show logs");
		
		try {
			
			int filesequence = 1; // 맵에 로그 파일 번호를 매기기고 출력하기 위한 변수
			
			if (files != null) {
				System.out.println("=========================================");
				for (File file : files) {
					String fileName = file.getName();
	
					log.put(filesequence,"logs\\"+fileName);
					System.out.println(filesequence+"."+fileName);
						
					if (filesequence % 10 == 0) { // 로그 파일 이름을 10개까지만 출력
						System.out.println("==================================================");
						System.out.print("로그를 확인하실려면 1를 입력하세요 나머지 입력은 모두 계속 출력합니다 > ");
						input = sc.nextLine();
						if (input.equals("1")) {
							break;
						} else {
							System.out.println("\n\n\n");
						}
					}
					filesequence++;
				}
			}
			System.out.println("==================================================");
			System.out.print("확인하실 로그파일의 번호(숫자)를 입력하세요 > ");
			input = sc.nextLine();
	
			if (log.get(Integer.parseInt(input)) != null) { // 맵에 번호로 할당된 로그 파일이 존재하는 지 확인
			showLog(log.get(Integer.parseInt(input)),sc);
			} else {
				System.out.println("잘못 입력하셨습니다. 메뉴로 돌아갑니다");
			}
		} catch (NumberFormatException e) {
			System.out.println("잘못 입력하셨습니다. 메뉴로 돌아갑니다.");
			
		} catch (Exception e) {
			// 로그 찾기에서 에러 발생시 실행
			logger.error("Find Log error");
			System.out.println("오류발생 메뉴로 돌아갑니다.");
		}

		return;
		
	}
	
	/*
	 * 메소드명: findLog
	 * 작성자 : 백동민
	 * 파라미터: String fileName, Scanner sc
	 * 저장된 로그파일을 불러오기 위한 상대경로와 파일명이 할당된 파라미터
	 * 스캐너를 통한 입력을 받기 위해 받은 파라미터
	 * 반환값: 없음(void)
	 * 기능설명:	파라미터로 받은 로그 파일의 상대경로로 로그파일을 찾아 해당 로그파일의 내용을 출력하는 메소드
	 * 			로그파일의 내용을 처음부터 끝까지 출력한다
	 */
	private void showLog(String fileName, Scanner sc) {
		
		// 로그를 보여주는 메소드입니다.
		// fileName는 로그파일의 상대경로(logs\\application-(날짜))입니다
		
		File logFile = new File(fileName);
		logger.info("admin show " + fileName);
		String line = null;
		logger.info("admin check " + fileName);
        try {
        	FileReader fileReader = new FileReader(logFile);
    		BufferedReader bufferedReader = new BufferedReader(fileReader);
               
            while ((line = bufferedReader.readLine()) != null) {
            	System.out.println(line);
            }
            bufferedReader.close();
        } catch (Exception e) {
        	logger.error("show Log error");
        	logger.error(e.toString());
        	System.out.println("오류발생 메뉴로 돌아갑니다.");
        	return;
          }
        
        System.out.println("==================================================");
        System.out.println("빠져나올려면 아무키나 눌러주세요.");
        sc.nextLine();
        return;
       }
	}

