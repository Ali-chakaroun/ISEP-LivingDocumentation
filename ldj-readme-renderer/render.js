#!/usr/bin/env node

// Project-specific renderer that renders a README in GitHub Flavored Markdown,
// including a table of command-line options for the analyzer.
//
// Usage: node render.js /path/to/analyzed.json

const fs = require('node:fs');
const process = require('node:process');

const OUTPUT = `README.md`;

const TEMPLATE = `[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=Ali-chakaroun_ISEP-LivingDocumentation&metric=coverage)](https://sonarcloud.io/summary/new_code?id=Ali-chakaroun_ISEP-LivingDocumentation)
[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=Ali-chakaroun_ISEP-LivingDocumentation&metric=sqale_index)](https://sonarcloud.io/summary/new_code?id=Ali-chakaroun_ISEP-LivingDocumentation)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=Ali-chakaroun_ISEP-LivingDocumentation&metric=reliability_rating)](https://sonarcloud.io/summary/new_code?id=Ali-chakaroun_ISEP-LivingDocumentation)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=Ali-chakaroun_ISEP-LivingDocumentation&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=Ali-chakaroun_ISEP-LivingDocumentation)
# Living Documentation Java

This project is an extension of the [Living Documentation][ldoc] set of tools to
Java. As part of the Living Documentation toolchain, it can be used to generate
documentation and diagrams from source code so that documentation is always
up-to-date.

## Analyzer

The analyzer parses Java projects into Living Documentation JSON files that can
be rendered by Living Documentation renderers. It accepts the following command
line options:

| Short option | Long option | Description |
| ------------ | ----------- | ----------- |
~

[ldoc]: https://github.com/eNeRGy164/LivingDocumentation
`;

function render(d) {
  if (d.Name === 'addOption' || d.Name === 'addRequiredOption') {
    const args = d.Arguments.map(a => a.Text).map(JSON.parse);
    const short = args[0] ? `\`-${args[0]}\`` : '';
    const long = args[1] ? `\`--${args[1]}\`` : '';
    const required = d.Name.includes('Required') ? '(Required.)' : '';
    return `| ${short} | ${long} | ${args[3]} ${required} |\n`;
  } else if (d instanceof Array) {
    return d.map(render).join('');
  } else if (d instanceof Object) {
    return Object.values(d).map(render).join('');
  }
}

const json = JSON.parse(fs.readFileSync(process.argv[2]));
fs.writeFileSync(OUTPUT, TEMPLATE.replace('~', () => render(json)));
