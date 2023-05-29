package gitp.gitcustom;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;
import org.springframework.shell.standard.ShellComponent;

@ShellComponent
public class GitCustomApplication {

	private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	private static ProcessBuilder pb = null;
	private static Process process = null;

	private static String beforeName;
	private static String afterName;
	private static String branchName;
	private static LinkedHashMap<String, String> filePathAndMessages = new LinkedHashMap<>();
	private static PriorityQueue<DateAndFilePath> filePathAndDates = new PriorityQueue<>();

	private static class DateAndFilePath implements Comparable<DateAndFilePath>{
		private int date;
		private String filePath;

		public DateAndFilePath(int priorityRank, String path) {
			date = priorityRank;
			filePath = path;
		}

		@Override
		public int compareTo(DateAndFilePath other) {
			return Integer.compare(this.date, other.date);
		}
	}

	public static void main(String[] args) throws IOException {
		System.out.println(" = ");
		while(true){
			filePathAndMessages.clear();
			filePathAndDates.clear();

			if(branchName == null){
				System.out.print("Enter your branchName or EXIT : ");
				branchName = br.readLine();
			}

			if(branchName.toLowerCase().replaceAll(" ", "").equals("exit"))
				break;

			try{

				System.out.print("Enter original file name : ");
				beforeName = br.readLine();
				System.out.print("Enter new file name : ");
				afterName = br.readLine();

				setFilePathWithMessagesAndDate(".");

				renameFiles(new File("."));
				stageAndCommit();

				new ProcessBuilder("git", "push", "origin", branchName)
						.inheritIO()
						.directory(new File("."))
						.start()
						.waitFor();


			} catch(Exception e){
				System.out.println("[Error] try again.");
				e.printStackTrace();
			}
		}

	}

	private static void setFilePathWithMessagesAndDate(String fileName) throws Exception {
		File file = new File(fileName);

		setCommittedMessage(file);
		setCommittedDate(file);

		// nested
		if(file.isDirectory()) {
			Arrays.stream(file.listFiles())
					.filter(childFile -> !childFile.getName().equals(".git"))
					.forEach(childFileName -> {
						try {
							setFilePathWithMessagesAndDate(childFileName.toString());
						} catch (Exception e) {
							throw new RuntimeException(e);
						}
					});
		}
	}

	private static void setCommittedMessage(File file) throws IOException, InterruptedException {
		pb = new ProcessBuilder("git", "log", "-1", "--pretty=format:%s", "\"" + file.getPath() + "\"")
				.directory(new File("."));

		System.out.println("<< " + String.join(" ", pb.command()));

		process = pb.start();
		List<String> logOutput = IOUtils.readLines(process.getInputStream(), StandardCharsets.UTF_8);
		process.waitFor();

		System.out.println(">> " + logOutput);
		if (!logOutput.isEmpty()) {
			String commitMsg = logOutput.get(0).trim();
			filePathAndMessages.put(file.getPath(), commitMsg);
		}
	}

	private static void setCommittedDate(File file) throws IOException, InterruptedException {
		pb = new ProcessBuilder("git", "log", "-1", "--pretty=format:%ad", "\"" + file.getPath() + "\"")
				.directory(new File("."));

		System.out.println("<< " + String.join(" ", pb.command()));

		process = pb.start();
		String dateOutput = IOUtils.readLines(process.getInputStream(), StandardCharsets.UTF_8)
				.stream()
				.collect(Collectors.joining(System.lineSeparator()));
		process.waitFor();

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss yyyy ZZZZ");
		LocalDateTime dateTime = LocalDateTime.parse(dateOutput, formatter);

		DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
		int priorityRank = Integer.parseInt(dateTime.format(outputFormatter));

		System.out.println("priorityRank = " + priorityRank);
		filePathAndDates.add(new DateAndFilePath(priorityRank, file.getPath()));
	}

	private static void renameFiles(File file) throws Exception {
		if(file.getName().contains(".idea"))
			return;

		file.renameTo(new File(file.getPath().replaceAll(beforeName, afterName)));

		if(file.isDirectory() && file.listFiles().length > 0) {
			for (File childFile : file.listFiles()) {
				renameFiles(childFile);
			}
		}
	}

	private static void stageAndCommit() throws Exception {
		while(!filePathAndDates.isEmpty()){
			File file = new File(filePathAndDates.poll().filePath);
			String committedMsg = filePathAndMessages.get(file.getPath().replaceAll(afterName,beforeName));

			ProcessBuilder pb1 = new ProcessBuilder("git", "add", file.getPath())
					.directory(new File("."))
					.inheritIO();
			System.out.println(">> " + pb1.command());
			pb1.start().waitFor();


			ProcessBuilder pb2 = new ProcessBuilder("git", "commit", "-m",
					filePathAndMessages.get(file.getPath().replaceAll(afterName, beforeName)))
					.directory(new File("."))
					.inheritIO();
			System.out.println(">> " + pb2.command());
			pb2.start().waitFor();
		}
	}
}