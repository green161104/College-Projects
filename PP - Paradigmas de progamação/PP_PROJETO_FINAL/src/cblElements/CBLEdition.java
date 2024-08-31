/**
 * Nome: Sónia Raquel Degtyarëva de Oliveira
 * Número: 8220114
 * Turma: Turma 4
 * <p>
 * Nome: João Pedro da Silva Santos
 * Número: 8220256
 * Turma: Turma 1
 * <p>
 * This class is the implementation of the Edition interface, as per specified in the resources provided to us.
 * It details and implements the methods defined in the interface, which pertain to actions related to
 * managing one instance of a CBL edition.
 */
package cblElements;

import cblElements.exceptions.IllegalProjectException;
import cblElements.exceptions.NoProjectsException;
import ma02_resources.participants.Participant;
import ma02_resources.project.Edition;
import ma02_resources.project.Project;
import ma02_resources.project.Status;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Scanner;

public class CBLEdition implements Edition {

    private final String name;
    private final LocalDate start;
    private final String projectTemplate;
    private Status status;
    private MyProject[] projects;
    private LocalDate end;
    private int numberOfProjects;

    /**
     * Constructor for the CBL edition
     *
     * @param name            Name for the edition
     * @param start           Starting date for the edition
     * @param projectTemplate FilePath for the project structure template
     * @param end             End date for the edition
     */
    public CBLEdition(String name, LocalDate start, String projectTemplate, LocalDate end) {
        this.name = name;
        this.start = start;
        this.projectTemplate = projectTemplate;

        this.status = Status.INACTIVE;
        this.end = end;
        this.numberOfProjects = 0;
        int NUMBER_PROJECTS = 10;
        this.projects = new MyProject[NUMBER_PROJECTS];
    }

    /**
     * Getter for the name of the edition
     *
     * @return returns the edition's name
     */
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * Getter for the start of the edition
     *
     * @return returns the starting date for the edition
     */
    @Override
    public LocalDate getStart() {
        return this.start;
    }

    /**
     * Getter for the project template filepath
     *
     * @return returns the filepath for the project template
     */
    @Override
    public String getProjectTemplate() {
        return this.projectTemplate;
    }

    /**
     * Getter for the status of the edition
     *
     * @return returns the status of the edition
     */
    @Override
    public Status getStatus() {
        return this.status;
    }

    /**
     * Setter for the status of the edition
     *
     * @param status desired status for the edition
     */
    @Override
    public void setStatus(Status status) {
        this.status = status;
    }

    /**
     * Receives the information required to create a project, parses and reads
     * the json file containing the project template and declares the structure
     * needed to create a project
     *
     * @param name        chosen name for the project
     * @param description description to be given to the project
     * @param tags        tags to be associated with the project
     * @throws IOException              if there's an issue reading the file
     * @throws java.text.ParseException if there's an issue parsing the file
     */
    @Override
    public void addProject(String name, String description, String[] tags) throws IOException, java.text.ParseException {

        try (FileReader fileReader = new FileReader(this.projectTemplate); BufferedReader bufferedReader = new BufferedReader(fileReader)) {

            StringBuilder jsonBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                jsonBuilder.append(line);
            }

            String json = jsonBuilder.toString();
            JSONObject projectJson = (JSONObject) new JSONParser().parse(json);

            // Extract the necessary information from the JSON object
            int numberOfFacilitators = ((Long) projectJson.get("number_of_facilitors")).intValue();
            int numberOfStudents = ((Long) projectJson.get("number_of_students")).intValue();
            int numberOfPartners = ((Long) projectJson.get("number_of_partners")).intValue();
            int numberOfParticipants = numberOfStudents + numberOfFacilitators + numberOfPartners;
            JSONArray tasksArray = (JSONArray) projectJson.get("tasks");
            int numberOfTasks = tasksArray.size();
            //calls the constructor from the MyProject class to create a project with the desired characteristics, as well as providing the structure
            MyProject project = new MyProject(name, description, tags, numberOfTasks, numberOfParticipants, numberOfStudents, numberOfFacilitators, numberOfPartners);
            this.projects[this.numberOfProjects] = project;
            this.numberOfProjects++;

        } catch (ParseException | IOException ex) {
            System.out.println("Error parsing the template : " + ex.getMessage());
        }
    }

    /**
     * Asks the user to input information about the project they want to create,
     * and afterwards calls the method "addProject", passing it the user's
     * preferences.
     */
    public void createProject() {

        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter project name:");
        String name = scanner.nextLine();

        System.out.println("Enter project description:");
        String description = scanner.nextLine();

        System.out.println("Enter project tags (separated by comma):");
        String tagsInput = scanner.nextLine();
        String[] tags = tagsInput.split(",");

        try {
            addProject(name, description, tags);
            System.out.println("Project created successfully!");
        } catch (IOException | java.text.ParseException ex) {
            System.out.println("Error creating project: " + ex.getMessage());
        }
    }

    /**
     * Iterates the project array in search of the desired project, removing it
     * if found.
     *
     * @param name name of the project to be removed
     */
    @Override
    public void removeProject(String name) {
        int projectIndex = findProject(name);
        try {
            if (projectIndex != -1) {
                for (int i = projectIndex; i < numberOfProjects - 1; i++) {
                    projects[i] = projects[i + 1];
                }
                projects[numberOfProjects - 1] = null;
                numberOfProjects--;
            } else {
                throw new IllegalProjectException("Project doesn't exist");
            }
        } catch (IllegalProjectException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Iterates the project array in search of a certain project, returning its
     * index if found, -1 otherwise
     *
     * @param name name of the project being searched
     * @return the project's index if found, -1 if not
     */
    public int findProject(String name) {
        for (int i = 0; i < this.numberOfProjects; i++) {
            if (this.projects[i].getName().equals(name)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * @param name name of the project being searched
     * @return returns the requested project, or null if it's not found.
     */
    @Override
    public Project getProject(String name) {
        try {
            if (this.projects.length == 0) {
                throw new NoProjectsException("No projects found.");
            }
            for (int i = 0; i < this.numberOfProjects; i++) {
                if (this.projects[i].getName().equals(name)) {
                    return projects[i];
                }
            }

            // If no matching project is found
            throw new IllegalProjectException("Project not found.");
        } catch (NoProjectsException | IllegalProjectException e) {
            System.out.println(e.getMessage());
            return null;
        }

    }

    /**
     * Getter for the project array
     *
     * @return returns this edition's array of projects
     */
    @Override
    public Project[] getProjects() {
        return this.projects;
    }

    /**
     * Searches the array for projects tagged with a specific tag, and returns
     * an array of all the projects tagged with said tag
     *
     * @param tag the tag requested
     * @return an array of all the projects tagged with that tag
     */
    @Override
    public Project[] getProjectsByTag(String tag) {
        Project[] projectsWithTag = new Project[this.numberOfProjects];
        int count = 0;

        for (int i = 0; i < this.numberOfProjects; i++) {
            String[] tags = this.projects[i].getTags();

            for (String projectTag : tags) {
                if (projectTag != null && projectTag.equals(tag)) {
                    projectsWithTag[count] = this.projects[i];
                    count++;
                    break;
                }
            }
        }

        Project[] trimmedProjects = new Project[count];
        System.arraycopy(projectsWithTag, 0, trimmedProjects, 0, count);

        return trimmedProjects;
    }

    /**
     * Searches the array looking for projects associated with a certain
     * participant and returns a trimmed array containing that participant's
     * projects
     *
     * @param email the email belonging to the participant
     * @return an array of projects the participant is involved in
     */
    @Override
    public Project[] getProjectsOf(String email) {
        Project[] projectsWithParticipant = new Project[this.numberOfProjects];
        int count = 0;

        for (int i = 0; i < this.numberOfProjects; i++) {
            Participant[] participants = this.projects[i].getParticipants();

            for (Participant participant : participants) {
                if (participant != null && participant.getEmail().equals(email)) {
                    projectsWithParticipant[count] = this.projects[i];
                    count++;
                    break;
                }
            }
        }

        Project[] trimmedProjects = new Project[count];
        System.arraycopy(projectsWithParticipant, 0, trimmedProjects, 0, count);

        return trimmedProjects;
    }

    /**
     * Getter for the number of projects in the edition
     *
     * @return this edition's number of projects
     */
    @Override
    public int getNumberOfProjects() {
        return this.numberOfProjects;
    }

    /**
     * Looks for the task within the project with the latest end date
     *
     * @return the end date of the latest task within the projects
     */
    @Override
    public LocalDate getEnd() {
        LocalDate latest = null;
        try {
            if (this.numberOfProjects == 0) {
                throw new NoProjectsException("Projects not found.");
            }
            for (int i = 0; i < this.numberOfProjects; i++) {
                for (int j = 0; j < this.projects[i].getTasks().length; j++) {
                    LocalDate nextEnd = this.projects[i].getTasks()[j].getEnd();
                    if (latest == null || nextEnd.isAfter(latest)) {
                        latest = nextEnd;
                    }
                }
            }
        } catch (NoProjectsException e) {
            System.out.println(e.getMessage());
        }
        this.end = latest;
        return latest;  // Return the latest end date
    }


    /**
     * Determines whether two editions are the same by comparing their names
     *
     * @param obj an object to be compared
     * @return true if they're the same, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CBLEdition other = (CBLEdition) obj;
        return Objects.equals(this.name, other.name);
    }

    /**
     * This method generates a string detailing a progress report of the edition,
     * taking into account the completed projects, which are based on the submissions
     * made.
     *
     * @return a string detailing the progress of the edition
     */
    public String editionProgress() {
        String s = "";
        int counter = 0;
        for (int i = 0; i < this.numberOfProjects; i++) {
            if (this.projects[i].isCompleted()) {
                counter++;
            }
        }
        s += "Edition progress report \n";
        s += "Edition: " + this.name + "\n";
        s += "Started on: " + this.start.toString() + "\n";
        s += "Ends on: " + this.end.toString() + "\n";
        s += "Projects finished: " + counter + " out of " + this.numberOfProjects;
        return s;
    }

    /**
     * Print summary of all th projects
     * @return a string containing all the content pertaining to edition projects
     */
    public String projectsSummary() {
        String s = "";
        for (int i = 0; i < this.numberOfProjects; i++) {
            s += this.projects[i].toString() + "\n";
            s += "--------------------";
        }
        return s;
    }


}
