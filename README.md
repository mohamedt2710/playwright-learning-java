# Playwright Learning (Java)

A very small practice project: I am learning Playwright, and this is me getting it running for the first time with Java.

This is a learning project, not a full test suite. My main automation project is the bigger [Selenium one](https://github.com/mohamedt2710/saucedemo-ui-automation) - this repo is just my first Playwright steps, kept honest and small on purpose.

- **Site used for practice:** [Swag Labs](https://www.saucedemo.com)
- **Tools:** Java, Playwright, JUnit 5, Maven
- **Tests:** 3 tiny ones - page title, a valid login, and a wrong-password error

## How to run it

You need Java 11+ and Maven.

```bash
git clone https://github.com/mohamedt2710/playwright-learning-java.git
cd playwright-learning-java
mvn test
```

The first run downloads the Playwright browser by itself, so it takes a minute.

## What I learned so far

- Playwright opens the browser through `Playwright.create()` and everything hangs off `Browser` > `Page`
- Locators like `page.locator("#user-name")` feel cleaner than `findElement(By.id(...))`
- Assertions with `assertTrue`/`assertEquals` work the same as in my Selenium project
- Playwright waits for elements automatically before clicking, which means less waiting code than Selenium
