# ioscc-evaluator

![Build](https://github.com/mohagan9/ioscc-evaluator/workflows/Build/badge.svg)
[![Version](https://img.shields.io/jetbrains/plugin/v/PLUGIN_ID.svg)](https://plugins.jetbrains.com/plugin/17047)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/PLUGIN_ID.svg)](https://plugins.jetbrains.com/plugin/17047)
![Code Quality](https://www.code-inspector.com/project/21797/score/svg)

<!-- Plugin description -->
### iOS Class Chain Evaluator Plugin for IntelliJ

Similar to *Evaluate XPath...* but for Appium's iOS Class Chains.

Currently iOS Predicates are supported with the following operators:
* AND
* OR
* ==
* !=

#### Usage Instructions ####

1. Open an XML document with XCUI Elements
1. Go to Tools > XML Actions > Evaluate iOS Class Chain (Ctrl+Alt+C, E)
2. Enter a predicate into the text box, and evaluate
3. See number of matching results

<!-- Plugin description end -->

## Installation

- Using IDE built-in plugin system:
  
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd> > <kbd>Search for "ioscc-evaluator"</kbd> >
  <kbd>Install Plugin</kbd>
  
- Manually:

  Download the [latest release](https://github.com/mohagan9/ioscc-evaluator/releases/latest) and install it manually using
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>


---
Plugin based on the [IntelliJ Platform Plugin Template][template].

[template]: https://github.com/JetBrains/intellij-platform-plugin-template
