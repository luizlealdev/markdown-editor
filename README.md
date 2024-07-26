
# Markdown Editor
<a href="https://github.com/luizlealdev/markdown-editor/blob/master/LICENSE"><img src="https://img.shields.io/badge/License-Apache%202.0-blue.svg" alt="License"></a>
<a href="https://android-arsenal.com/api?level=24"><img src="https://img.shields.io/badge/API-24%2B-brightgreen.svg?style=flat" alt="API version"></a>
<a href="https://github.com/luizlealdev"><img src="https://img.shields.io/badge/Github-luizlealdev-2ea44f?logo=github&logoColor=white" alt="Luiz Leal's github profile"></a>

Markdown Editor demonstrates a simple editor that converts your markdown code and visualize it.

## 📥 Download

<a href="https://github.com/luizlealdev/markdown-editor/releases/latest"><img src="./images/get_it_on_github.png" alt="Get it on GitHub" height="70"></a>

## 📷 Previews
<div align="center">
  <img width="31%" src="./images/previews/preview-1.png" alt="Screenshot 1"/>
  <img width="31%" src="./images/previews/preview-2.png" alt="Screenshot 2"/>
  <img width="31%" src="./images/previews/preview-3.png" alt="Screenshot 3"/>
</div>

## 🛠 Tech Stack & Open Source Libraries
- Minimum SDK level 24.
- [Kotlin language](https://kotlinlang.org/)
- Jetpack
    - ViewBinding: Connect the components from the XML in Kotlin through a class that ensures type safety and other advantages.
    - Lifecycle: Observe Android lifecycles and handle user interface states after lifecycle changes.
    - Room Database: Database abstraction library for SQLite that ensures compile-time safety and ease of use.
    - ViewModel: Manages the holder of data related to the user interface and the lifecycle. Allows data to survive configuration changes, such as screen rotations.
- Architecture
    - MVVM (View - ViewModel - Model)
    - Communication between ViewModel and View through LiveData
    - Communication between ViewModel and Model through Kotlin Flow
    - Repositories for abstraction of communication with the data layer.
- Libraries
    - [OkHttp3](https://github.com/square/retrofit): For making requests following the HTTP standard.
    - [Markwon](https://github.com/noties/Markwon): For process the markdown code.
    - [Fab SpeedDial](https://github.com/leinardi/FloatingActionButtonSpeedDial): For add a Floating Action Button Speed Dial implementation.
    - [CodeView](https://github.com/amrdeveloper/codeview): For add add syntax highlight and line numbers into a EditText.

## 📐 Architecture
**Markdown Editor** follows the [Google's official architecture](https://developer.android.com/topic/architecture).

![MVVM Example](https://i.imgur.com/jV5iwpZ.png "MVVM example")

## ✨ Features

### Search markdown note
<img width="33%" src="./images/features_previews/search-preview.gif" alt="Search markdown note feature preview"/>

### Import markdown from file
<img width="33%" src="./images/features_previews/import-from-file-preview.gif" alt="Search markdown note feature preview"/>

### Import markdown from URL
<img width="33%" src="./images/features_previews/import-from-url-preview.gif" alt="Search markdown note feature preview"/>

### Markdown tips bar
<img width="33%" src="./images/features_previews/writing-code-preview.gif" alt="Search markdown note feature preview"/>

## 📄 License
```
Copyright 2024 Luiz Leal

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```