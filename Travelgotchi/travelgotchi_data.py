import json

# Basisdaten: Länder enthalten KEINE explizite Liste von Städten mehr.
countries = {
    "germany": {
        "name": "Deutschland",
        "imageUrl": "https://www.opengovpartnership.org/wp-content/uploads/2017/04/Germany.jpg",
        "continent": "europe",
        "infos": {
            "geographische Daten": ["Deutschland hat 84,48 Mio. Einwohner (Stand 2023) und eine Fläche von 357.588 Quadratkilometern."],
            "essenskultur": ["Curry-Wurst", "Döner"],
            "apps": ["Too good to go", "Neotaste"],
            "kulturelle_besonderheiten": ["In Deutschland wird gerne Bier getrunken"],
            "events": ["Oktoberfest", "Stocherkahnrennen", "Weihnachtsmarkt"]
        }
    },
    "france": {
        "name": "Frankreich",
        "imageUrl": "https://www.vispronet.de/blog/wp-content/uploads/beitragsbild-flagge-frankreich-1-jpg-webp.webp",
        "continent": "europe",
        "infos": {
            "geographische Daten": ["Frankreich hat 68,17 Mio. Einwohner (Stand 2023) und eine Fläche von 632.734 Quadratkilometern."],
            "essenskultur": ["Schnecken", "Froschschenkel"],
            "apps": ["Too good to go"],
            "kulturelle_besonderheiten": ["pending"],
            "events": ["pending"]
        }
    },
    "japan": {
        "name": "Japan",
        "imageUrl": "https://finanzmarktwelt.de/wp-content/uploads/2024/07/Bank-of-Japan-Zinserhoehung.-Savvapanf-Freepik.com_-scaled.jpg",
        "continent": "asia",
        "infos": {
            "geographische Daten": ["Japan hat 125,52 Mio. Einwohner (Stand 2023) und eine Fläche von 377.974 Quadratkilometern."],
            "essenskultur": ["Sushi", "Ramen"],
            "apps": ["pending"],
            "kulturelle_besonderheiten": ["pending"],
            "events": ["Penis-Festival"]
        }
    },
    "greece": {
            	"name": "Griechenland",
            	"imageUrl": "https://www.reisereporter.de/resizer/v2/EUPPNMYCTVBCFL26A3ZXOWQV44.jpg?auth=343f7fa9bf9e439cb46c84c6327deb6806dded7995688faad15de387c2ecdbd0&quality=70&width=428&height=241&smart=true",
  				"continent": "europe",
				"infos": {
					"geographische Daten":["Griechenland hat 10,36 Mio. Einwohner (Stand 2023) und eine Fläche von 131.694 Quadratkilometern."],
					"essenskultur": ["Gyros", "Souvlaki"],
					"apps": ["pending"],
					"kulturelle_besonderheiten": ["pending"],
					"events": ["pending"]
				}
    }
}

# Städte enthalten eine Referenz auf ihr zugehöriges Land.
cities = {
    "berlin": {
        "name": "Berlin",
        "imageUrl": "https://www.germany.travel/media/redaktion/staedte_kultur_content/berlin/Berlin_Brandenburger_Tor_am_Pariser_Platz_im_Sonnenuntergang_Leitmotiv_German_Summer_Cities.jpg",
        "country": "germany",
        "info": {
            "Sehenswürdigkeiten": ["Brandenburger-Tor", "Fernsehturm"],
            "Rabatte und kostenloses für alle": ["Museen am Anfang des Monats"],
            "Rabatte und kostenlos für bestimmte Personengruppen": ["pending"],
            "Unbedingt vorab buchen": ["pending"]
        },
        "Vom Flughafen in die Stadt": ["Mit dem Bus...", "Mit der Bahn...", "Mit dem Taxi..."],
        "Innerhalb der Stadt": {
            "Ticketarten": ["pending"],
            "Apps": ["pending"]
        }
    },
    "paris": {
        "name": "Paris",
        "imageUrl": "https://upload.wikimedia.org/wikipedia/commons/e/e6/Paris_Night.jpg",
        "country": "france",
        "info": {
            "Sehenswürdigkeiten": ["Eiffelturm", "Louvre"],
            "Rabatte und kostenloses für alle": ["Am ersten Sonntag im Monat sind viele Museen gratis"],
            "Rabatte und kostenlos für bestimmte Personengruppen": ["pending"],
            "Unbedingt vorab buchen": ["Eiffelturm"]
        },
        "Vom Flughafen in die Stadt": ["RER B Zug", "Buslinien", "Taxi"],
        "Innerhalb der Stadt": {
            "Ticketarten": ["T+ Tickets", "Navigo Pass"],
            "Apps": ["RATP", "Citymapper"]
        }
    }
}

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
    country_id = city_data["country"]
    if "cities" not in countries[country_id]:
        countries[country_id]["cities"] = []
    countries[country_id]["cities"].append(city_id)

# JSON-Struktur zusammenfügen
data = {
    "continents": continents,
    "countries": countries,
    "cities": cities
}

# JSON-Datei schreiben
with open("travelgotchi_data.json", "w", encoding="utf-8") as file:
    json.dump(data, file, ensure_ascii=False, indent=4)

print("Optimierte JSON-Datei wurde erfolgreich erstellt!")