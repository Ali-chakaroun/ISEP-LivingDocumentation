#!/usr/bin/env node

// Project-specific renderer that renders a README in GitHub Flavored Markdown,
// including a table of command-line options for the analyzer.
//
// Usage: node render.js /path/to/analyzed.json

const fs = require('node:fs');
const process = require('node:process');

const OUTPUT = `../README.md`;
const README_TEMPLATE = 'README_template.md';

const json = JSON.parse(fs.readFileSync(process.argv[2]));

// Section for rendering the command line options
const CMD_LINE_TABLE_TEMPLATE = `
| Short option | Long option | Description |
| ------------ | ----------- | ----------- |
~
`;

function renderCmdOptions(d) {
  if (d.Name === 'addOption' || d.Name === 'addRequiredOption') {
    const args = d.Arguments.map(a => a.Text).map(JSON.parse);
    const short = args[0] ? `\`-${args[0]}\`` : '';
    const long = args[1] ? `\`--${args[1]}\`` : '';
    const required = d.Name.includes('Required') ? '(Required.)' : '';
    return `| ${short} | ${long} | ${args[3]} ${required} |\n`;
  } else if (d instanceof Array) {
    return d.map(renderCmdOptions).join('');
  } else if (d instanceof Object) {
    return Object.values(d).map(renderCmdOptions).join('');
  }
}

const cmdOptionsTable = CMD_LINE_TABLE_TEMPLATE.replace('~', () => renderCmdOptions(json));

// Combining everything
const readmeTemplate = fs.readFileSync(README_TEMPLATE, 'utf8');
const finalReadme = readmeTemplate.replace('[~cmd-line-options]', cmdOptionsTable);
fs.writeFileSync(OUTPUT, finalReadme);



