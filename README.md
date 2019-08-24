# cockatrice-my-collection
## info
I like [Cockatrice](https://github.com/Cockatrice/Cockatrice "Cockatrice GitHub Page") tabletop CCG simulator. 
I don't like that I cannot create a collection of cards I own to build decks with them.
I can search through all existing MTG cards but I don't remember which I own.
This application creates a custom collection out of list of cards I provide.
## more info
It uses 2 files - Cockatrice cards.xml as database of all existing cards and your list of cards you own.
Application filters through your cards, finds matching cards in Cockatrice's cards.xml and creates mycards.xml containing only the cards from your collection using cockatrice's format.
This xml can then be used by Cockatrice as your own collection/database to search for cards to build decks.
## usage
currently used from Eclipse IDE
### Steps to create a personal cards collection:
1. take existing cards.xml from cockatrice folder and put in root application folder
2. take my collection cards.txt and put in root application folder
3. run application - it will filter cards.xml, take only cards that are present in my cards.txt and store them as new mycards.xml
4. backup cards.xml in cockatrice folder 
5. copy mycards.xml into cockatrice folder, rename as cards.xml
6. startup cockatrice - it will use your card collection