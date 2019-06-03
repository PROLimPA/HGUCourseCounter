package edu.handong.analysis.datamodel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class Student{
	private String studentId;
	private ArrayList<Course> courseTaken = new ArrayList<Course>();
	private Map<String, Integer> semestersByYearAndSemester;
	//key: Year-Semester	e.g., 2003-1
	
	public Student(String studentId) {
		this.studentId = studentId;
	}
	
	public void addCourse(Course newRecord) {
		courseTaken.add(newRecord);
	}
	
	public ArrayList<Course> getCourse(){
		return courseTaken;
	}
	
	//for "-a 1"
	public Map<String, Integer> getSemestersByYearAndSemester(){
		HashMap<String, Integer> prev = new HashMap<String, Integer>();
		
		int nthSemester = 1;
		for(int i = 0; i < courseTaken.size(); i++) {
			Course course = courseTaken.get(i);
			String yearTaken = Integer.toString(course.getYearTaken());
			String semesterTaken = Integer.toString(course.getSemesterCourseTaken());
			String string = yearTaken + "-" + semesterTaken;
			if(prev.containsKey(string)) continue;
			else {
				prev.put(string, nthSemester);
				nthSemester++;
			}
		}		
		Map<String, Integer> map = new TreeMap<String, Integer>(prev);
		
		return map ;
	}
	//for "-a 1"
	public int getNumCourseInNthSemester(int semester) {
		semestersByYearAndSemester = getSemestersByYearAndSemester();
		int yearTaken = 0;
		int semesterTaken = 0;
		int numCourseInNthSemster = 0;
		
		for(String nthSemester : semestersByYearAndSemester.keySet()) {
			if(semestersByYearAndSemester.get(nthSemester) == semester) {
				yearTaken = Integer.parseInt(nthSemester.split("-")[0]);
				semesterTaken = Integer.parseInt(nthSemester.split("-")[1]);
				break;
			}
		}
		
		for(int i = 0; i < courseTaken.size(); i++) {
			Course reservedCourse = courseTaken.get(i);
			if(yearTaken == reservedCourse.getYearTaken() && semesterTaken == reservedCourse.getSemesterCourseTaken()) {
				numCourseInNthSemster++;
			}
		}
		
		return numCourseInNthSemster;
	}
	//for "-a 1"
	public int getTotalNumberOfSemestersRegistered() {
		semestersByYearAndSemester = getSemestersByYearAndSemester();
		int totalNumberOfSemestersRegistered = 0;
		for(String nthSemester : semestersByYearAndSemester.keySet()) totalNumberOfSemestersRegistered++;
		
		return totalNumberOfSemestersRegistered;
	}

	//for "-a 2" CourseName(succeed)
	public String getCourseName(Map<String, Student> map, String courseCode) {
		for(String string : map.keySet()) {
			Student student = map.get(string);
			for(Course course : student.getCourse()) {
				if(course.getCourseCode().equals(courseCode)) 
					return course.getCourseName();
			}
		}
		return "No such coursecode exist!";
	}
	//for "-a 2" TotalNumberOfStudentsInNthSemester
	public ArrayList<Course> getCoursesInfoInNthSemester(int semester){
		ArrayList<Course> coursesInfoInNthSemester = new ArrayList<Course>();
		
		for(int i = 0; i < courseTaken.size(); i++) {
			int semesterTaken = courseTaken.get(i).getSemesterCourseTaken();
			if(semesterTaken == semester) coursesInfoInNthSemester.add(courseTaken.get(i));
		}
		return coursesInfoInNthSemester;
	}
	//for "-a 2" TotalNumberOfStudentsInNthSemester
	public String getTotalNumberOfStudentsInNthSemester(int semester){
		ArrayList<Course> listOfStudentInfoInNthSemester = getCoursesInfoInNthSemester(semester);
		ArrayList<String> listOfStudentId = new ArrayList<String>();
		int totalNumberOfStudentsRegistered = 0;
		
		for(Course course : listOfStudentInfoInNthSemester) {
			String studentId = course.getStudentId();
			if(listOfStudentId.contains(studentId)) continue;
			else {
				listOfStudentId.add(studentId);
				totalNumberOfStudentsRegistered++;
			}
		}
		return Integer.toString(totalNumberOfStudentsRegistered);
	}
	//for "-a 2" StudentsTakenCourseCode
	public String getStudentsTakenCourseCode(String courseCode, int semester) {
		ArrayList<Course> listOfStudentInfoInNthSemester = getCoursesInfoInNthSemester(semester);
		int studentsTakenCourseCode = 0;
		
		for(Course course : listOfStudentInfoInNthSemester) {
			if(course.getCourseCode().equals(courseCode)) studentsTakenCourseCode++;
		}
		
		return Integer.toString(studentsTakenCourseCode);
	}
}