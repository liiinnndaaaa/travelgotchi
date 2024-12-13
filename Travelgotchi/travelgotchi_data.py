import json

# Basisdaten: Länder enthalten KEINE explizite Liste von Städten mehr.
countries = {
    "Deutschland": {
        "name": "Deutschland",
        "imageUrl": "https://www.opengovpartnership.org/wp-content/uploads/2017/04/Germany.jpg",
        "continent": "europe",
        "infos": {
            "geographicalData": "Deutschland hat 84,48 Mio. Einwohner (Stand 2023) und eine Fläche von 357.588 Quadratkilometern.",
            "essenskultur": ["Curry-Wurst", "Döner"],
            "apps": ["Too good to go", "Neotaste"],
            "culturalSpecials": ["In Deutschland wird gerne Bier getrunken"],
            "events": ["Oktoberfest", "Stocherkahnrennen", "Weihnachtsmarkt"]
        }
    },
    "Frankreich": {
        "name": "Frankreich",
        "imageUrl": "https://www.vispronet.de/blog/wp-content/uploads/beitragsbild-flagge-frankreich-1-jpg-webp.webp",
        "continent": "europe",
        "infos": {
            "geographicalData": "Frankreich hat 68,17 Mio. Einwohner (Stand 2023) und eine Fläche von 632.734 Quadratkilometern.",
            "essenskultur": ["Schnecken", "Froschschenkel"],
            "apps": ["Too good to go"],
            "culturalSpecials": ["pending"],
            "events": ["pending"]
        }
    },
    "Japan": {
        "name": "Japan",
        "imageUrl": "https://finanzmarktwelt.de/wp-content/uploads/2024/07/Bank-of-Japan-Zinserhoehung.-Savvapanf-Freepik.com_-scaled.jpg",
        "continent": "asia",
        "infos": {
            "geographicalData": "Japan hat 125,52 Mio. Einwohner (Stand 2023) und eine Fläche von 377.974 Quadratkilometern.",
            "essenskultur": ["Sushi", "Ramen"],
            "apps": ["pending"],
            "culturalSpecials": ["pending"],
            "events": ["Penis-Festival"]
        }
    },
    "Griechenland": {
            	"name": "Griechenland",
            	"imageUrl": "https://www.reisereporter.de/resizer/v2/EUPPNMYCTVBCFL26A3ZXOWQV44.jpg?auth=343f7fa9bf9e439cb46c84c6327deb6806dded7995688faad15de387c2ecdbd0&quality=70&width=428&height=241&smart=true",
  				"continent": "europe",
				"infos": {
					"geographicalData": "Griechenland hat 10,36 Mio. Einwohner (Stand 2023) und eine Fläche von 131.694 Quadratkilometern.",
					"essenskultur": ["Gyros", "Souvlaki"],
					"apps": ["pending"],
					"culturalSpecials": ["pending"],
					"events": ["pending"]
				}
    },
    "Mexiko": {
            	"name": "Mexiko",
            	"imageUrl": "https://natuerlich.reisen/wp-content/uploads/2023/02/Zocalo-Square-und-Mexico-City-Cathedral-Mexiko-Stadt-Mexiko.jpg",
  				"continent": "northamerica",
				"infos": {
					"geographicalData": "pending",
					"essenskultur": ["Tacos", "Burritos"],
					"apps": ["pending"],
					"culturalSpecials": ["pending"],
					"events": ["pending"]
				}
    }
}

# Städte enthalten eine Referenz auf ihr zugehöriges Land.
cities = {
    "Berlin": {
        "name": "Berlin",
        "imageUrl": "https://www.germany.travel/media/redaktion/staedte_kultur_content/berlin/Berlin_Brandenburger_Tor_am_Pariser_Platz_im_Sonnenuntergang_Leitmotiv_German_Summer_Cities.jpg",
        "country": "Deutschland",
        "info": {
            "sights": ["Brandenburger-Tor", "Fernsehturm"],
            "discount_free_all": ["Museen am Anfang des Monats"],
            "Rabatte und kostenlos für bestimmte Personengruppen": ["pending"],
            "Unbedingt vorab buchen": ["pending"]
        },
        "airport_to_city": ["Mit dem Bus...", "Mit der Bahn...", "Mit dem Taxi..."],
        "inner_city": {
            "Ticketarten": ["pending"],
            "Apps": ["pending"]
        }
    },
    "Paris": {
        "name": "Paris",
        "imageUrl": "https://upload.wikimedia.org/wikipedia/commons/e/e6/Paris_Night.jpg",
        "country": "Frankreich",
        "info": {
            "sights": ["Eiffelturm", "Louvre"],
            "discount_free_all": ["Am ersten Sonntag im Monat sind viele Museen gratis"],
            "Rabatte und kostenlos für bestimmte Personengruppen": ["pending"],
            "Unbedingt vorab buchen": ["Eiffelturm"]
        },
        "airport_to_city": ["RER B Zug", "Buslinien", "Taxi"],
        "inner_city": {
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