# Welcome to the TDS Load Test Project

The tds-loadtest project contains a jMeter test plan used to profile the performance of the Test
Delivery System (TDS). The project also contains Java code for generating response data for
test questions, which is used by the load test, as well as python scripts for generating test seed data.

## General
The load test plan is built to simulate a large number of students taking a test simultaneously, as well
as their respective proctors logging in and managing test sessions. The test plan works with two groups of threads
running in parallel.

The first phase begins with proctor threads logging in and generating new test sessions.
After generating a new session id and writing it into memory, each proctor will wait while students begin to join the
test sessions. Proctors wait to approve students, while the student threads begin to login and select their tests,
chosen at random.

During the second phase, students login and begin the test, as the proctor threads approve each student. Students then
take the test to completion, and proctors begin to close sessions after all respective students have completed their tests.

## Load Test Setup and Configuration
Each jMeter server node will need a copy of the tds-loadtest.jmx test plan. This test plan
contains the properties and logic used by jMeter to execute a performance test. Each jMeter
node must also contain a unique set of student and proctor logins (**student_logins.csv** and **proctor_logins.csv**).


### JMeter Server Node Instructions
1. Create a directory for the load test

    ````
    mkdir ~/load-test
    cd ~/load-test

    ````
2. Generate student and proctor login account credentials by running the python scripts in
the project's /bootstrapping/ directory. Place the .csv files that are generated by these
scripts into the load test directory on the jMeter server. The .csv files _must_ be named
**student_logins.csv** and **proctor_logins.csv** respectively. The **student_logins.csv**
file can also be imported directly into ART. Provide the **-h** flag to either seed script for
more options.
````
    python /project/path/bootstrapping/create_students.py -n \<num of students\> -g \<grade of students\>

    python /project/path/bootstrapping/create-proctors.py -n \<num of proctors\>
````
3. Configure jMeter test variables by editing the tds-loadtest.jtl file and changing the values
for the following properties:
    * **TOTAL_STUDENT_COUNT** - The total number of students threads _for each jmeter node_ that will take a test.
    * **STUDENTS_PER_PROCTOR** - The ratio of the amount of students for each proctor.
    * **PROCTOR_HOST** - The proctor host URL.
    * **SSO_HOST** - The SSO host URL.
    * **STUDENT_START_DELAY_IN_MILLIS** - The amount of milliseconds that the student threads should be delayed to allow
    the proctor threads to create sessions.
    * **PROCTOR_RAMP_UP_TIME_IN_SECS** - The period of time that proctor threads should begin executing over.
    * **STUDENT_RAMP_UP_TIME_IN_SECS** - The period of time that student threads should begin executing over.

4. Configure jMeter, Java, and system settings on the jMeter to allow for a large number of open file pointers and
sufficient Java heapspace.

5. After manually smoke testing the TDS and ensuring that all other other components are functional, you are ready to
execute the jmeter test plan.
````
jmeter -n -t ./tds-loadtest.jmx -l <testoutput-filename>.jtl &
````

6. A progress summary line will be printed to standard output every 30 seconds. All test progress will the written to
the .jtl file specified, and the **jmeter.log** will be placed at the test root level. Upon the conclusion of the test,
test results can be analyzed. Analysis can be done on a machine running jMeter in GUI mode. Open the .jtl file using the
"View Results Tree" or "Summary Report" tool to view the result data.

### Useful commands for viewing test run errors
* **cat jmeter.log | grep "ERROR"** 						_- all general errors_
* **cat jmeter.log | grep "NullPointer" -c** 				_- count of null pointer exceptions_
* **cat jmeter.log | grep "OutOfBounds" -c** 				_- count of all "ArrayIndexOutOfBounds" errors_
* **cat jmeter.log | grep "Connection has timed out" -c**	_- count of errors related to leaky connections_
* **cat testoutput.jtl | grep "1.1 5" -c**  				_- count of all "5xx" HTTP errors_
* **cat testoutput.jtl | grep "1.1 4" -c** 				    _- count of all "4xx" HTTP errors_

**NOTE:** - Be sure to clear the jmeter.log and .jtl file after each test execution. For accurate results, be sure to
 use fresh proctor and student accounts between subsequent load test executions.

## Dependencies
* Apache JMeter 2.13
* Java 7
* Python (for running the scripts to generate the proctor/user .csv)

