const { Then} = require('@cucumber/cucumber');
const { expect } = require('@playwright/test');

Then('I should see the application title in the header', async function () {
    await expect(this.page.locator('.site-header')).toBeVisible();
    await expect(this.page.locator('.header-left')).toBeVisible();
});

Then('I should see the form field {string}', async function (field) {
    await expect(this.page.getByText(field, { exact: true })).toBeVisible();
});

Then('I should see the language selector', async function () {
    await expect(this.page.locator('#book-language')).toBeVisible();
});

Then('I should see the minimum year field', async function () {
    await expect(this.page.locator('#book-year')).toBeVisible();
});

Then('I should see the footer with the text {string}', async function (text) {
    const footer = this.page.locator('.site-footer');
    await expect(footer).toBeVisible();
    await expect(footer).toContainText(text);
});

