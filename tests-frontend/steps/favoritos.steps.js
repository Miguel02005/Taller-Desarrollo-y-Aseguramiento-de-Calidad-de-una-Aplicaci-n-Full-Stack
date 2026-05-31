const { Given, When, Then } = require('@cucumber/cucumber');
const { expect } = require('@playwright/test');

Given('I have at least one book in favorites', async function () {
    await this.page.waitForSelector('.book-card', { timeout: 15000 });
    await this.page.locator('.fav-btn').first().click();
    await this.page.waitForSelector('.fav-message', { timeout: 8000 });
    // Esperar a que el backend guarde el libro en OpenLibrary
    await this.page.waitForTimeout(5000);
});

Given('the favorites panel is open', async function () {
    await this.page.locator('.header-fav-btn').click();
    await this.page.waitForSelector('.favorites-panel', { timeout: 5000 });
    // Esperar a que el panel cargue los datos
    await this.page.waitForTimeout(3000);
});

When('I click the heart button of the first book', async function () {
    await this.page.waitForSelector('.book-card', { timeout: 15000 });
    await this.page.locator('.fav-btn').first().click();
});

When('I click the favorites button in the header', async function () {
    await this.page.locator('.header-fav-btn').click();
});

When('I click the remove button of the first favorite', async function () {
    await this.page.locator('.favorite-remove').first().click();
});

When('I click the close button of the panel', async function () {
    await this.page.locator('.favorites-close').click();
});

Then('I should see the message {string}', async function (message) {
    const selectors = ['.fav-message', '.favorites-empty h3', '.search-error'];
    let found = false;
    for (const sel of selectors) {
        try {
            await this.page.waitForSelector(sel, { timeout: 3000 });
            const el = this.page.locator(sel, { hasText: message });
            if (await el.isVisible().catch(() => false)) {
                found = true;
                break;
            }
        } catch (_) {}
    }
    expect(found, `Message not found: "${message}"`).toBe(true);
});

Then('the favorites counter in the header should be {string}', async function (number) {
    await expect(this.page.locator('.fav-badge')).toBeVisible({ timeout: 8000 });
    await expect(this.page.locator('.fav-badge')).toHaveText(number);
});

Then('I should see the panel {string}', async function (title) {
    const panel = this.page.locator('.favorites-panel');
    await expect(panel).toBeVisible({ timeout: 5000 });
    await expect(panel.locator('h2', { hasText: title })).toBeVisible();
});

Then('I should see the saved book in the list', async function () {
    // Esperar hasta 20s porque el backend llama a OpenLibrary
    await expect(this.page.locator('.favorite-item').first()).toBeVisible({ timeout: 20000 });
});

Then('the book should disappear from the favorites list', async function () {
    // Esperar a que el backend procese el delete y el frontend refresque
    await this.page.waitForTimeout(3000);
    const count = await this.page.locator('.favorite-item').count();
    const isEmpty = await this.page.locator('.favorites-empty').isVisible().catch(() => false);
    expect(count === 0 || isEmpty, `Expected panel to be empty but found ${count} items`).toBe(true);
});

Then('the favorites panel should be closed', async function () {
    await expect(this.page.locator('.favorites-panel')).not.toBeVisible({ timeout: 5000 });
});

