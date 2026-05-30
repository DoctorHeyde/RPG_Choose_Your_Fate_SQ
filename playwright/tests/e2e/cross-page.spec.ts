import { test, expect } from '@playwright/test';

test.beforeEach(async ({ page }) => {
  await page.goto('');
});

test('test', async ({ page }) => {
  
});

//Create test that confirms that when you enter the base page (login page), you can switch to the register page by using the button.
//Create a test of the reverse scenario i.e from register page to login page. THEY MUST NOT BE THE SAME TEST!!!

//Create test that creates new account on register page and then is capable of logging in with that same account after being redirected to the login page and that the account page is then opened.