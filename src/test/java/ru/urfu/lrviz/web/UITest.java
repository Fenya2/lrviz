package ru.urfu.lrviz.web;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import ru.urfu.lrviz.GrammarDtoExamples;
import ru.urfu.lrviz.LrvizBackApplication;
import ru.urfu.lrviz.api.dto.GrammarDto;
import ru.urfu.lrviz.api.dto.RuleDto;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;
import static ru.urfu.lrviz.core.GrammarExamples.G_1;
import static ru.urfu.lrviz.core.GrammarExamples.G_2;

/**
 * @author fenya
 * @since 16.04.2026
 */
@SpringBootTest(classes = LrvizBackApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UITest {
    private static final String BUILD_BUTTON_ID = "buildButton";
    private static final String TERMINAL_INPUT_FIELD_ID = "terminalInputField";
    private static final String ADD_TERMINAL_BUTTON_ID = "addTerminalButton";
    private static final String NON_TERMINAL_INPUT_FIELD_ID = "nonTerminalInputField";
    private static final String ADD_NON_TERMINAL_BUTTON_ID = "addNonTerminalButton";
    private static final String LEFT_PART_RULE_SELECTOR_ID = "leftPartRuleSelector";
    private static final String RIGHT_PART_RULE_INPUT_FIELD_ID = "rightPartRuleInputField";
    private static final String ADD_RULE_BUTTON_ID = "addRuleButton";
    private static final String START_SYMBOL_SELECTOR_ID = "startSymbolSelector";
    private static final String LAST_STEP_BUTTON_ID = "lastStepButton";
    private static final String FIRST_STEP_BUTTON_ID = "firstStepButton";
    private static final String PREV_STEP_BUTTON_ID = "prevStepButton";
    private static final String NEXT_STEP_BUTTON_ID = "nextStepButton";
    private static final String STEP_COUNTER_ID = "stepCounter";
    private static final String STEP_INPUT_ID = "stepInput";
    private static final String GO_STEP_BUTTON_ID = "goStepButton";
    private static final String GRAPH_CONTAINER_ID = "graphContainer";
    private static final int WAIT_TIME = 10;

    private final WebDriver driver;
    private final WebDriverWait wait;

    @LocalServerPort
    private int serverPort;

    @Autowired
    UITest(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(WAIT_TIME));
    }

    @BeforeEach
    void setUp() {
        driver.get("http://localhost:" + serverPort);
    }

    @AfterEach
    void tearDown() {
        driver.close();
    }

    @Test
    void testAlertWhenGrammarNotSet() {
        driver.findElement(By.id(BUILD_BUTTON_ID)).click();
        assertAlertTextAndAccept("Грамматика не задана");
    }

    @Test
    void testAlertWithIncompleteGrammarOnlyTerminals() {
        addTerminal("a");
        driver.findElement(By.id(BUILD_BUTTON_ID)).click();
        assertAlertTextAndAccept("Грамматика не задана");
    }

    @Test
    void testAlertWithIncompleteGrammarMissingStartSymbol() {
        addTerminal("a");
        addNonTerminal("S");
        addRule("S", "a");
        driver.findElement(By.id(BUILD_BUTTON_ID)).click();
        assertAlertTextAndAccept("Грамматика не задана");
    }

    @Test
    void testSuccessfulBuildWithValidGrammar() {
        GrammarDto g2 = GrammarDtoExamples.getAsDto(G_2);
        fillGrammar(g2);
        driver.findElement(By.id(BUILD_BUTTON_ID)).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id(LAST_STEP_BUTTON_ID)));
        boolean noAlert = wait.until(ExpectedConditions.not(ExpectedConditions.alertIsPresent()));
        assertTrue(noAlert, "Unexpected alert present after successful build");
    }

    @Test
    void testNavigationControlsPresent() {
        GrammarDto g2 = GrammarDtoExamples.getAsDto(G_1);
        fillGrammar(g2);
        driver.findElement(By.id(BUILD_BUTTON_ID)).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id(GRAPH_CONTAINER_ID)));
        assertTrue(driver.findElement(By.id(FIRST_STEP_BUTTON_ID)).isDisplayed(),
                "First step button should be displayed");
        assertTrue(driver.findElement(By.id(PREV_STEP_BUTTON_ID)).isDisplayed(),
                "Previous step button should be displayed");
        assertTrue(driver.findElement(By.id(NEXT_STEP_BUTTON_ID)).isDisplayed(),
                "Next step button should be displayed");
        assertTrue(driver.findElement(By.id(LAST_STEP_BUTTON_ID)).isDisplayed(),
                "Last step button should be displayed");
        assertTrue(driver.findElement(By.id(STEP_COUNTER_ID)).isDisplayed(),
                "Step counter should be displayed");
        assertTrue(driver.findElement(By.id(STEP_INPUT_ID)).isDisplayed(),
                "Step input should be displayed");
        assertTrue(driver.findElement(By.id(GO_STEP_BUTTON_ID)).isDisplayed(),
                "Go button should be displayed");
    }

    @Test
    void testStepCounterInitialValue() {
        GrammarDto g2 = GrammarDtoExamples.getAsDto(G_2);
        fillGrammar(g2);
        driver.findElement(By.id(BUILD_BUTTON_ID)).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id(GRAPH_CONTAINER_ID)));
        WebElement stepCounter = driver.findElement(By.id(STEP_COUNTER_ID));
        String counterText = stepCounter.getText();
        assertTrue(counterText.startsWith("0/"),
                "Initial step counter should start with '0/', got: " + counterText);
    }

    @Test
    void testNavigationButtonsDisabledInitially() {
        GrammarDto g2 = GrammarDtoExamples.getAsDto(G_2);
        fillGrammar(g2);
        driver.findElement(By.id(BUILD_BUTTON_ID)).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id(GRAPH_CONTAINER_ID)));
        assertNotNull(driver.findElement(By.id(FIRST_STEP_BUTTON_ID)).getAttribute("disabled"),
                "First button should be disabled at step 0");
        assertNotNull(driver.findElement(By.id(PREV_STEP_BUTTON_ID)).getAttribute("disabled"),
                "Previous button should be disabled at step 0");
    }

    @Test
    void testStepInputAndGoButton() {
        GrammarDto g2 = GrammarDtoExamples.getAsDto(G_2);
        fillGrammar(g2);
        driver.findElement(By.id(BUILD_BUTTON_ID)).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id(GRAPH_CONTAINER_ID)));

        WebElement stepInput = driver.findElement(By.id(STEP_INPUT_ID));
        WebElement goButton = driver.findElement(By.id(GO_STEP_BUTTON_ID));
        WebElement stepCounter = driver.findElement(By.id(STEP_COUNTER_ID));

        String counterText = stepCounter.getText();
        String totalStepsStr = counterText.substring(counterText.indexOf('/') + 1);
        int totalSteps = Integer.parseInt(totalStepsStr);

        if (totalSteps > 0) {
            stepInput.clear();
            stepInput.sendKeys("1");
            goButton.click();
            wait.until(ExpectedConditions.not(ExpectedConditions.textToBe(By.id(STEP_COUNTER_ID), "0/" + totalStepsStr)));

            counterText = stepCounter.getText();
            assertTrue(counterText.startsWith("1/"),
                    "Step counter should show step 1 after clicking Go");

            stepInput.clear();
            stepInput.sendKeys("0");
            stepInput.sendKeys(org.openqa.selenium.Keys.ENTER);
            wait.until(ExpectedConditions.textToBe(By.id(STEP_COUNTER_ID), "0/" + totalStepsStr));
        }
    }

    @Test
    void testStepInputInvalidValue() {
        GrammarDto g2 = GrammarDtoExamples.getAsDto(G_2);
        fillGrammar(g2);
        driver.findElement(By.id(BUILD_BUTTON_ID)).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id(GRAPH_CONTAINER_ID)));

        var stepInput = driver.findElement(By.id(STEP_INPUT_ID));
        var goButton = driver.findElement(By.id(GO_STEP_BUTTON_ID));

        stepInput.clear();
        stepInput.sendKeys("-1");
        goButton.click();
        assertAlertTextAndAccept("Недопустимый номер операции");
    }

    @Test
    void testNextButtonNavigation() {
        GrammarDto g2 = GrammarDtoExamples.getAsDto(G_2);
        fillGrammar(g2);
        driver.findElement(By.id(BUILD_BUTTON_ID)).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id(GRAPH_CONTAINER_ID)));

        var stepCounter = driver.findElement(By.id(STEP_COUNTER_ID));
        String initialText = stepCounter.getText();

        var nextButton = driver.findElement(By.id(NEXT_STEP_BUTTON_ID));
        String totalStepsStr = initialText.substring(initialText.indexOf('/') + 1);
        int totalSteps = Integer.parseInt(totalStepsStr);

        if (totalSteps > 0) {
            nextButton.click();
            wait.until(ExpectedConditions.not(ExpectedConditions.textToBe(By.id(STEP_COUNTER_ID), initialText)));

            String newText = stepCounter.getText();
            assertTrue(newText.startsWith("1/"),
                    "Step counter should increment after clicking Next");
        }
    }

    @Test
    void testLastButtonNavigation() {
        GrammarDto g2 = GrammarDtoExamples.getAsDto(G_2);
        fillGrammar(g2);
        driver.findElement(By.id(BUILD_BUTTON_ID)).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id(GRAPH_CONTAINER_ID)));

        var stepCounter = driver.findElement(By.id(STEP_COUNTER_ID));
        var lastButton = driver.findElement(By.id(LAST_STEP_BUTTON_ID));

        lastButton.click();

        String counterText = stepCounter.getText();
        String totalStepsStr = counterText.substring(counterText.indexOf('/') + 1);
        String currentStepStr = counterText.substring(0, counterText.indexOf('/'));

        assertEquals(totalStepsStr, currentStepStr,
                "After clicking Last button, current step should equal total steps");
    }

    private void fillGrammar(GrammarDto grammar) {
        for (String terminal : grammar.terminals()) {
            addTerminal(terminal);
        }
        for (String nonTerminal : grammar.nonTerminals()) {
            addNonTerminal(nonTerminal);
        }
        for (RuleDto rule : grammar.rules()) {
            addRule(rule.left(), rule.right());
        }
        selectStartSymbol(grammar.startSymbol());
    }

    private void addTerminal(String terminal) {
        var input = driver.findElement(By.id(TERMINAL_INPUT_FIELD_ID));
        input.sendKeys(terminal);
        driver.findElement(By.id(ADD_TERMINAL_BUTTON_ID)).click();
    }

    private void addNonTerminal(String nonTerminal) {
        var input = driver.findElement(By.id(NON_TERMINAL_INPUT_FIELD_ID));
        input.sendKeys(nonTerminal);
        driver.findElement(By.id(ADD_NON_TERMINAL_BUTTON_ID)).click();
    }

    private void addRule(String left, String right) {
        var leftSelector = new Select(driver.findElement(By.id(LEFT_PART_RULE_SELECTOR_ID)));
        leftSelector.selectByValue(left);

        var rightInput = driver.findElement(By.id(RIGHT_PART_RULE_INPUT_FIELD_ID));
        rightInput.sendKeys(right);

        driver.findElement(By.id(ADD_RULE_BUTTON_ID)).click();
    }

    private void selectStartSymbol(String symbol) {
        var selector = new Select(driver.findElement(By.id(START_SYMBOL_SELECTOR_ID)));
        selector.selectByValue(symbol);
    }

    private void assertAlertTextAndAccept(String expectedText) {
        wait.until(ExpectedConditions.alertIsPresent());
        Alert alert = driver.switchTo().alert();
        assertEquals(expectedText, alert.getText());
        alert.accept();
    }
}
