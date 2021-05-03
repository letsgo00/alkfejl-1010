# Alkalmazás fejlesztés - 1010 kötelező program

## Leírás 
___
Ezt a projektet az alkalmazásfejlesztés I. kurzusra készült.
>* A webes modul használatához **[Tomcat 9.0.45-re](https://tomcat.apache.org/download-90.cgi)** van szükség.
>* A projekt **JDK 11-en** készült.
>* A desktop modul **JavaFX 11-et** használ.

## A játék menete 
___
Új játék indítása után a játékosnak egy 10X10-es mezőre kell behúzni az alul megjelenő 3 alakzatot. Ha ez sikerül akkor újabb 3 alakzatot kap.<br/>
Ha egy sor vagy oszlop betelik akkor az a sor vagy oszlop törlésre kerül, és a játékos kap 10 pontot. A játékos minden lerakott alakzat után kap egy pontot. <br/>
A játékosnak rendelkezésére áll 3X3 lehetőség, mindegyik 50 pontjába kerül.
> 1. Sor törlése
> 2. Oszlop törlése
> 3. Új elemek kérése
A játéknak akkor van vége, ha a játékos nem tud több elemet lerakni, és elfogyott az összes segítsége, vagy ha nincs elég pontja segítségre.
A játék végén a játékosnak lehetősége van elmenteni a pontszámát.

## game-core
___
A core modul tartalmazza a Model réteget valamint a DAO-kat.

## game-dekstop
___
A desktop modul tartalmazza a játékot és a ranglista megtekintésére ad lehetőséget.


## game-web
___
A web modul a ranglista megtekintésésre alkalmas.

## Extra beállítások
A game-core-ban az application.properties fileban a db.url-t kell beállítani.
