package tests;

import com.opencsv.bean.CsvToBeanBuilder;
import io.github.cozyloon.EzConfig;
import model.CSVDetail;
import model.MRWProduct;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.testng.annotations.Test;
import util.TestBase;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

import static common.Constants.CSV_FILE_PATH;

public class Example extends TestBase {
    static WebDriver webDriver;
    WebElement title;
    WebElement isbn;
    WebElement doi;
    static final String fileName = CSV_FILE_PATH + "MRW_PRODUCTS.csv";

    @Test
    public void compareCSVDataWithDB() throws FileNotFoundException {


        List<CSVDetail> beans = new CsvToBeanBuilder(new FileReader(fileName))
                .withType(CSVDetail.class)
                .withSkipLines(1)
                .build()
                .parse();


        for (CSVDetail bean : beans) {


            MRWProduct mrwModel = dbHelper.fetchMRWProductDetails(bean.getDOI());

            if (bean != null && bean.getTITLE().equals(mrwModel.getNAME()) && bean.getDOI().equals(mrwModel.getDOI())) {
                try {
                    ChromeDriverService.Builder withSilent = new ChromeDriverService.Builder().withSilent(true);
                    ChromeDriverService build = withSilent.build();

                    webDriver = new ChromeDriver(build);
                    webDriver.manage().window().maximize();
                    webDriver.get("https://staging.onlinelibrary.wiley.com/doi/book/" + bean.getDOI());

                    title = webDriver.findElement(By.id("banner-text"));
                    isbn = webDriver.findElement(By.xpath("//div[@class='info-block']//following::div[1]//span[5]"));
                    doi = webDriver.findElement(By.xpath("//div[@class='info-block']//following::div[1]//span[8]"));

                    if (title.getText().equals(mrwModel.getNAME()) && isbn.getText().equals(bean.getONLINEISSN()) &&
                            doi.getText().equals(mrwModel.getDOI()) && mrwModel.getURL().equals(webDriver.getCurrentUrl())) {

                        System.out.println("Records matched !!!\n" + bean + webDriver.getCurrentUrl() + "\n" + mrwModel);
                        System.out.println("=================================================================================");
                        webDriver.quit();
                    }
                } catch (NoSuchElementException | NullPointerException e) {

                    System.out.println("Missing or invalid DOI,Book Title or ISBN in online library -> " + e.getClass());
                    System.out.println(bean + webDriver.getCurrentUrl() + "\n" + mrwModel);
                    webDriver.quit();
                    continue;
                }
               /* System.out.println("Records matched !!!\n" + bean + "\n" + mrwModel);
                System.out.println("=================================================================================");
*/
            } else {
                EzConfig.logWARNING("Records not matched !!!\n" + bean + "\n" + mrwModel, null);
                System.out.println("=================================================================================");
            }
        }
    }


}
