import java.util.List;

/**
 * Class to store students to improve code readability. Responsible for mostly
 * data storage and within student calculations.
 * 
 * @author David Alderliesten
 *
 */
public class Student implements Comparable<Student> {
	// Variables to store relevant student information and grade.
	int studentID;
	double averageGrade;

	// Variable to store participation status.
	boolean isActive;

	/**
	 * Constructor for the student object, which generates the object and calculates
	 * relevant aspects required for the tool. Also sets passed arguments as local
	 * variables.
	 * 
	 * @param studentID        the identification parameter for the student.
	 * @param grades           the list of grades for the student.
	 * @param handinOnly       determines if the average is calculated with all
	 *                         grades or handed-in grades only.
	 * @param totalAssignments number of assignments total the course has, only used
	 *                         iff handinOnly false.
	 */
	public Student(int studentID, List<Double> grades, boolean handinOnly, int totalAssignments) {
		this.studentID = studentID;

		// Set activity to be false, meaning student isn't active.
		this.isActive = false;

		for (Double currentGrade : grades) {
			this.averageGrade += currentGrade;

			// Since (at least) a grade has been found, student is active in the course.
			this.isActive = true;
		}

		// Divide either by all assignments or by handed-in assignments to calculate
		// average.
		if (handinOnly) {
			this.averageGrade = this.averageGrade / grades.size();
		} else {
			this.averageGrade = this.averageGrade / totalAssignments;
		}
	}

	/**
	 * Returns a string representation of the Student object.
	 * 
	 * @return a string with information.
	 */
	@Override
	public String toString() {
		return "Student: " + this.studentID + ", with an average grade of: " + this.averageGrade;
	}

	/**
	 * Forces comparison on average grades, in ascending order, meaning lowest grade
	 * first and highest last.
	 */
	@Override
	public int compareTo(Student other) {
		return Double.compare(this.averageGrade, other.averageGrade);
	}
}
