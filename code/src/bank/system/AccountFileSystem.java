package bank.system;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Scanner;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public abstract class AccountFileSystem {
	// 로깅용 로그 선언
	final static Logger logger = LogManager.getLogger(AccountFileSystem.class);
	
	//필드
	private int AdminPassword = 1234;
	
	//패턴
	public Pattern namePattern = Pattern.compile("^[가-힣]{2,}"); // 이름 확인용 패턴
	public Pattern accNumPattern = Pattern.compile("^(012|021)068(\\d{6})$"); // 계좌번호 확인용 패턴
	public Pattern ssnPattern = Pattern.compile("^\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12][0-9]|3[01])[1-4][0-9]{6}$"); // 주민등록번호 확인용 패턴
	public Pattern phonePattern = Pattern.compile("^01(?:0|1|[6-9])(\\d{4})(\\d{4})$"); // 전화번호 확인용 패턴	
	public Pattern passwdPattern = Pattern.compile("^\\d{4}$"); // 비밀번호용 패턴	
	


	// 메소드
	/*
	 * 메소드 : getAdminPassword()
	 * 작성자 : 백동민
	 * 파라미터 : 없음(None)
	 * 반환값 : AdminPassword(int)
	 * 기능설명 : AdminPassword의 값을 가져오는 getter 메소드
	 */
	
	public int getAdminPassword() {
		return AdminPassword;
	}
	
	/*
	 * 메소드 : inputAccountsDate()
	 * 작성자 : 백동민
	 * 파라미터 : 없음(None)
	 * 반환값 : 없음(void)
	 * 기능설명 : File_system폴더에 있는 user_date폴더안에 accounts엑셀파일에 저장된 이름,계좌번호,주민등록,주소,전화번호,잔액,계좌유형,최종거래일자,비밀번호,계좌상태확인번호를 2번째 행부터 읽어서
	 * 		   읽어온 정보들로 필드값으로 설정한 AccountCreate객체를 생성하여 BankMain에 있는 arrayList list에 저장한다 
	 * 		   1행은 표제목들이 저장되어있어 가져오지않는다.
	 * 		   이후 BankMain에 있는 multikeyMap에 생성된 객체가 할당된 list의 인덱스 번호를 계좌번호 와 계좌비밀번호를 키로한 multikeyMap에 값에 할당한다  
	 * 		   그이후 BankMain에 있는 Map에 생성된 객체가 할당된 list의 인덱스 번호를 계좌번호를 키로한 Map에 값에 할당한다
	 * 		   만약 기록된 최종거래일 기준으로 예금 금액이 1만원 미만이면 1년 3만원이상 5만원 미만이면 2년 5만원 이상 10만원 미만은 3년동안 거래가 없었다면 휴면상태로 전환 시킨다
	 * 		   만약 엑셀파일이나 폴더가 없다면(FileNotFoundException에러 발생 시) createUserExcelFile메소드를 실행 한다
	 */
	private void inputAccountsDate() {
		try {
			
			AccountSecurity as = new AccountSecurity();
			
			logger.info("Loading data");
			
			String name = null; 
			String accountNumber = null;
			String ssn = null;
			String homeAddress = null;
			String phoneNumber = null; 
			long balance = 0;
			String accountType = null; 
			String date = null;
			String accountPasswd = null;
			int state = 0;
			
			int account = 0; // 계좌의 인덱스 번호를 담아둘 변수
			int lastTrade = 0; // 계좌가 휴면계좌 인지 확인할 변수
			
			FileInputStream accfile = new FileInputStream("File_system\\user_date\\accounts.xlsx"); // 상대 경로 프로젝트의 file_system폴더안 user_date폴더에 accounts.xlsx엑셀 파일을 불러옴
			Workbook workbook = WorkbookFactory.create(accfile);
			Sheet sheet = workbook.getSheetAt(0); // 시트는 0번
			
			// 저장된 계좌 입력 반복문
			// 0번째 행은 쓰지않고 
		exfor: for (int i = 1; i > 0; i++) {
				Row row = sheet.getRow(i); // i번째 행 1행(0)은 쓰지않음
				for (int j = 0; j <= 9; j++) {
					try {
						Cell cell = row.getCell(j); // j번째 열을 지정
						switch (j) {
						case 0:
							// 이름 입력 : 0열
							name = cell.getStringCellValue();
							break;
						case 1:
							// 계좌 번호 입력 : 1열
							accountNumber = cell.getStringCellValue().replace("-", "");
							break;
						case 2:
							// 주민 번호 입력 : 2열
							ssn = cell.getStringCellValue().replace("-", "");
							break;
						case 3:
							// 주소 입력 : 3열
							homeAddress = cell.getStringCellValue();
							break;
						case 4:
							// 전화 번호 입력 : 4열
							phoneNumber = cell.getStringCellValue().replace("-", "");
							break;
						case 5:
							// 잔액 입력 : 5열(숫자)
							balance = (long) cell.getNumericCellValue();
							break;
						case 6:
							// 계좌 종류 입력 : 6열
							accountType = cell.getStringCellValue();
							break;
						case 7:
							// 최종거래일자 : 7열
							date = cell.getStringCellValue().replace("-", "");
							break;							
						case 8:
							// 비밀번호 : 8열
							accountPasswd = cell.getStringCellValue();

							break;
						case 9:
							// 계좌 상태 : 9열
							state = (int) cell.getNumericCellValue();
							break;
						}
					} catch (Exception e) {
						// 엑셀 내용이 없을때 실행 반복문 종료
							break exfor;
					  }
				}				
															
				// 리스트에 추가(객체 생성) 및 multKeyMap과 Map 설정
				BankMain.list.add(new AccountCreate(name, accountNumber, ssn, homeAddress, phoneNumber, balance, accountType, date, accountPasswd, state)); // 엑셀에서 불러온 정보를 가진 객체를 생성해 list에 할당
				BankMain.multikeyMap.put(accountNumber, accountPasswd, account); // 계좌번호와 계좌비밀번호를 키와 인덱스 번호값을 가진 멀티맵을 생성
				BankMain.map.put(accountNumber, account); // 계좌번호를 키와 인덱스 번호값을 가진 멀티맵을 생성
				
				// 휴면계좌 설정 1만원 미만 1년 3만원이상 5만원 미만 2년 5만원 이상 10만원 미만은 3년 그 이상은 휴면 없음
				if (BankMain.list.get(account).getState() != 2) { // 휴면 계좌 일경우 스킵함
					if (BankMain.list.get(account).getBalance() < 10000) {
						lastTrade = as.nonTrading(account);
						if (lastTrade >= 365) {
							BankMain.list.get(account).setNonTradingAcc(true);
							BankMain.list.get(account).setState(2);
							state = 2;
						}
					} else if (BankMain.list.get(account).getBalance() < 50000) {
						lastTrade = as.nonTrading(account);
						if (lastTrade >= 730) {
							BankMain.list.get(account).setNonTradingAcc(true);
							BankMain.list.get(account).setState(2);
							state = 2;
						}
						
					} else if (BankMain.list.get(account).getBalance() < 100000) {
						lastTrade = as.nonTrading(account);
						if (lastTrade >= 1095) {
							BankMain.list.get(account).setNonTradingAcc(true);
							BankMain.list.get(account).setState(2);
							state = 2;
						}
					}
				}

				BankMain.list.get(account).accountStatus(state); // 계좌 상태설정
				account++; // 계좌 인덱스 번호 숫자 증가
				
	 		}
			
			getLimitTime(); // 동결시간, 이체제한,출금제한을 불러옴
			workbook.close();// 워크북 닫기
		
		}catch (FileNotFoundException e) {
			// 파일이 없을때 실행
			System.out.println("유저 파일이 존재하지않습니다 파일을 생성합니다. 잠시만 기다려 주십시오.");
			createUserExcelFile();
			return;
			
		} catch (Exception e) {
			// 그외 에러 발생시 실행
			logger.info("data Load Failed");
			logger.fatal(e.toString());
			System.out.println("치명적인 오류가 발생하였습니다. 관리자에게 이야기해주십시오. 프로그램을 종료합니다.");
			System.exit(0);
		}
		logger.info("data Load end");	
		return;
	}
	
	/*
	 * 메소드 : createUserExcelFile()
	 * 작성자 : 백동민
	 * 파라미터 : 없음(None)
	 * 반환값 : 없음(void)
	 * 기능설명 :	만약 엑셀파일이나 저장 폴더가 없을때 실행되는 메소드 경로를 상대 경로(File_System\\user_date)로 지정하여 엑셀파일을 생성한다
	 * 			만약 엑셀파일을 담아둘 File_System이나 user_date폴더가 없다면 해당 폴더를 생성한다
	 * 			
	 */
	
	private void createUserExcelFile() {
		File File_System = new File("File_System");
		File user_date = new File("File_System\\user_date");
		
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Sheet1"); // 시트 생성
            Row row = sheet.createRow(0); // 행 생성
            @SuppressWarnings("unused")
			Cell cell = row.createCell(0); // 셀 생성
            if (!File_System.exists()) { // File_System폴더가 있는지 확인
            	try{
            		File_System.mkdir(); // File_System폴더가 없다면 File_System 폴더를 생성
        	        } 
        	        catch(Exception e){
        		    logger.error(e.toString());
        			System.out.println("치명적인 오류가 발생하였습니다. 관리자에게 이야기해주십시오. 프로그램을 종료합니다.");
        			System.exit(0);
        		}
        		if (!user_date.exists()) { // File_System안에 user_date폴더가 있는지 확인
                	try{
                		user_date.mkdir(); // File_System안에 user_date폴더가 없다면 user_date 폴더 생성
            	        } 
            	        catch(Exception e){
            	        logger.error(e.toString());
            			System.out.println("치명적인 오류가 발생하였습니다. 관리자에게 이야기해주십시오. 프로그램을 종료합니다.");
            			System.exit(0);
            		} 
				}
      
			}
        	FileOutputStream fos = new FileOutputStream("File_system\\user_date\\accounts.xlsx"); // 엑셀파일생성
            workbook.write(fos);
            getLimitTime();
            return;
            }
        
        catch (Exception e) {
        	logger.info("data Load Failed");
			logger.fatal(e.toString());
			System.out.println("치명적인 오류가 발생하였습니다. 관리자에게 이야기해주십시오. 프로그램을 종료합니다.");
			System.exit(0);
        }
	}
	
	/*
	 * 메소드 : inputFileSystem()
	 * 작성자 : 백동민
	 * 파라미터 : 없음(None)
	 * 반환값 : 없음(void)
	 * 기능설명 :	엑셀의 시트를 삭제한후 첫번째행(0)에 이름,계좌 번호,주민등록번호,주소,전화번호,잔액,계좌유형,최종거래일자,비밀번호,계좌상태확인번호라는 표제목을 입력한다
	 * 			두번째행(1)부터BankMain에 있는 list에 저장된 객체의 필드값으로 설정된 이름,계좌번호,주민등록,주소,전화번호,잔액,계좌유형,최종거래일자,비밀번호,계좌상태확인번호 모두 저장된 새로운 행을 생성한다
	 * 			위의 모든 행이 입력되면 기존 엑셀 파일을 삭제하고 위의 값들이 입력된 엑셀파일을 새로 생성한다.
	 * 			
	 */
	
	protected void inputFileSystem() 
	{
		// list의 정보를 엑셀파일에 입력하는 메소드입니다.
		try {	
			FileInputStream accfile = new FileInputStream("File_system\\user_date\\accounts.xlsx");
			Workbook workbook = WorkbookFactory.create(accfile);
			@SuppressWarnings("unused")
			Sheet sheet = workbook.getSheetAt(0);
			workbook.removeSheetAt(0);
			
			Sheet newSheet = workbook.createSheet("sheet");
			Row row = newSheet.createRow(0);
			Cell newCell;
			
			for (int i = 0; i < 10; i++) {
				newCell = row.createCell(i);
				switch (i) {
				case 0:
					// 이름 입력 : 0번
					newCell.setCellValue("이름");
					break;
				case 1:
					// 계좌 번호 입력 : 1번
					newCell.setCellValue("계좌 번호");
					break;
				case 2:
					// 주민 번호 입력 : 2번
					newCell.setCellValue("주민등록번호");
					break;
				case 3:
					// 주소 입력 : 3번
					newCell.setCellValue("주소");
					break;
				case 4:
					// 전화 번호 입력 : 4번
					newCell.setCellValue("전화 번호");
					break;
				case 5:
					// 잔액 입력 : 5번(숫자)
					newCell.setCellValue("잔액");
					break;
				case 6:
					// 계좌 종류 입력 : 6번
					newCell.setCellValue("계좌 유형");
					break;
				case 7:
					// 최종거래일자 : 7번
					newCell.setCellValue("최종거래일자");
					break;
					
				case 8:
					// 비밀번호 : 8번
					newCell.setCellValue("비밀번호");
					break;
				case 9:
					// 계좌 상태 : 9번
					newCell.setCellValue("계좌 상태");
					break;
				}
			}
			
			int rowNumber = 1;
			String str = null;
			
			try {
				for (AccountCreate list : BankMain.list) {
					row = newSheet.createRow(rowNumber);
					for (int i = 0; i < 10; i++) {
						newCell = row.createCell(i);
					
						switch (i) {
						case 0:
							// 이름 입력
							newCell.setCellValue(list.getName());
							break;
						case 1:
							// 계좌 번호 입력
							str = String.format("%s-%s-%s",
									list.getAccountNumber().substring(0,3),
									list.getAccountNumber().substring(3,6),
									list.getAccountNumber().substring(6));
							newCell.setCellValue(str);
						break;
						case 2:
							// 주민 번호 입력
							str = String.format("%s-%s",
									list.getSsn().substring(0,6),
									list.getSsn().substring(6));
							newCell.setCellValue(str);
							break;
						case 3:
							// 주소 입력
							newCell.setCellValue(list.getHomeAddress());
							break;
						case 4:
							// 전화 번호 입력
							str = String.format("%s-%s-%s",
									list.getPhoneNumber().substring(0,3),
									list.getPhoneNumber().substring(3,7),
									list.getPhoneNumber().substring(7));
							newCell.setCellValue(str);
							break;
						case 5:
							// 잔액 입력
							newCell.setCellValue(list.getBalance());
							break;
						case 6:
							// 계좌 종류 입력
							newCell.setCellValue(list.getAccountType());
							break;
						case 7:
							// 최종거래일자
							str = String.format("%s-%s-%s",
									list.getDate().substring(0,4),
									list.getDate().substring(4,6),
									list.getDate().substring(6));
							newCell.setCellValue(str);
							break;
							
						case 8:
							// 비밀번호
							newCell.setCellValue(list.getAccountPasswd());;
							break;
							
						case 9:
							// 계좌 상태
							newCell.setCellValue(list.getState());
							break;
						}
					}
					
					// 계좌 설정 완료
					BankMain.multikeyMap.put(list.getAccountNumber(), list.getAccountPasswd(), rowNumber-1);
					BankMain.map.put(list.getAccountNumber(), rowNumber-1);
					list.accountStatus(list.getState());
					rowNumber++;
					
				}
			} catch (ArrayIndexOutOfBoundsException  e) {
				// list의 값을 다넣었을때 종료위한 캐치 
			}
			
			setLimitTime();
			FileOutputStream outFile = new FileOutputStream(new File("File_system\\user_date\\accounts.xlsx"));
			workbook.write(outFile);
			outFile.close();
			workbook.close();
			
		} catch (Exception e) {
			// 에러 발생시 실행
			logger.fatal(e.toString());
			System.out.println("치명적인 오류가 발생하였습니다. 프로그램을 다시 실행해주세요. 프로그램을 종료합니다.");
			System.exit(0);
		}
		return;

	}
	
	/*
	 * 메소드 : getLimitTime()
	 * 작성자 : 백동민
	 * 파라미터 : 없음(None)
	 * 반환값 : 없음(void) 
	 * 기능설명 : 	계좌에 제한사항(동결시간,출금한도,이체한도)를 user_limit.xlsx 엑셀 파일 1행 2열부터 가져와 BankMain에 있는 list에 저장된 객체만큼 맞춰 할당한다.
	 * 			0행은 표제목들이 1열은 계좌 번호들이 저장되어있어 가져오지않는다.
	 * 			만약 동결시간이 저장되어 있다면 BankMain에 있는 freezMap에 계좌번호를 키로 동결시간을 값으로 할당하지 않는다.
	 * 			동결시간이 저장되어있지 않다면 freezMap에 할당하지 않는다.
	 * 			만약 File_system 폴더안 user_date폴더에 user_limit.xlsx 엑셀 파일이 없다면 FileNotFoundException에러가 발생후 createLimitExcelFile을 실행한다
	 */
	
	// 저장된 제한들을 엑셀에서 읽어오는 메소드
	private void getLimitTime() {
		FileInputStream accfile;
		String str = null;
		int i = 1;
		long inputNumber = 0;
		try {
			accfile = new FileInputStream("File_system\\user_date\\user_limit.xlsx");
			Workbook workbook = WorkbookFactory.create(accfile);
			Sheet sheet = workbook.getSheetAt(0);	
			try {
					
					for (AccountCreate list : BankMain.list) {	
						Row row = sheet.getRow(i);
						for (int j = 1; j <= 3; j++) {
							Cell cell = row.getCell(j);
							switch (j) {
								case 1:
									str = cell.getStringCellValue();
									list.setFreezTime(str);
									if (list.getFreezTime().equals("0")) {
										list.setFreez(false);
										break;
									}
									
									list.setFreez(true);
									BankMain.freezMap.put(list.getAccountNumber(), list.getFreezTime());
									break;
								case 2:
									inputNumber = (long) cell.getNumericCellValue();
									list.setWithdrawalLimits(inputNumber);
									break;
								case 3:
									inputNumber = (long) cell.getNumericCellValue();
									list.setTransferLimits(inputNumber);
									break;
							}
						}
						
						i++;
					}

			
			} catch (Exception e) {
				// 엑셀 불러오기 종료용 캐치
			}
	
			workbook.close();

		} catch (FileNotFoundException e) {
			// 파일이 없을때 실행
			createLimitExcelFile();
			return;
			
		} catch (Exception e) {
			// 에러 발생시 프로그램을 종료시킴
			logger.fatal(e.toString());
			System.out.println("치명적인 오류가 발생하였습니다. 관리자에게 이야기해주십시오. 프로그램을 종료합니다.");
			System.exit(0);
		}
	}
	
	/*
	 * 메소드 : createLimitExcelFile()
	 * 작성자 : 백동민
	 * 파라미터 : 없음(None)
	 * 반환값 : 없음(void) 
	 * 기능설명 : 만약 File_system 폴더안 user_date폴더에 user_limit.xlsx 엑셀파일이 없을떼 File_system 폴더안 user_date폴더에 user_limit.xlsx 엑셀파일을 생성한다 
	 */
	
	private void createLimitExcelFile() {
		
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Sheet1"); // 시트 생성
            Row row = sheet.createRow(0); // 행 생성
            @SuppressWarnings("unused")
			Cell cell = row.createCell(0); // 셀 생성
        	FileOutputStream fos = new FileOutputStream("File_system\\user_date\\user_limit.xlsx");
            workbook.write(fos);
            } 
        
        catch (Exception e) {
        	logger.info("data Load Failed");
			logger.fatal(e.toString());
			System.out.println("치명적인 오류가 발생하였습니다. 관리자에게 이야기해주십시오. 프로그램을 종료합니다.");
			System.exit(0);
        }
	}
	
	
	/*
	 * 메소드 : setLimitTime()
	 * 작성자 : 백동민
	 * 파라미터 : 없음(None)
	 * 반환값 : 없음(void) 
	 * 기능설명 : 	엑셀의 시트를 삭제한후 첫번째행(0)에 계좌번호, 동결 시간, 출금 금액, 이체 금액이라는 표제목을 입력한다
	 * 			계좌에 제한사항(동결시간,출금 금액,이체 금액)과 계좌번호를 를 BankMain에 있는 list로 부터 불러와 엑셀파일에 1행부터 계좌번호 동결시간 출금금액 이체금액을 저장한다
	 * 			만약 동결시간이 저장되어 있다면 동결시간은 0으로 저장된다
	 */
	
	
	// 제한 시간을 엑셀에 입력하는 메소드
	private void setLimitTime() {
		try {
			
			FileInputStream accfile = new FileInputStream("File_system\\user_date\\user_limit.xlsx");
			Workbook workbook = WorkbookFactory.create(accfile);
			@SuppressWarnings("unused")
			Sheet sheet = workbook.getSheetAt(0);
			workbook.removeSheetAt(0);
			
			Sheet newSheet = workbook.createSheet("sheet");
			Row row = newSheet.createRow(0);
			Cell newCell;
			int index = 1;
			
			for (int i = 0; i < 10; i++) {
				newCell = row.createCell(i);
				switch (i) {
				case 0:
					newCell.setCellValue("계좌번호");
					break;
				case 1:
					
					newCell.setCellValue("동결 시간");
					break;
				case 2:
					
					newCell.setCellValue("출금 금액");
					break;
				case 3:
					
					newCell.setCellValue("이체 금액");
					break;
				}
			}
			try {
				
	            for (AccountCreate list : BankMain.list) {
	                row = newSheet.createRow(index);

	                Cell cell = row.createCell(0);
	                
	                String accountNumber = String.format("%s-%s-%s",
							list.getAccountNumber().substring(0,3),
							list.getAccountNumber().substring(3,6),
							list.getAccountNumber().substring(6));
	                cell.setCellValue(accountNumber);

	                cell = row.createCell(1);
	                if (list.isFreez()) {
	                    cell.setCellValue(BankMain.freezMap.get(list.getAccountNumber()));
	                } else {
	                    cell.setCellValue("0");
	                }

	                cell = row.createCell(2);
	                cell.setCellValue(list.getWithdrawalLimits());

	                cell = row.createCell(3);
	                cell.setCellValue(list.getTransferLimits());

	                index++;
	            }
	            
			} catch (Exception e) {
				// 에러 발생시 프로그램을 종료시킴
				logger.error(e.toString());
				System.out.println("치명적인 오류가 발생하였습니다. 프로그램을 다시 실행해주세요. 프로그램을 종료합니다.");
				System.exit(0);
			}
			
			FileOutputStream outFile = new FileOutputStream(new File("File_system\\user_date\\user_limit.xlsx"));
			workbook.write(outFile);
			outFile.close();
			workbook.close();
			
			
		} catch (Exception e) {
			// 에러 발생시 프로그램을 종료시킴
			logger.fatal(e.toString());
			System.out.println("치명적인 오류가 발생하였습니다. 다시 실행해주세요. 프로그램을 종료합니다.");
			System.exit(0);
		}

	}
	/*
	 * 메소드 : runSystem()
	 * 작성자 : 백동민
	 * 파라미터 : 없음(None)
	 * 반환값 : 없음(void) 
	 * 기능설명 : private제한자를 쓰고있는 inputAccountsDate을 사용하기 위한 메소드
	 */
	
	protected void runSystem() {
		inputAccountsDate();
	}
	
	
	
	// 계좌 추상 메소드
	void modifyAccount(int number, Scanner sc) {}
	
}
