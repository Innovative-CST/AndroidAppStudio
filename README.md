# Android AppStudio

[![Android CI](https://github.com/Innovative-CST/AndroidAppStudio/actions/workflows/android.yml/badge.svg)](https://github.com/Innovative-CST/AndroidAppStudio/actions/workflows/android.yml)
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/94a4082a551c496cae031bcdb6f2040d)](https://codacy.com/gh/Innovative-CST/AndroidAppStudio/dashboard)
![Commit Activity](https://img.shields.io/github/commit-activity/m/TS-Code-Editor/AndroidAppStudio)
[![GitHub contributors](https://img.shields.io/github/contributors/TS-Code-Editor/AndroidAppStudio)](https://github.com/Innovative-CST/AndroidAppStudio/graphs/contributors)
[![GitHub last commit](https://img.shields.io/github/last-commit/TS-Code-Editor/AndroidAppStudio)](https://github.com/Innovative-CST/AndroidAppStudio/commits/)
[![Discord server stats](https://img.shields.io/discord/1069271293150625853)](http://discord.gg/RM5qaZs4kd)
[![Repository Size](https://img.shields.io/github/repo-size/TS-Code-Editor/AndroidAppStudio)](https://github.com/Innovative-CST/AndroidAppStudio)

Welcome to the Android App Studio! This project enables building Android applications using a drag-and-drop interface.
Currently this app is under development so it doesn't have much features.

## Preview

<div align="center">
  <img src="assets/screenshots/screenshot1.png" alt="Screenshot 1" style="width: 40%; height: auto; margin: 5px; border-radius: 9px;">
  <img src="assets/screenshots/screenshot2.png" alt="Screenshot 2" style="width: 40%; height: auto; margin: 5px; border-radius: 9px;">
  <img src="assets/screenshots/screenshot3.png" alt="Screenshot 3" style="width: 40%; height: auto; margin: 5px; border-radius: 9px;">
  <img src="assets/screenshots/screenshot4.png" alt="Screenshot 4" style="width: 40%; height: auto; margin: 5px; border-radius: 9px;">
  <img src="assets/screenshots/screenshot5.png" alt="Screenshot 5" style="width: 40%; height: auto; margin: 5px; border-radius: 9px;">
  <img src="assets/screenshots/screenshot6.png" alt="Screenshot 6" style="width: 40%; height: auto; margin: 5px; border-radius: 9px;">
  <img src="assets/screenshots/screenshot7.png" alt="Screenshot 7" style="width: 40%; height: auto; margin: 5px; border-radius: 9px;">
</div>

## Getting Started

This project is currently in its early stages, and there's a lot to be done. If you're interested in contributing, please follow the guidelines.

# Features
- [x] Block drag-drop logic editor
- [x] Layout Editor
- [x] Java Code generator
- [x] Layout Code generator
- [ ] Apk Builder
- [x] Integrated in-app terminal
  - [x] Open JDK 17 installed by default.
  - [x] Git
  - [x] Nano editor
- [x] Dynamic Themes
- [x] Manifest editor (Manually only)
- [x] Integrated mini code editor


# Discord
[![Join our discord](https://invidget.switchblade.xyz/RM5qaZs4kd)](https://discord.gg/RM5qaZs4kd)

## Contribution Guidelines

To contribute to Android App Studio, follow these steps:

1. Fork the repository.
2. Create a new branch for your feature or bug fix: `git checkout -b feature-name`.
3. Make your changes and commit them with descriptive messages.
4. Push your changes to your fork: `git push origin feature-name`.
5. Open a pull request to the `main` branch.

Please make sure to follow the existing code style and conventions.

### Modules Map

| Module           | Role                                        |
| --------------- | ------------------------------------------- |
| `app`      | Main application module       |
| `core`      | All the models are present in this module that are resposible for storing the data of project that is created by the app (Please do not work on this, it is going to replaced soon with new one)    |
| `block`      | All the model that are in core is represented to user by this module in the form of View. Old logic editor is also included here. (Please do not work on this, it is going to replaced soon with new one)           |
| `extension`      | Module responsible for generating blocks (by serialization) This module currently uses model from `core` module but it will use models from `beans` once `logiceditor` module gets some stability. |
| `prdownloader`      | A library reponsible for downloading file from internet. |
| `treeview`      | A library responsible for presenting a folder in a tree structure. |
| `viewedtior`      | Responsible for layout designing for projects |
| `logiceditor`      | New logiceditor (for block-based programming), it will replace logic editor present in `block` module soon.    |
| `test/logiceditor`      | App to test the new logiceditor mentioned above.    |

# Special Thanks to
[rooki_eplay](https://www.instagram.com/rooki_eplay) for UI designing helping.
[JonForShort](https://github.com/JonForShort) for providing android-tools binaries [files](https://github.com/JonForShort/android-tools).

<a href="https://www.flaticon.com/free-icons/pointer" title="pointer icons">Pointer icons created by Pixel perfect - Flaticon</a>
<a href="https://www.flaticon.com/free-icons/not" title="not icons">Drop not allowed icons created by berkahicon - Flaticon</a>
