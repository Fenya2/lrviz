package ru.urfu.lrviz.web;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
        assertAlertAndAccept();
    }

    @Test
    void testAlertWithIncompleteGrammarOnlyTerminals() {
        addTerminal("a");
        driver.findElement(By.id(BUILD_BUTTON_ID)).click();
        assertAlertAndAccept();
    }

    @Test
    void testAlertWithIncompleteGrammarMissingStartSymbol() {
        addTerminal("a");
        addNonTerminal("S");
        addRule("S", "a");
        driver.findElement(By.id(BUILD_BUTTON_ID)).click();
        assertAlertAndAccept();
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

    private void assertAlertAndAccept() {
        wait.until(ExpectedConditions.alertIsPresent());
        Alert alert = driver.switchTo().alert();
        assertEquals("Грамматика не задана", alert.getText());
        alert.accept();
    }
}
