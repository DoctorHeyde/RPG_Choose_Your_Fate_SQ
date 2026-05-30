import { test } from '@playwright/test';

test('debug env', async () => {
    console.log('API_URL:', process.env.API_URL);
    console.log('ADMIN_TOKEN:', process.env.ADMIN_TOKEN?.substring(0, 20));
});