package tests;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVReader;
import model.Menu;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@DisplayName("Тесты на парсинг различных типов файлов - xls, csv, pdf, json")
public class ParsingFilesTests {

    private ClassLoader cl = ParsingFilesTests.class.getClassLoader();

    private final static ObjectMapper om = new ObjectMapper();


    //pdf parsing
    @DisplayName("Получени из zip архива pdf файла, проверка автора и части содержимого файла")
    @Test
    void xlsFromZipFileParsingTest() throws Exception {

        PDF pdf = null;

        try(ZipInputStream zis = new ZipInputStream(cl.getResourceAsStream("zipTest.zip"))) {

            ZipEntry entry;

            while ((entry = zis.getNextEntry()) != null) {
                if (entry.getName().equals("sample.pdf")) {
                    pdf = new PDF(zis);
                    break;
                }
            }

            Assertions.assertEquals("Philip Hutchison", pdf.author);
            Assertions.assertTrue(pdf.text.contains("Pellentesque sit amet lectus"));

        }
    }

    @DisplayName("Получени из zip архива csv файла, проверка количества строк и содержимого файла")
    @Test
    void csvFromZipParsingTest() throws Exception {

        CSVReader reader = null;
        try(ZipInputStream zis = new ZipInputStream(cl.getResourceAsStream("zipTest.zip"))) {

            ZipEntry entry;

            while ((entry = zis.getNextEntry()) != null) {
                if (entry.getName().equals("csvTestFile.csv")) {
                    reader = new CSVReader(new InputStreamReader(zis));
                    break;
                }
            }

            List<String[]> data = reader.readAll();

            Assertions.assertEquals(3, data.size());
            Assertions.assertArrayEquals(
                    new String[] {"data1 ", " test 1"},
                    data.get(0)
            );
            Assertions.assertArrayEquals(
                    new String[] {"data2 ", " test 2"},
                    data.get(1)
            );
            Assertions.assertArrayEquals(
                    new String[] {"data3 ", " test 3"},
                    data.get(2)
            );
        }
    }

    @DisplayName("Получени из zip архива xls файла, проверка содержимого ячейки xls файла")
    @Test
    void xlsFromZipParsingTest() throws Exception {

        XLS xls = null;

        try(ZipInputStream zis = new ZipInputStream(cl.getResourceAsStream("zipTest.zip"))) {

            ZipEntry entry;

            while ((entry = zis.getNextEntry()) != null) {
                if (entry.getName().equals("tests-example.xls")) {
                    xls = new XLS(zis);
                    break;
                }
            }

            System.out.println();

            String actualValue =
                    xls.excel.getSheetAt(0).getRow(1).getCell(1).getStringCellValue();

            Assertions.assertEquals("What C datatypes are 8 bits? (assume i386)", actualValue);

        }
    }

    @DisplayName("Тест на проверку Json")
    @Test
    void jsonParsingTest() throws Exception {
        try (Reader reader = new InputStreamReader(
                cl.getResourceAsStream("menu.json")
        )) {
            Menu menu = om.readValue(reader, Menu.class);

            Assertions.assertEquals("5", menu.getMenuVersion(),
                    "Версия меню не совпадает с ожидаемой");

            Assertions.assertEquals(1, menu.getItems().get(0).getId());
            Assertions.assertEquals("Coffee", menu.getItems().get(0).getType());
            Assertions.assertEquals("Americano", menu.getItems().get(0).getName());
            Assertions.assertEquals(120, menu.getItems().get(0).getPrice());

            Assertions.assertEquals(2, menu.getItems().get(1).getId());
            Assertions.assertEquals("Coffee", menu.getItems().get(1).getType());
            Assertions.assertEquals("Cappuccino", menu.getItems().get(1).getName());
            Assertions.assertEquals(160, menu.getItems().get(1).getPrice());

            Assertions.assertEquals(3, menu.getItems().get(2).getId());
            Assertions.assertEquals("Food", menu.getItems().get(2).getType());
            Assertions.assertEquals("Cupcake", menu.getItems().get(2).getName());
            Assertions.assertEquals(100, menu.getItems().get(2).getPrice());

            Assertions.assertEquals(4, menu.getItems().get(3).getId());
            Assertions.assertEquals("Food", menu.getItems().get(3).getType());
            Assertions.assertEquals("Sandwich", menu.getItems().get(3).getName());
            Assertions.assertEquals(130, menu.getItems().get(3).getPrice());
        }
    }


}
