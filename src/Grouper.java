import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Application that creates groups that can be imported within Brightspace based
 * on desired criteria. This version of the application creates groups that are
 * mixed based on the average labwork grade or steamrolls groups based on
 * student grade, creating equal groups.
 * 
 * Export must be: OrgID, Points Grade, and all relevant graded assignments in a
 * CSV format.
 * 
 * @author David Alderliesten
 * @author Dana van Hassel
 * 
 */
public class Grouper {
	// Variable to determine group size.
	static int groupSize = 6;

	// Variable for determining group 'group' descriptor.
	static String groupDescriptor = "Project";

	// Variables determining read file and write file location.
	static String readFileLocation = "C:\\Users\\David\\Downloads\\testy.csv";
	static String writeFileLocation = "C:\\Users\\David\\Downloads\\result.csv";

	// List of all students for tracking.
	static ArrayList<Student> studentList = new ArrayList<>();

	// List of generated groups.
	static HashMap<Integer, ArrayList<Student>> groupList = new HashMap<>();
	static int numberOfGroups;

	public static void main(String[] args) throws IOException {
		// Get file and load students.
		loadStudents(readFileLocation);

		// Randomize student list (to randomize bottom-up group order), then sort
		// students by grade order, enforced by compareTo.
		Collections.shuffle(studentList);
		Collections.sort(studentList);

		// Initialize the groups in the HashMap.
		initializeGroups();

		// Creates groups of students with the same-ish grade.
		createSameGroups();

		// Creates groups of students where the groups have the same-ish average.
		// createAverageGroups();

		// Write the found groups to a CSV file for importing.
		writeGroups();
	}

	/**
	 * Loads students from the provided CSV file as exported from Brightspace and
	 * generates a list of student objects based on it, which it returns. Also sorts
	 * the students into buckets for later utilization.
	 * 
	 * @param fileLocation the location of the file.
	 * @return a list of students from that file.
	 */
	private static void loadStudents(String fileLocation) {
		// Create a scanner to iterate over file.
		Scanner inputReader;

		// Try file related tasks, catch errors if not found.
		try {
			// Find file and open scanner to read through.
			inputReader = new Scanner(new File(fileLocation));

			// Since we have a CSV, set comma as the delimiter.
			inputReader.useDelimiter(",");

			// Skip the first line of content to avoid the header.
			inputReader.nextLine();

			// For all students, loop to iterate and create objects.
			while (inputReader.hasNextLine()) {
				// Get all elements of the current student.
				String currentLine[] = inputReader.nextLine().split(",");

				// Store the student ID at index 0 (first position).
				int studentID = Integer.valueOf(currentLine[0].replace("#", ""));

				// Iterate through all the grades given and store them in a list.
				ArrayList<Double> gradeList = new ArrayList<Double>();

				// Iterate from index 4 to length - 1 (delimiter '#' symbol not needed).
				for (int i = 1; i < currentLine.length - 1; i++) {
					// If grade exists, and not empty CSV position, add value.
					if (!currentLine[i].equals("")) {
						gradeList.add(Double.valueOf(currentLine[i]));
					}
				}

				// Create current student for adding and sorting into buckets.
				Student toAdd = new Student(studentID, gradeList);

				// If the student has a grade, meaning they are active in the course.
				if (toAdd.isActive) {
					// Place student into required bucket based on grade.
					studentList.add(toAdd);
				}
			}

		} catch (FileNotFoundException e) {
			// Catch errors and inform user.
			System.out.println("File wasn't found, terminating.");
			System.exit(1);
		}
	}

	/**
	 * Initializes the groups within the hashmap, ensuring that an arraylist for
	 * students exist at each index.
	 */
	private static void initializeGroups() {
		// Determine the number of groups to be created.
		numberOfGroups = (studentList.size() / groupSize) + 1;

		// Iterate through all group indexes in the HashMap to instantiate arraylists.
		for (int i = 0; i < numberOfGroups; i++) {
			ArrayList<Student> currentGroup = new ArrayList<>();
			groupList.put(i, currentGroup);
		}
	}

	/**
	 * Creates groups where all group members have the same score or a score in the
	 * ballpark (such a full group of students with an average of 0, average of 1,
	 * etc).
	 */
	@SuppressWarnings("unused")
	private static void createSameGroups() {
		// Create a variable for tracking the current group size and value.
		int currentGroupSize = 0;
		int groupTracker = 0;

		// Iterate over the sorted student list, and fill groups until no space is
		// available.
		for (int i = 0; i < studentList.size(); i++) {
			// Get current student to add to the group.
			Student currStudent = studentList.get(i);

			// Add current student to the group.
			ArrayList<Student> currentGroup = groupList.get(groupTracker);
			currentGroup.add(currStudent);

			// Update group size since a student has been added.
			currentGroupSize += 1;

			// Place the updated student group list in the HashMap.
			groupList.put(groupTracker, currentGroup);

			// Check if the current group isn't full, if it is, update group value and reset
			// size.
			if (currentGroup.size() >= groupSize) {
				currentGroupSize = 0;
				groupTracker += 1;
			}
		}
	}

	/**
	 * Creates groups that have averaged scores, meaning each group's average score
	 * should be relatively in the same ballpark.
	 */
	@SuppressWarnings("unused")
	private static void createAverageGroups() {
		// Iterate through all students and label students based on their modulo index,
		// and assign them to that group.
		for (int i = 0; i < studentList.size(); i++) {
			// Get current student and group index.
			Student currStudent = studentList.get(i);
			int groupIndex = i % numberOfGroups;

			// Fetch current group list and add student to that group.
			ArrayList<Student> currentGroup = groupList.get(groupIndex);
			currentGroup.add(currStudent);

			// Place the updated student group list in the HashMap.
			groupList.put(groupIndex, currentGroup);
		}
	}

	/**
	 * Writes groups that have been determined to a file for importing within
	 * Brightspace using a CSV format of studentnumber, group number.
	 * 
	 * @throws IOException
	 */
	private static void writeGroups() throws IOException {
		// Create output file object and indicated location.
		File toWrite = new File("C:\\Users\\David\\Downloads\\result.csv");

		// Create the file if it doesn't exist, and print information to console.
		if (toWrite.createNewFile()) {
			System.out.println("Output file was created.");
		} else {
			System.out.println("Output file exists, overwriting old content.");
		}

		// Create string for output result.
		String result = "OrgDefinedId,Username,LastName,FirstName,Email,GroupCategory,GroupName\n";

		// Iterate through all groups for writing.
		for (int i = 0; i < numberOfGroups; i++) {
			// Get current group for operations and writing.
			ArrayList<Student> currGroup = groupList.get(i);

			// Print result of group and students.
			System.out.println("Group " + (i + 1) + " with " + currGroup.size() + " students:");

			// Iterate over students to write them to the output file and provide debug
			// information.
			for (Student currStudent : currGroup) {
				// Print student details as debug information.
				System.out.println("	Student: " + currStudent.studentID + ", Grade: " + currStudent.averageGrade);

				// Write student to file in order specified with comma's for required unused
				// columns.
				result = result + "#" + currStudent.studentID + ",,,,," + groupDescriptor + "," + (i + 1) + "\n";
			}
		}

		// Create a file writer to write the content to the output CSV.
		FileWriter toFile = new FileWriter(toWrite);

		// Writing the result string to the file as final output.
		toFile.write(result);

		// Close the file writer to prevent resource leaks.
		toFile.close();
	}
}
