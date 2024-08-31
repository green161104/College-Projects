/**
 * Nome: Sónia Raquel Degtyarëva de Oliveira
 * Número: 8220114
 * Turma: Turma 4
 * <p>
 * Nome: João Pedro da Silva Santos
 * Número: 8220256
 * Turma: Turma 1
 * <p>
 * This class pertains to the handling of user input and the creation of objects based on it.
 */
package userInteraction;

import cblElements.CBLEdition;
import cblElements.MySubmission;
import cblElements.MyTask;
import cblElements.exceptions.IllegalSubmissionException;
import cblParticipants.*;
import ma02_resources.participants.InstituitionType;
import ma02_resources.participants.Participant;
import ma02_resources.participants.Student;
import ma02_resources.project.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class UserInputHandler {

    private final Scanner scanner;

    /**
     * The constructor for this class, which opens a scanner to receive input
     */
    public UserInputHandler() {
        scanner = new Scanner(System.in);
    }

    /**
     * This method creates a task with user input
     *
     * @return the task created
     */
    public Task createTask() {
        System.out.println("Task title: ");
        String taskTitle = scanner.nextLine();
        System.out.println("Task description: ");
        String taskDescription = scanner.nextLine();
        System.out.println("Enter a date (YYYY-MM-DD): ");
        String userInput = scanner.nextLine();
        System.out.println("How many days should the task last?");
        int taskDuration = scanner.nextInt();

        scanner.nextLine(); // Consume newline character

        LocalDate date = LocalDate.parse(userInput, DateTimeFormatter.ISO_DATE);

        return new MyTask(taskTitle, taskDescription, taskDuration, date);
    }

    /**
     * 
     * @param project project to which the submission is going to be assigned
     * @return the submission created
     * @throws IllegalSubmissionException in case the submission isnt valid 
     */
    public Submission createSubmission(Project project) throws IllegalSubmissionException {
        try  {
            System.out.println("Please enter your email:");
            String email = scanner.nextLine();
            System.out.println("Please submit your text:");
            String submittedText = scanner.nextLine();

            if (email.isEmpty() || submittedText.isEmpty()) {
                throw new IllegalSubmissionException("Email and submitted text cannot be empty.");
            }

            Participant participant = project.getParticipant(email);
            if (participant instanceof Student) {
                Student student = (Student) participant;
                return new MySubmission(LocalDateTime.now(), submittedText, student);
            } else {
                throw new IllegalSubmissionException("Participant is not registered as a student.");
            }
        }catch (IllegalSubmissionException ex){
            System.out.println(ex.getMessage());
        }
        return null;
    }


    /**
     * Creates a new CBL edition with user input
     *
     * @return returns the edition created
     */
    public Edition createEdition() {

        System.out.println("Edition name: ");

        String editionName = scanner.nextLine();
        System.getProperty("user.dir");

        System.out.print("Enter a starting date (YYYY-MM-DD): ");
        String startDateString = scanner.nextLine();

        System.out.print("Enter an end date (YYYY-MM-DD): ");
        String endDateString = scanner.nextLine();
        try {
            LocalDate startDate = LocalDate.parse(startDateString, DateTimeFormatter.ISO_LOCAL_DATE);
            LocalDate endDate = LocalDate.parse(endDateString, DateTimeFormatter.ISO_LOCAL_DATE);

            String currentDirectory = System.getProperty("user.dir");

            String thisProjectTemplate = currentDirectory + "\\projects\\project_template.json";

            return new CBLEdition(editionName, startDate, thisProjectTemplate, endDate);
        } catch (DateTimeParseException e) {
            System.out.println("Invalid dates.");
            return null;
        }
    }

    /**
     * Requests user input to create a contact
     *
     * @return the contact created
     */
    public MyContact createContact() {

        System.out.println("Enter contact street:");
        String street = scanner.nextLine();

        System.out.println("Enter contact city:");
        String city = scanner.nextLine();

        System.out.println("Enter contact state:");
        String state = scanner.nextLine();

        System.out.println("Enter contact zip code:");
        String zipCode = scanner.nextLine();

        System.out.println("Enter contact country:");
        String country = scanner.nextLine();

        System.out.println("Enter contact phone:");
        String phone = scanner.nextLine();

        return new MyContact(street, city, state, zipCode, country, phone);
    }

    /**
     * Requests user input to create an Institution
     *
     * @return the created instruction
     */
    public MyInstitution createInstitution() {

        InstituitionType institutionType;

        System.out.println("Enter institution name:");
        String name = scanner.nextLine();

        System.out.println("Enter institution email:");
        String email = scanner.nextLine();

        System.out.println("Institution type?\n");
        System.out.println("1 - " + "NGO");
        System.out.println("2 - " + "UNIVERSITY");
        System.out.println("3 - " + "COMPANY");
        System.out.println("4 - " + "OTHER");

        int option = scanner.nextInt();
        switch (option) {
            case 1:
                institutionType = InstituitionType.NGO;
                break;
            case 2:
                institutionType = InstituitionType.UNIVERSITY;
                break;
            case 3:
                institutionType = InstituitionType.COMPANY;
                break;
            case 4:
                institutionType = InstituitionType.OTHER;
                break;
            default:
                return null;
        }
        scanner.nextLine();
        System.out.println("Enter institution website:");
        String website = scanner.nextLine();

        System.out.println("Enter institution description:");
        String description = scanner.nextLine();

        System.out.println("Please provide contact information for the institution: \n");
        MyContact contact = createContact();

        return new MyInstitution(name, email, institutionType, contact, website, description);
    }

    /**
     * Requests user input to create a student
     *
     * @param institution student's institution
     * @return the created student
     */
    public MyStudent createStudent(MyInstitution institution) {

        System.out.println("Enter student name:");
        String name = scanner.nextLine();

        System.out.println("Enter student email:");
        String email = scanner.nextLine();

        System.out.println("Enter student number:");
        int studentNumber = scanner.nextInt();

        scanner.nextLine();

        MyContact contact = createContact();

        return new MyStudent(name, email, institution, contact, studentNumber);
    }


    /**
     * Requests user input to create a facilitator
     *
     * @param institution facilitator's institution
     * @return the created facilitator
     */
    public MyFacilitator createFacilitator(MyInstitution institution) {

        System.out.println("Enter name:");
        String name = scanner.nextLine();

        System.out.println("Enter email:");
        String email = scanner.nextLine();

        System.out.println("Enter area of expertise:");
        String areaOfExpertise = scanner.nextLine();

        scanner.nextLine();

        MyContact contact = createContact();

        return new MyFacilitator(name, email, institution, contact, areaOfExpertise);

    }

    /**
     * Requests user input to create a partner
     *
     * @param institution partner's institution
     * @return the created partner
     */
    public MyPartner createPartner(MyInstitution institution) {

        System.out.println("Enter name:");
        String name = scanner.nextLine();

        System.out.println("Enter email:");
        String email = scanner.nextLine();

        System.out.println("Enter area of website:");
        String website = scanner.nextLine();

        System.out.println("Enter VAT number: ");
        String vat = scanner.nextLine();

        scanner.nextLine();

        MyContact contact = createContact();


        return new MyPartner(name, email, institution, contact, vat, website);
    }

    /**
     * Asks the user to input a name and returns it, so it can later be used by other methods
     *
     * @return the name requested by the user
     */
    public String searchEdition() {
        System.out.println("Edition name?");
        String editionToDelete = scanner.nextLine();
        return editionToDelete;
    }

    /**
     * Asks the user to input a name and returns it, so it can later be used by other methods
     *
     * @return the name requested by the user
     */
    public String searchProject() {
        System.out.println("Project name?");
        String project = scanner.nextLine();
        System.out.println();
        return project;
    }

    /**
     * Asks the user to input a name and returns it, so it can later be used by other methods
     *
     * @return the name requested by the user
     */
    public String searchTask() {
        System.out.println("Task name?");
        String task = scanner.nextLine();
        System.out.println();
        return task;
    }

  /**
   * 
   * @return the new participant
   */
    public Participant createParticipant() {
        System.out.println("Please specify the institution:\n");
        MyInstitution tempInst = this.createInstitution();
        scanner.nextLine();
        System.out.println("1 - Create Student");
        System.out.println("2 - Create Facilitator");
        System.out.println("3 - Create Partner");

        int option = scanner.nextInt();
        scanner.nextLine();
        switch (option) {
            case 1:
                return this.createStudent(tempInst);

            case 2:
                return this.createPartner(tempInst);

            case 3:
                return this.createFacilitator(tempInst);

            default:
                return null;
        }
    }

    /**
     * Prompts a user with a choice menu that allows them to alter the status of an edition
     *
     * @return the desired status
     */
    public Status changeStatus() {
        System.out.println("1 - " + "INACTIVE");
        System.out.println("2 - " + "CANCELED");
        System.out.println("3 - " + "CLOSED");

        int option = scanner.nextInt();
        switch (option) {
            case 1:
                return Status.INACTIVE;

            case 2:
                return Status.CANCELED;

            case 3:
                return Status.CLOSED;

            default:
                return null;
        }

    }

    /**
     * Asks the user to input a name and returns it, so it can later be used by other methods
     *
     * @return the name requested by the user
     */
    public String searchParticipant() {
        System.out.println("Participant name?");
        String name = scanner.nextLine();
        return name;
    }


    public static int getIntStatic() {
        int input = -1;
        boolean isValidInput = false;
        Scanner scanner = new Scanner(System.in);
        while (!isValidInput) {
            if (scanner.hasNextInt()) {
                input = scanner.nextInt();
                isValidInput = true;
                scanner.nextLine(); // Clear the input buffer
            }
        }
        return input;
    }

    public void clearBuffer(){
        scanner.nextLine();
        System.out.println();
    }
}

