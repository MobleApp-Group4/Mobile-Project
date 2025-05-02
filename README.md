# Foodie Genie
FoodieGenie is a modern recipe discovery and shopping assistant app built for Android using Jetpack Compose and Firebase. It enables users to explore meals, add recipes to cart, place orders, and manage their profiles — all in a sleek, role-aware interface with support for both regular users and admins.

## 📑 Table of Contents
- [📱Installation](#-installation--quick-start)
- [👨🏻‍🏫 Introduction](#-introduction)
- [🧪 Features](#-features)
- [🖼️ App UI Preview](#-app-ui-preview)
- [📚 Pages Overview](#-pages-overview)
    - [🔍 Recipe Page](#-recipes-page-home-page)
    - [❤️ Favorites Page](#-favorites-page)
    - [🛒 Cart Page](#-cart-page)
    - [ Check Out Page]()
    - [🧾 Orders Page](#-orders-page)
    - [👤 Profile Page](#-profile-page)
    - [⚙️ Settings Page](#-settings-page)
- 🧩 [Technologies Used](#-technologies-used)
- 🔥 Firebase Setup
- 📂 Folder Structure
- 🛠️ Contribution
- 📜 License
- 👥 Author


### 📱 Installation & Quick Start

#### Step 1. Clone the Repository
   git clone https://github.com/MobleApp-Group4/Mobile-Project.git
#### Step 2. Open in Android Studio
- Make sure Kotlin and Android SDK 33+ are installed
- Place your google-services.json file inside the app/ directory
#### Step 3. Run the App
- Click "Run" in Android Studio
- Or use command line:

### 👨🏻‍🏫 Introduction
FoodieGenie is a user-centric food and recipe app that integrates with Firebase for real-time authentication, storage, and data syncing. The app supports browsing curated recipes, adding them to a cart, placing orders, and even offers admin-level views for managing all orders.

### 🧪 Features

- 🔐 Firebase Authentication (Email + Google)
- 🍽 Browse curated recipes from API
- 🔍 Search and filter recipes by diet (Vegan, Paleo, etc.)
- ❤️ Mark recipes as favorites
- 📦 Add meals to cart and confirm order with address and time
- 📅 Date and time slot picker (Lunch / Dinner)
- 🛒 Shopping cart with quantity
- 📦 Place and track orders
- 👤 Manage profile with avatar upload
- 🧑‍💼 Role-based views (User/Admin)
- ☁️ Integrated with Firebase for real-time user data
- 🌙 Dark mode support

### 🖼️ App UI Preview
Add screenshots here for key pages like Home, Cart, Profile, Orders

### 📚 Pages Overview

#### 🏠 Recipes Page (Home Page)
Displays a list of recipes with a search bar and diet-based chips filter.

#### 🔍 Recipe Info Page
Browse recipes from a remote API and view detailed ingredients, instructions, and images.

#### ❤️ Favorites Page
View your favorited recipes and navigate back to their details.

#### 🛒 Cart Page
See selected recipes, adjust quantities, remove items, and proceed to checkout.

#### 🧾 Orders Page
Users: View your own order history

#### Admins: View and manage all orders placed by users

#### 👤 Profile Page
Edit user information including name, address, gender, and upload a profile picture.

#### ⚙️ Settings Page
Basic settings and logout functionality.

### 🧪 Technologies Used
| Technology                        | Description                                                                                                 |
| --------------------------------- | ----------------------------------------------------------------------------------------------------------- |
| **Kotlin**                        | Main programming language for Android development. Offers type safety, null safety, and concise syntax.     |
| **Jetpack Compose**               | Android’s modern declarative UI toolkit used to build reactive and efficient UI components (`@Composable`). |
| **ViewModel (Android Jetpack)**   | Manages UI-related data in a lifecycle-conscious way. Ensures data survives configuration changes.          |
| **StateFlow (Kotlin Coroutines)** | Used for managing UI state reactively, enabling Compose to observe data changes efficiently.                |
| **Navigation Component**          | Facilitates type-safe screen-to-screen navigation with back stack management.                               |
| **Firebase Authentication**       | Handles user login, registration, and session management securely.                                          |
| **Firebase Firestore**            | Cloud-hosted NoSQL database used for storing user profiles, cart data, orders, and more.                    |
| **Material 3 Components**         | Google's modern design system used for UI elements like buttons, chips, dialogs, etc.                       |
| **DatePickerDialog**              | Native Android component for selecting date inputs in `CheckoutScreen`.                                     |
| **Coil (via painterResource)**    | Efficient image loading for displaying app logo or recipe images.                                           |
| **FlowRow (Compose Foundation)**  | For wrapping filter chips responsively in `RecipeScreen`.                                                   |
| **MutableState + remember{}**     | Local state handling in Compose for fields like address, note, date, etc.                                   |
