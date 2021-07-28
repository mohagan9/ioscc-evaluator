# ioscc-evaluator

![Build](https://github.com/mohagan9/ioscc-evaluator/workflows/Build/badge.svg)
[![Version](https://img.shields.io/jetbrains/plugin/v/17047.svg)](https://plugins.jetbrains.com/plugin/17047)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/17047.svg)](https://plugins.jetbrains.com/plugin/17047)
![Code Quality](https://www.code-inspector.com/project/21797/score/svg)

<!-- Plugin description -->
### iOS Class Chain Evaluator Plugin for IntelliJ

Similar to *Evaluate XPath...* but for Appium's iOS Class Chains.

---
### Usage Instructions ###

1. Open an XML document with XCUI Elements
1. Go to Tools > XML Actions > Evaluate iOS Class Chain (Ctrl+Alt+C, E)
2. Enter a query into the text box, and evaluate
3. See number of matching results

<b>Note:</b> If you want to run iOS Predicates, then wrap your query in <b>$</b>

---
### Supported Class Chain Query Construction Rules ###
 * <kbd>XCUIElement</kbd> Direct children
 * <kbd>**/XCUIElement</kbd> Descendant children
 * <kbd>XCUIElement[2]</kbd> Nth child
 * <kbd>XCUIElement[$name == "Joe"$]</kbd> Select elements with predicate matching descendant children

### Supported Predicate Operators ###
* AND, OR
* ==, !=

<!-- Plugin description end -->

---
### Installation

- Using IDE built-in plugin system:
  
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd> > <kbd>Search for "ioscc-evaluator"</kbd> >
  <kbd>Install Plugin</kbd>
  
- Manually:

  Download the [latest release](https://github.com/mohagan9/ioscc-evaluator/releases/latest) and install it manually using
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>


---
Plugin based on the [IntelliJ Platform Plugin Template][template].

[template]: https://github.com/JetBrains/intellij-platform-plugin-template
