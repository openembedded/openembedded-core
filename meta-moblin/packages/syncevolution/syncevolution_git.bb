DESCRIPTION = "Data synchronization tool"
SRC_URI = "git://git.moblin.org/${PN}.git;protocol=git;branch=syncevolution-0-9-branch"
LICENSE = "LGPLv2.1"
PV = "0.0+git${SRCPV}"
PR = "r1"

DEPENDS = "libsynthesis libsoup curl boost nbtk dbus-glib glib-2.0 libglade libunique gnome-keyring eds-dbus"

EXTRA_OECONF = "--enable-gui=moblin"

S = "${WORKDIR}/git"

inherit autotools

do_configure_prepend () {
    ${S}/gen-autotools.sh
}