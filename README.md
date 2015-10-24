# DesktopKeyboard2Android
## Use your laptop's keyboard for typing in your Android phone

[WifiPassword](https://play.google.com/store/apps/details?id=com.volosyukivan&hl=en) is a great android app that lets you use your laptop's keyboard to type into your android. You connect your laptop and android together by wifi or usb. In your laptop, you use a web browser, "the client", to type into your android.

This project replaces "the client", while still useing the android app. The differences are:
* The client is a standalone programme, instead of a web page
* There are not parameters to set-up (so, no interaction with the application needed)
* It works with non-US keyboards (eg, accents)


## Use
- Install the [WifiPassword](https://play.google.com/store/apps/details?id=com.volosyukivan&hl=en) app
- Follow its instructions to connect your laptop and android together through wifi or usb
- Test that it works by browsing this web page from your laptop and type some text: [http://localhost:7777/](http://localhost:7777/)
- Install and run the [latest release of DesktopKeyboard2Android](https://github.com/dportabella/DesktopKeyboard2Android/releases)
- What you type now in your laptop, it will be forwarded to your Android


## Compile
To debug this application:

    $ git clone git remote add origin https://github.com/dportabella/DesktopKeyboard2Android.git
    $ cd DesktopKeyboard2Android
    $ mvn exec:java -Dexec.mainClass="desktopKeyboard2Android.DesktopKeyboard2Android"

To show the java key events and understand how they are filtered and mapped for Android:

    $ mvn exec:java -Dexec.mainClass="desktopKeyboard2Android.ShowJavaKeyEvents"

To build the installer for this application:

    $ mvn jfx:native


## Todo
* Replace the GUI window by a system tray icon
* Remove the need to first run the adb command
* Put the key events in a queue, instead of blocking the application


## Story of this application
I am trekking in Nepal Annapurna region for more than one month. I use my laptop to organize photos and type my journal. Suddenly, the retro-illumination of my screen stops working, which means that I can only see very small regions of my screen if I put a torch in front of it, very painful to work with. Typing my journal into my android phone is not feasible. The Google Keyboard app makes it a bit faster to type long texts, but it does not work in the Catalan language.

I find the WifiKeyboard app, which allows me to use my laptop's keyboard to type into my android. That's fine for writing my journal. However, it is difficult to set-up each time that I want to type something, as the WifiKeyboard client requires to open a webpage in the laptop and click some parameters. That's difficult in my case as the laptop's screen is not working properly. There are two other issues: (1) I cannot type some of the accents on my Swiss-French keyboard, and (2) I cannot reorganize parts of the text as Crtl-C & Crtl-V do not work with the international keyboard.

So I decided to develop this programm to solve these issues while trekking in Nepal Annapurna, with elevations from 2000 to 5000m. I travel in a very relaxed way, trekking fast some days, and resting in a mountain hut for some other days. The working conditions are a bit painful, as my laptop screen is hardly working, the wifi and internet connection works slowly and intermittently, there are power cuts, I use IntelliJ in a very small screen (I mirror my laptop screen to my Samsung Galaxy S4 using Chrome Remote Desktop), and the whole set-up only works from time to time. But it is fun. And no, this does not spoil my trekking vacations. I enjoy doing both. :)
