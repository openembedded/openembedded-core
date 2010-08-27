DESCRIPTION = "Base MIME and Application database for GNOME"
HOMEPAGE = "http://www.gnome.org/"
BUGTRACKER = "https://bugzilla.gnome.org/"

LICENSE = "GPLv2 & GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=59530bdf33659b29e73d4adb9f9f6552 \
                    file://check-mime.pl;endline=26;md5=a95b63c92c33d4ca1af61a315888f450"

inherit gnome
inherit autotools
PR = "r3"

SRC_URI += "file://pkgconfig.patch;patch=1"

DEPENDS += "shared-mime-info intltool-native"
RDEPENDS = "shared-mime-info"
