package com.stgconsulting;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Richard Harkins on 7/15/2016.
 */
@SuppressWarnings("Since15")
public class Utils extends SeleniumWebdriverBaseClass
{
    public Utils() throws IOException {
    }

    List<WebsiteWordsRecord> webpageWordsList = new ArrayList<WebsiteWordsRecord>();
    List<TotalWordsRecord> allWordsList = new ArrayList<TotalWordsRecord>();

    @Parameters({"webPageURL", "titleStringToTest"})
    @Test
    public void verifyPageTitle(String webPageURL, String titleStringToTest) throws IOException, InterruptedException {
        // Open Webpage URL
        driver.get(webPageURL);

/*
        // Get Html WebElement
        WebElement htmlElement = driver.findElement(By.xpath("/html"));
        String allTextOnPage = htmlElement.getText();
//        String newLinesRemoved = allTextOnPage.replaceAll("[\\s]+|[\\n]+|[\\u25BC]+|[\\&]+|:|\\\\|\\.|,|\\?|©", " ");
        String newLinesRemoved = allTextOnPage.replaceAll("[^A-Za-z]+", " ");
        String spaceDelimiterToOne = newLinesRemoved.replaceAll("\\s+", " ");
        String[] stringArray = spaceDelimiterToOne.split("[\\s]");
        for (String arrayValue : stringArray)
        {
            WebsiteWordsRecord initialWordsRecord = new WebsiteWordsRecord();
            initialWordsRecord.setWord(arrayValue);
            initialWordsRecord.setWebsite(webPageURL);
            initialWordsRecord.setOccurrences(1);
            if (webpageWordsList.contains(initialWordsRecord))
            {
                int initialWordsrecordIndex = webpageWordsList.indexOf(initialWordsRecord);
                WebsiteWordsRecord updateRecord = new WebsiteWordsRecord();
                updateRecord = webpageWordsList.get(initialWordsrecordIndex);
                updateRecord.setOccurrences(updateRecord.getOccurrences()+1);
//                webpageWordsList.add(initialWordsrecordIndex, updateRecord);
            }
            else
            {
                webpageWordsList.add(initialWordsRecord);
            }
//            initialWordsRecord.setWord(null);
//            initialWordsRecord.setWebsite(null);
//            initialWordsRecord.setOccurrences(0);
        }
        WebsiteWordsRecord wordsRecordTest = new WebsiteWordsRecord();
        wordsRecordTest.setWord("CEDAR");
        wordsRecordTest.setWebsite("https://www.skiutah.com/");
        wordsRecordTest.setOccurrences(10);
        boolean containsTest = webpageWordsList.contains(wordsRecordTest);
*/


        // Get page title of current page
        String pageTitle = driver.getTitle();
        // Print page title of current page
        consoleAndOutputFilePrint(automation_code_challenge_8_console_bw, "Page title of current page is: " + pageTitle);
        // Print title string to test
        consoleAndOutputFilePrint(automation_code_challenge_8_console_bw, "Title String to Test is: " + titleStringToTest);
        // Test that the titleStringToTest = title of current page
        Assert.assertTrue(pageTitle.equals(titleStringToTest), "Current Page Title is not equal to the expected page title value");
        // If there is no Assertion Error, Print out that the Current Page Title = Expected Page Title
        consoleAndOutputFilePrint(automation_code_challenge_8_console_bw, "Current Page Title = Expected Page Title");

    }

    public void verifyNavigation(String navigationMenu, String validationString) throws IOException, InterruptedException {
        // Build CSS Selector based on navigation menu user wants to click on
        String cssSelectorText = "a[title='" + navigationMenu + "']";
        // Find menu WebElement based on CSS Selector
        WebElement navigationMenuWebElement = driver.findElement(By.cssSelector(cssSelectorText));
        // Get href attributte from menu WebElement
        String navigationMenuURL = navigationMenuWebElement.getAttribute("href");
        // Navigate to href and validate page title
        verifyPageTitle(navigationMenuURL, validationString);
    }

    public void subMenuNavigation(String navigationMenu, String navigationSubMenu) throws InterruptedException, IOException {
        // Build CSS Selector based on navigation menu user wants to click on
        String cssSelectorTextNavigationMenu = "a[title='" + navigationMenu + "']";
        // Find menu WebElement based on CSS Selector
        Boolean isPresent = driver.findElements(By.cssSelector(cssSelectorTextNavigationMenu)).size() == 1;
        // Check if navigation menu item exists
        if (isPresent)
        {
            // Get navigation menu WebElement
            WebElement navigationMenuWebElement = driver.findElement(By.cssSelector(cssSelectorTextNavigationMenu));
            // Get href attributte from navigation menu WebElement
            String navigationMenuURL = navigationMenuWebElement.getAttribute("href");
            //Create Actions object
            Actions mouseHover = new Actions(driver);
            // Move to navigation menu WebElement to initiate a hover event
            mouseHover.moveToElement(navigationMenuWebElement).perform();
            //String cssSelectorTextSubMenu = "a[title='" + navigationSubMenu + "']";
            // Build navigation submenu xpath to anchor tag
            String xpathSelectorTextSubmenu = "//a[.='" + navigationSubMenu + "']";
            //WebElement navigationSubMenuWebElement = driver.findElement(By.linkText(navigationSubMenu));
            // Get navigation submenu WebElement
            WebElement navigationSubMenuWebElement = driver.findElement(By.xpath(xpathSelectorTextSubmenu));
            // Check if navigation submenu exists
            Assert.assertTrue(navigationSubMenuWebElement.isEnabled(), (navigationSubMenu + " navigation submenu does not exist on this page"));
            // Click on navigation submenu WebElement
            navigationSubMenuWebElement.click();
            //mouseHover.perform();
            // Navigate to href and validate page title
            //VerifyPageTitle(navigationMenuURL, "Ski and Snowboard The Greatest Snow on Earth® - Ski Utah");
        }
        else
        {
            // Print message indicating that the navigation menu passed in to this method does not exist on the page
            consoleAndOutputFilePrint(automation_code_challenge_8_console_bw, navigationMenu + " navigation menu does not exist on this page");
        }
    }

    // This method accepts string parameters for the search dialogs
    public void searchForDialog(String whatParameter, String byResortParameter, String subCategoryParameter) throws IOException {
        Boolean searchDialogPresent = driver.findElements(By.xpath(".//*[@class='ListingFilter']")).size() == 1;
        // Check if the Search Dialog exists on this page
        if (searchDialogPresent)
        {
            Select whatParameterDropdown = new Select(driver.findElement(By.xpath(".//*[@name='filter-category-select']")));
            Select byResortParameterDropdown = new Select(driver.findElement(By.xpath(".//*[@name='filter-resort-select']")));
            Boolean subCategoryDropdownPresent = driver.findElements(By.xpath(".//*[@name='filter-sub-category-select']")).size() == 1;
            WebElement okButton = driver.findElement(By.xpath(".//*[@type='submit'][@value='OK']"));

            whatParameterDropdown.selectByVisibleText(whatParameter);
            byResortParameterDropdown.selectByVisibleText(byResortParameter);

            // Check if subcategory dropdown is present
            if (subCategoryDropdownPresent)
            {
                Select subCategoryParameterDropdown = new Select(driver.findElement(By.xpath(".//*[@name='filter-sub-category-select']")));
                subCategoryParameterDropdown.selectByVisibleText(subCategoryParameter);
            }

            // Start search
            okButton.click();

            // Get all search result elements
            List<WebElement> pageSearchResultElements = driver.findElements(By.xpath(".//*[@class='ListingResults-item']"));
            // Get the Next Page Button Element (if it exists, or null if it does not exist)
            List<WebElement> nextPageButton = driver.findElements(By.xpath(".//*[@class='BatchLinks-next ']"));
            // Write search results header if there is at least 1 element in the search result
            if (pageSearchResultElements.size() > 0) {
                consoleAndOutputFilePrint(automation_code_challenge_8_console_bw, "");
                consoleAndOutputFilePrint(automation_code_challenge_8_console_bw, "Search Results");
                consoleAndOutputFilePrint(automation_code_challenge_8_console_bw, "");
            }
            // Print this page's search results to the console
            consoleSearchPrint(pageSearchResultElements);
            // Print this page's search results to the output file
            outputFileSearchPrint(pageSearchResultElements);

            // Execute these statements as long as there is a Next Page Button active
            while (nextPageButton.size() == 1)
            {
                nextPageButton.get(0).click();
                pageSearchResultElements = driver.findElements(By.xpath(".//*[@class='ListingResults-item']"));
                consoleSearchPrint(pageSearchResultElements);
                outputFileSearchPrint(pageSearchResultElements);

                nextPageButton.clear();
                nextPageButton = driver.findElements(By.xpath(".//*[@class='BatchLinks-next ']"));
            }
        }
        // If search dialog does not exist on this page
        else
        {
            // Print message indicating that the search dialog does not exist on the page
            consoleAndOutputFilePrint(automation_code_challenge_8_console_bw, "The current page does not contain a Search For dialog box");
        }

    }

    // This method accepts a list of WebElements and prints the text of those elements and subelements to the console
    public void consoleSearchPrint(List<WebElement> resultList) throws IOException {
        for (int listIndex = 0; listIndex < resultList.size(); listIndex++)
        {
            consoleAndOutputFilePrint(automation_code_challenge_8_console_bw, resultList.get(listIndex).getText());
            consoleAndOutputFilePrint(automation_code_challenge_8_console_bw, "--------------------");
        }

    }

    // This method accepts a list of WebElements and prints the text of those elements and subelements to an output file
    public void outputFileSearchPrint(List<WebElement> resultList) throws IOException
    {
        for (int listIndex = 0; listIndex < resultList.size(); listIndex++)
        {
            automation_code_challenge_8_console_bw.write(resultList.get(listIndex).getText());
            automation_code_challenge_8_console_bw.newLine();
            automation_code_challenge_8_console_bw.write("--------------------");
            automation_code_challenge_8_console_bw.newLine();
        }
    }

    @Test
    public void cleanup() throws IOException
    {
//        automation_code_challenge_7_webpage_word_list_bw.close();
//        automation_code_challenge_7_total_word_list_bw.close();
        automation_code_challenge_8_console_bw.close();
        automation_code_challenge_8_good_images_list_bw.close();
        automation_code_challenge_8_broken_images_list_bw.close();
        automation_code_challenge_8_broken_images_list_unknown_host_exception_bw.close();
    }

    @Test
    // This method crawls every page of a domain
    public void webCrawler() throws InterruptedException, IOException {
        LocalDateTime crawlerStartTime = LocalDateTime.now();
        List<WebElement> anchorTags = null;
        List<String> hrefAttributeValues = new ArrayList<String>();
        List<String> pagesToVisit = new ArrayList<String>();
        List<String> pagesVisited = new ArrayList<String>();
        List<String> imageSrcAttributeValues = new ArrayList<String>();
        List<String> imagesToVisit = new ArrayList<String>();
        List<String> imagesVisited = new ArrayList<String>();
        String currentPageURL = baseWebPageURL;
        String regexRemoveSpecialCharacters = "[\\s]+|[\\n]+|▼|&|:|\\\\|\\.|,|\\?|©";
        String regexRemoveRemainingSpaces = "[\\s]+";
        List<WebElement> bodyTags = null;
        pagesVisited.add(baseWebPageURL);
        consoleAndOutputFilePrint(automation_code_challenge_8_console_bw, "WebCrawler Start Time - " + crawlerStartTime);
        consoleAndOutputFilePrint(automation_code_challenge_8_console_bw, "pagesVisited size = " + pagesVisited.size());
        consoleAndOutputFilePrint(automation_code_challenge_8_console_bw, "pagesVisited website = " + pagesVisited.get(pagesVisited.size()-1));

//        anchorTags = driver.findElements(By.xpath(".//a[@href[starts-with(.,'https://www.skiutah.com') and not(contains(., '@@')) and not(contains(., '?'))]]"));
        anchorTags = driver.findElements(By.xpath(".//a[@href[starts-with(.,'https://www.skiutah.com') and not(contains(., '@@')) and not(contains(., 'blog')) and not(contains(., '?'))]]"));
//        anchorTags = driver.findElements(By.xpath(".//a[@href[starts-with(.,'https://www.skiutah.com') or starts-with(.,'/')]]"));
//        anchorTags = driver.findElements(By.xpath(".//a[@href[starts-with(.,'/')]]"));


        bodyTags = driver.findElements(By.xpath("/html/body"));

        // Broken Image Processing Start - Home Page
        // Start Broken Images processing
        // Check that src attribute is not null, does not contain base64 images (jre does not process these), and does not contain html5 <embed> tag
        List<WebElement> imageElements = driver.findElements(By.xpath("//img[@src[(string()) and not(contains(., 'base64')) and (starts-with(., 'http')) and not(contains(., '<embed'))]]"));
//        List<WebElement> imageElements = driver.findElements(By.xpath("//*[@src[(string()) and not(contains(., 'base64'))]]"));
        // Get all image src attribute values on current page
        for (WebElement imageTagElement : imageElements)
        {
            imageSrcAttributeValues.add(imageTagElement.getAttribute("src"));
        }
        // Check that the src values have not been previously visited
        for (String srcValues : imageSrcAttributeValues)
        {
            if (!imagesVisited.contains(srcValues) && !imagesToVisit.contains(srcValues))
            {
                imagesToVisit.add(srcValues);
//                consoleAndOutputFilePrint(automation_code_challenge_8_console_bw, srcValues);
            }
        }
        for (String imageIndex : imagesToVisit)
        {
//            consoleAndOutputFilePrint(automation_code_challenge_8_console_bw, imageIndex);
            URL initialWebsiteURL = new URL(imageIndex);
            HttpURLConnection initialHttpUrlConnection = (HttpURLConnection) initialWebsiteURL.openConnection();
            try
            {
                int initialResponseCode = initialHttpUrlConnection.getResponseCode();
            }
            catch (UnknownHostException e)
            {
                consoleAndOutputFilePrint(automation_code_challenge_8_console_bw, "Host IP address could not be determined - " + imageIndex);
                outputFilePrint(automation_code_challenge_8_broken_images_list_unknown_host_exception_bw, "Host IP address could not be determined - " + imageIndex);
                initialHttpUrlConnection.disconnect();
                imagesVisited.add(imageIndex);
                continue;
            }
            int initialResponseCode = initialHttpUrlConnection.getResponseCode();
            consoleAndOutputFilePrint(automation_code_challenge_8_console_bw, "Response code for " + imageIndex + " is " + initialResponseCode);
            if (initialResponseCode == 200)
            {
                automation_code_challenge_8_good_images_list_bw.write('"' + imageIndex + '"' + ", " + '"' + initialResponseCode + '"');
                automation_code_challenge_8_good_images_list_bw.newLine();
            }
            else if (initialResponseCode != 200)
            {
                automation_code_challenge_8_broken_images_list_bw.write('"' + imageIndex + '"' + ", " + '"' + initialResponseCode + '"');
                automation_code_challenge_8_broken_images_list_bw.newLine();
            }
            initialHttpUrlConnection.disconnect();
            imagesVisited.add(imageIndex);
        }
//        automation_code_challenge_8_broken_images_list_bw.close();
        imagesToVisit.clear();
        // End Broken Image Processing
        // Broken Image Processing End - Home Page

        // Get all href attribute values on current page
        for (WebElement anchorTagElement : anchorTags)
        {
            hrefAttributeValues.add(anchorTagElement.getAttribute("href"));
        }
        // Check that the href values have not been previously visited
        for (String hrefValues : hrefAttributeValues)
        {
            if (!pagesVisited.contains(hrefValues) && !pagesToVisit.contains(hrefValues))
            {
                pagesToVisit.add(hrefValues);
                consoleAndOutputFilePrint(automation_code_challenge_8_console_bw, hrefValues);
            }
        }
        consoleAndOutputFilePrint(automation_code_challenge_8_console_bw, pagesToVisit.size() + " - pages left to visit");
        consoleAndOutputFilePrint(automation_code_challenge_8_console_bw, "--------------------");
        // Remove current page from pagesToVisit List
        pagesToVisit.remove(currentPageURL);
        // Loop through pagesToVisit List until list is empty
        while (pagesToVisit.size() > 0)
        // Go to next page in Pages to visit list
        {
            anchorTags.clear();
            driver.get(pagesToVisit.get(0));
            pagesVisited.add(pagesToVisit.get(0));
            consoleAndOutputFilePrint(automation_code_challenge_8_console_bw, "pagesVisited size = " + pagesVisited.size());
            consoleAndOutputFilePrint(automation_code_challenge_8_console_bw, "pagesVisited website = " + pagesVisited.get(pagesVisited.size()-1));
            currentPageURL = pagesToVisit.get(0);
//        anchorTags = driver.findElements(By.xpath(".//a[@href[starts-with(.,'https://www.skiutah.com') and not(contains(., '@@')) and not(contains(., '?'))]]"));
            anchorTags = driver.findElements(By.xpath(".//a[@href[starts-with(.,'https://www.skiutah.com') and not(contains(., '@@')) and not(contains(., 'blog')) and not(contains(., '?'))]]"));
//        anchorTags = driver.findElements(By.xpath(".//a[@href[starts-with(.,'https://www.skiutah.com') or starts-with(.,'/')]]"));
//        anchorTags = driver.findElements(By.xpath(".//a[@href[starts-with(.,'/')]]"));

            // Start Broken Images processing
            // Check that src attribute is not null, does not contain base64 images (jre does not process these), and does not contain html5 <embed> tag
            imageElements = driver.findElements(By.xpath("//img[@src[(string()) and not(contains(., 'base64')) and (starts-with(., 'http')) and not(contains(., '<embed'))]]"));
            // Get all image src attribute values on current page
            for (WebElement imageTagElement : imageElements)
            {
                imageSrcAttributeValues.add(imageTagElement.getAttribute("src"));
            }
            // Check that the src values have not bveen previously visited
            for (String srcValues : imageSrcAttributeValues)
            {
                if (!imagesVisited.contains(srcValues) && !imagesToVisit.contains(srcValues))
                {
                    imagesToVisit.add(srcValues);
//                    consoleAndOutputFilePrint(automation_code_challenge_8_console_bw, srcValues);
                }
            }
            for (String imageIndex : imagesToVisit)
            {
//                Thread.sleep(75);
//                consoleAndOutputFilePrint(automation_code_challenge_8_console_bw, imageIndex);
                URL initialWebsiteURL = new URL(imageIndex);
                HttpURLConnection initialHttpUrlConnection = (HttpURLConnection) initialWebsiteURL.openConnection();
                try
                {
                    int initialResponseCode = initialHttpUrlConnection.getResponseCode();
                }
                catch (UnknownHostException e)
                {
                    consoleAndOutputFilePrint(automation_code_challenge_8_console_bw, "Host IP address could not be determined - " + imageIndex);
                    outputFilePrint(automation_code_challenge_8_broken_images_list_unknown_host_exception_bw, "Host IP address could not be determined - " + imageIndex);
                    initialHttpUrlConnection.disconnect();
                    imagesVisited.add(imageIndex);
                    continue;
                }
                int initialResponseCode = initialHttpUrlConnection.getResponseCode();
                consoleAndOutputFilePrint(automation_code_challenge_8_console_bw, "Response code for " + imageIndex + " is " + initialResponseCode);
                if (initialResponseCode == 200)
                {
                    automation_code_challenge_8_good_images_list_bw.write('"' + imageIndex + '"' + ", " + '"' + initialResponseCode + '"');
                    automation_code_challenge_8_good_images_list_bw.newLine();
                }
                else if (initialResponseCode != 200)
                {
                    automation_code_challenge_8_broken_images_list_bw.write('"' + imageIndex + '"' + ", " + '"' + initialResponseCode + '"');
                    automation_code_challenge_8_broken_images_list_bw.newLine();
                }
                initialHttpUrlConnection.disconnect();
                imagesVisited.add(imageIndex);
            }
            imagesToVisit.clear();
            // End Broken Image Processing


            // Get all href attribute values on current page
            for (WebElement anchorTagElement : anchorTags)
            {
                hrefAttributeValues.add(anchorTagElement.getAttribute("href"));
            }
            // Check that the href values have not been previously visited
            for (String hrefValues : hrefAttributeValues)
            {
                if (!pagesVisited.contains(hrefValues) && !pagesToVisit.contains(hrefValues))
                {
                    pagesToVisit.add(hrefValues);
                    consoleAndOutputFilePrint(automation_code_challenge_8_console_bw, hrefValues);
                }
            }
            consoleAndOutputFilePrint(automation_code_challenge_8_console_bw, pagesToVisit.size() + " - pages left to visit");
            consoleAndOutputFilePrint(automation_code_challenge_8_console_bw, "--------------------");

/*
            // Webpage words processing - begin
            WebElement htmlElement = driver.findElement(By.xpath("/html"));
            String allTextOnPage = htmlElement.getText();
//            String newLinesRemoved = allTextOnPage.replaceAll("[\\s]+|[\\n]+|[\\u25BC]+|[\\&]+|:|\\\\|\\.|,|\\?|©|®|[(]+|[)]+", " ");
            String newLinesRemoved = allTextOnPage.replaceAll("[^A-Za-z]+", " ");
            String spaceDelimiterToOne = newLinesRemoved.replaceAll("\\s+", " ");
            String[] stringArray = spaceDelimiterToOne.split("[\\s]");
            for (String arrayValue : stringArray)
            {
                WebsiteWordsRecord initialWordsRecord = new WebsiteWordsRecord();
                initialWordsRecord.setWord(arrayValue);
                initialWordsRecord.setWebsite(currentPageURL);
                initialWordsRecord.setOccurrences(1);
                if (webpageWordsList.contains(initialWordsRecord))
                {
                    int initialWordsrecordIndex = webpageWordsList.indexOf(initialWordsRecord);
                    WebsiteWordsRecord updateRecord = new WebsiteWordsRecord();
                    updateRecord = webpageWordsList.get(initialWordsrecordIndex);
                    updateRecord.setOccurrences(updateRecord.getOccurrences()+1);
//                webpageWordsList.add(initialWordsrecordIndex, updateRecord);
                }
                else
                {
                    webpageWordsList.add(initialWordsRecord);
                }
//            initialWordsRecord.setWord(null);
//            initialWordsRecord.setWebsite(null);
//            initialWordsRecord.setOccurrences(0);
            }
            // Webpage word processing - end
*/

            // Remove current page from pagesToVisit List
            pagesToVisit.remove(currentPageURL);
            // Clear out hrefAttributeValues list
            hrefAttributeValues.clear();

        }

        // Populate Total Words Array List
        for (WebsiteWordsRecord WebsiteWordsIndex : webpageWordsList)
        {
            TotalWordsRecord totalRecord = new TotalWordsRecord();
            totalRecord.setWord(WebsiteWordsIndex.getWord());
            totalRecord.setOccurrences(WebsiteWordsIndex.getOccurrences());
            if (allWordsList.contains(totalRecord))
            {
                int totalWordsIndex = allWordsList.indexOf(totalRecord);
                TotalWordsRecord totalWordsRecordUpdate = new TotalWordsRecord();
                totalWordsRecordUpdate = allWordsList.get(totalWordsIndex);
                totalWordsRecordUpdate.setOccurrences(totalWordsRecordUpdate.getOccurrences() + WebsiteWordsIndex.getOccurrences());
            }
            else
            {
                allWordsList.add(totalRecord);
            }
        }

        consoleAndOutputFilePrint(automation_code_challenge_8_console_bw, String.valueOf(pagesVisited.size()));
        LocalDateTime crawlerEndTime = LocalDateTime.now();
        consoleAndOutputFilePrint(automation_code_challenge_8_console_bw, "WebCrawler End Time - " + crawlerEndTime);
        Duration elapsedCrawlerTime = Duration.between(crawlerStartTime, crawlerEndTime);
        long elapsedCrawlerTimeInMinutes = elapsedCrawlerTime.toMinutes();
        long elapsedCrawlerTimeInHours = elapsedCrawlerTime.toHours();
        Duration elapsedCrawlerTimeRemainingMinutesDuration = elapsedCrawlerTime.minusHours(elapsedCrawlerTimeInHours);
        long elapsedCrawlerTimeRemainingMinutes = elapsedCrawlerTimeRemainingMinutesDuration.toMinutes();
        if (elapsedCrawlerTimeRemainingMinutes == 1)
        {
            consoleAndOutputFilePrint(automation_code_challenge_8_console_bw, "Total WebCrawler Elapsed Time - " + elapsedCrawlerTimeInHours + " hours" + " " + elapsedCrawlerTimeRemainingMinutes + " minute");
        }
        else
        {
            consoleAndOutputFilePrint(automation_code_challenge_8_console_bw, "Total WebCrawler Elapsed Time - " + elapsedCrawlerTimeInHours + " hours" + " " + elapsedCrawlerTimeRemainingMinutes + " minutes");

        }

    }

    public void printWebpageWordsList() throws IOException {
        for (WebsiteWordsRecord wordsRecordIndex : webpageWordsList)
        {
//            consoleAndOutputFilePrint(automation_code_challenge_8_console_bw, '"' + wordsRecordIndex.getWord() + '"' + ", " + '"' + wordsRecordIndex.getWebsite() + '"' + ", " + '"' + wordsRecordIndex.getOccurrences() + '"');
//            automation_code_challenge_7_webpage_word_list_bw.write('"' + wordsRecordIndex.getWord() + '"' + ", " + '"' + wordsRecordIndex.getWebsite() + '"' + ", " + '"' + wordsRecordIndex.getOccurrences() + '"');
//            automation_code_challenge_7_webpage_word_list_bw.newLine();
        }
    }

    public void printTotalWordsList() throws IOException {
        for (TotalWordsRecord totalWordsRecordIndex : allWordsList)
        {
//            consoleAndOutputFilePrint(automation_code_challenge_8_console_bw, '"' + totalWordsRecordIndex.getWord() + '"' + ", " + '"' + totalWordsRecordIndex.getOccurrences() + '"');
//            automation_code_challenge_7_total_word_list_bw.write('"' + totalWordsRecordIndex.getWord() + '"' + ", " + '"' + totalWordsRecordIndex.getOccurrences() + '"');
//            automation_code_challenge_7_total_word_list_bw.newLine();
        }
    }

    @Test
    // This method is the main method for launching tests
    public void testLauncher() throws InterruptedException, IOException {

        verifyPageTitle(baseWebPageURL, "Ski Utah - Ski Utah");

        webCrawler();
        // Test against Explore menu - no Search Dialog
//        verifyNavigation("Explore", "Utah Areas 101 - Ski Utah");

        // Test against Plan Your Trip Search Dialog
//        verifyNavigation("Plan Your Trip", "All Services - Ski Utah");
//        searchForDialog("Activities", "Snowbasin Resort", "All");

        // Test against Deals Search Dialog - no subcategory dropdown
//        verifyNavigation("Deals", "Ski and Snowboard The Greatest Snow on Earth® - Ski Utah");
//        searchForDialog("All", "All Resorts", "All");

//        printWebpageWordsList();

//        printTotalWordsList();

        cleanup();

//        HomePage.getResortMileageFromAirport("EAGLE point");
//        subMenuNavigation("Explore", "Stories - Photos - Videos");

    }
}
