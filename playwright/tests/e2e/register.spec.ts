import { test, expect } from '@playwright/test';
import { expectAlert } from '../helpers/helper-functions';

test.beforeEach(async ({ page }) => {
  await page.goto('/register');
  await expect(page).toHaveURL(/.*\/register/);
});

//Happy path for creating user
test('create user', async ({ page }) => {
  await expect(page.getByRole('textbox', { name: 'Username' })).toBeVisible();
  await expect(page.getByRole('textbox', { name: 'email' })).toBeVisible();
  await expect(page.getByRole('textbox', { name: 'Password' })).toBeVisible();
  await expect(page.getByRole('button', { name: 'Register new account' })).toBeVisible();

  const username = Math.random().toString(36).substring(2, 10);
  const email = Math.random().toString(36).substring(2, 10) + "test.dk";

  await page.getByRole('textbox', { name: 'Username' }).fill(username);
  await page.getByRole('textbox', { name: 'email' }).fill(email);
  await page.getByRole('textbox', { name: 'Password' }).click();
  await page.getByRole('textbox', { name: 'Password' }).fill('2');
  await page.getByRole('button', { name: 'Register new account' }).click();

  await expect(page).not.toHaveURL(/.*\/register/);
  await expect(page.getByRole('button', { name: 'Login' })).toBeVisible();
  await expect(page.getByRole('button', { name: 'Go to Register' })).toBeVisible();
  await expect(page.locator('#root')).toMatchAriaSnapshot(`
    - heading "Login" [level=1]
    - textbox "Username"
    - textbox "Password"
    - button "Go to Register"
    - button "Login"
    `);
});

//Should encounter an alert that has a message.
test('register account Username already exists', async ({ page }) => {
    await expect(page.getByRole('textbox', { name: 'Username' })).toBeVisible();
    await expect(page.getByRole('textbox', { name: 'email' })).toBeVisible();
    await expect(page.getByRole('textbox', { name: 'Password' })).toBeVisible();
    await expect(page.getByRole('button', { name: 'Register new account' })).toBeVisible();

    const email = Math.random().toString(36).substring(2, 10) + "test.dk";

    await page.getByRole('textbox', { name: 'Username' }).fill("admin"); // This name is apart of our seed data.
    await page.getByRole('textbox', { name: 'email' }).fill(email); //email should not break it.
    await page.getByRole('textbox', { name: 'Password' }).click();
    await page.getByRole('textbox', { name: 'Password' }).fill('2');

    const alertPromise = expectAlert(page); //Should always be placed before the action that activates the alert

    await page.getByRole('button', { name: 'Register new account' }).click();

    await alertPromise; //This is what actually handles the alert popping up

    await expect(page).toHaveURL(/.*\/register/);

    await expect(page.getByRole('textbox', { name: 'Username' })).toBeVisible();
    await expect(page.getByRole('textbox', { name: 'email' })).toBeVisible();
    await expect(page.getByRole('textbox', { name: 'Password' })).toBeVisible();
    await expect(page.getByRole('button', { name: 'Register new account' })).toBeVisible();
});

//Should encounter an alert that has a message.
test('register account email already exists', async ({ page }) => {
    await expect(page.getByRole('textbox', { name: 'Username' })).toBeVisible();
    await expect(page.getByRole('textbox', { name: 'email' })).toBeVisible();
    await expect(page.getByRole('textbox', { name: 'Password' })).toBeVisible();
    await expect(page.getByRole('button', { name: 'Register new account' })).toBeVisible();

    const username = Math.random().toString(36).substring(2, 10);
    const email = Math.random().toString(36).substring(2, 10) + "test.dk";

    await page.getByRole('textbox', { name: 'Username' }).fill(username); // This name is apart of our seed data.
    await page.getByRole('textbox', { name: 'email' }).fill("admin@chooseyourfate.dk"); //email should not break it.
    await page.getByRole('textbox', { name: 'Password' }).click();
    await page.getByRole('textbox', { name: 'Password' }).fill('2');

    const alertPromise = expectAlert(page); //Should always be placed before the action that activates the alert

    await page.getByRole('button', { name: 'Register new account' }).click();

    await alertPromise; //This is what actually handles the alert popping up

    await expect(page).toHaveURL(/.*\/register/);

    await expect(page.getByRole('textbox', { name: 'Username' })).toBeVisible();
    await expect(page.getByRole('textbox', { name: 'email' })).toBeVisible();
    await expect(page.getByRole('textbox', { name: 'Password' })).toBeVisible();
    await expect(page.getByRole('button', { name: 'Register new account' })).toBeVisible();
});