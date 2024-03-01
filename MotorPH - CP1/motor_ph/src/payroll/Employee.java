package payroll;

public class Employee implements EmployeeDetails {

    private final String lastName;
    private final String firstName;
    private final String birthday;
    private final String address;
    private final String phoneNum;
    private final String sssNum;
    private final String philhealthNum;
    private final String pagibigNum;
    private final String tinNum;

    // Constructors
    public Employee(String lastName, String firstName, String birthday, String address, String phoneNum, String sssNum, String philhealthNum, String pagibigNum, String tinNum) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.birthday = birthday;
        this.address = address;
        this.phoneNum = phoneNum;
        this.sssNum = sssNum;
        this.philhealthNum = philhealthNum;
        this.pagibigNum = pagibigNum;
        this.tinNum = tinNum;
    }

    // Getters and setters

    @Override
    public String getLastName() {
        return lastName;
    }

    @Override
    public String getFirstName() {
        return firstName;
    }

    @Override
    public String getBirthday() {
        return birthday;
    }

    @Override
    public String getAddress() {
        return address;
    }

    @Override
    public String getPhoneNum() {
        return phoneNum;
    }

    @Override
    public String getSssNum() {
        return sssNum;
    }

    @Override
    public String getPhilhealthNum() {
        return philhealthNum;
    }

    @Override
    public String getPagibigNum() {
        return pagibigNum;
    }

    @Override
    public String getTinNum() {
        return tinNum;
    }
}

