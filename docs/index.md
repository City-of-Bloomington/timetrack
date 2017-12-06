---
layout: default
---

At the [City of Bloomington, Indiana](https://bloomington.in.gov), we use a number of different solutions to keep track of hours for employees. We have some employees who utilize a paper based punch clock, but we want to automate the process of digitizing those hours. 

We're using a Raspberry Pi and touchscreen to serve as a simple kiosk terminal. This is set up to show our web based timeclock application. Employees can use their RFID enabled badge to clock in or clock out of the system. 

![prototype]({{ site.github.url }}/assets/IMG_1027_l.JPG)

Parts list
--------------------

RaspberryPi 1x:

<https://www.raspberrypi.org/products/raspberry-pi-3-model-b/>

Screen: 1x:

<https://www.raspberrypi.org/products/raspberry-pi-touch-display/>

Case: 1x:

<http://smarticase.com/collections/all/products/smartipi-touch>

RFID Reader:

<https://www.rfideas.com/files/downloads/data-sheets/pcProx-Surface_Mount.pdf>

Power Supply 1x:

Memory Card 1x:

![supplies]({{ site.github.url }}/assets/IMG_1008_l_sq.JPG)


Assembly
--------------------

Start by opening the memory card and transferring the latest Raspbian image to it. Since we will be using a desktop environment, be sure to use "Raspbian Jessie with PIXEL":

<https://downloads.raspberrypi.org/raspbian_latest>

via:

<https://www.raspberrypi.org/downloads/raspbian/>

I needed to use the torrent for the download to complete; the direct download stalled out for me.

TODO:
transfer this to the local network since it is rather large (1.4GB)

![transfer]({{ site.github.url }}/assets/IMG_1013_l_sq.JPG)

Next up is to transfer the downloaded image to the SD memory card. There are many good guides on the options here:

<https://www.raspberrypi.org/learning/software-guide/>

Since we'll be doing this for multiple cards, it makes sense to skip the NOOBS approach and go with an image burner. Etcher is a nice one. Download here:

<https://www.etcher.io/>

![etcher screenshot]({{ site.github.url }}/assets/etcher.png)

Once the image has been transferred to the SD card, eject that from your system and insert it into the RaspberryPi.

![insert SD card]({{ site.github.url }}/assets/IMG_1015_l.JPG)

Next, install the Pi in the case and install the touch screen too. This is a good video to follow:

<http://smarticase.com/touchsetup>

goes to:

<https://www.youtube.com/watch?v=XKVd5638T_8>

One aspect that was glossed over is how to connect the ribbon cables. Be sure to pull out the black plastic tab that keeps the cable secure first:

![ribbon cable 1]({{ site.github.url }}/assets/IMG_1021_l_sq.JPG)
![ribbon cable 2]({{ site.github.url }}/assets/IMG_1022_l_sq.JPG)
![ribbon cable 3]({{ site.github.url }}/assets/IMG_1023_l_sq.JPG)
![ribbon cable 4]({{ site.github.url }}/assets/IMG_1024_l_sq.JPG)

Once you've done your first build, it's easier to do subsequent ones.

At this point everything should be assembled and hooked up, including "Y" USB B cable for power. 

Plug in the usb RFID reader

If all goes well, plug in the power supply to the "Y" cable and the machine should boot up. Woo woo!

Configuration
--------------------

At this point it will be helpful to have a USB keyboard available. You may also want a USB mouse, but the touch screen does work as a mouse.

Change the default keyboard layout. Open Start->Preferences->Mouse and Keyboard Settings

Choose the Keyboard tab and click the Keyboard Layout... button.

Choose United States->English (US)

(Can test the keyboard config with !@#$ keys. That's not an expletive. :) )

Next configure a network connection. Consider the destination location. If it has a wired network connection, we'll want to use that. Otherwise we'll want to use the wireless connection. For wireless, choose one from the networking menu in the top right.

Next, open a terminal and type:

    ifconfig

Note the network interface's MAC address ("HWaddr" in output). If you're using the wired network connection, it will be in the "eth0" section. If you're using the wireless connection it will be in the "wlan0" section.

At the end of this process, send the MAC address and physical location to Mike so that he can configure the right address in our DHCP server. 


Update the core system:

    sudo apt-get update && sudo apt-get upgrade -y

Restart the Pi to apply any kernel updates.

Next run:

    sudo rpi-update

This fixes an issue with the display where occasionally the backlight will not come back on after it has been asleep.

    sudo vi /boot/cmdline.txt

Replace console=tty1 with console=tty3 to redirect boot messages to the third console. 

Hide the logo by adding: logo.nologo

Disable the rainbow splash screen at boot:

    sudo vi /boot/config.txt

and add:

    disable_splash=1

To hide the mouse cursor, add a nocursor option as follows in the file (/etc/lightdm/lightdm.conf)

    xserver-command = X -nocursor


TODO:

  - consider SSH server installation (easier remote configurations)
  - change default pi password
  - create a different (un-privileged) user to boot in to automatically


Configure the browser to launch in kiosk mode:


    sudo vi ~/.config/autostart/autoChromium.desktop


```
[Desktop Entry]
Type=Application
Exec=/usr/bin/chromium-browser --noerrdialogs --disable-session-crashed-bubble --disable-infobars --kiosk http://www.website.com
Hidden=false
X-GNOME-Autostart-enabled=true
Name[en_US]=AutoChromium
Name=AutoChromium
Comment=Start Chromium when GNOME starts
```

Restart the system to make sure the browser boots up automatically.

Alt-F4 on a connected keyboard will exit out of browser



Thanks
----------

This guide was loosely based on the following:

<https://www.danpurdy.co.uk/web-development/raspberry-pi-kiosk-screen-tutorial/>

This was the source for the autostart configuration that worked:

<http://raspberrypi.stackexchange.com/questions/38515/auto-start-chromium-on-raspbian-jessie-11-2015>

This explained how to hide the mouse cursor:

<http://raspberrypi.stackexchange.com/questions/53127/how-to-permanently-hide-mouse-pointer-or-cursor-on-raspberry-pi>

A good overview of the different early boot settings:

<http://blog.fraggod.net/2015/11/28/raspberry-pi-early-boot-splash-logo-screen.html>

Many thanks!


