const { Before, After, setDefaultTimeout } = require('@cucumber/cucumber');
const { chromium } = require('@playwright/test');

// Timeout global para cada step (30 segundos)
setDefaultTimeout(30 * 1000);

let browser;

// Se ejecuta UNA VEZ antes de todos los escenarios
Before({ tags: '@all' }, async function () {});

// Se ejecuta antes de CADA escenario
Before(async function () {
    // Lanzar el navegador si no está abierto
    if (!browser) {
        browser = await chromium.launch({
            headless: false, // Navegador visible
            slowMo: 500,     // Pausa de 500ms entre acciones para poder seguirlas visualmente
        });
    }

    // Crear un contexto nuevo (equivale a una sesión limpia)
    this.context = await browser.newContext({
        viewport: { width: 1280, height: 720 },
    });

    // Crear una nueva página
    this.page = await this.context.newPage();

    // Capturar errores de consola del navegador (opcional, útil para debug)
    this.page.on('console', (msg) => {
        if (msg.type() === 'error') {
            console.error('[Browser Error]', msg.text());
        }
    });
});

// Se ejecuta después de CADA escenario
After(async function (scenario) {
    // Si el escenario falló, tomar captura de pantalla
    if (scenario.result?.status === 'FAILED') {
        const screenshotName = scenario.pickle.name
            .replace(/\s+/g, '_')
            .toLowerCase();

        const screenshot = await this.page.screenshot({ fullPage: true });
        this.attach(screenshot, 'image/png');

        console.log(`❌ Escenario fallido: ${scenario.pickle.name}`);
    }

    // Cerrar página y contexto
    if (this.page) await this.page.close();
    if (this.context) await this.context.close();
});

// Cerrar el navegador al terminar todos los tests
process.on('exit', async () => {
    if (browser) await browser.close();
});