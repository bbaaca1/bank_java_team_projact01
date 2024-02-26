package bank.system;

import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AccountTransfer extends AccountFileSystem 
{
	final static Logger logger = LogManager.getLogger(AccountTransfer.class);
	
	/*
	 메소드 명: transferMeun(홍지희)
	 파라미터:  Scanner sc
	 입력받기 위해 스캐너를 사용
	 반환값: void
	 기능 설명: 사용자로부터 송금 계좌번호와 송금 금액을 입력받은 후 수금 계좌번호를 입력받아 계좌이체를 처리합니다.
			  입력받은 송금 계좌번호의 정보가 데이터파일과 동일한 지 확인하고 송금 계좌의 정보를 받아 저장합니다.
			  입력받은 송금액이 정확히 입력되었는지 확인하고 송금 계좌의 잔액과 비교하여 송금할 수 있으면 송금액을 저장합니다.
			  입력받은 수금 계좌번호의 정보가 데이터파일과 동일한 지 확인하고, 송금 계좌번호와 일치하는지 확인한 후 일치하지 않으면 수금 계좌번호를 저장합니다.
			  정상적인 입력이 이루어지면 저장된 정보를 사용하여 계좌 잔액을 업데이트하고 거래가 완료되었음을 알립니다.
			  거래 날짜, 송금인 계좌번호, 송금액, 통장 잔액, 수금인 계좌번호를 출력하여 명세표를 확인할 수 있습니다.
			  모두 완료되면 명세표 출력 여부에 상관없이 엑셀에 업데이트된 값을 저장하고 메인화면으로 돌아갑니다.
	 */
	
	
	@SuppressWarnings("unused")
	public void transferMeun(Scanner sc) 
	{
		AccountTime date = new AccountTime();
		AccountSecurity as = new AccountSecurity();
		int[] tradeDate;//거래 날짜 받는 거[0]은 년도 [1] 월 [2]는 일
		
//		마지막 거래일과 cuurenttime 일자를 받아서 음수면 초기화 아니면
//		마지막 거래월과 currenttime 월을 받아서 음수면 초기화 아니면
//		마지막 거래년과 cuurenttime 년을 받아서 음수면 초기화
		String LastTradeyear;//마지막 거래 연도
		String LastTrademonth;//마지막 거래 월
		String LastTradeday;//마지막 거래 일
		
//		String.valueof(list.get(i).getDate()) i번째 객체의 마지막 거래일자를 가져와서 문자열 처리를 한다. 
//		YYYY.MM.DD
//		0123456789
		
		
//		일일 출금 금액.
//		sender가 sendingCash를 receiver에게 보낸다
		
		// 이체 메뉴화면 메소드입니다.
		boolean check = true; // 예외처리 반복 위한 불값 선언
		
		long senderNumber = 0; // 송금인 계좌번호 long으로 받기
		long receiverNumber = 0;// 수금인 계좌번호 long으로 받기
		
		String sendingAccountNumber = null; //송금인 계좌번호
		String receivingAccountNumber = null;//수금인 계좌번호
		
		long senderBalance = 0; //송금인 잔액
		long receiverBalance = 0;//수금인 잔액
		
		String senderPW = null; // 송금인 비밀번호
		String inputsenderPW = null;//입력받은 송금인 비밀번호
		
		int transferinfoSelector; // 계좌이체 간 정보확인용 선택자
		int transferreceiptSelector;//계좌이체 간 명세표 선택자
		boolean accountAvailability; // 계좌 유무 검사
		
		long sendingCash = 0; //송금할 금액을 받을 변수
		
		int senderIndex = 0; //송금자의 list 내 인덱스 번호
		int receiverIndex = 0;//수금자의 list 내 인덱스 번호
		
		long senderTransLimit = 0;// 송금자의 일일 이체 한도
		
		String inputTradedate = null;// 거래날짜
		
		String toString = null;
		
		long senderDailyTradeReceipt;
		
		logger.info("Transfer Start");
		
		System.out.println(" ==========================================================");
		System.out.println(" | 계좌 이체 메뉴 입니다  잘못들어오셨다면 0번을 입력하면 메인메뉴로 돌아갑니다.|");
		System.out.println(" ==========================================================");
		System.out.println(" 보이스피싱 예방을 위해 300만원 이상의 금액이 이체될 경우, 입금 계좌의 거래가 10분간 동결됩니다.");
		System.out.println(" 계좌 이체시 최소 1원 이상 최대 600만원까지 가능하며, 1일 최대 3000만원까지 이체가 가능합니다.");
		System.out.println(" 일일이체 금액이 3000만원이 넘게되면 해당 일의 출금이 제한됩니다.");
		System.out.print("\n하이픈(-)을 제외한 송금인의 계좌 번호 12 자리를 입력해주세요 > ");
		try 
		{
			sendingAccountNumber = sc.next("[0-9]*$");
//				Long.valueOf(sendingAccountNumber);
//				System.out.println(sendingAccountNumber.length());자릿수 확인
			if (sendingAccountNumber.length() == 12) //자릿수 확인
			{
				for (int i = 0; i < BankMain.list.size(); i++)// 리스트 돌기 
				{
					if (sendingAccountNumber.equals(BankMain.list.get(i).getAccountNumber()))//입력받은 계좌번호가 리스트 내의 계좌번호 중 같은 것이 있는지 확인하기 
					{
						LastTradeyear = String.valueOf(BankMain.list.get(i).getDate()).substring(0,4);//YYYY받기
						LastTrademonth = String.valueOf(BankMain.list.get(i).getDate()).substring(4,6);//MM받기
						LastTradeday = String.valueOf(BankMain.list.get(i).getDate()).substring(6, 8);//DD받기
						tradeDate = date.currentTime(i, false);//현재 거래 날짜 받아오기
						if(Integer.parseInt(LastTradeday)-tradeDate[2] != 0 || Integer.parseInt(LastTrademonth)-tradeDate[1] != 0 || Integer.parseInt(LastTradeyear)-tradeDate[0] != 0)
						{//위의 if문은 하루, 혹은 그 이상 지났는지 확인함
//								System.out.println(BankMain.list.get(i).getTransferLimits());
							BankMain.list.get(i).setTransferLimits(0);
							BankMain.list.get(i).setTransferLimit(false);
							if(BankMain.list.get(i).getState() == 4) {
								BankMain.list.get(i).setState(0);
							}
							else if(BankMain.list.get(i).getState() == 5) {
								BankMain.list.get(i).setState(3);
							}
							else if(BankMain.list.get(i).getState() == 7) {
								BankMain.list.get(i).setState(1);
							}
							else if(BankMain.list.get(i).getState() == 8) {
								BankMain.list.get(i).setState(6);
							}
							BankMain.list.get(i).accountStatus(BankMain.list.get(i).getState());
//								System.out.println(BankMain.list.get(i).getTransferLimits());
						}
						if(as.cheakState(BankMain.map.get(sendingAccountNumber), 1)) {
//							System.out.println("송금할 계좌의 이체 한도: " + BankMain.list.get(i).getTransferLimits());
							System.out.println("메인메뉴로 돌아갑니다.");
							sc.nextLine();
							return;
						}
//						if (as.accState(BankMain.map.get(sendingAccountNumber), 1) //이체한도가 초과되어 이체가 불가능한지 확인
//						{
//							sc.nextLine();
//							System.out.println();
//							System.out.println("이체 제한 계좌입니다.");
//							System.out.println("송금할 계좌의 이체 한도: " + BankMain.list.get(i).getTransferLimits());
//							System.out.println("메인메뉴로 돌아갑니다.");
//							return;								
//						}
//						if (BankMain.list.get(i).getState() == 1 || BankMain.list.get(i).getState() == 2 || BankMain.list.get(i).getState() == 8 ) 
//						{//1,8은 동결계좌 2는 휴면계좌 
//							sc.nextLine();
//							System.out.println();
//							System.out.println("휴면 혹은 동결 계좌입니다.");
//							System.out.println("메인메뉴로 돌아갑니다.");
//							return;				
//						}
						
//							System.out.println(BankMain.list.get(i).getTransferLimits());//이체한도 확인 용
						senderBalance = BankMain.list.get(i).getBalance();//송금자 잔액 받아오기
						senderPW = BankMain.list.get(i).getAccountPasswd();//송금자 비밀번호 받아오기
						senderTransLimit = BankMain.list.get(i).getTransferLimits();//송금자 이체한도 받아오기
//							BankMain.list.get(i).setTransferLimits(0);
//							System.out.println(list.get(i).getDate());//2022.07.07.
//							System.out.println(String.valueOf(list.get(i).getDate()).substring(0,4));//2022
//							System.out.println(String.valueOf(list.get(i).getDate()).substring(5,7));//07
//							System.out.println(String.valueOf(list.get(i).getDate()).substring(8));07
						senderIndex = i;//list 내 송금자 위치 받아오기
						break;
					}
					if (i == BankMain.list.size()-1) //계좌번호가 list내에 등록되지 않은 경우
					{
						sc.nextLine();
						System.out.println();
						System.out.println("존재하지 않는 계좌입니다. 메인으로 돌아갑니다.");
						return;
					}
				}
			}
			else  
			{
				sc.nextLine();
				System.out.println();
				System.out.println("잘못 입력하셨습니다. 메인으로 돌아갑니다.");
				return;//리스트 정보 확인 후 리스트 정보 반환해야함.
			}
		} 
		catch (Exception e) 
		{
			sc.nextLine();
			System.out.println();
			System.out.println("잘못 입력하셨습니다. 메인으로 돌아갑니다.");
			return;
		}
//			inputAccountNumber = sc.nextLine();
		senderNumber = Long.parseLong(sendingAccountNumber);//0번 입력한경우 메인으로 돌아가기
		if (senderNumber == 0) 
		{
			System.out.println("메인메뉴로 돌아갑니다.");
			return;
		}
		
		if (sendingAccountNumber.contains("021")) {
			System.out.println("적금 통장은 이체 하실 수 없습니다. 메뉴로 돌아갑니다.");
			sc.nextLine();
			return;
		}
		
		check = false;
		
		check = true;
		
		logger.info("sender account" + sendingAccountNumber + " selected");
		sc.nextLine();
		System.out.print("\n송금할 금액을 입력해주세요 > ");
		while (check) 
		{
			try 
			{
				sendingCash = Long.parseLong(sc.nextLine()); // balance 가 long 이므로 long으로 받았습니다.
				
				if (sendingCash <= 0) 
				{
					System.out.println();
					System.out.println("잘못 입력하셨습니다. 메인으로 돌아갑니다.");
					return;
				}
				else if (6000000 < sendingCash) //금액이 0이하거나 6백만원 이상인지 확인하기
				{
					System.out.println();
					System.out.println("1회 최대 이체 금액은 6백만 원입니다. 메인으로 돌아갑니다.");
					return;
				} 
				else if(senderBalance < sendingCash)//금액이 잔액보다 많은지 확인하기
				{
					System.out.println();
					System.out.println("잔액이 모자랍니다.\n현재 잔액: " + senderBalance +"\n메인으로 돌아갑니다.");
					return;
				}
				senderTransLimit = sendingCash;
			}
			catch (Exception e) 
			{
				System.out.println();
				System.out.println("잘못 입력하셨습니다. 메인으로 돌아갑니다.");
				return;
			}
			if (BankMain.list.get(senderIndex).getTransferLimits() + sendingCash > 30000000) //이체한도가 초과되어 이체가 불가능한지 확인
			{
				System.out.println();
				System.out.println("이체 한도 초과입니다.");
				System.out.println("송금할 계좌의 남은 한도: " + (30000000 - BankMain.list.get(senderIndex).getTransferLimits()));
				System.out.println("메인메뉴로 돌아갑니다.");
				BankMain.list.get(senderIndex).setState(4);
				return;								
			}
			check = false;
		}
		check = true;
		System.out.print("\n하이픈(-)을 제외한 수금인의 계좌 번호 12 자리를 입력해주세요 > ");
		while (check) 
		{
			try 
			{
				receivingAccountNumber = sc.next("[0-9]*$");//숫자 입력제한
				if (receivingAccountNumber.length() == 12) //자릿수 제한
				{
					for (int i = 0; i < BankMain.list.size(); i++) 
					{
						if (receivingAccountNumber.equals(BankMain.list.get(i).getAccountNumber()))//계좌유효성검증 
						{
							receiverBalance = BankMain.list.get(i).getBalance();//있으면 수금자 잔액을 받아옴
							receiverIndex = i;//수금자의 다중배열 번호를 받아옴
							if (receiverIndex == senderIndex)//수금자와 송금자가 같을 때 
							{
								sc.nextLine();
								System.out.println();
								System.out.println("계좌번호가 동일합니다. 메인으로 돌아갑니다.");
								return;
							}
							break;
						}
						if (i == BankMain.list.size()-1)// 계좌가 없을 때 
						{
							sc.nextLine();
							System.out.println();
							System.out.println("존재하지 않는 계좌입니다. 메인으로 돌아갑니다.");
							return;
						}
					}
				}
				else  //수금인의 계좌번호가 12자리가 아닐 때
				{
					sc.nextLine();
					System.out.println();
					System.out.println("잘못 입력하셨습니다. 메인으로 돌아갑니다.");
					return;//리스트 정보 확인 후 리스트 정보 반환해야함.
				}
			} 
			catch (Exception e) 
			{
				sc.nextLine();
				System.out.println();
				System.out.println("잘못 입력하셨습니다. 메인으로 돌아갑니다.");
				return;
			}
//			inputAccountNumber = sc.nextLine();
			receiverNumber = Long.parseLong(receivingAccountNumber);//수금인 계좌번호 입력시에도 0입력하면 메인으로 돌아가기
			if (receiverNumber == 0) 
			{
				System.out.println("메인메뉴로 돌아갑니다.");
				return;
			}
			check = false;
		}
		check = true;
		logger.info("receiving account" + sendingAccountNumber + " selected");
		System.out.printf("입력하신 송금인 계좌 번호: " + sendingAccountNumber + "\n");
		System.out.printf("입력하신 금액: " + sendingCash + "\n");
		System.out.println();
		System.out.printf("입력하신 수금인 계좌 번호: " + receivingAccountNumber + "\n");
		System.out.println();
		System.out.printf("비밀번호 4자리를 입력해 주세요 > ");
		try 
		{
			inputsenderPW = sc.next("[0-9]*$");
//			System.out.println(sendingAccountNumber.length());자릿수 확인
			if (inputsenderPW.length() == 4) //비밀번호 4자리인지 확인하기
			{
				if (!inputsenderPW.equals(senderPW))//비밀번호가 같지 않으면 메인으로 돌아가기 
				{
					sc.nextLine();
					System.out.println();
					System.out.println("비밀번호를 잘못입력하셨습니다. 메인으로 돌아갑니다.");
					return;//리스트 정보 확인 후 리스트 정보 반환해야함.
				}
			}
			else  //4자리가 아니면 메인으로 돌아가기
			{
				sc.nextLine();
				System.out.println();
				System.out.println("잘못 입력하셨습니다. 메인으로 돌아갑니다.");
				return;//리스트 정보 확인 후 리스트 정보 반환해야함.
			}
		} 
		catch (Exception e) 
		{
			sc.nextLine();
			System.out.println();
			System.out.println("잘못 입력하셨습니다. 메인으로 돌아갑니다.");
			return;
		}
		System.out.println();
		System.out.printf("입력하신 정보가 맞다면 1을 입력해 주세요\n그 외의 값은 취소입니다 > ");
		switch (transferinfoSelector = sc.nextInt()) 
		{
		case 1:
			if(sendingCash >= 3000000) {
				BankMain.list.get(BankMain.map.get(receivingAccountNumber)).setFreez(true);
				
				if(BankMain.list.get(BankMain.map.get(receivingAccountNumber)).getState() == 0) {
					BankMain.list.get(BankMain.map.get(receivingAccountNumber)).setState(1);
				}
				else if(BankMain.list.get(BankMain.map.get(receivingAccountNumber)).getState() == 3) {
					BankMain.list.get(BankMain.map.get(receivingAccountNumber)).setState(6);
				}
				
				as.voicePhishing(BankMain.list.get(BankMain.map.get(receivingAccountNumber)).getAccountNumber(), BankMain.list.get(BankMain.map.get(receivingAccountNumber)).isFreez());
			}
			sc.nextLine();
			System.out.println();
			System.out.println("거래가 완료되었습니다.");
			
			senderBalance -= sendingCash;//송금자의 잔액에서 송금액을 빼서 대입
			
			BankMain.list.get(senderIndex).setBalance(senderBalance);//위의 잔액을 list 내 송금자의 잔액으로 바꿔주기
			receiverBalance += sendingCash;//수금자의 잔액에 송금액을 더해서 대입
			
			BankMain.list.get(receiverIndex).setBalance(receiverBalance);//위의 잔액을 list 내 수금자의 잔액으로 바꿔주기
			tradeDate = date.currentTime(senderIndex,true);//거래일자 넣어주기
			//tradeDate = date.currentTime(receiverIndex,true);//거래일자 넣어주기
			BankMain.list.get(senderIndex).setTransferLimits(senderTransLimit);//송금자의 이체한도를 송금액을 더한 이체한도로 바꿔주기
//			inputTradedate = Integer.toString(tradeDate[0]) + Integer.toString(tradeDate[1]) + Integer.toString(tradeDate[2]);
			break;
		default:
			sc.nextLine();
			System.out.println("거래를 취소하고 메인메뉴로 돌아갑니다.");
			return;
		}
		
		logger.info(sendingAccountNumber + " to "+ receivingAccountNumber + " send " + sendingCash);
		
		System.out.printf("명세표를 출력하시겠다면 1을 입력해 주세요\n메인으로 돌아갑니다 > ");
		logger.info("Transfer end");
		switch (transferreceiptSelector = sc.nextInt()) 
		{
		case 1:
			sc.nextLine();
			System.out.println();
			System.out.println("거래 날짜: " + tradeDate[0] + "/" + tradeDate[1] + "/" + tradeDate[2]);
			System.out.println("송금인 계좌 번호: " + sendingAccountNumber);
			System.out.printf("송금액: %,3d",sendingCash);
			System.out.printf("\n남은 이체 한도: %,3d / 30,000,000",BankMain.list.get(senderIndex).getTransferLimits());
			System.out.printf("\n통장 잔액: %,3d",BankMain.list.get(senderIndex).getBalance());
			System.out.println("\n수금인 계좌 번호: " + receivingAccountNumber);
			System.out.println("메인메뉴로 돌아갑니다.");
			inputFileSystem();
			return;
		default:
			sc.nextLine();
			System.out.println("메인메뉴로 돌아갑니다.");
			inputFileSystem();
			return;
		}
	} 
}

