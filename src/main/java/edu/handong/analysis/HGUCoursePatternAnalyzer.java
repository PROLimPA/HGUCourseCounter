package edu.handong.analysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import edu.handong.analysis.datamodel.Course;
import edu.handong.analysis.datamodel.Student;
import edu.handong.analysis.utils.NotEnoughArgumentException;
import edu.handong.analysis.utils.Utils;

public class HGUCoursePatternAnalyzer {

	private HashMap<String,Student> students;
	
	/**
	 * This method runs our analysis logic to save the number courses taken by each student per semester in a result file.
	 * Run method must not be changed!!
	 * @param args
	 */
	public void run(String[] args) {
		
		try {
			// when there are not enough arguments from CLI, it throws the NotEnoughArgmentException which must be defined by you.
			if(args.length<2)
				throw new NotEnoughArgumentException();
		} catch (NotEnoughArgumentException e) {
			System.out.println(e.getMessage());
			System.exit(0);
		}
		
		String dataPath = args[0]; // csv file to be analyzed
		String resultPath = args[1]; // the file path where the results are saved.
		ArrayList<String> lines = Utils.getLines(dataPath, true);
		
		students = loadStudentCourseRecords(lines);
		
		// To sort HashMap entries by key values so that we can save the results by student ids in ascending order.
		Map<String, Student> sortedStudents = new TreeMap<String,Student>(students); 

		// Generate result lines to be saved.
		ArrayList<String> linesToBeSaved = countNumberOfCoursesTakenInEachSemester(sortedStudents);
		
		// Write a file (named like the value of resultPath) with linesTobeSaved.
		Utils.writeAFile(linesToBeSaved, resultPath);
	}
	
	/**
	 * This method create HashMap<String,Student> from the data csv file. Key is a student id and the corresponding object is an instance of Student.
	 * The Student instance have all the Course instances taken by the student.
	 * @param lines
	 * @return
	 */
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
	
	/**
	 * This method generate the number of courses taken by a student in each semester. The result file look like this:
	 * StudentID, TotalNumberOfSemestersRegistered, Semester, NumCoursesTakenInTheSemester
	 * 0001,14,1,9
     * 0001,14,2,8
	 * ....
	 * 
	 * 0001,14,1,9 => this means, 0001 student registered 14 semesters in total. In the first semester (1), the student took 9 courses.
	 * 
	 * 
	 * @param sortedStudents
	 * @return
	 */
	private ArrayList<String> countNumberOfCoursesTakenInEachSemester(Map<String, Student> sortedStudents) {
		ArrayList<String> NumberOfCoursesTakenInEachSemester = new ArrayList<String>();
		
		String studentId = "studentID";
		String totalNumberOfSemestersRegistered = "TotalNumberOfSemestersRegistered";
		String semester = "Semester";
		String numCoursesTakenInTheSemester = "NumCoursesTakenInTheSemester";
		NumberOfCoursesTakenInEachSemester.add(studentId + "," + totalNumberOfSemestersRegistered + "," +
				semester + "," + numCoursesTakenInTheSemester);
		
		for(String keyString : sortedStudents.keySet()) {
			studentId = keyString;
			Student studentInfo = sortedStudents.get(keyString);
			ArrayList<Course> courseInfo = studentInfo.getCourse();
			int iterationTimes = 1;
			int numSemester = 1;
			
			for(Course reserved : courseInfo) {
				if(iterationTimes == studentInfo.getNumCourseInNthSemester(numSemester)) {
				totalNumberOfSemestersRegistered = Integer.toString(studentInfo.getTotalNumberOfSemestersRegistered());
				semester = Integer.toString(numSemester);
				numCoursesTakenInTheSemester = Integer.toString(studentInfo.getNumCourseInNthSemester(numSemester));
				
				NumberOfCoursesTakenInEachSemester.add(studentId + "," + totalNumberOfSemestersRegistered +
						"," + semester + "," + numCoursesTakenInTheSemester);
				
				numSemester++;
				iterationTimes = 1;
				}
				iterationTimes++;
			}
			
		}
		
		return NumberOfCoursesTakenInEachSemester;
	}
}