package dk.jonlind.account;

import java.util.*;

import dk.jonlind.account.ConvertPostingsLogic.*;

public class AccountConfig {
	private static Map<String, List<String>> accountConfigs = new HashMap<String, List<String>>();
	private static List<String> indexes = new ArrayList<String>();

	static {
		addAccountConfig("Mad", "irma", "brugsen", "superbest", "spar", "focus", "emmerys", "bager");;
	}
	
	public static List<String> getIndexes() {
		return indexes;
	}

	private static void addAccountConfig(String category, String...patterns) {
		accountConfigs.put(category, new ArrayList<String>());
		for (int i = 0; i < patterns.length; i++) {
			accountConfigs.get(category).add(patterns[i]);
		}
	}

	public static int getIndex(Posting posting) {
		if (!isPostingRegistered(posting)) {
			registerPosting(posting);
		}
		if (isAccountRegistered(posting)) {
			return indexes.indexOf(getAccountName(posting));
		}
		return indexes.indexOf(posting.getText());
	}
	
	
	private static void registerPosting(Posting posting) {
		if (isAccountRegistered(posting)) {
			indexes.add(getAccountName(posting));
		} else {
			indexes.add(posting.getText());
		}
	}
	
	private static boolean isPostingRegistered(Posting posting) {
		return indexes.contains(getAccountName(posting)) || indexes.contains(posting.getText());
	}
	
	private static boolean isAccountRegistered(Posting posting) {
		return getAccountName(posting) != null;
	}
	
	private static String getAccountName(Posting posting) {
		for (String account : accountConfigs.keySet()) {
			for (String pattern : accountConfigs.get(account)) {
				if (posting.getText().toLowerCase().contains(pattern.toLowerCase())) {
					return account;
				}
			}
		}
		return null;
	}
}
