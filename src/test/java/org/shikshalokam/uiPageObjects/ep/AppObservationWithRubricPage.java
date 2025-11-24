package org.shikshalokam.uiPageObjects.ep;
import com.microsoft.playwright.options.AriaRole;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.shikshalokam.uiPageObjects.PWBasePage;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class AppObservationWithRubricPage extends PWBasePage
{
    AppObservationWithRubricPage OBSWith;
    private static final Logger logger = LogManager.getLogger(AppObservationWithoutRubricPage.class);

    public AppObservationWithRubricPage(String givenTitleName)
    {
        super(givenTitleName);
        this.OBSWith = this;

    }



}
