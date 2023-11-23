#!/usr/bin/env node

// Project-specific renderer that renders a README in GitHub Flavored Markdown,
// including a table of command-line options for the analyzer.
//
// Usage: node render.js /path/to/analyzed.json

const fs = require('node:fs');
const process = require('node:process');

const OUTPUT = `../README.md`;
const README_TEMPLATE = 'README_template.md';
const JAVAPARSER_DOC_SITE = 'https://javadoc.io/static/com.github.javaparser/javaparser-core/3.25.1/';

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

// Section rendering the implemented visited methods
function getMarkdownLink(type) {
  const splitted = type.split('.');
  const displayName = splitted[splitted.length -1];
  const relUrl = splitted.join('/');
  return `[${displayName}](${JAVAPARSER_DOC_SITE}${relUrl}.html)`;
}

const visitor = json.filter((type) => type.FullName === 'com.infosupport.ldoc.analyzerj.AnalysisVisitor')[0];
const parserTypes = visitor.Methods.filter((method) => method.Name === 'visit').map((method) => method.Parameters[0].Type);
const implementedTypesList = parserTypes.map(getMarkdownLink).join(', ');

// Combining everything
try {
  const readmeTemplate = fs.readFileSync(README_TEMPLATE, 'utf8');
  let finalReadme = readmeTemplate.replace('[~cmd-line-options]', cmdOptionsTable);
  finalReadme = finalReadme.replace('[~implemented-visit-types]', implementedTypesList);
  fs.writeFileSync(OUTPUT, finalReadme);
} catch (err) {
  console.error(err);
}



