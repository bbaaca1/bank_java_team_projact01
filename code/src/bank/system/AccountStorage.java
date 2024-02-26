package bank.system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections4.map.MultiKeyMap;

public class AccountStorage {
	
	private MultiKeyMap<String, Integer> multikeyMap = new MultiKeyMap<String, Integer>(); // 계좌번호와 계좌비밀번호를 키로 가지고 해당 계좌번호가 저장되어있는 list의 인덱스 번호를 값으로 가지고 있는 MultiKeyMap
	private Map<String, Integer> map = new HashMap<String, Integer>(); // 계좌번호를 키로 가지고 해당 계좌번호가 저장되어있는 list의 인덱스 번호를 값으로 가지고 있는 HashMap
	private Map<String, String> freezMap = new HashMap<String, String>(); // 계좌번호를 키로가지고 해당 계좌 동결시간을 값으로 가지고 있는 HashMap
	private ArrayList<AccountCreate> list = new ArrayList<AccountCreate>(); // 계좌에 관한 모든 정보를 가진 객체를 저장하는 ArrayList
	
	
	
	
	public int getMultikeyMap(String accountNumber, String accountPassword) {
		return multikeyMap.get(accountNumber, accountPassword);
	}
	
	public void setMultikeyMap(String accountNumber, String accountPassword, int index) {
		multikeyMap.put(accountNumber, accountPassword, index);
	}
	
	public int getMap(String accountNumber) {
		return map.get(accountNumber);
	}
	public void setMap(String accountNumber, int index) {
		map.put(accountNumber, index);
	}
	
	public String getFreezMap(String accountNumber) {
		return freezMap.get(accountNumber);
	}
	
	public void setFreezMap(Map<String, String> freezMap) {
		this.freezMap = freezMap;
	}
	public AccountCreate getList(int index) {
		return list.get(index);
	}
	public void setList(AccountCreate accountCreate) {
		list.add(accountCreate);
	}
	
	public ArrayList<AccountCreate> toList() {
		return list;
	}

}
