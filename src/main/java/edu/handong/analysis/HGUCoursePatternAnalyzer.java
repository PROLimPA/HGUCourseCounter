package edu.handong.analysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

import edu.handong.analysis.datamodel.Course;
import edu.handong.analysis.datamodel.Student;
import edu.handong.analysis.utils.NotEnoughArgumentException;
import edu.handong.analysis.utils.Utils;

public class HGUCoursePatternAnalyzer {

	private HashMap<String,Student> students;
	String dataPath;
	String resultPath;
	String analysisType;
	String courseCode;
	String startYear;
	String endYear;
	boolean help;
	
	public void run(String[] args) {
		
		try {
			// when there are not enough arguments from CLI, it throws the NotEnoughArgmentException which must be defined by you.
			if(args.length<2)
				throw new NotEnoughArgumentException();
		} catch (NotEnoughArgumentException e) {
			System.out.println(e.getMessage());
			System.exit(0);
		}
		
		Options options = createOptions();
		if(parseOptions(options, args)){
			if (help){
				printHelp(options);
				return;
			}

			ArrayList<String> lines = Utils.getLines(dataPath, true);
			
			if(analysisType.equals("1")) {
				students = loadStudentCourseRecords(lines);
				Map<String, Student> sortedStudents = new TreeMap<String,Student>(students); 
				ArrayList<String> linesToBeSaved = countNumberOfCoursesTakenInEachSemester(sortedStudents);			
				Utils.writeAFile(linesToBeSaved, resultPath);
			}
			else if(analysisType.equals("2") && courseCode != "") {
				students = loadCourseYearTakenRecords(lines);
				Map<String, Student> sortedCourseCode = new TreeMap<String, Student>(students);
				ArrayList<String> linesToBeSaved = countNumberOfStudentsTakenInSemester(sortedCourseCode);
				Utils.writeAFile(linesToBeSaved, resultPath);
			}
			
		}
	}
	
	private HashMap<String,Student> loadStudentCourseRecords(ArrayList<String> lines) {
		HashMap<String, Student> studentCourseRecords = new HashMap<String, Student>(); 
		
		for(String line: lines) {
			Course newCourse = new Course(line);
			String studentId = newCourse.getStudentId();
			
			if(studentCourseRecords.containsKey(studentId)) {
				studentCourseRecords.get(studentId).addCourse(newCourse);
			} else {
				Student newStudent = new Student(studentId);
				newStudent.addCourse(newCourse);
				studentCourseRecords.put(studentId, newStudent);
			}
		}
	
		return studentCourseRecords;
	}
	
	private HashMap<String,Student> loadCourseYearTakenRecords(ArrayList<String> lines) {
		HashMap<String, Student> courseYearTakenRecords = new HashMap<String, Student>(); 
		
		for(String line: lines) {
			Course newCourse = new Course(line);
			String yearTaken = Integer.toString(newCourse.getYearTaken());
			
			if(courseYearTakenRecords.containsKey(yearTaken)) {
				courseYearTakenRecords.get(yearTaken).addCourse(newCourse);
			} else {
				Student newStudent = new Student(yearTaken);
				newStudent.addCourse(newCourse);
				courseYearTakenRecords.put(yearTaken, newStudent);
			}
		}
	
		return courseYearTakenRecords;
	}
	
	private ArrayList<String> countNumberOfCoursesTakenInEachSemester(Map<String, Student> sortedStudents) {
		ArrayList<String> NumberOfCoursesTakenInEachSemester = new ArrayList<String>();
		
		String studentId = "studentID";
		String totalNumberOfSemestersRegistered = "TotalNumberOfSemestersRegistered";
		String semester = "Semester";
		String numCoursesTakenInTheSemester = "NumCoursesTakenInTheSemester";
		NumberOfCoursesTakenInEachSemester.add(studentId + "," + totalNumberOfSemestersRegistered + "," +
				semester + "," + numCoursesTakenInTheSemester);
		
		for(String keyString : sortedStudents.keySet()) {
			Student studentInfo = sortedStudents.get(keyString);
			studentId = keyString;
			totalNumberOfSemestersRegistered = Integer.toString(studentInfo.getTotalNumberOfSemestersRegistered());
			
			for(String string : studentInfo.getSemestersByYearAndSemester().keySet()) {
				Integer yearTaken = Integer.parseInt(string.split("-")[0]);
				Integer semesterTaken = Integer.parseInt(string.split("-")[1]);
				semester = Integer.toString(studentInfo.getSemestersByYearAndSemester().get(string));
				if(yearTaken >= Integer.parseInt(startYear) && yearTaken <= Integer.parseInt(endYear)) {
					int numCoursesTaken = 0;
					for(Course course : studentInfo.getCourse()) {
						if(course.getYearTaken() == yearTaken && course.getSemesterCourseTaken() == semesterTaken) numCoursesTaken++;
					}
					numCoursesTakenInTheSemester = Integer.toString(numCoursesTaken);
					
					NumberOfCoursesTakenInEachSemester.add(studentId + "," + totalNumberOfSemestersRegistered + "," +
							semester + "," + numCoursesTakenInTheSemester);					
				}
			}
		}
		return NumberOfCoursesTakenInEachSemester;
	}
	
	private ArrayList<String> countNumberOfStudentsTakenInSemester(Map<String, Student> sortedCourseCode){
		ArrayList<String> numberOfStudentsTakenInCouresCode = new ArrayList<String>();
		
		String year = "Year";
		String semester = "Semester";
		String courseCode = "CourseCode";
		String courseName = "CourseName";
		String totalStudents = "TotalStudents";
		String studentsTaken = "Taken";
		String rate = "Rate";
		numberOfStudentsTakenInCouresCode.add(year +","+ semester +","+ courseCode +","+ courseName +","+ totalStudents +","+
												studentsTaken +"," + rate);
		
		courseCode = this.courseCode;
		for(String keyString : sortedCourseCode.keySet()) {
			Student studentInfo = sortedCourseCode.get(keyString);
			year = keyString;
			courseName = studentInfo.getCourseName(sortedCourseCode, courseCode);
			
			Integer keyYear = Integer.parseInt(keyString);			
			if(keyYear >= Integer.parseInt(startYear) && keyYear <= Integer.parseInt(endYear)) {
				for(int i = 1; i <= 4; i++) {
					semester = Integer.toString(i);
					totalStudents = studentInfo.getTotalNumberOfStudentsInNthSemester(i);
					studentsTaken = studentInfo.getStudentsTakenCourseCode(courseCode, i);
					
					if(Integer.parseInt(studentsTaken) <= 0) rate = "0.0%";
					else {
						double division = (double)Integer.parseInt(studentsTaken)/Integer.parseInt(totalStudents) * 100;
						rate = String.valueOf(division) +"%";
					}
					numberOfStudentsTakenInCouresCode.add(year +","+ semester +","+ courseCode +","+ courseName +","+ totalStudents +","+
							studentsTaken +"," + rate);
				}
			}
			else continue;
		}
		return numberOfStudentsTakenInCouresCode;
	}
	
	
	private boolean parseOptions(Options options, String[] args) {
		CommandLineParser parser = new DefaultParser();

		try {
			CommandLine cmd = parser.parse(options, args);

			dataPath = cmd.getOptionValue("i");
			resultPath = cmd.getOptionValue("o");
			analysisType = cmd.getOptionValue("a");	
			
			if(analysisType.equals("2"))
				courseCode = cmd.getOptionValue("c");
			else
				courseCode = "";
			startYear = cmd.getOptionValue("s");
			endYear = cmd.getOptionValue("e");
			help = cmd.hasOption("f");
			

		} catch (Exception e) {
			printHelp(options);
			return false;
		}

		return true;
	}
	
	private Options createOptions() {
		Options options = new Options();
		
		options.addOption(Option.builder("i").longOpt("input")
				.desc("Set an input file path")
				.hasArg()
				.argName("Input path")
				.required()
				.build());
		
		options.addOption(Option.builder("o").longOpt("output")
				.desc("Set an output file path")
				.hasArg()
				.argName("Output path")
				.required()
				.build());
		
		options.addOption(Option.builder("a").longOpt("analysis")
				.desc("1: Count courses per semester, 2: Count per course name and year")
				.hasArg()
				.argName("Analysis option")
				.required()
				.build());
		
		options.addOption(Option.builder("c").longOpt("coursecode")
				.desc("Course code for '-a 2' option")
				.hasArg()
				.argName("course code")
				.build());
		
		options.addOption(Option.builder("s").longOpt("startyear")
				.desc("Set the start year for analysis e.g., -s 2002")
				.hasArg()
				.argName("Start year for analysis")
				.required()
				.build());
		
		options.addOption(Option.builder("e").longOpt("endyear")
				.desc("Set the end year for analysis e.g., -e 2005")
				.hasArg()
				.argName("End year for analysis")
				.required()
				.build());
		
		options.addOption(Option.builder("h").longOpt("help")
				.desc("Show a Help page")
				.argName("Help")
				.build());
		
		return options;
	}
	
	private void printHelp(Options options) {
		HelpFormatter formatter = new HelpFormatter();
		String header = "HGU Course Analyzer";
		String footer ="";
		formatter.printHelp("HGUCourseCounter", header, options, footer, true);
	}
}