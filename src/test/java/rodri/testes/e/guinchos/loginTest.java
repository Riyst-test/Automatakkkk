package rodri.testes.e.guinchos;



import com.microsoft.playwright.*;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import com.microsoft.playwright.options.AriaRole;
import org.junit.jupiter.api.*;

public class loginTest {

    private static Playwright playwright;
    private static Browser browser;
    private static BrowserContext browserContext;

    //Variáveis para armazenar usuário e senha
    private String username;
    private String password;
    private String loginWarningText;

    Page page;

    //Funções para tirar a repetitividade do coDigo
    private void loginMethod(String username, String password, String loginWarningText) {

        page.fill("#username", username);       //Preenche o campo user
        page.fill("#password", password);       //Preenche o campo pw

        System.out.println("Usuário: " + username);
        System.out.println("Seha: " + password + "\n");

        page.getByRole(AriaRole.BUTTON,
                        new Page.GetByRoleOptions().setName("Login"))           //Localizando o botão através da role "Login"
                .click();                                                       //Clica no butão (atchau)

        System.out.println("Teste de login realizado:");
        Locator avisoLogin = page.locator(".flash");                  //Localiza o pop-up (flash) informativo sobre o login

        PlaywrightAssertions.assertThat(avisoLogin).isVisible();
        PlaywrightAssertions.assertThat(avisoLogin).containsText(loginWarningText);       //Verifica se a mensagem de login bem sucedido aparece
        System.out.println(avisoLogin.textContent());
    }

    private void checkCloseAlertButton() {
        PlaywrightAssertions.assertThat(page.locator(".close")).isVisible();        //Verifica se o botão close está visível
        System.out.println("Botão close é visível");

        Locator closeWarning = page.locator(".close");      //Clica no botão de fechar o aviso
        closeWarning.click();

        PlaywrightAssertions.assertThat(page.locator(".flash.success")).isHidden();       //Valida se o aviso saiu da tela ao clicar no botão de fechar o aviso após o aviso aparecer com os dados do aviso
        System.out.println("Botão close é funcional\n");

    }

    @BeforeAll
    public static void setUpBrowser() {                     //Configs do navegador

        playwright = Playwright.create();
        browser = playwright.chromium().launch(             //Inicializa o Chromium
                new BrowserType.LaunchOptions()
                        .setHeadless(false)                 //Desativa modo headless (sem interface)

        );

        browserContext = browser.newContext();              //Cria um Browser Context
    }

    @BeforeEach
    public void setUp() {

        page = browserContext.newPage();                                    //Abre uma nova aba no contexto para cada teste
        page.navigate("https://the-internet.herokuapp.com/login");      //Acessa o site do desafio garai zoio é maluco demais

    }

    @AfterAll
    public static void oDesligas() {

        browser.close();                                    //Encerra o navegador após o fim de todos os testes
        playwright.close();                                 //Encerra o playright

    }

    @Test
    void loginSuccess() {

        System.out.println("--------------------------------------------\nInicializando teste com dados corretos\n--------------------------------------------\n");

        username = "tomsmith";  //Atribuição do user name e senha
        password = "SuperSecretPassword!";
        loginWarningText = "You logged into a secure area!"; //Definição do texto, ou parte dele, que irá aparecer após clicar no butão

        loginMethod(username, password, loginWarningText);    //Chama a função de preencher os dados de login após atribuir os novos dados

        checkCloseAlertButton();        //Chama a função que verifica o botão de fechar o aviso

    }


    @Test
    void invalidUser() {

        System.out.println("--------------------------------------------\nInicializando teste com username incorreto\n--------------------------------------------\n");

        username = "rodriguincho";
        password = "SuperSecretPassword!";
        loginWarningText = "Your username is invalid!";

        loginMethod(username, password, loginWarningText);

        checkCloseAlertButton();

    }

    @Test
    void invalidPass() {

        System.out.println("--------------------------------------------\nInicializando teste com senha incorreta\n--------------------------------------------\n");

        username = "tomsmith";
        password = "SenhaSuperErrada!";
        loginWarningText = "Your password is invalid!";

        loginMethod(username, password, loginWarningText);

        checkCloseAlertButton();

    }

    @Test
    void blankFields() {

        System.out.println("--------------------------------------------\nInicializando teste com campos em branco\n--------------------------------------------\n");

        username = "";
        password = "";
        loginWarningText = "Your username is invalid!";

        loginMethod(username, password, loginWarningText);

        checkCloseAlertButton();

    }

}
