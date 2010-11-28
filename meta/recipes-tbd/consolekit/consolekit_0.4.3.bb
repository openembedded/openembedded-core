DESCRIPTION = "ConsoleKit is a framework for defining and tracking users, login sessions, and seats."
HOMEPAGE="http://www.freedesktop.org/wiki/Software/ConsoleKit"
BUGTRACKER="https://bugs.freedesktop.org/buglist.cgi?query_format=specific&product=ConsoleKit"

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=59530bdf33659b29e73d4adb9f9f6552 \
                    file://src/main.c;endline=21;md5=0a994e09769780220163255d8f9071c3"

DEPENDS = "dbus"

inherit gnome

SRC_URI = "http://www.freedesktop.org/software/ConsoleKit/dist/ConsoleKit-0.4.3.tar.bz2 \
           file://nopolkit.patch;patch=1"
S = "${WORKDIR}/ConsoleKit-${PV}"

FILES_${PN} += "${libdir}/ConsoleKit ${datadir}/dbus-1 ${datadir}/PolicyKit"




