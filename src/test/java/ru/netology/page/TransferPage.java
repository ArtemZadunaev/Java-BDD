package ru.netology.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.Keys;
import ru.netology.data.DataHelper;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class TransferPage {
    SelenideElement amount = $("[data-test-id= amount] input");
    SelenideElement from = $("[data-test-id=from] input");
    SelenideElement to = $("[data-test-id=to] input");
    SelenideElement transferButton = $("[data-test-id=action-transfer].button");
    ElementsCollection chancelButton = $$("button");
    String hiddenNumCardFrom ;

    public TransferPage(String hiddenCardNumberFrom) {
        to.shouldBe(visible).shouldHave(Condition.value(hiddenCardNumberFrom));
        this.hiddenNumCardFrom = hiddenCardNumberFrom;
    }

    private SelenideElement getChancelButton() {
        return chancelButton.find(Condition.text("Отмена"));
    }

    public DashboardPage chancelTransfer() {
        getChancelButton().click();
        return new DashboardPage();
    }

    public void transferFromCard(String cardNumberTo, int amount) {
        this.amount.setValue(Integer.toString(amount));
        from.setValue(cardNumberTo);
        transferButton.click();

    }

public void verifyTransferPage(){
        amount.shouldBe(visible);
        from.shouldBe(visible);
}
}
