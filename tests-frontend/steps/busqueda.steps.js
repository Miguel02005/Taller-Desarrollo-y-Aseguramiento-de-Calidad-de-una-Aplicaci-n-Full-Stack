const { Given, When, Then } = require('@cucumber/cucumber');
const { expect } = require('@playwright/test');

Given('I am on the Smart Book Finder home page', async function () {
    await this.page.goto('http://localhost:5173');
    await this.page.waitForSelector('.site-header', { timeout: 10000 });
});

When('I type {string} in the title field', async function (text) {
    await this.page.locator('#book-title').fill(text);
});

When('I type {string} in the author field', async function (text) {
    await this.page.locator('#book-author').fill(text);
});

When('I click the {string} button', async function (name) {
    await this.page.getByRole('button', { name: name }).click();
});

When('I click the search panel header', async function () {
    await this.page.locator('.search-header').click();
});

Then('I should see the section {string}', async function (title) {
    await expect(this.page.locator('h1', { hasText: title })).toBeVisible({ timeout: 15000 });
});

Then('I should see at least one book in the grid', async function () {
    await expect(this.page.locator('.books-grid')).toBeVisible({ timeout: 15000 });
    await expect(this.page.locator('.book-card').first()).toBeVisible({ timeout: 15000 });
});

Then('I should see the error message {string}', async function (message) {
    const error = this.page.locator('.search-error');
    await expect(error).toBeVisible();
    await expect(error).toContainText(message);
});

Then('the title field should be empty', async function () {
    await expect(this.page.locator('#book-title')).toHaveValue('');
});

Then('the search form should be hidden', async function () {
    await expect(this.page.locator('.search-form')).not.toBeVisible();
});

Then('the search form should be visible', async function () {
    await expect(this.page.locator('.search-form')).toBeVisible();
});

