package bank.system;

import java.util.Scanner;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AccountDeposit extends AccountFileSystem {
	
	final static Logger logger = LogManager.getLogger(AccountDeposit.class);

	
	public void depositMenu(Scanner sc) {
		/*
		 메소드 명: depositMenu(홍지희)
		 파라미터:  Scanner sc
		 입력받기 위해 스캐너를 사용
		 반환값: void
		 기능 설명:  사용자로부터 계좌번호와 입금 금액을 입력받아 입금을 처리합니다.
				  입력한 계좌번호의 정보가 데이터파일과 동일한지 확인 후 동일하다면 그다음 단계인 입금 금액을 입력하고
				  동일하지 않거나 사용자가 0을 입력하면 메인메뉴로 돌아갑니다.
				  입력한 계좌의 상태를 확인하여 잠겨있는 경우에도 메인메뉴로 돌아갑니다.
				  정상적인 입력이 이루어지면 계좌 잔액을 업데이트하고 거래가 완료되었음을 알립니다.
				  거래 날짜, 계좌번호, 입금 금액, 통장 잔액을 출력하여 명세표를 확인할 수 있습니다.
				  모두 완료되면 엑셀에 업데이트된 값을 저장합니다.
		 */
		logger.info("Deposit Start");
		
		long depositAmount = 0; // 입금금액 저장할 변수
		String inputNumber = ""; // 계좌번호 저장할 변수
		String inputAccountNumber = null;// 맨 처음 입력받을 계좌번호 저장할 변수
		String inputNumbercheck = null; // 입력한 정보 확인할 변수
		
		AccountTime at = new AccountTime();
		AccountSecurity as = new AccountSecurity();
		
		int[] time;
		
		System.out.println(" ==========================================================");
		System.out.println(" | 예금 입금 메뉴 입니다 잘못들어오셨다면 0번을 입력하면 메인메뉴로 돌아갑니다. |");
		System.out.println(" ==========================================================");
		System.out.print("\n하이픈(-)을 제외한 계좌 번호를 12 자리를 입력해주세요 > ");

		inputNumber = sc.nextLine();

			if (!accNumPattern.matcher(inputNumber).matches()) {
				System.out.println();
				System.out.print("\n잘못 입력하셨습니다. 메인으로 돌아갑니다.");
				return;
			}

			if (inputNumber.equals("0")) {
				System.out.println();
				System.out.println("메인메뉴로 돌아갑니다.");
				return;
			}
			if(as.cheakState(BankMain.map.get(inputNumber), 2)) {
				System.out.println("메인메뉴로 돌아갑니다.");
				return;
			}
			
			logger.info("Account " + inputAccountNumber+ " is select");
			System.out.print("입금 금액을 입력해주세요. > "); // 입금액 입력
			String gap = sc.nextLine();
			depositAmount = Long.parseLong(gap);

			if (depositAmount < 1) { // 입금액이 1보다 작으면 메인화면
				System.out.println();
				System.out.print("\n잘못 입력하셨습니다. 메인으로 돌아갑니다.");

				return;
			}

			System.out.printf("\n입력하신 계좌 번호: %s\n", inputNumber);
			System.out.printf("입력하신 금액: %d\n", depositAmount);

			System.out.println("\n입력하신 정보가 맞다면 1을 입력해 주세요");
			
			int index = BankMain.map.get(inputNumber);
			BankMain.list.get(index).setBalance(BankMain.list.get(index).getBalance() + depositAmount);
			
			System.out.print("그 이외 값은 메인으로 돌아갑니다 > ");
			inputNumbercheck = sc.nextLine();
			if (!Pattern.matches("^\\d{1}$", inputNumbercheck)) {
				System.out.println();
				System.out.print("\n잘못 입력하셨습니다. 메인으로 돌아갑니다.");
				return;
			} else if (Integer.parseInt(inputNumbercheck) != 1) { // 1이 아니라면 메인화면
				System.out.println();
				System.out.print("\n잘못 입력하셨습니다. 메인으로 돌아갑니다.");

				return;

			} else {
				System.out.println("\n거래가 완료되었습니다.");
			}

			System.out.println();

			time = at.currentTime(index, true);
			
			inputFileSystem(); // 리스트에 있는 값을 엑셀에 저장하기
			
			logger.info("Account" + inputAccountNumber +" has been successfully deposited");
			System.out.println("명세표를 출력하시겠다면 1을 입력해 주세요");
			System.out.print("그 이외 값은 취소입니다 > ");
			inputNumbercheck = sc.nextLine();

			if (!Pattern.matches("^\\d{1}$", inputNumbercheck)) {

			} else if (Integer.parseInt(inputNumbercheck) == 1) { // 1입력 시 명세표 출력
				System.out.printf("\n거래 날짜: %d/%d/%d\n", time[0], time[1], time[2]);
				System.out.printf("계좌번호: %s\n", inputNumber);
				System.out.printf("금액: %,3d\n", depositAmount);
				System.out.printf("통장 잔액: %,3d\n\n\n", BankMain.list.get(index).getBalance());
				System.out.println("메인메뉴로 돌아갑니다.");
			}

			logger.info("Deposit end");
			return;
	}

	@Override
	void modifyAccount(int number, Scanner sc) {
		// TODO Auto-generated method stub

	}

}
