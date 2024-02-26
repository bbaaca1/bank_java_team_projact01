package bank.system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;

import org.apache.commons.collections4.map.MultiKeyMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class BankMain {
	
	
	public static MultiKeyMap<String, Integer> multikeyMap = new MultiKeyMap<String, Integer>(); // 계좌번호와 계좌비밀번호를 키로 가지고 해당 계좌번호가 저장되어있는 list의 인덱스 번호를 값으로 가지고 있는 MultiKeyMap
	public static Map<String, Integer> map = new HashMap<String, Integer>(); // 계좌번호를 키로 가지고 해당 계좌번호가 저장되어있는 list의 인덱스 번호를 값으로 가지고 있는 HashMap
	public static Map<String, String> freezMap = new HashMap<String, String>(); // 계좌번호를 키로가지고 해당 계좌 동결시간을 값으로 가지고 있는 HashMap
	public static ArrayList<AccountCreate> list = new ArrayList<AccountCreate>(); // 계좌에 관한 모든 정보를 가진 객체를 저장하는 ArrayList
	
	final static Logger logger = LogManager.getLogger(BankMain.class);
	
	/* 은행 관리 시스템의 메인 메소드
	 * 
	 * 기능 설명:
	 * 사용자로부터 메뉴 선택을 받아 각각의 기능을 수행한다.
	 * 선택된 메뉴에 따라 계좌 관리, 계좌 입금,계좌 출금,계좌 이체의 기능을 수행한다
	 * 특정입력(9번 입력시)관리자 화면 등의 기능을 수행할 수 있다.
	 */
	
	public static void main(String[] args) {
		// 은행 메인메뉴 입니다.
		// 메인 메인에는 가능한 코드를 사용하지 말아주세요.
		
		
		Scanner sc = new Scanner(System.in);
		
		AccountDeposit ad = new AccountDeposit();
		AccountWithdrawal aw = new AccountWithdrawal();
		AccountTransfer at = new AccountTransfer();
		AccountManage am = new AccountManage();
		AdminMenu adminmeun = new AdminMenu();
		
		am.startTheSystem();
				
		int input = 0;
		
		logger.info("System launched"); // 로깅 : 시스템 시작
		
		do {
			System.out.println("==========================================================");
			System.out.println("|\t\t\t대우 은행 관리 시스템 \t\t |");
			System.out.println("==========================================================");
			System.out.println("|    1.계좌 관리 | 2.예금 입금 | 3.예금 출금 | 4.계좌 이체 | 0.종료\t |");
			System.out.println("==========================================================");
			System.out.print("\n메뉴을 선택해주세요(정수만 입력해주세요) > ");
			
			try {
				input = Integer.parseInt(sc.nextLine());
				
				switch (input) {
				// 1.계좌 관리 메뉴 2. 입금메뉴 3. 출금메뉴 4. 이체메뉴 0. 종료 9. 관리자 화면
				
				case 1:
					am.manageMeun(sc);
					break;
					
				case 2:
					ad.depositMenu(sc);
					break;
					
				case 3:
					aw.withdrawalMeun(sc);
					break;
					
				case 4:
					at.transferMeun(sc);
					break;
					
				case 0:
					System.out.println("프로그램을 종료합니다.");
					logger.info("End of System"); // 로깅 : 시스템 종료
					sc.close();
					return;
					
				case 9:
					adminmeun.AdminLogin(sc);
					break;
					
				default:
					System.out.println();
					System.out.println("잘못 입력하셨습니다. 메인으로 돌아갑니다.");
					break;
				}
			} catch (NumberFormatException e) {
				// 숫자(정수)외 입력시 실행합니다.
				System.out.println();
				System.out.println("잘못 입력하셨습니다. 메인으로 돌아갑니다.");
				logger.error(e.toString());
			} catch (NoSuchElementException  e) {
				// TODO: handle exception
				
				System.out.println("");
			} catch (Exception e) {
				System.out.println();
				System.out.println("잘못 입력하셨습니다. 메인으로 돌아갑니다.");
				logger.error(e.toString());
			}
			
			System.out.println("\n\n\n\n\n");// 이전 메뉴가 가능하면 안나오게 하기 위해 줄바꿈
		}while (true);					

	}

}
