package ru.netology.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import ru.netology.data.DataHelper;

import java.text.MessageFormat;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class DashboardPage {
    private final SelenideElement heading = $("[data-test-id=dashboard]");
    private ElementsCollection cards = $$(".list__item div");
    private final String balanceStart = "баланс: ";
    private final String balanceFinish = " р.";

    public void verifyIsDashboardPage(){
        heading.shouldBe(visible);
    }
    private SelenideElement getCardElement(String cardNumber) {
        return cards.findBy(Condition.attribute("data-test-id", getcardTestId(cardNumber)));
    }
    private String getcardTestId(String cardNumber){
        String hiddenNumber = getCardDisableValue(cardNumber);
        return cards.find(Condition.text(hiddenNumber)).getAttribute("data-test-id");
    }
    private String getCardDisableValue(String cardNumber) {
        String cutCardNum = cardNumber.substring(15);
        cutCardNum = MessageFormat.format(
                "**** **** **** {0}",
                cutCardNum);

        return cutCardNum;
    }

    private SelenideElement getCardTransferButton(String cardNumber) {
        return getCardElement(cardNumber).find(By.cssSelector("[data-test-id=action-deposit]"));
    }

    public TransferPage cardTransfer(String cardNumber) {
        getCardTransferButton(cardNumber).click();
        return new TransferPage(getCardDisableValue(cardNumber));
    }

    public int getCardBalance(String cardNumber) {
        var elementText = getCardElement(cardNumber).getText();
        return extractBalance(elementText);
    }

    private int extractBalance(String text) {
        var start = text.indexOf(balanceStart);
        var finish = text.indexOf(balanceFinish);
        var value = text.substring(start + balanceStart.length(), finish);
        return Integer.parseInt(value);
    }

}
