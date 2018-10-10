# TimeTrack Raspberry Pi

At the [City of Bloomington, Indiana](https://bloomington.in.gov), we use a number of different solutions to keep track of hours for employees. We have some employees who utilize a paper based punch clock, but we want to automate the process of digitizing those hours.

We're using a Raspberry Pi and touchscreen to serve as a simple kiosk terminal. This is set up to show our web based timeclock application. Employees can use their RFID enabled badge to clock in or clock out of the system.

![prototype]({{ site.github.url }}/assets/IMG_1027_l.JPG)

## Parts list

* [Raspberry Pi 3] x1
* [Screen] x1
* [Case] x1
* [RFID Reader] x1
* Power Supply x1
* Memory Card x1

![supplies]({{ site.github.url }}/assets/IMG_1008_l_sq.JPG)
---
## Flashing the image

Start by opening the memory card and inserting it into your PC/laptop's reader.

We will be using RDS (Raspberry Digital Signage) to run our kiosk, so download and extract it. We are also using the Donator's version, which allows for additional options in the system and kiosk areas.

**Download: [RDS]**

*This guide is written as if your intent is to flash this image to many different SD cards, and as a result some of the steps may be more complex than if you were just doing it for a single device.*

---
### Imaging software

![transfer]({{ site.github.url }}/assets/IMG_1013_l_sq.JPG)

Next up is to transfer the extracted image to the SD memory card.

Etcher is a nice multi-platform piece of software for easily transferring images. Win32DiskImager is another nice application for Windows, which can also read back the image from your card when you're done modifying it.

For Linux/macOS you can also just used the **dd** command to read/write images.

**Download: [Etcher.io]**

**Download: [Win32DiskImager]**

---

### Flashing
![etcher screenshot]({{ site.github.url }}/assets/etcher.png)

Once flashing is complete, eject the Micro SD card from your system.

## Assembly
Insert the Micro SD card from the previous section into the RaspberryPi.

![insert SD card]({{ site.github.url }}/assets/IMG_1015_l.JPG)

Next, install the Pi in the case and install the touch screen too. This is a good video to follow:

[SmartiCase Video Tutorial]

---

### Connecting the Ribbon Cable

One aspect that was glossed over is how to connect the ribbon cables. Be sure to pull out the black plastic tab that keeps the cable secure first:

![ribbon cable 1]({{ site.github.url }}/assets/IMG_1021_l_sq.JPG)
![ribbon cable 2]({{ site.github.url }}/assets/IMG_1022_l_sq.JPG)
![ribbon cable 3]({{ site.github.url }}/assets/IMG_1023_l_sq.JPG)
![ribbon cable 4]({{ site.github.url }}/assets/IMG_1024_l_sq.JPG)

Once you've done your first build, it's easier to do subsequent ones.

At this point everything should be assembled and hooked up, including "Y" USB B cable for power.

Plug in the usb RFID reader

If all goes well, plug in the power supply to the "Y" cable and the machine should boot up. During the boot process, a brief 15-second window will be given to enter into the configuration menu.

![RDS_15sec]({{ site.github.url }}/assets/RDS_15sec.png)

---

## Configuration
### Configuring Networking

If the machine is plugged in via Ethernet, RDS will use DHCP by default and assign an address. If you wish to use WiFi, leave the cable unplugged and a menu will appear with options to type in your wireless network options.

---

### Setting up Basic Options

If you click the option to enter the setup during the 15-second boot window, you will be taken to a screen with several options to control how the kiosk behaves.

![RDS_KioskSettings]({{ site.github.url }}/assets/RDS_KioskSettings.png)

Be sure to **change the password** using the link at the bottom.

---

### Fetching MAC info

Next, open a SSH session to the Pi. The IP address should be displayed onscreen. The username is "pi" and the password is whatever you set it to in the options. Run the following command:

    ifconfig

Note the network interface's MAC address ("HWaddr" in output). If you're using the wired network connection, it will be in the "eth0" section. If you're using the wireless connection it will be in the "wlan0" section.

At the end of this process, send the MAC address and physical location to the system administrator so they can configure the right address in our DHCP server.



[RDS]:http://www.binaryemotions.com/digital-signage/raspberry-digital-signage/
[Raspberry Pi 3]: https://www.raspberrypi.org/products/raspberry-pi-3-model-b/
[Screen]: https://www.raspberrypi.org/products/raspberry-pi-touch-display/
[Case]: http://smarticase.com/collections/all/products/smartipi-touch
[RFID Reader]: https://www.rfideas.com/files/downloads/data-sheets/pcProx-Surface_Mount.pdf
[Etcher.io]: https://www.etcher.io/
[SmartiCase Video Tutorial]: http://smarticase.com/touchsetup
[Win32DiskImager]: https://sourceforge.net/projects/win32diskimager/
