package bank.system;

import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AdminMenu {
	
	final static Logger logger = LogManager.getLogger(AdminMenu.class);
	
	
	/*
	 * 메소드명: AdminLogin
	 * 작성자 : 백동민
	 * 파라미터: Scanner sc
	 * 스캐너를 통한 입력을 받기 위해 받은 파라미터
	 * 반환값: 없음(void)
	 * 기능설명: 메인메뉴에서 관리자 메뉴 호출하면 관리자 비밀번호를 입력받는 메소드 만약 비밀번호가 일치하면 Adminmain메소드가 호출되 일치하지않다면 BankMain으로 돌아간다
	 */
	
	
	public void AdminLogin(Scanner sc) {
		// 관리자 인증 메소드입니다
		
		logger.info("start admin authentication");
		System.out.print("관리자 비밀번호를 입력하세요 > "); // 현재 관리자 비밀번호는 1234
		AccountFileSystem af = new AccountFileSystem() {};
		try {
			
			int input = sc.nextInt();
			
			if (input == af.getAdminPassword()) {
				logger.info("admin authentication");
				System.out.println("인증되었습니다.");
				Adminmain(sc);
				return;
			} else {
				logger.info("admin filled authentication");
				System.out.println("잘못 입력하셨습니다. 메인으로 돌아갑니다.");
				return;
			}
		} catch (NumberFormatException e) {
			System.out.println("잘못 입력하셨습니다. 메인으로 돌아갑니다.");
			
		} catch (Exception e) {
			logger.error(e.toString());
			System.out.println("에러가 발생하였습니다.");
		}
		
	}
	
	/*
	 * 메소드명: Adminmain
	 * 작성자 : 백동민
	 * 파라미터: Scanner sc
	 * 스캐너를 통한 입력을 받기 위해 받은 파라미터
	 * 반환값: 없음(void)
	 * 기능설명:	괸라자 인증이 완료되면 관리자 메뉴를 출력하는 메소드 관리자의 입력에 따라 
	 * 		 	AdminShowInformation에 informationMeun메소드를 호출하거나
	 * 			AdminAccMangemunt에 modifyAccount를 호출한다
	 */
	
	private void Adminmain(Scanner sc) {
		// 관리자 메뉴 메소드입니다
		sc.nextLine();
		AdminShowInformation aif = new AdminShowInformation();
		AdminAccManagemeunt aam = new AdminAccManagemeunt();
		int input = 0;
		
		logger.info("admin login");
		do {
			try {
				System.out.println("1.사용자 관리 | 2.서버 정보 확인 | 0.메인메뉴");
				System.out.print("메뉴를 선택해 주세요 > ");
				input = Integer.parseInt(sc.nextLine());
				
				switch (input) {
				
				case 1:
					System.out.println("1.사용자 수정 | 2.사용자 추가 | 3.사용자 삭제 | 0.메인메뉴");
					System.out.print("메뉴를 선택해 주세요 > ");
					input = Integer.parseInt(sc.nextLine());
					aam.modifyAccount(input, sc);
					break;
				case 2:
					aif.informationMeun(sc);
					break;
					
				case 0:
					logger.info("return to main meun");
					System.out.println("메뉴로 돌아갑니다.");
					return;
					
				default:
					System.out.println("잘못입력하셨습니다. 다시 입력하세요.");
					break;

				}
				
			} catch (NumberFormatException e) {
				System.out.println("\n잘못 입력하셨습니다. 다시 입력하세요.");
				
			} catch (Exception e) {
				logger.error(e.toString());
				System.out.println("에러가 발생하였습니다.");
			}

			
			
		} while (true);

		
	}
}
