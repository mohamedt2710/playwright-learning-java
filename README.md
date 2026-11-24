# Playwright Learning (Java)

A very small practice project: I am learning Playwright, and this is me getting it running for the first time with Java.

This is a learning project, not a full test suite (it runs on GitHub Actions on every push). My main automation project is the bigger [Selenium one](https://github.com/mohamedt2710/saucedemo-ui-automation) - this repo is just my first Playwright steps, kept honest and small on purpose.

- **Site used for practice:** [Swag Labs](https://www.saucedemo.com)
- **Tools:** Java, Playwright, JUnit 5, Maven
- **Tests:** 13 in total. `PlaywrightLearningTest` has my first 3 (page title, valid login, wrong password). `SwagLabsFlowTest` has 10 more: locked-out user, empty username, product count, cart badge add/remove, cart contents, price sort, checkout validation, full checkout, and logout

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
