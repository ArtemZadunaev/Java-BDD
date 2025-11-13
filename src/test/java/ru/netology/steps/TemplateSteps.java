package ru.netology.steps;

import com.codeborne.selenide.Selenide;
import io.cucumber.java.ru.И;
import org.junit.jupiter.api.Assertions;
import ru.netology.page.DashboardPage;
import ru.netology.page.LoginPage;
import ru.netology.page.TransferPage;
import ru.netology.page.VerificationPage;


public class TemplateSteps {
    private static LoginPage loginPage;
    private static DashboardPage dashboardPage;
    private static VerificationPage verificationPage;
    private static TransferPage transferpage;
    private int expBalanceFromCard;
    private int expBalanceToCard;
    private String fromCardNumber;
    private String toCardNumber;


    @И("открыта страница с формой авторизации {string}")
    public void openAuthPage(String url) {
        loginPage = Selenide.open(url, LoginPage.class);
    }


    @И("пользователь пытается авторизоваться с именем {string} и паролем {string}")
    public void loginWithNameAndPassword(String login, String password) {
        verificationPage = loginPage.validLogin(login, password);
    }
    @И("пользователь выбирает карту {string} для перевода")
    public void selectCardToTransfer(String cardNumber){
        expBalanceToCard= dashboardPage.getCardBalance(cardNumber);
        toCardNumber=cardNumber;
        transferpage = dashboardPage.cardTransfer(cardNumber);
    }
    @И("пользователь вводит проверочный код 'из смс' {string}")
    public void setValidCode(String verificationCode) {
        dashboardPage = verificationPage.validVerify(verificationCode);
    }

    @И("происходит успешная авторизация и пользователь попадает на страницу 'Личный кабинет'")
    public void verifyDashboardPage() {
        dashboardPage.verifyIsDashboardPage();
    }

    @И("появляется ошибка о неверном коде проверки")
    public void verifyCodeIsInvalid() {
        verificationPage.verifyCodeIsInvalid();
    }
    @И("пользователь вводит сумму {int} и номер карты {string} с которой списать средства и нажимает кнопку перевода")
     public void transferFromCard(int amount,String fromCard){
        transferpage.chancelTransfer();
        fromCardNumber = fromCard;
        expBalanceFromCard= dashboardPage.getCardBalance(fromCardNumber);
        selectCardToTransfer(toCardNumber);
        expBalanceFromCard-=amount;
        expBalanceToCard+=amount;

        transferpage.transferFromCard(fromCard,amount);
    }
    @И("пользователь видит страницу cо списком карт")
    public void succesfullTransfer(){
        verifyDashboardPage();

    }
    @И("пользователь проверяет правильность перевода")
    public void verifyTransaction(){
        Assertions.assertEquals(expBalanceFromCard,dashboardPage.getCardBalance(fromCardNumber));
        Assertions.assertEquals(expBalanceToCard,dashboardPage.getCardBalance(toCardNumber));
    }

    @И("происходит переход на страницу перевода")
    public void verifyTRansferPage(){
        transferpage.verifyTransferPage();
    }
}
