import java.io.*;
import java.util.*;

public class DelovnaProekt {

	public static void main(String[] args) throws FileNotFoundException {
		//Methods.createDatasetFile();
	}
}

class Methods {

	public static void fixFormatMine() throws FileNotFoundException {
		File sourceDir = new File("D:\\=DARKO=\\Fax\\DP\\FinalData");
		File[] data = sourceDir.listFiles(new Filter());

		PrintWriter pw;
		Scanner input;
		String name, line, path = "D:\\=DARKO=\\Fax\\DP\\JustECG_M_30s\\";
		for (File measurement : data) {
			input = new Scanner(new FileInputStream(measurement));
			name = path + measurement.getName().split("_")[0] + ".txt";
			pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(name)));
			int value = 0;
			while (input.hasNextLine()) {
				value++;
				line = input.nextLine();
				if (input.hasNextLine() && value < 3464)
					pw.println(line.split(",")[2]);
				else
					pw.print(line.split(",")[2]);
				if (value == 3464)
					break;
			}
			input.close();
			pw.flush();
			pw.close();
		}
	}

	public static void fixFormatOthers() throws FileNotFoundException {
		File sourceDir = new File("D:\\=DARKO=\\Fax\\DP\\FinalDataNI\\нови_мерења");
		File[] data = sourceDir.listFiles(new Filter());

		PrintWriter pw;
		Scanner input;
		String name, line, path = "D:\\=DARKO=\\Fax\\DP\\folder2\\";
		StringBuilder sb;
		for (File measurement : data) {
			input = new Scanner(new FileInputStream(measurement));
			sb = new StringBuilder();
			int value = 0;
			while (input.hasNextLine()) {
				value++;
				line = input.nextLine();
				if (input.hasNextLine() && value < 3464)
					sb.append(String.format("%s\n", line.split(" ")[0]));
				else
					sb.append(line.split(" ")[0]);
				if (value == 3464)
					break;
			}
			input.close();
			if (value == 3464) {
				name = path + measurement.getName().split("_")[0] + ".txt";
				pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(name)));
				pw.print(sb.toString());
				pw.flush();
				pw.close();
			}
		}
	}

	public static void createDatasetFile() throws FileNotFoundException {
		File sourceDirP = new File("D:\\=DARKO=\\Fax\\DP\\JustECG_30s");
		File[] dataP = sourceDirP.listFiles();

		File sourceDirN = new File("D:\\=DARKO=\\Fax\\DP\\NegativeSamples");
		File[] dataN = sourceDirN.listFiles();
		PrintWriter pw = new PrintWriter(
				new OutputStreamWriter(new FileOutputStream("D:\\=DARKO=\\Fax\\DP\\dataset.csv")));

		StringBuilder sb = new StringBuilder();
		sb.append("ID,A1,B1,B2,B3,B4,B5,C1,ECG\n");
		Scanner input;
		File[] data = dataP;

		for (int i = 0; i < 2; i++) {
			for (File measurement : data) {
				int a1 = 0, b1 = 0, b2 = 0, b3 = 0, b4 = 0, b5 = 0, c1 = 0, id;
				float value, vStart = 0, vEnd = 0;
				boolean first = true;

				input = new Scanner(new FileInputStream(measurement));
				while (input.hasNextLine()) {
					value = Float.parseFloat(input.nextLine());
					if (first) {
						vStart = vEnd = value;
						first = false;
					}

					if (value >= 0 && value < 1.4)
						b1++;
					else if (value >= 1.4 && value < 1.9)
						b2++;
					else if (value >= 1.9 && value < 2.5)
						b3++;
					else if (value >= 2.5 && value < 3.5)
						b4++;
					else if (value >= 3.5 && value <= 5)
						b5++;
					else
						a1++;

					if (value >= vEnd)
						vEnd = value;
					else {
						if (vEnd - vStart >= 0.4)
							c1++;
						vStart = vEnd = value;
					}
				}
				input.close();
				id = Integer.parseInt(measurement.getName().split("\\.")[0]);
				if (id > 100 && id < 400)
					sb.append(String.format("%s,%d,%d,%d,%d,%d,%d,%d,P\n", measurement.getName().split("\\.")[0], a1,
							b1, b2, b3, b4, b5, c1));
				else
					sb.append(String.format("%s,%d,%d,%d,%d,%d,%d,%d,N\n", measurement.getName().split("\\.")[0], a1,
							b1, b2, b3, b4, b5, c1));
			}
			data = dataN;
		}
		pw.print(sb.toString());
		pw.flush();
		pw.close();
	}

	public static void generateNegativeFile() throws FileNotFoundException {
		PrintWriter pw = new PrintWriter(
				new OutputStreamWriter(new FileOutputStream("D:\\=DARKO=\\Fax\\DP\\NegativeSamples\\432.txt")));
		StringBuilder sb = new StringBuilder();

		double number, scalar = -2;
		int sign, function;
		Random random = new Random();
		for (int i = 0; i < 3464; i++) {

			number = Math.sin(scalar);
			/*
			 * function = random.nextInt(3); if (function == 0) number =
			 * Math.sqrt(scalar); else if (function == 1) number =
			 * Math.pow(scalar, 2); else number = Math.log(scalar);
			 */
			scalar += 0.1;
			// number = random.nextInt(1) + random.nextFloat() + 1;
			// number = random.nextFloat() + 1;
			// sign = random.nextInt(2);
			// if (sign == 0)
			// number *= -1;

			if (i != 3463)
				sb.append(String.format("%.3f\n", number));
			else
				sb.append(String.format("%.3f", number));
		}

		pw.print(sb.toString().replace(',', '.'));
		pw.flush();
		pw.close();
	}

}

class Filter implements FileFilter {

	@Override
	public boolean accept(File pathname) {
		int duration = Integer.parseInt(pathname.getName().split("_")[1].substring(0, 2));
		if (duration >= 30)
			return true;
		else
			return false;
	}
}