package ru.netology.steps;

import com.codeborne.selenide.Selenide;
import io.cucumber.java.ru.И;
import io.cucumber.java.ru.Когда;
import io.cucumber.java.ru.Пусть;
import io.cucumber.java.ru.Тогда;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import ru.netology.data.DataHelper;
import ru.netology.page.DashboardPage;
import ru.netology.page.LoginPage;
import ru.netology.page.TransferPage;
import ru.netology.page.VerificationPage;

import java.util.HashMap;
import java.util.Map;


public class TemplateSteps {
    private static LoginPage loginPage;
    private static DashboardPage dashboardPage;
    private static VerificationPage verificationPage;
    private static TransferPage transferpage;
    private DataHelper data;
    int startBalanceToCard;
    int startBalanceFromCard;
    int finalBalanceToCard;
    int finalBalanceFromCard;


    @Пусть("открыта страница с формой авторизации {string}")
    public void openAuthPage(String url) {
        loginPage = Selenide.open(url, LoginPage.class);
    }

    @Когда("пользователь пытается авторизоваться с именем {string} и паролем {string}")
    public void loginWithNameAndPassword(String login, String password) {
        verificationPage = loginPage.validLogin(login, password);
    }

    @И("пользователь вводит проверочный код 'из смс' {string}")
    public void setValidCode(String verificationCode) {
        dashboardPage = verificationPage.validVerify(verificationCode);
    }

    @Тогда("происходит успешная авторизация и пользователь попадает на страницу 'Личный кабинет'")
    public void verifyDashboardPage() {
        dashboardPage.verifyIsDashboardPage();
    }

    @Тогда("появляется ошибка о неверном коде проверки")
    public void verifyCodeIsInvalid() {
        verificationPage.verifyCodeIsInvalid();
    }

    @Пусть("пользователь залогинен с именем {string} и паролем {string}")
    public void validLogin(String name, String pass) {
        openAuthPage("http://localhost:9999");
        loginWithNameAndPassword(name, pass);
        DataHelper.AuthInfo user = new DataHelper.AuthInfo(name, pass);
        var verCode = DataHelper.getVerificationCodeFor(user);
        setValidCode(verCode.getCode());
        verifyDashboardPage();
    }

    @Когда("пользователь переводит {int} рублей с карты с номером {string} на свою {int} карту с главной страницы")
    public void validTransaction(int amount, String fromCardNumber, int cardIndex) {
        startBalanceToCard = dashboardPage.getBalanceByIndex(cardIndex);
        startBalanceFromCard = dashboardPage.getCardBalance(fromCardNumber);
        transferpage = dashboardPage.transferById(cardIndex);
        transferpage.verifyTransferPage();
        transferpage.transferFromCard(fromCardNumber, amount);
        finalBalanceFromCard = dashboardPage.getCardBalance(fromCardNumber);
        finalBalanceToCard = dashboardPage.getBalanceByIndex(cardIndex);
    }

    @Тогда("Тогда баланс его {int} карты из списка на главной странице должен стать {int} рублей")
    public void verifyBalance(int cardIndex, int expectedAmount) {
        Assertions.assertEquals(expectedAmount, dashboardPage.getBalanceByIndex(cardIndex));
    }
}
