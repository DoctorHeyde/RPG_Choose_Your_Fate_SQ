import { expect, Page, Dialog } from '@playwright/test';

export async function expectAlert(
    page: Page) {
    // We haven't made specific error messages for the frontend as we haven't had the time
    // It should be made for better frontend quality for the user, but it shouldn't exactly be the error messages we get from the backend
    // since these don't always make sence for a user. Therefore, currently the frontend just writes Login failed. 

    const dialog = await page.waitForEvent('dialog');

    expect(dialog.message()).toContain('Login failed');

    await dialog.accept();
}