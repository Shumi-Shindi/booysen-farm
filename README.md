# Booysen Farm Management System

Java Swing desktop app with a MySQL database for managing livestock.

## Features
- Role-based login (Owner / Employee)
- Animal records for cattle, poultry and sheep (Animal superclass with subclasses)
- Medical records, stock monitoring, staff attendance
- Exportable farm status reports

## Setup
1. Install MySQL and import the `.sql` files in `database/` into a schema named `booysen_farm`.
2. Set your MySQL password in `BooysenFarm/src/booysenfarm/FarmDatabase.java`.
3. Open `BooysenFarm` in NetBeans, add the MySQL Connector/J jar, and run `BooysenFarm.java`.

Demo logins in the sample data are for testing only.

Built by Shumirai Shindi.
