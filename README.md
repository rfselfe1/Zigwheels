# ZigWheels Test Automation Framework

A Java-based test automation framework for the ZigWheels website, developed as a capstone project.

The framework combines Selenium WebDriver, Cucumber BDD, TestNG, Maven, Log4j, Extent Reports, Allure, axe-core accessibility testing, passive security observations and a controlled JMeter performance observation.

## Project objectives

The framework automates the following requirements:

- Validate that the ZigWheels home page opens successfully.
- Extract upcoming Honda bikes priced below ₹400,000.
- Display each matching bike's name, price and expected launch date.
- Extract popular used-car models displayed for Chennai.
- Open the Google sign-in popup and capture validation for an invalid account.
- Demonstrate browser-window handling.
- Execute Cucumber scenarios in parallel.
- Capture screenshots when scenarios fail.
- Generate multiple test reports.
- Record automated accessibility findings.
- Record passive, browser-visible security observations.
- Measure one approved page request using a controlled JMeter plan.

## Technology stack

| Technology | Purpose |
|---|---|
| Java 17 | Framework implementation |
| Maven | Dependency management and test execution |
| Selenium WebDriver | Browser automation |
| Cucumber | BDD features and scenarios |
| TestNG | Test runner and parallel execution |
| Log4j 2 | Console and rolling-file logging |
| Extent Reports | Interactive HTML reporting |
| Allure | Interactive test-result reporting |
| axe-core | Automated accessibility auditing |
| Apache JMeter | Single-user performance observation |
| Git and GitHub | Version control and pull-request workflow |

## Automated scenarios

| Tag | Scenario |
|---|---|
| `@smoke` | Validate the ZigWheels home page title and URL |
| `@bikes` | Extract upcoming Honda bikes below ₹400,000 |
| `@cars` | Extract popular used-car models in Chennai |
| `@login` | Capture validation for an invalid Google account |
| `@accessibility` | Run an axe-core accessibility audit |
| `@security` | Record passive browser-visible security observations |

## Framework design

The project uses the Page Object Model to separate browser interactions from Cucumber step definitions.

```text
Feature files
     |
     v
Cucumber step definitions
     |
     v
Page objects
     |
     v
Thread-local WebDriver
     |
     v
Chrome
```

`DriverManager` uses `ThreadLocal<WebDriver>` so each parallel scenario receives an independent browser instance.

Shared Cucumber hooks create the browser before each scenario, capture a screenshot on failure and close the browser afterward.

## Project structure

```text
CapstoneZigWheels
├── performance
│   └── zigwheels-single-user.jmx
├── src
│   └── test
│       ├── java
│       │   └── com
│       │       └── hackathon
│       │           ├── driver
│       │           │   └── DriverManager.java
│       │           ├── hooks
│       │           │   └── Hooks.java
│       │           ├── models
│       │           │   └── Bike.java
│       │           ├── pages
│       │           │   ├── AccessibilityPage.java
│       │           │   ├── HomePage.java
│       │           │   ├── LoginPage.java
│       │           │   ├── PassiveSecurityPage.java
│       │           │   ├── UpcomingBikesPage.java
│       │           │   └── UsedCarsPage.java
│       │           ├── runners
│       │           │   └── TestRunner.java
│       │           └── stepdefinitions
│       │               ├── AccessibilitySteps.java
│       │               ├── HomePageSteps.java
│       │               ├── LoginSteps.java
│       │               ├── PassiveSecuritySteps.java
│       │               ├── UpcomingBikesSteps.java
│       │               └── UsedCarsSteps.java
│       └── resources
│           ├── features
│           │   ├── accessibility.feature
│           │   ├── home_page.feature
│           │   ├── invalid_login.feature
│           │   ├── passive_security.feature
│           │   ├── upcoming_bikes.feature
│           │   └── used_cars.feature
│           ├── allure.properties
│           ├── extent.properties
│           └── log4j2.xml
├── pom.xml
├── testng.xml
└── README.md
```

## Prerequisites

Install the following:

- Java JDK 17 or newer
- Apache Maven
- Google Chrome
- Git
- Apache JMeter 5.6.3 for the performance observation
- Allure command-line tool for viewing Allure reports

Verify Java and Maven:

```bash
java -version
javac -version
mvn -version
```

Verify JMeter and Allure if required:

```bash
jmeter -v
allure --version
```

## Clone the repository

```bash
git clone https://github.com/rfselfe1/Zigwheels.git
cd Zigwheels
```

## Run the complete test suite

```bash
mvn clean test
```

Maven Surefire loads `testng.xml`, and the Cucumber scenarios execute in parallel through TestNG.

Expected result:

```text
Tests run: 6, Failures: 0
```

Because ZigWheels and Google are live external websites, displayed data and execution time can change.

## Run scenarios by tag

Examples:

```bash
mvn test -Dcucumber.filter.tags="@smoke"
```

```bash
mvn test -Dcucumber.filter.tags="@bikes"
```

```bash
mvn test -Dcucumber.filter.tags="@cars"
```

```bash
mvn test -Dcucumber.filter.tags="@login"
```

```bash
mvn test -Dcucumber.filter.tags="@accessibility"
```

```bash
mvn test -Dcucumber.filter.tags="@security"
```

## Parallel execution

The Cucumber TestNG runner overrides the scenario data provider:

```java
@Override
@DataProvider(parallel = true)
public Object[][] scenarios() {
    return super.scenarios();
}
```

Thread-local WebDriver storage prevents parallel scenarios from sharing browser sessions.

Thread names are included in the Log4j output, making parallel activity visible:

```text
[TestNG-PoolService-1]
[TestNG-PoolService-2]
```

## Logging

Log4j records:

- Scenario start and completion
- Browser creation and shutdown
- Extracted bike and used-car data
- Accessibility findings
- Passive-security observations
- Failure details and screenshot activity

The rolling log file is generated at:

```text
logs/automation.log
```

Generated logs are excluded from Git.

## Failure screenshots

When a Cucumber scenario fails, the shared `Hooks` class:

1. Detects the failed scenario.
2. Captures a PNG screenshot through Selenium.
3. Attaches the screenshot to the Cucumber reporting lifecycle.
4. Logs the failure.
5. Closes the browser in a `finally` block.

## Reports

### Cucumber HTML report

```text
target/cucumber-report.html
```

### Cucumber JSON report

```text
target/cucumber-report.json
```

### Extent report

```text
target/extent-report/ExtentReport.html
```

Open on Windows:

```bash
start target/extent-report/ExtentReport.html
```

### Allure report

Raw results are generated at:

```text
target/allure-results
```

Generate and open the report:

```bash
allure generate target/allure-results --clean -o target/allure-report
allure open target/allure-report
```

Press `Ctrl+C` in the terminal to stop the local Allure report server.

## Accessibility audit

The accessibility scenario uses Deque axe-core against the normally loaded ZigWheels home page.

The audit records:

- Rule identifier
- Impact
- Description
- Number of affected nodes
- Deque help URL

Examples of findings observed during development included:

- Missing accessible button names
- Missing accessible link names
- Colour-contrast issues
- Heading-order issues
- Landmark-region issues
- Scrollable regions requiring keyboard access

The scenario verifies that the audit executes and returns valid results. It does not require a third-party website to have zero violations.

Automated accessibility testing does not replace manual testing with keyboards, screen readers or other assistive technologies.

## Passive security observations

Security testing is restricted to passive, non-invasive inspection of ZigWheels only.

The scenario records:

- Whether the final URL uses HTTPS
- Cookies visible to WebDriver
- Cookie `Secure` attributes
- Cookie `HttpOnly` attributes
- HTTP resource references in the loaded DOM
- HTTP form actions in the loaded DOM

The security test does not perform:

- Active vulnerability scanning
- Exploitation
- Fuzzing
- Port scanning
- Authentication attacks
- Spidering or crawling
- Payload injection
- Testing against any website other than ZigWheels

Cookie observations are not automatically labelled as vulnerabilities. Some preference and analytics cookies legitimately require JavaScript access.

This test is not a penetration test or a complete security assessment.

## JMeter performance observation

The JMeter plan is located at:

```text
performance/zigwheels-single-user.jmx
```

It is intentionally limited to:

- One virtual user
- One iteration
- A three-second request delay
- One GET request to the approved ZigWheels home page
- No embedded-resource downloads
- An HTTP 200 response assertion

Run it with:

```bash
jmeter -n \
  -t performance/zigwheels-single-user.jmx \
  -l target/jmeter-results.jtl \
  -e \
  -o target/jmeter-report
```

Open the generated dashboard:

```bash
start target/jmeter-report/index.html
```

This is a single-user response-time observation. It is not a load, stress, spike or endurance test.

## Git workflow

Development was completed using dedicated branches and GitHub pull requests.

Examples include:

```text
feature/used-cars
feature/invalid-login
feature/parallel-execution
feature/logging
feature/extent-reporting
feature/allure-reporting
feature/accessibility
feature/passive-security
feature/performance-observation
```

Each feature was implemented, tested, pushed and merged into `main`.

## Known limitations

- ZigWheels is a live third-party website and its content or DOM may change.
- Google controls the external sign-in page and may change its markup or validation behaviour.
- Bike availability, prices and launch dates may change.
- Used-car popularity data may change.
- Automated accessibility checks cover only issues detectable by axe-core.
- Passive security observations are not a full security assessment.
- The JMeter plan deliberately avoids generating meaningful load.
- Browser and Selenium versions may produce non-fatal Chrome DevTools Protocol warnings.

## Responsible testing statement

All automation is restricted to the approved ZigWheels website and its normal Google sign-in integration.

The framework avoids destructive, intrusive or high-volume testing. Accessibility, security and performance checks are designed to gather evidence responsibly without attacking or placing load on the live service.

## Author

Developed by rfselfe1 as a test automation capstone project.