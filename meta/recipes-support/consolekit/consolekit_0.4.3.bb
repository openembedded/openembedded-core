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

SRC_URI[md5sum] = "4c39c6eacc75334b890f21eead1d2945"
SRC_URI[sha256sum] = "fe02aca2b4c21df59aa5fbe6f28ea3ea4b3c9dd64c48c50634e9a53cf9c0dcc5"
S = "${WORKDIR}/ConsoleKit-${PV}"

FILES_${PN} += "${libdir}/ConsoleKit ${datadir}/dbus-1 ${datadir}/PolicyKit"




