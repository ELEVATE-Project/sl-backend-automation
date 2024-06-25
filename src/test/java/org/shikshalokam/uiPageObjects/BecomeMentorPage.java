package org.shikshalokam.uiPageObjects;

import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class BecomeMentorPage extends PWBasePage{
	 
	private BecomeMentorPage mentorPage;
	    private static final Logger logger = LogManager.getLogger(BecomeMentorPage.class);
	    public BecomeMentorPage(String givenTitleName) {
	        super(givenTitleName);
	        this.mentorPage=this;
	    }

<<<<<<< Updated upstream
	public void  becomeMentor() throws InterruptedException {
        this.validPage();
	     page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Become a Mentor")).click();
	     page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Request to be a Mentor")).click();
   }
	
   public void  acceptAsMentor() throws InterruptedException {
		this.validPage();
		 page.getByText("Workspace").click();
		 page.locator("ion-card-content").filter(new Locator.FilterOptions().setHasText("Manage user")).click();
		 page.locator("//td[text()='UserSignup']/..//td[4]//ion-button[1]").click();
		 page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Yes")).click();
    }
   
   public void  becomeMentorSuccessfull() {
        this.validPage();
		 page.getByRole(AriaRole.NAVIGATION).getByText("Mentor", new Locator.GetByTextOptions()).isVisible();
	}
=======
	public BecomeMentorPage  requestToBeAMentor()  {
        this.validPage();
	     page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Request to be a Mentor")).click();
	     try {
	            TimeUnit.SECONDS.sleep(10);
		 } catch (InterruptedException e) {
			 logger.info("Exception from the sleep method "+ e.getMessage());
		 }
		 return mentorPage;
		 
		 
	}
  
>>>>>>> Stashed changes
}

	
	
