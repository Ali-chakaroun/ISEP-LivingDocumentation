#!/usr/bin/env node

// Project-specific renderer that renders a README in GitHub Flavored Markdown,
// including a table of command-line options for the analyzer.
//
// Usage: node render.js /path/to/analyzed.json

const fs = require('node:fs');
const process = require('node:process');

const OUTPUT = `../README.md`;

const CMD_LINE_TABLE_TEMPLATE = `
| Short option | Long option | Description |
| ------------ | ----------- | ----------- |
~
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
const cmdOptionsTable = CMD_LINE_TABLE_TEMPLATE.replace('~', () => render(json));

try {
  let readmeTemplate = fs.readFileSync('README_template.md', 'utf8');
  let finalReadme = readmeTemplate.replace('[~cmd-line-options]', cmdOptionsTable);
  fs.writeFileSync(OUTPUT, finalReadme);
} catch (err) {
  console.error(err);
}



