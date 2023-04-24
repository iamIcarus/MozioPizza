# Pizza Delivery App

## Description

A small pizza delivery company was looking for growth and wanted to develop their own pizza delivery app. They hired you to develop this app.

In order to keep this small, we'll only work on a small portion of that app: the menu and flavor selection, plus a confirmation screen.

Write an app that has the following features:

- A menu of pizza flavors (read from the JSON above) where the user can select the flavors they want. A pizza can have one flavor (full pizza of the same flavor) or two flavors (half/half)
- To keep the scope small, only one pizza can be chosen at a time. No need for a shopping cart
- Flavors have their own prices. Total pizza price is calculated based on the price of each half  (i.e. if a flavor costs $10, a half of this flavor will cost $5 and a full pizza of this flavor $10)
- A button to finish ordering, which takes the user to an "order successful" screen. Simply show a summary of the selected pizza and the final price
- This scope includes only the flavor selection, pricing and confirmation. Extras are not necessarily a pro

## Languages used

Kotlin

## Libraries used

- Dagger2 - Dependency injection framework
- Moshi - A modern JSON library for Android, Java and Kotlin. It makes it easy to parse JSON into Java and Kotlin classes
- Retrofit2 - A type-safe HTTP client for Android and Java

## App UI flow

Single screen application. User can choose the main Full or Half/Half option from top radio button and udapte the list below.
Allows offline login by retreiving data from the db but always checks data again on checkout for any changes and invalidates order if changes are found

## Screenshots

![image](https://i.postimg.cc/qRzvw716/Pizza-Full.png)

![image](https://i.postimg.cc/bJJ9RQX0/Pizza-Half.png)

![image](https://i.postimg.cc/gkq4mcKZ/checkout.png)
