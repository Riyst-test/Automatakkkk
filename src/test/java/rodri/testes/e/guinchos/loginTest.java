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


    Page page;

    //Funções para tirar repetitividade de preenchimento
    private void inputDadosLogin(String username, String password) {

        page.fill("#username", username);
        page.fill("#password", password);

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

        System.out.println("Inicializando teste com com dados corretos\n");

        username = "tomsmith";  //Atribuição do user name e senha
        password = "SuperSecretPassword!";

        inputDadosLogin(username, password);    //Chama a função de preencher os dados de login após atribuir os novos dados

        page.getByRole(AriaRole.BUTTON,
                        new Page.GetByRoleOptions().setName("Login"))           //Localizando o botão através da role "Login"
                .click();                                                       //Clica no butão (atchau)

        Locator avisoSucesso = page.locator(".flash.success");                  //Localiza o pop-up (flash) informativo sobre o login

        PlaywrightAssertions.assertThat(avisoSucesso).containsText("You logged into a secure area!");           //Verifica se a mensagem de login bem sucedido aparece

        System.out.println("Texto:");                                  //Traz no console o texto contido no aviso
        System.out.println(avisoSucesso.textContent());
    }


    @Test
    void invalidUser() {

        System.out.println("Inicializando teste com username incorreto\n");

        username = "rodriguincho";
        password = "SuperSecretPassword!";

        inputDadosLogin(username, password);

        page.getByRole(AriaRole.BUTTON,
                        new Page.GetByRoleOptions().setName("Login"))
                .click();
        Locator avisoFalhaUser = page.locator(".flash.error");

        PlaywrightAssertions.assertThat(avisoFalhaUser).containsText("Your username is invalid!");

        System.out.println("Texto:");
        System.out.println(avisoFalhaUser.textContent());

    }

    @Test
    void invalidPass() {

        System.out.println("Inicializando teste com senha incorreta\n");

        username = "tomsmith";
        password = "SenhaSuperErrada!";

        inputDadosLogin(username, password);

        page.getByRole(AriaRole.BUTTON,
                        new Page.GetByRoleOptions().setName("Login"))
                .click();
        Locator avisoFalhaSenha = page.locator(".flash.error");

        PlaywrightAssertions.assertThat(avisoFalhaSenha).containsText("Your password is invalid!");

        System.out.println("Texto:");
        System.out.println(avisoFalhaSenha.textContent());

    }

    @Test
    void blankFields() {

        System.out.println("Inicializando teste com campos em branco\n");

        page.getByRole(AriaRole.BUTTON,
                        new Page.GetByRoleOptions().setName("Login"))
                .click();
        Locator avisoFalhaSenha = page.locator(".flash.error");

        PlaywrightAssertions.assertThat(avisoFalhaSenha).isVisible();

        System.out.println("Texto:");
        System.out.println(avisoFalhaSenha.textContent());

    }

}
