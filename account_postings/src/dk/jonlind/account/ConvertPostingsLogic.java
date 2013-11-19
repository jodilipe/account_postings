package dk.jonlind.account;

import java.io.*;
import java.util.*;

public class ConvertPostingsLogic {
	private List<Posting> postings = new ArrayList<ConvertPostingsLogic.Posting>();

	public static void main(String[] args) {
		String path = "/Users/jon/workspace_private/account_postings/account_postings/files/";
		new ConvertPostingsLogic().convert(path + "jan-okt.csv", path + "out.csv");
		System.out.println("done");
	}

	private void convert(String inputFile, String outputFile) {
		readPostings(inputFile);
		writePostings(outputFile);
	}

	private void writePostings(String outputFile) {
		try {
			FileWriter fw = new FileWriter(outputFile);
			fw.write(";Dato;");
			for (String indexString : AccountConfig.getIndexes()) {
				fw.write(indexString + ";");
			}
			fw.write("\n");
			for (Posting posting : this.postings) {
				StringBuffer sb = new StringBuffer();
				sb.append(posting.text);
				sb.append(";");
				Calendar calendar = new GregorianCalendar();
				calendar.setTime(posting.getDate());
				sb.append(calendar.get(Calendar.DATE) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.YEAR));
				sb.append(";");
				int idx = AccountConfig.getIndex(posting);
				int i = 0;
				while (i++ < idx) {
					sb.append(";");
				}
				sb.append(Double.toString(posting.amount).replace(".", ","));
				while (i++ < AccountConfig.getIndexes().size()) {
					sb.append(";");
				}
//				System.out.println(sb.toString());
				fw.write(sb.toString() + "\n");
			}
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void readPostings(String inputFile) {
		try {
			FileReader fr = new FileReader(inputFile);
			BufferedReader br = new BufferedReader(fr);
			String line = br.readLine();
			line = br.readLine(); // read past headings
			while (line != null) {
				handlePosting(line);
				line = br.readLine();
			}
			br.close();
			fr.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void handlePosting(String line) {
		StringTokenizer st = new StringTokenizer(line, ";");
		String dateString = st.nextToken().replace("\"", "");
		String text = st.nextToken().replace("\"", "");
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			if (!Character.isDigit(c)) {
				sb.append(c);
			}
		}
		text = sb.toString().replace(",", "").replace("-", "").trim();
//		System.out.println(text);
		String amountString = st.nextToken().replace("\"", "");
		int day = Integer.parseInt(dateString.substring(0, 2));
		int month = Integer.parseInt(dateString.substring(3, 5));
		int year = Integer.parseInt(dateString.substring(6, 10));
		double amount = Double.parseDouble(amountString.replace(".", "").replace(",", "."));
		Posting posting = new Posting(text, new GregorianCalendar(year, month + 1, day).getTime(), amount);
		postings.add(posting);
		AccountConfig.getIndex(posting);
	}

	public class Posting {
		private String text;
		private Date date;
		private double amount;

		public Posting(String text, Date date, double amount) {
			this.text = text;
			this.date = date;
			this.amount = amount;
		}

		public String getText() {
			return text;
		}

		public void setText(String text) {
			this.text = text;
		}

		public Date getDate() {
			return date;
		}

		public void setDate(Date date) {
			this.date = date;
		}

		public double getAmount() {
			return amount;
		}

		public void setAmount(double amount) {
			this.amount = amount;
		}
	}
}
