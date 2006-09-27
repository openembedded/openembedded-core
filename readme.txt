Introduction
==

'Poky' is a combined cross build system and linux distribution based
upon OpenEmbedded. It features support for building X11/Matchbox/GTK
based filesystem images for various embedded devices and boards.


Required Packages
===

Running Poky on Ubuntu Breezy requires the following extra packages to
be installed;

build-essential
diffstat
texi2html
cvs
subversion
gcc-3.4
libsdl1.2-dev
zlib1g-dev

For Debian/Fedora - *Todo*

Building An Image
===

Simply run;

source poky-init-build-env
bitbake oh-image-pda

will result in a jffs2 image for the c7x0 series.

To build for the 770 change to 

MACHINE = "nokia770"

in build/conf/local.conf

NOTE: The above commands must be run in the build directory. Running 
them anywhere else will cause confusion.


Notes:
===

Useful Links;

OpenedHand
http://openedhand.com

Poky Homepage
http://projects.o-hand.com/poky

OE Homepage and wiki
http://openembedded.org
 
