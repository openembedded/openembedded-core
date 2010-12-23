SUMMARY = "a Telepathy based IM client"
DESCRIPTION = "Instant messaging program supporting text, voice, video, file \
transfers and interapplication communication over many different protocols, \
include: AIM, MSN, Google Talk (Jabber/XMPP), Facebook, Yahoo!, Salut, \
Gadu-Gadu, Groupwise, ICQ and QQ."
HOMEPAGE = "http://blogs.gnome.org/view/xclaesse/2007/04/26/0"
LICENSE = "GPL"
DEPENDS = "libcanberra telepathy-farsight gnome-doc-utils python-native telepathy-python telepathy-mission-control libtelepathy telepathy-glib gtk+ gconf libglade eds-dbus"
RDEPENDS_${PN} = "telepathy-mission-control"
RRECOMMENDS_${PN} = "telepathy-gabble"
PR = "r3"

inherit gnome

PARALLEL_MAKE = ""

EXTRA_OECONF += "--disable-scrollkeeper"

PACKAGES =+ "empathy-scrollkeeper-junk"
FILES_empathy-scrollkeeper-junk = "/var/lib/scrollkeeper"

FILES_${PN} += "${datadir}/mission-control/profiles/*.profile \
        ${datadir}/dbus-1/services/*.service \
        ${datadir}/telepathy/managers/*.chandler \
	${datadir}/icons \
	${libdir}/python*"

FILES_${PN}-dbg += "${libdir}/python*/*/.debug"

