import json
from city_data import cities
from country_data import countries
from food_data import food
from sights_data import sights


# Kontinente dynamisch erstellen.
continents = {}
for country_id, country_data in countries.items():
    continent_id = country_data["continent"]
    if continent_id not in continents:
        continents[continent_id] = {
            "name": continent_id.capitalize(),
            "countries": []
        }
    continents[continent_id]["countries"].append(country_id)

# Dynamische Zuordnung der Städte zu Ländern
for city_id, city_data in cities.items():
    country_id = city_data["upper_class"]
    if "cities" not in countries[country_id]:
        countries[country_id]["cities"] = []
    countries[country_id]["cities"].append(city_id)

for food_id, food_data in food.items():
    country_id = food_data["upper_class"]
    if "food" not in countries[country_id]["infos"]:
        countries[country_id]["infos"]["food"] = []
    countries[country_id]["infos"]["food"].append(food_id)

for sights_id, sights_data in sights.items():
    city_id = sights_data["upper_class"]
    if "sights" not in cities[city_id]:
        cities[city_id]["info"]["sights"] = []
    cities[city_id]["info"]["sights"].append(sights_id)

# JSON-Struktur zusammenfügen
data = {
    "continents": continents,
    "countries": countries,
    "cities": cities,
    "food": food,
    "sights": sights
}

# JSON-Datei schreiben
with open("travelgotchi_data.json", "w", encoding="utf-8") as file:
    json.dump(data, file, ensure_ascii=False, indent=4)

print("Optimierte JSON-Datei wurde erfolgreich erstellt!")