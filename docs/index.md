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

## Assembly

Start by opening the memory card and inserting it into your PC/laptop's reader.

We will be using FullPageOS to run our kiosk, so download and extract it.

*This guide is written as if your intent is to flash this image to many different SD cards, and as a result some of the steps may be more complex than if you were just doing it for a single card.
The below steps regarding editing the image are unnecessary if you just want to alter the files. (you can just plug in the SD card after flashing and edit)*

[FullPageOS]

[Source code]

## Image Modification

By default, the Ethernet interface will use DHCP and shouldn't have any issues connecting. If you want to adjust the connection method in any way, you can edit the image file before you begin your flashing.

A super easy way to edit the image files on Windows is [OSFMount]. On Linux, you should be able to mount the .img file via your DE's built in image mounting or mounting to a directory via the terminal.

### TODO: add instructions for linux and OSX

### OSFMount (Windows)

For OSFMount, open the application and select the option to mount a new image:

![mount new img]({{ site.github.url }}/assets/OSFMount_mountnew.png)

Browse to the location of your image and select it. When the dialog pops up asking to select a partition, pick the FAT one. This is the partition that shows up when you insert the SD into a computer.

![choose partition]({{ site.github.url }}/assets/OSFMount_partition.png)

After selecting the partition, un-check the **Read-Only Drive** option and check the **Removable Media** option (*this prevents Windows from cluttering up the disk with System Restore folders*).

Your image will now show up as a new disk on your computer.

### Editing the files

Open **fullpageos-network.txt** *with an editor that supports Unix line endings* (not Notepad) if you want to adjust the Wired connection. Ignore any text about the Wi-Fi in here as it has all been deprecated. If you want to adjust Wireless, open **fullpageos-wpa-supplicant.txt**

Change any values to your desired settings and then save them.

To disable the rainbow splash screen at boot, open config.txt and add:

    disable_splash=1

### Saving

When you are done, close out of any editors or file browsers that my be interacting with the image. Go back to OSFMount and dismount the disk.

## Flashing the image

![transfer]({{ site.github.url }}/assets/IMG_1013_l_sq.JPG)

Next up is to transfer the extracted image to the SD memory card.

Etcher is a nice multi-platform piece of software for easily transferring images.

Download here: [Etcher.io]

![etcher screenshot]({{ site.github.url }}/assets/etcher.png)

Once you are finished, eject that from your system and insert it into the RaspberryPi.

![insert SD card]({{ site.github.url }}/assets/IMG_1015_l.JPG)

Next, install the Pi in the case and install the touch screen too. This is a good video to follow:

[SmartiCase Video Tutorial]

## Connecting the Ribbon Cable

One aspect that was glossed over is how to connect the ribbon cables. Be sure to pull out the black plastic tab that keeps the cable secure first:

![ribbon cable 1]({{ site.github.url }}/assets/IMG_1021_l_sq.JPG)
![ribbon cable 2]({{ site.github.url }}/assets/IMG_1022_l_sq.JPG)
![ribbon cable 3]({{ site.github.url }}/assets/IMG_1023_l_sq.JPG)
![ribbon cable 4]({{ site.github.url }}/assets/IMG_1024_l_sq.JPG)

Once you've done your first build, it's easier to do subsequent ones.

At this point everything should be assembled and hooked up, including "Y" USB B cable for power.

Plug in the usb RFID reader

If all goes well, plug in the power supply to the "Y" cable and the machine should boot up. Woo woo!

## Fetching MAC info

Next, open a SSH session to the Pi. The IP address should be displayed onscreen. The username is "pi" and the password is stored in KeePass. Run the following command:

    ifconfig

Note the network interface's MAC address ("HWaddr" in output). If you're using the wired network connection, it will be in the "eth0" section. If you're using the wireless connection it will be in the "wlan0" section.

At the end of this process, send the MAC address and physical location to the system administrator so that he can configure the right address in our DHCP server.

TODO:

* change default pi password

## Thanks

This guide was loosely based on the following:

<https://www.danpurdy.co.uk/web-development/raspberry-pi-kiosk-screen-tutorial/>

This was the source for the autostart configuration that worked:

<http://raspberrypi.stackexchange.com/questions/38515/auto-start-chromium-on-raspbian-jessie-11-2015>

This explained how to hide the mouse cursor:

<http://raspberrypi.stackexchange.com/questions/53127/how-to-permanently-hide-mouse-pointer-or-cursor-on-raspberry-pi>

A good overview of the different early boot settings:

<http://blog.fraggod.net/2015/11/28/raspberry-pi-early-boot-splash-logo-screen.html>

Many thanks!

[FullPageOS]: http://unofficialpi.org/Distros/FullPageOS/
[OSFMount]: https://www.osforensics.com/tools/mount-disk-images.html
[Source Code]: https://github.com/guysoft/FullPageOS
[Raspberry Pi 3]: https://www.raspberrypi.org/products/raspberry-pi-3-model-b/
[Screen]: https://www.raspberrypi.org/products/raspberry-pi-touch-display/
[Case]: http://smarticase.com/collections/all/products/smartipi-touch
[RFID Reader]: https://www.rfideas.com/files/downloads/data-sheets/pcProx-Surface_Mount.pdf
[Etcher.io]: https://www.etcher.io/
[SmartiCase Video Tutorial]: http://smarticase.com/touchsetup